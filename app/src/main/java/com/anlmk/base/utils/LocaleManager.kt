package com.anlmk.base.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.anlmk.base.R
import java.util.*

object LocaleManager {
    fun setLocale(c: Context): Context {
        return setNewLocale(c, getLanguage(c))
    }

    fun setNewLocale(c: Context, language: String): Context {
        return updateResources(c, language)
    }

    fun getLanguage(c: Context?): String {
        if (c != null) {
            setLanguageDefault(c)
        }
        return Tags.text_defaultUAGE
    }

    private fun setLanguageDefault(c: Context) {

    }
    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val res = context.resources
        val config = Configuration(res.configuration)
        return if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context.createConfigurationContext(config)
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.displayMetrics)
            context
        }

    }
}