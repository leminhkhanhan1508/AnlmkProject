package com.anlmk.base.ui.activities.addMealTime

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import com.anlmk.base.R
import com.anlmk.base.data.`object`.ChooseImage
import com.anlmk.base.data.`object`.CommonEntity
import com.anlmk.base.databinding.ActivityAddMealTimeBinding
import com.anlmk.base.extensions.*
import com.anlmk.base.ui.base.BaseActivity
import com.anlmk.base.ui.dialogs.BottomSheetMenuDialog
import com.anlmk.base.utils.RequestCode
import com.anlmk.base.utils.Utils
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class AddMealTimeActivity : BaseActivity() {
    override val model: MealsTimeViewModel by viewModel()
    override val binding by lazy {
        ActivityAddMealTimeBinding.inflate(layoutInflater)
    }
    private var sessionDialog: BottomSheetMenuDialog? = null
    private var chooseImageDialog: BottomSheetMenuDialog? = null
    private var outputUri: Uri? = null
    private var sessionChoose: CommonEntity? = null
        set(value) {
            field = value
            binding.txtSession.text = value?.getTitle()
        }
    private var imageOfMeal: Bitmap? = null
        set(value) {
            field = value
            handleSetUIImageOfMeal(value)
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {
            outputUri?.let { uri ->
                setImageOfMeal(uri)
            }
        }
        if (requestCode == RequestCode.REQUEST_GALLERY && resultCode == RESULT_OK && data != null) {
            data.data?.let { uri ->
                setImageOfMeal(uri)
            }
        }
    }

    override fun initView() {
        super.initView()
        sessionDialog = BottomSheetMenuDialog(this)
        sessionDialog?.setTitleDialog(getString(R.string.choose_session))
        chooseImageDialog = BottomSheetMenuDialog(this)
        chooseImageDialog?.setTitleDialog(getString(R.string.choose_image))
        initData()
    }

    override fun onListener() {
        super.onListener()
        binding.txtDateOfMeal.setSafeOnClickListener {
            binding.txtDateOfMeal.showDialogDate()
        }
        binding.txtTimeOfMeal.setSafeOnClickListener {
            binding.txtTimeOfMeal.showDialogTimePicker()
        }
        binding.txtSession.setSafeOnClickListener {
            sessionDialog?.setDataMenu(model.getSessionMenu())?.show()
        }
        binding.imgChooseImage.setSafeOnClickListener {
            handleChooseImage()
        }
        binding.imgImageOfMeal.setSafeOnClickListener {
            handleChooseImage()
        }
        sessionDialog?.onClick = {
            if (it is CommonEntity) {
                sessionChoose = it
            }
        }
        chooseImageDialog?.onClick = {
            if (it is CommonEntity) {
                handleOpenChooseImage(it.codeFunction)
            }
        }
        binding.btnSave.setSafeOnClickListener {
            handleSaveInformation()
        }
        binding.btnCancel.setSafeOnClickListener {
            onBackPressed()
        }
        binding.toolbar.icOther.setSafeOnClickListener {
            onBackPressed()
        }
    }

    private fun handleChooseImage() {
        requestPermission(
            permissions = Utils.getListMediaPermission(),
            requestCode = RequestCode.PERMISSION_READ_MEDIA_IMAGES,
            onAction = {
                chooseImageDialog?.setDataMenu(model.setChooseImageMenu())?.show()
            }
        )
        chooseImageDialog?.setDataMenu(model.setChooseImageMenu())?.show()

    }

    override fun onBackPressed() {
        setResult(RESULT_OK)
        finish()
    }

    override fun onObserveData() {
        super.onObserveData()
        model.apply {
            insertMealtime.observe(this@AddMealTimeActivity, androidx.lifecycle.Observer {
                confirm.newBuild()
                    .setNotice(getString(R.string.add_new_information_success))
                    .setTitleDialog(getString(R.string.noti_popup))
                    .addButtonRight {
                        initData()
                    }
            })
        }
    }

    private fun handleSaveInformation() {
        if (model.validateBeforeSave(
                binding.edtFoodOfMeal.text.toString(),
                binding.edtmmolValue.text.toString()
            )
        ) {
            model.insertMealInformation(
                model.formatInformationBeforeSave(
                    binding.txtDateOfMeal.text.toString(),
                    sessionChoose,
                    binding.txtTimeOfMeal.text.toString(),
                    binding.edtFoodOfMeal.text.toString(),
                    binding.edtmmolValue.text.toString(),
                )
            )
        } else {
            confirm.newBuild().setNotice(getString(R.string.error_input_data))
        }
    }

    private fun initData() {
        //get current time to set time default for UI
        val currentTime = Calendar.getInstance().time
        val todayStr = currentTime.formatDateDefaults()
        val currencyTimeStr = currentTime.formatTimeDefaults()
        sessionChoose = model.initSession(Calendar.getInstance())
        binding.txtDateOfMeal.text = todayStr
        binding.txtTimeOfMeal.text = currencyTimeStr
        binding.edtmmolValue.text?.clear()
        binding.edtFoodOfMeal.text?.clear()
        imageOfMeal = null
    }

    private fun handleSetUIImageOfMeal(value: Bitmap?) {
        if (value != null) {
            binding.imgImageOfMeal.setImageBitmap(value)
            binding.imgChooseImage.visibility = View.GONE
            binding.imgImageOfMeal.visibility = View.VISIBLE
        } else {
            binding.imgImageOfMeal.visibility = View.GONE
            binding.imgChooseImage.visibility = View.VISIBLE
        }
    }

    private fun setImageOfMeal(uri: Uri) {
        imageOfMeal = getBitmapFromUri(uri)
    }

    fun handleOpenChooseImage(codeFunction: Int?) {
        when (ChooseImage.getTypeChooseImage(codeFunction)) {
            ChooseImage.Camera -> {
                outputUri = openCamera()
            }
            ChooseImage.Album -> {
                openFileSystemManager()
            }
        }
    }

}