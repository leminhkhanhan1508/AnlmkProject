package com.anlmk.base.ui.activities.addMealTime

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.anlmk.base.R
import com.anlmk.base.data.impl.Mealtime
import com.anlmk.base.databinding.ActivityAddMealTimeBinding
import com.anlmk.base.databinding.ActivityDetailMealDateBinding
import com.anlmk.base.extensions.deleteFileFromInternalStorage
import com.anlmk.base.extensions.setSafeOnClickListener
import com.anlmk.base.extensions.toJson
import com.anlmk.base.ui.adapters.CommonAdapter
import com.anlmk.base.ui.base.BaseActivity
import com.anlmk.base.utils.RequestCode
import com.anlmk.base.utils.Tags
import com.anlmk.base.utils.Utils
import org.koin.android.viewmodel.ext.android.viewModel

class DetailMealOfDateActivity : BaseActivity() {
    override val model: MealsTimeViewModel by viewModel()
    override val binding by lazy {
        ActivityDetailMealDateBinding.inflate(layoutInflater)
    }
    private var adapterMealOfDateDetail: CommonAdapter? = null
    private var dateOfMeals = ""
    override fun onBackPressed() {
        setResult(RESULT_OK)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.REQUEST_ADD_MEAL -> {
                if (resultCode == RESULT_OK) {
                    model.getMealTimeByDateList(dateOfMeals)
                }
            }
        }
    }

    override fun initView() {
        super.initView()
        setAdapterItemDetail()
        if (intent.hasExtra(Tags.EXTRA_MEAL_TIME)) {
            dateOfMeals = intent.getStringExtra(Tags.EXTRA_MEAL_TIME) ?: ""
            binding.toolbar.txtTitle.text = dateOfMeals
            model.getMealTimeByDateList(dateOfMeals)
        }

    }

    override fun onListener() {
        super.onListener()
        binding.toolbar.icOther.setSafeOnClickListener {
            onBackPressed()
        }
        adapterMealOfDateDetail?.onClick = {
            val intentAdd = Intent(this, AddMealTimeActivity::class.java)
            intentAdd.putExtra(
                Tags.EXTRA_MEAL_TIME,
                it?.toJson()
            )
            startActivityForResult(
                intentAdd,
                RequestCode.REQUEST_ADD_MEAL
            )
        }
        adapterMealOfDateDetail?.onSwipeDelete = {
            if (it is Mealtime) {
                model.deleteMealTimeItem(it)
            }
        }
    }

    override fun onObserveData() {
        super.onObserveData()
        model.apply {
            listMealTimeByDateLive.observe(this@DetailMealOfDateActivity, Observer {
                adapterMealOfDateDetail?.updateData(it as List<Any>?)
            })
            deleteMealtime.observe(this@DetailMealOfDateActivity, Observer {
                it.imageOfFood?.let { fileName ->
                    this@DetailMealOfDateActivity.deleteFileFromInternalStorage(
                        fileName
                    )
                }
                Toast.makeText(
                    this@DetailMealOfDateActivity,
                    this@DetailMealOfDateActivity.getString(R.string.delete_item_success),
                    Toast.LENGTH_SHORT
                ).show()
            })
        }
    }

    private fun setAdapterItemDetail() {
        adapterMealOfDateDetail = CommonAdapter()
        binding.rcvMealOfDate.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rcvMealOfDate.adapter = adapterMealOfDateDetail
        adapterMealOfDateDetail?.let {
            val callback = CommonAdapter.SwipeToDeleteCallback(it)
            val itemTouchHelper = ItemTouchHelper(callback)
            itemTouchHelper.attachToRecyclerView(binding.rcvMealOfDate)
        }

    }


}