package com.abs.launcher.screens

import android.view.View

/**
 * Created by zy on 17-12-10.
 */
class CellLayoutManager(var countX: Int, var countY: Int) {

    var cells: MutableList<Cell> = ArrayList()
    var hStartPadding: Int = 0
    var hEndPadding: Int = 0
    var vStartPadding: Int = 0
    var vEndPadding: Int = 0

    fun Int.getMode() = View.MeasureSpec.getMode(this)
    fun Int.getSize() = View.MeasureSpec.getSize(this)

    fun measure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMode = widthMeasureSpec.getMode()
        var heightMode = heightMeasureSpec.getMode()
        if (widthMode != View.MeasureSpec.EXACTLY || heightMode != View.MeasureSpec.EXACTLY) {
            throw IllegalStateException("Only exact mode available")
        }
        var widthSize = widthMeasureSpec.getSize()
        var heightSize = heightMeasureSpec.getSize()
        for (cell in cells) {
            cell.setup(widthSize, heightSize, hStartPadding, hEndPadding, vStartPadding, vEndPadding, countX, countY)
        }
    }

    class Cell(var cellX: Int, var cellY: Int, var spanX: Int, var spanY: Int) {
        var x: Int = 0
        var y: Int = 0
        var width: Int = 0
        var height: Int = 0

        fun setup(w: Int, h: Int, hs: Int, he: Int, vs: Int, ve: Int, cx: Int, cy: Int) {
            var hSpace = w - hs - he
            var uh = hSpace / cx
            var vSpace = h - vs - ve
            var uv = vSpace / cy
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

    interface Cellable {
        fun getCell(): Cell
    }
}