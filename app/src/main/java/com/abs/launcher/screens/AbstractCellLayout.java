package com.abs.launcher.screens;

import com.abs.launcher.ItemInfo;

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

    public void resolveLayout(CellItemInfo info) {

    }

    public void getCellRect(int cellX, int cellY, int spanX , int spanY) {

    }

    public static class CellItemInfo {
        int x, y, width, height;
        boolean resolved;
        ItemInfo item;
        public void pendingResolve(ItemInfo info) {
            resolved = false;
            this.item = info;
        }

        public void resolved() {
            resolved = true;
            this.item = null;
        }
    }
}
