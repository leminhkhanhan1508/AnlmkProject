package com.anlmk.base.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.Window
import com.anlmk.base.R
import com.anlmk.base.ui.views.CircleProgressBar

class LoadingDialog(context: Context) : Dialog(context) {

    private lateinit var progress: CircleProgressBar
    private val handler = Handler()
    private lateinit var run: Runnable
    private var dialog: Dialog? = null

    init {
        init()
    }

    private fun init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        dialog = this
        run = Runnable {
            try {
                if (dialog != null && isShowing) {
                    dismiss()
                }
            } catch (e: Exception) {
                //LogVnp.Shape1(Shape1);
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_dialog_layout)
        progress = findViewById(R.id.progressLoading)
    }

    override fun onStop() {
        super.onStop()
    }

    fun setCancel(isCancel: Boolean) {
        setCancelable(isCancel)
        setCanceledOnTouchOutside(isCancel)
    }

    override fun show() {
        super.show()
        handler.postDelayed(run, 180000)
    }

    override fun dismiss() {
        try {
            super.dismiss()
            handler.removeCallbacks(run)
        } catch (ex: Exception) {

        }
    }
}
