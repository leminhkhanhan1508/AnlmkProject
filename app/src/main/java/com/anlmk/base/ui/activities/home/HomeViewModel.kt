package com.anlmk.base.ui.activities.home

import android.os.Build
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.anlmk.base.R
import com.anlmk.base.data.impl.Mealtime
import com.anlmk.base.data.impl.MealtimeDao
import com.anlmk.base.data.`object`.CommonEntity
import com.anlmk.base.data.`object`.MealsEntity
import com.anlmk.base.data.`object`.Session
import com.anlmk.base.di.ResourceProvider
import com.anlmk.base.extensions.exportMealtimeListToCSV
import com.anlmk.base.extensions.exportMealtimeListToCSVUseMediaStore
import com.anlmk.base.ui.base.BaseViewModel
import com.anlmk.base.utils.Utils

class HomeViewModel(
    private val mealtimeDao: MealtimeDao,
    val resourcesProvider: ResourceProvider) : BaseViewModel()
{
    val listMealTimeLive = MutableLiveData<List<MealsEntity>>()
    var listMealTime: List<Mealtime?> = arrayListOf()
    fun getMealTimeList() = launchFromDatabase {
        listMealTime = mealtimeDao.getAllMealtime() ?: arrayListOf()
        listMealTimeLive.value = getMealTimeList(listMealTime.sortedByDescending { it?.dateOfMeal })
    }
    private fun getMealTimeList(list: List<Mealtime?>): List<MealsEntity> {
        val groupedMealtime = list.groupBy { it?.dateOfMeal }
        val mealsEntityList = groupedMealtime.map { (date, group) ->
            MealsEntity().apply {
                dateOfMeal = Utils.getDateFormat().format(date)
                mealBreakfast = group.sortedBy { it?.timeOfMeal }.firstOrNull{ it?.sessionId == Session.getValueSession(Session.Morning) }
                mealLunch = group.sortedBy { it?.timeOfMeal }.firstOrNull { it?.sessionId == Session.getValueSession(Session.Afternoon) }
                mealDinner = group.sortedBy { it?.timeOfMeal }.firstOrNull { it?.sessionId == Session.getValueSession(Session.Night) } }
        }
        return mealsEntityList
    }

    fun getHomeBottomBarFunction(): MutableList<CommonEntity> {
        return  mutableListOf(
            CommonEntity().apply {
                this.setTitle(resourcesProvider.getString(R.string.home_page))
                this.setDescript(resourcesProvider.getString(R.string.splash_hello))
                this.setIcon(R.drawable.baseline_home_24)
            },
            CommonEntity().apply {
                this.setTitle(resourcesProvider.getString(R.string.setting_page))
                this.setDescript(resourcesProvider.getString(R.string.splash_hello))
                this.setIcon(R.drawable.baseline_settings_suggest_24)
            },
            CommonEntity().apply {
                this.setTitle(resourcesProvider.getString(R.string.other_page))
                this.setDescript(resourcesProvider.getString(R.string.splash_hello))
                this.setIcon(R.drawable.baseline_scatter_plot_24)
            }
        )
    }

    fun handleExportData(homeActivity: HomeActivity) {
        val listMealTime = listMealTime.sortedBy{ it?.timeOfMeal }.sortedByDescending { it?.dateOfMeal }
        val isSuccess = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            homeActivity.exportMealtimeListToCSVUseMediaStore(listMealTime)
        } else {
            homeActivity.exportMealtimeListToCSV(listMealTime)
        }
        if (isSuccess) {
            Toast.makeText(
                homeActivity,
                resourcesProvider.getString(R.string.export_file_success),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}