package com.anlmk.base.di

import com.anlmk.base.ui.dialogs.LoadingDialog
import com.anlmk.base.ui.dialogs.ConfirmDialog
import org.koin.dsl.module

val dialogs = module {
    factory { createLoadingDialog() }
    factory { createConfirmDialog() }
}

fun createLoadingDialog(): LoadingDialog {
    return LoadingDialog(Common.currentActivity)
}

fun createConfirmDialog(): ConfirmDialog {
    return ConfirmDialog(Common.currentActivity)
}
