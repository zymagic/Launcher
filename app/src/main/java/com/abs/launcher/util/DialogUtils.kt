package com.abs.launcher.util

import android.app.AlertDialog
import android.content.DialogInterface
import com.abs.launcher.LauncherApp

/**
 * Created by zy on 17-12-27.
 */
fun alert(init: AlertDialog.Builder.() -> Unit): AlertDialog {
    return AlertDialog.Builder(LauncherApp.instance).apply(init).show()
}

fun AlertDialog.Builder.yes(txt: Int = android.R.string.ok, onClick: ()-> Unit = {}): AlertDialog.Builder {
    if (txt != 0) {
        setPositiveButton(txt, {_, _ -> onClick()})
    }
    return this
}

fun AlertDialog.Builder.yes(txt: String, onClick: ()-> Unit = {}): AlertDialog.Builder {
    setPositiveButton(txt, {_, _ -> onClick()})
    return this
}

fun AlertDialog.Builder.no(txt: String, onClick: ()-> Unit = {}): AlertDialog.Builder {
    if (txt != null && txt.isNotEmpty()) {
        setNegativeButton(txt, {_,_ -> onClick()})
    }
    return this
}

fun AlertDialog.Builder.no(txt: Int = android.R.string.cancel, onClick: ()-> Unit = {}): AlertDialog.Builder {
    if (txt != 0) {
        setNegativeButton(txt, {_,_ -> onClick()})
    }
    return this
}

fun AlertDialog.Builder.onCancel(c: () -> Unit): AlertDialog.Builder {
    setOnCancelListener({_ -> c})
    return this
}

fun AlertDialog.onDismiss(dismiss: () -> Unit): AlertDialog {
    setOnDismissListener({_ -> dismiss()})
    return this
}

fun AlertDialog.onCancel(dismiss: () -> Unit) : AlertDialog {
    setOnCancelListener({_ -> dismiss()})
    return this
}

fun ttt() {
    alert {
        setTitle("aaaa")
        setMessage("aaa")
        yes {  }
        no {  }
    }
}

