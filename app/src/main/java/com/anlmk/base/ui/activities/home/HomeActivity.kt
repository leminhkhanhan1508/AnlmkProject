package com.anlmk.base.ui.activities.home

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.anlmk.base.data.impl.Mealtime
import com.anlmk.base.data.`object`.MealsEntity
import com.anlmk.base.data.`object`.Session
import com.anlmk.base.databinding.ActivityHomeBinding
import com.anlmk.base.extensions.setSafeOnClickListener
import com.anlmk.base.extensions.toJson
import com.anlmk.base.ui.activities.addMealTime.AddMealTimeActivity
import com.anlmk.base.ui.activities.addMealTime.DetailMealOfDateActivity
import com.anlmk.base.ui.adapters.CommonAdapter
import com.anlmk.base.ui.base.BaseActivity
import com.anlmk.base.utils.RequestCode
import com.anlmk.base.utils.Tags
import com.anlmk.base.utils.Utils
import org.koin.android.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity() {
    override val model: HomeViewModel by viewModel()
    override val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    private var adapterMealOfDateHome: CommonAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.REQUEST_ADD_MEAL -> {
                if (resultCode == RESULT_OK) {
                    model.getMealTimeList()
                }
            }
        }
    }

    private fun initListener() {
        adapterMealOfDateHome?.setItemClick { i, any ->
            val intentAdd = Intent(this, AddMealTimeActivity::class.java)
            var isUpdate = false
            if (any is MealsEntity) {
                when (Session.getTypeSession(i)) {
                    Session.Morning -> {
                        any.mealBreakfast?.let {
                            intentAdd.putExtra(
                                Tags.EXTRA_MEAL_TIME,
                                it.toJson()
                            )
                            isUpdate = true
                        } ?: run {
                            intentAdd.putExtra(
                                Tags.EXTRA_MEAL_TIME,
                                Mealtime(
                                    id = -1,
                                    sessionId = i,
                                    dateOfMeal = Utils.getDateFormat()
                                        .parse(any.dateOfMeal ?: "")?.time,
                                    timeOfMeal = Utils.getTimeFormat().parse("9:00")?.time
                                ).toJson()
                            )
                        }

                    }
                    Session.Afternoon -> {
                        any.mealLunch?.let {
                            intentAdd.putExtra(Tags.EXTRA_MEAL_TIME, it.toJson())
                            isUpdate = true
                        } ?: run {
                                intentAdd.putExtra(
                                    Tags.EXTRA_MEAL_TIME,
                                    Mealtime(
                                        id = -1,
                                        sessionId = i,
                                        dateOfMeal = Utils.getDateFormat()
                                            .parse(any.dateOfMeal ?: "")?.time,
                                        timeOfMeal = Utils.getTimeFormat().parse("14:00")?.time
                                    ).toJson()
                                )
                            }
                    }
                    Session.Night -> {
                        any.mealDinner?.let {
                            intentAdd.putExtra(
                                Tags.EXTRA_MEAL_TIME,
                                it.toJson()
                            )
                            isUpdate = true
                        } ?: run {
                            intentAdd.putExtra(
                                Tags.EXTRA_MEAL_TIME,
                                Mealtime(
                                    id = -1,
                                    sessionId = i,
                                    dateOfMeal = Utils.getDateFormat()
                                        .parse(any.dateOfMeal ?: "")?.time,
                                    timeOfMeal = Utils.getTimeFormat().parse("20:00")?.time
                                ).toJson()
                            )
                        }
                    }
                }
                startActivityForResult(
                    intentAdd,
                    RequestCode.REQUEST_ADD_MEAL
                )
            }
        }
        adapterMealOfDateHome?.onClick = {
            if (it is MealsEntity){
                val intentDetail = Intent(this, DetailMealOfDateActivity::class.java)
                intentDetail.putExtra(Tags.EXTRA_MEAL_TIME, it.dateOfMeal)
                startActivityForResult(
                    intentDetail,
                    RequestCode.REQUEST_ADD_MEAL
                )
            }
        }
        binding.btnAddInformation.setSafeOnClickListener {
            startActivityForResult(
                Intent(this, AddMealTimeActivity::class.java),
                RequestCode.REQUEST_ADD_MEAL
            )
        }

        binding.btnExportFile.setSafeOnClickListener {
            model.handleExportData(this)
        }
    }

    override fun initView() {
        model.getMealTimeList()
        setAdapterServiceHome()
    }

    override fun onObserveData() {
        super.onObserveData()
        model.listMealTimeLive.observe(this, Observer {
            adapterMealOfDateHome?.updateData(it)
        })
    }

    private fun setAdapterServiceHome() {
        adapterMealOfDateHome = CommonAdapter()
        binding.rcvMealOfDateHome.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rcvMealOfDateHome.adapter = adapterMealOfDateHome
    }
}