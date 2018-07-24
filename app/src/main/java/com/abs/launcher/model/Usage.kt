package com.abs.launcher.model

data class Usage(var lastUpdateTime: Long = 0,
                 var lastCalledTime: Long = 0,
                 var calledNum: Int = 0)