package com.abs.launcher.util

import kotlin.math.absoluteValue

/**
 * Created by zy on 18-1-31.
 */
fun <R : Comparable<R>> R.clamp(min: R, max: R) : R {
    return when {
        this < min -> min
        this > max -> max
        else -> this
    }
}

infix fun <T : Comparable<T>> T.clamp(pair: Pair<T, T>) : T {
    return when {
        this < pair.first -> pair.first
        this > pair.second -> pair.second
        else -> this
    }
}

infix fun <T : Comparable<T>> T.minTo(other: T) : T {
    return if (this < other) other else this
}

infix fun <T : Comparable<T>> T.maxTo(other: T) : T {
    return if (this > other) other else this
}

fun <T> Boolean.select(a: T, b: T) : T {
    return if (this) a else b
}

fun <T> select(a: T, b: T, f: () -> Boolean): T {
    return f().select(a, b)
}

fun <T> select(vararg a: T, f: ()-> Int): T {
    return a[f()]
}

fun Int.sqrt() = kotlin.math.sqrt(absoluteValue.toFloat())
fun Float.sqrt() = kotlin.math.sqrt(absoluteValue)