package com.abs.launcher;

import java.util.List;

/**
 * Created by ZY on 2017/4/2.
 */

public abstract class ItemInfo {
    public static final int NO_ID = -1;
    int id = -1;
    int type;
    int container, screen, x, y, spanX, spanY;
    int category;

    public abstract List<ItemAction> getDeleteAction(List<ItemAction> actions);

    public List<ItemAction> getQuickAction(List<ItemAction> actions) {
        return actions;
    }
}
