package com.abs.launcher.util

import android.widget.Toast
import com.abs.launcher.LauncherApp

/**
 * Created by zy on 17-12-27.
 */
fun toast(duration: Int = Toast.LENGTH_SHORT, txt: () -> String) {
    runInLooper {
        Toast.makeText(LauncherApp.instance, txt(), duration).show()
    }
}

fun String.toast(duration: Int = Toast.LENGTH_SHORT) {
    runInLooper {
        Toast.makeText(LauncherApp.instance, this, duration).show()
    }
}

fun Int.toast(duration: Int = Toast.LENGTH_SHORT) {
    runInLooper {
        Toast.makeText(LauncherApp.instance, this, duration).show()
    }
}

fun Throwable.toast(duration: Int = Toast.LENGTH_SHORT) {
    runInLooper {
        message?.let { Toast.makeText(LauncherApp.instance, message, duration).show() }
    }
}

fun testToast() {
    toast { "test" }
}