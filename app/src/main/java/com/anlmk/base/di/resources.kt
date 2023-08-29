package com.anlmk.base.di

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.*
import androidx.core.content.ContextCompat
import org.koin.dsl.module

interface ResourceProvider {
    fun getString(resourceIdentifier: Int, vararg arguments: Any = arrayOf()): String

    fun getStringArray(resourceIdentifier: Int): Array<String>

    fun getInteger(resourceIdentifier: Int): Int

    fun getIntegerArray(resourceIdentifier: Int): Array<Int>

    fun getBoolean(resourceIdentifier: Int): Boolean

    fun getColor(resourceIdentifier: Int): Int
    fun startActvity(des: Class<out Activity>): Unit

}

val utilities = module {
    single<ResourceProvider> { AndroidResourceProvider(get()) }
}
class AndroidResourceProvider
constructor(private val context: Context)
    : ResourceProvider {
    override fun startActvity(des: Class<out Activity>) {
        context.startActivity(Intent(context, des))
    }

    override fun getString(@StringRes resourceIdentifier: Int, vararg arguments: Any): String {
        return if (arguments.isNotEmpty())
            Common.currentActivity.getString(resourceIdentifier, *arguments)
        else
            Common.currentActivity.getString(resourceIdentifier)
    }

    override fun getStringArray(@ArrayRes resourceIdentifier: Int): Array<String> =
        context.resources.getStringArray(resourceIdentifier)

    override fun getInteger(@IntegerRes resourceIdentifier: Int): Int =
        context.resources.getInteger(resourceIdentifier)

    override fun getIntegerArray(@ArrayRes resourceIdentifier: Int): Array<Int> =
        context.resources.getIntArray(resourceIdentifier).toTypedArray()


    override fun getBoolean(@BoolRes resourceIdentifier: Int): Boolean =
        context.resources.getBoolean(resourceIdentifier)

    override fun getColor(@ColorRes resourceIdentifier: Int): Int =
        ContextCompat.getColor(context, resourceIdentifier)

}

@SuppressLint("StaticFieldLeak")
object Common {
    @JvmStatic lateinit  var  currentActivity: Activity
}
