package com.abs.launcher

/**
 * Created by zy on 18-1-3.
 */

const val DEFAULT_HOME_SCREEN_COUNT = 3
const val DEFAULT_HOME_SCREEN_INDEX = 1

fun getHomeScreenCount() : Int {
    return DEFAULT_HOME_SCREEN_COUNT
}

fun getDefaultHomeScreen() : Int {
    return DEFAULT_HOME_SCREEN_INDEX
}