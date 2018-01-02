package com.abs.launcher.screens;

/**
 * Created by ZY on 2017/4/2.
 */

public abstract class AbstractCellLayout {
    private int countX, countY;
    private int horizontalPadding, verticalStartPadding, verticalEndPadding;

    private int cellWidth, cellHeight;
    private int cellHGap, cellVGap;

    public abstract int getWidth();
    public abstract int getHeight();

    public void setup() {

    }

    public void getCellRect(int cellX, int cellY, int spanX , int spanY) {

    }

}
