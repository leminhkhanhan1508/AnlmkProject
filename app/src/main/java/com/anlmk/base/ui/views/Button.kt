package com.anlmk.base.ui.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.anlmk.base.R

class Button : AppCompatButton {
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }


    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {

            val a = context.obtainStyledAttributes(attrs, R.styleable.TextView)

//            typeface = if (a.hasValue(R.styleable.TextView_cusfont)) {
//                val value = a.getInt(R.styleable.TextView_cusfont, 0)
//                getFontMult(value, context)
//            } else {
//                getFontMult(0, context)
//            }
        }
    }
//    fun setFont(int: Int){
//        typeface = getFontMult(int, context)
//    }

}