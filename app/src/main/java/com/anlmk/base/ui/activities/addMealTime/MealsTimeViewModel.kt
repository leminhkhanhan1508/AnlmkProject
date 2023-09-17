package com.anlmk.base.ui.activities.addMealTime

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.anlmk.base.R
import com.anlmk.base.data.impl.LoginRepo
import com.anlmk.base.data.impl.Mealtime
import com.anlmk.base.data.impl.MealtimeDao
import com.anlmk.base.data.`object`.ChooseImage
import com.anlmk.base.data.`object`.CommonEntity
import com.anlmk.base.data.`object`.Session
import com.anlmk.base.data.response.LoginResponse
import com.anlmk.base.di.Common
import com.anlmk.base.di.ResourceProvider
import com.anlmk.base.extensions.deleteFileFromInternalStorage
import com.anlmk.base.extensions.saveBitmapToInternalStorage
import com.anlmk.base.ui.base.BaseViewModel
import com.anlmk.base.utils.Utils
import java.util.*

class MealsTimeViewModel(
    private val mealtimeDao: MealtimeDao,
    private val resourcesProvider: ResourceProvider) : BaseViewModel()
{
    val listMealTimeByDateLive = MutableLiveData<List<Mealtime?>>()
    val insertMealtime = MutableLiveData<Mealtime>()
    val deleteMealtime = MutableLiveData<Mealtime>()
    fun deleteMealTimeItem(mealTime: Mealtime) = launchFromDatabase {
        mealtimeDao.deleteById(mealTime.id)
        deleteMealtime.value = mealTime
    }
    fun insertMealInformation(
        dateOfMeal: String,
        sessionChoose: CommonEntity?,
        timeOfMeal: String,
        foodOfMeal: String,
        mmolOfMeal: String,
        imageFood: Bitmap? = null
    ) = launchFromDatabase {
        val imagePath = imageFood?.let {
            Common.currentActivity.saveBitmapToInternalStorage(
                it,
                System.nanoTime().toString()
            )
        }
        val mealtime = formatInformationBeforeSave(
            dateOfMeal,
            sessionChoose,
            timeOfMeal,
            foodOfMeal,
            mmolOfMeal,
            imagePath
        )
        mealtimeDao.insertMealtime(mealtime)
        insertMealtime.value = mealtime
    }

    fun updateMealInformation(
        mealtime: Mealtime,
        dateOfMeal: String,
        sessionChoose: CommonEntity?,
        timeOfMeal: String,
        foodOfMeal: String,
        mmolOfMeal: String,
        imageFood: Bitmap? = null
    ) = launchFromDatabase {
        val date = Utils.getDateFormat().parse(dateOfMeal)?.time
        val time = Utils.getTimeFormat().parse(timeOfMeal)?.time
        if (mealtime.dateOfMeal != date) {
            mealtime.dateOfMeal = date
        }
        if (mealtime.timeOfMeal != time) {
            mealtime.timeOfMeal = time
        }
        if (mealtime.sessionId != sessionChoose?.codeFunction) {
            mealtime.sessionId = sessionChoose?.codeFunction
            mealtime.sessionName = sessionChoose?.getTitle()
        }
        if (mealtime.foodOfMeal != foodOfMeal) {
            mealtime.foodOfMeal = foodOfMeal
        }
        if (mealtime.molOfFood != mmolOfMeal) {
            mealtime.molOfFood = mmolOfMeal
        }
        if (imageFood != null) {
            Common.currentActivity.deleteFileFromInternalStorage(mealtime.imageOfFood ?: "")
            mealtime.imageOfFood = imageFood.let {
                Common.currentActivity.saveBitmapToInternalStorage(
                    it,
                    System.nanoTime().toString()
                )
            }
        }
        mealtimeDao.updateMealtime(mealtime)
        insertMealtime.value = mealtime
    }

    fun getMealTimeByDateList(dateOfMeal: String) = launchFromDatabase {
        val mealtimeList = mealtimeDao.getMealtimeByDate(Utils.getDateFormat().parse(dateOfMeal)?.time) ?: arrayListOf()
        listMealTimeByDateLive.value = mealtimeList.sortedBy { it?.timeOfMeal }
    }

    fun getSessionMenu(): MutableList<CommonEntity> {
        return  mutableListOf(
            CommonEntity().apply {
                this.setTitle(resourcesProvider.getString(R.string.morning))
                this.codeFunction =Session.getValueSession(Session.Morning)
            },
            CommonEntity().apply {
                this.setTitle(resourcesProvider.getString(R.string.afternoon))
                this.codeFunction =Session.getValueSession(Session.Afternoon)

            },
            CommonEntity().apply {
                this.setTitle(resourcesProvider.getString(R.string.night))
                this.codeFunction =Session.getValueSession(Session.Night)
            }
        )
    }

    fun validateBeforeSave(foodOfMeal: String?, mmolOfMeal: String?): Boolean {
        if (foodOfMeal.isNullOrEmpty() || mmolOfMeal.isNullOrEmpty())
            return false
        return true
    }

    fun formatInformationBeforeSave(
        dateOfMeal: String,
        sessionChoose: CommonEntity?,
        timeOfMeal: String,
        foodOfMeal: String,
        mmolOfMeal: String,
        imageFood: String?=null
    ): Mealtime {
        val date = Utils.getDateFormat().parse(dateOfMeal)?.time
        val time = Utils.getTimeFormat().parse(timeOfMeal)?.time
        return Mealtime(
            sessionId = sessionChoose?.codeFunction,
            sessionName = sessionChoose?.getTitle(),
            dateOfMeal = date,
            timeOfMeal = time,
            foodOfMeal = foodOfMeal,
            molOfFood = mmolOfMeal,
            imageOfFood = imageFood
        )
    }

    fun initSession(calendar: Calendar): CommonEntity? {
        return when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in 6..11 -> {
                getSessionMenu().find { it.codeFunction == Session.getValueSession(Session.Morning) }
            }
            in 12..17 -> {
                getSessionMenu().find { it.codeFunction == Session.getValueSession(Session.Afternoon) }
            }
            else -> {
                getSessionMenu().find { it.codeFunction == Session.getValueSession(Session.Night) }
            }
        }
    }

    fun setChooseImageMenu(): List<Any> {
        return  mutableListOf(
            CommonEntity().apply {
                this.setTitle(resourcesProvider.getString(R.string.camera))
                this.codeFunction =ChooseImage.getValueChooseImage(ChooseImage.Camera)
            },
            CommonEntity().apply {
                this.setTitle(resourcesProvider.getString(R.string.album))
                this.codeFunction =ChooseImage.getValueChooseImage(ChooseImage.Album)
            }
        )
    }


}