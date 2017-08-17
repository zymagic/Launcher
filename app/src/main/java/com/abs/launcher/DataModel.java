package com.abs.launcher;

import com.abs.launcher.model.AppInfo;
import com.abs.launcher.model.AppWidgetInfo;
import com.abs.launcher.model.FolderInfo;
import com.abs.launcher.model.HomeItemInfo;

import java.util.ArrayList;

/**
 * Created by ZY on 2017/4/2.
 */

public class DataModel {

    ArrayList<AppInfo> applications = new ArrayList<>();

    ArrayList<HomeItemInfo> shortcuts = new ArrayList<>();

    ArrayList<FolderInfo> folders = new ArrayList<FolderInfo>();

    ArrayList<AppWidgetInfo> widgets = new ArrayList<>();


}
