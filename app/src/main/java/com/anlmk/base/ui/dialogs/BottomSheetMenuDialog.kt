package com.anlmk.base.ui.dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.anlmk.base.R
import com.anlmk.base.data.`object`.CommonEntity
import com.anlmk.base.databinding.BottomSheetMenuDialogLayoutBinding
import com.anlmk.base.extensions.getScreenWidth
import com.anlmk.base.ui.adapters.BottomMenuAdapter
import com.anlmk.base.ui.adapters.CommonAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

class BottomSheetMenuDialog: BottomSheetDialog {
    private var binding = BottomSheetMenuDialogLayoutBinding.inflate(layoutInflater)
    constructor(context: Context) : super(context, R.style.AppBottomSheetDialogTheme)
    private var adapterMenuBottomSheet: BottomMenuAdapter? = null
    var onClick: (Any) -> Unit = {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.BOTTOM)
        setContentView(
            binding.root,
            LinearLayout.LayoutParams(
                context.getScreenWidth(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        setCancelable(true)

    }


    private fun initAdapter() {
        adapterMenuBottomSheet = BottomMenuAdapter()
        binding.rcvMenu.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rcvMenu.adapter = adapterMenuBottomSheet
        adapterMenuBottomSheet?.onClick = {
            onClick(it)
            this.dismiss()
        }
    }

    fun setDataMenu(listMenu: List<Any>?): BottomSheetMenuDialog {
        initAdapter()
        adapterMenuBottomSheet?.updateData(listMenu)
        return this
    }

    fun setTitleDialog(title: String) {
        binding.tvTitle.text = title
    }


}