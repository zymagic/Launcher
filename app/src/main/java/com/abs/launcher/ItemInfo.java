package com.abs.launcher;

import java.util.List;

/**
 * Created by ZY on 2017/4/2.
 */

public abstract class ItemInfo {
    int id;
    int type;
    int container, x, y, spanX, spanY;

    public abstract List<ItemAction> getDeleteAction(List<ItemAction> actions);

    public List<ItemAction> getPopupAction(List<ItemAction> actions) {
        return actions;
    }
}
