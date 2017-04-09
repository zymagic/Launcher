package com.abs.launcher;

import android.graphics.drawable.Drawable;

/**
 * Created by ZY on 2017/4/2.
 */

public class ItemAction {
    private int type;
    private String label;
    private Drawable icon;
    public ItemAction(String label, Drawable icon) {
        this.label = label;
        this.icon = icon;
    }
}
