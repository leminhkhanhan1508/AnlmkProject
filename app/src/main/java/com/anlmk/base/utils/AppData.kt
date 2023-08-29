package com.anlmk.base.utils

class AppData {

    companion object {
        private var instance: AppData? = null

        @JvmStatic
        fun g(): AppData {
            if (instance == null)
                instance = AppData()
            return instance!!
        }
    }
    var language: String? = Tags.text_defaultUAGE
}