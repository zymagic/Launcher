package com.abs.launcher.screens

import android.content.Context
import android.view.View
import android.view.ViewGroup

/**
 * Created by zy on 17-12-10.
 */
class CellLayout(context: Context, val config: CellLayoutConfig) : ViewGroup(context) {

    fun Int.getMode() = View.MeasureSpec.getMode(this)
    fun Int.getSize() = View.MeasureSpec.getSize(this)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (childCount == 0) {
            return
        }

        val widthMode = widthMeasureSpec.getMode()
        val heightMode = heightMeasureSpec.getMode()
        if (widthMode != View.MeasureSpec.EXACTLY || heightMode != View.MeasureSpec.EXACTLY) {
            throw IllegalStateException("Only exact mode available")
        }

        val widthSize = widthMeasureSpec.getSize()
        val heightSize = heightMeasureSpec.getSize()

        for (i in 0..childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as LayoutParams
            lp.setup(widthSize, heightSize, config.hStartPadding, config.hEndPadding, config.vStartPadding, config.vEndPadding, config.countX, config.countY)
            child.measure(MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY))
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount == 0) {
            return
        }
        for (i in 0..childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as LayoutParams
            child.layout(lp.x, lp.y, lp.width, lp.height)
        }
    }

    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        return LayoutParams()
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams?): ViewGroup.LayoutParams {
        return LayoutParams()
    }

    class LayoutParams(var cellX: Int = 0, var cellY: Int = 0, var spanX: Int = 1, var spanY: Int = 1) : ViewGroup.LayoutParams(0, 0) {
        var x: Int = 0
        var y: Int = 0

        fun setup(w: Int, h: Int, hs: Int, he: Int, vs: Int, ve: Int, cx: Int, cy: Int) {
            val hSpace = w - hs - he
            val uh = hSpace / cx
            val vSpace = h - vs - ve
            val uv = vSpace / cy
            if (spanX < cx) {
                width = spanX * uh
                x = hs + cellX * uh
            } else {
                width = w
                x = 0
            }
            height = spanY * uv
            y = vs + cellY * uv
        }
    }

}