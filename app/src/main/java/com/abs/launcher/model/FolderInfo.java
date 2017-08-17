package com.abs.launcher.model;

import com.abs.launcher.ItemAction;
import com.abs.launcher.ItemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 2017/4/2.
 */

public class FolderInfo extends ItemInfo {

    ArrayList<ItemInfo> folderItems = new ArrayList<>();

    @Override
    public List<ItemAction> getDeleteAction(List<ItemAction> actions) {
        if (actions == null) {
            actions = new ArrayList<>();
        }
        return actions;
    }
}
