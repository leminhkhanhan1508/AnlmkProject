package com.anlmk.base.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import coil.load
import com.anlmk.base.R
import com.anlmk.base.extensions.clear
import com.anlmk.base.extensions.remove
import com.anlmk.base.extensions.setSafeOnClickListener
import com.anlmk.base.extensions.show
import com.anlmk.base.ui.views.Button
import com.anlmk.base.ui.views.TextView

class ConfirmDialog(context: Context) : Dialog(context) {
    private var btnNoticeDlgRight: Button? = null
    private var btnNoticeDlgLeft: Button? = null
    private var tvNoticeDlgTitle: TextView? = null
    private var tvNoticeDlgContent: TextView? = null
    private var ivNoticeDlgLogo: ImageView? = null

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notice_dialog)
        findView()
        setTouchArea(false)

    }

    fun setTouchArea(b: Boolean) {
        setCancelable(b)
        setCanceledOnTouchOutside(b)
    }

    fun newBuild(): ConfirmDialog {
        show()
        tvNoticeDlgContent?.clear()
        btnNoticeDlgLeft?.remove()
        btnNoticeDlgRight?.show()
        tvNoticeDlgTitle?.text = context.getString(R.string.noti_popup)
        btnNoticeDlgRight?.text = context.getString(R.string.label_close)
        ivNoticeDlgLogo?.setImageResource(R.drawable.baseline_notifications_24)
        return this
    }

    fun setTitleDialog(content: String?): ConfirmDialog {
        tvNoticeDlgTitle?.text = content
        return this
    }

    fun setTitleDialog(@StringRes content: Int?): ConfirmDialog {
        content?.let {
            tvNoticeDlgTitle?.setText(it)
        }
        return this
    }

    fun setNotice(content: String?, nameButton: String? = null): ConfirmDialog {
        try {
            configShowErrorDialog(content, nameButton)
            btnNoticeDlgRight?.setSafeOnClickListener {
                dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this
    }

    fun setNotice(
        content: String?,
        nameButton: String? = null,
        callback: () -> Unit
    ): ConfirmDialog {
        try {
            configShowErrorDialog(content, nameButton)
            btnNoticeDlgRight?.setSafeOnClickListener {
                callback.invoke()
                dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this
    }

    fun setNotice(@StringRes content: Int): ConfirmDialog {
        try {
            configShowErrorDialog(context.getString(content))
            btnNoticeDlgRight?.setSafeOnClickListener {
                dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this
    }

    fun showSuccessDialog() {
        btnNoticeDlgLeft?.text = getContext().getString(R.string.continues)
        tvNoticeDlgTitle?.text = context.getString(R.string.success)
        ivNoticeDlgLogo?.setImageResource(R.drawable.baseline_notifications_24)
    }

    fun showWarningErrorDialog() {
        ivNoticeDlgLogo?.setImageResource(R.drawable.baseline_notifications_24)
    }

    fun setIconDialog(@DrawableRes icon: Int) {
        ivNoticeDlgLogo?.setImageResource(icon)
    }

    fun setIconDialog(url: String) {
        ivNoticeDlgLogo?.load(url)
    }

    fun addButtonRight(name: String? = null, onClick: () -> Unit): ConfirmDialog {
        btnNoticeDlgRight?.apply {
            setSafeOnClickListener {
                dismiss()
                onClick()
            }
            visibility = View.VISIBLE
            name?.let { text = it }
        }
        return this
    }

    fun addButtonLeft(name: String? = null, onClick: (() -> Unit)? = null): ConfirmDialog {
        btnNoticeDlgLeft?.apply {
            setSafeOnClickListener {
                dismiss()
                onClick?.invoke()
            }
            visibility = View.VISIBLE
            text = name ?: context.getString(R.string.label_cancel)
        }
        return this
    }

    private fun configShowErrorDialog(content: String?, nameButton: String? = null) {
        tvNoticeDlgContent?.text = content
        nameButton?.let {
            btnNoticeDlgRight?.text = it
        }
    }

    private fun findView() {
        btnNoticeDlgRight = findViewById(R.id.btnNoticeDlgRight)
        btnNoticeDlgLeft = findViewById(R.id.btnNoticeDlgLeft)
        tvNoticeDlgTitle = findViewById(R.id.tvNoticeDlgTitle)
        tvNoticeDlgContent = findViewById(R.id.tvNoticeDlgContent)
        ivNoticeDlgLogo = findViewById(R.id.ivNoticeDlgLogo)
    }
}
