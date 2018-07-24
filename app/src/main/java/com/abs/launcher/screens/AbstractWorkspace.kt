package com.abs.launcher.screens

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

class AbstractWorkspace(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : ViewGroup(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    val config: CellLayoutConfig

    init {
        config = CellLayoutConfig()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount == 0) {
            return
        }

        var left = 0
        for (i in 0..childCount) {
            val child = getChildAt(i)
            child.layout(left, 0, left + r - l, b - t)
            left += r - l
        }
    }


}