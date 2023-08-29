package com.anlmk.base.data.domain

object Domain {
    private const val DOMAIN_TEST = "http://172.20.10.4:1234/"
    fun getDomain(appCode: String): String {
        return when (appCode) {
            else -> {
                DOMAIN_TEST
            }
        }
    }
}