package com.abs.launcher.util

import android.widget.Toast
import com.abs.launcher.LauncherApp

/**
 * Created by zy on 17-12-27.
 */
fun toast(duration: Int = Toast.LENGTH_SHORT, txt: () -> String) {
    Toast.makeText(LauncherApp.instance, txt(), duration)
}

fun testToast() {
    toast { "test" }
}