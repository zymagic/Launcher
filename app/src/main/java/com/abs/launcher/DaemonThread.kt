package com.abs.launcher

import android.os.Handler
import android.os.HandlerThread

/**
 * Created by zy on 17-12-18.
 */
val WORK_THREAD: HandlerThread = HandlerThread("launcher-daemon").apply { this.start() }
val WORK_HANDLER: Handler = Handler(WORK_THREAD.looper)

fun Runnable.runInDaemon() = WORK_HANDLER.post(this)
fun Runnable.exitDaemon() = WORK_HANDLER.removeCallbacks(this)
fun runInDaemon(f: () -> Unit) = WORK_HANDLER.post(f)
