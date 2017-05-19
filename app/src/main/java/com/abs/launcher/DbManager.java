package com.abs.launcher;

import android.provider.BaseColumns;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Created by zy on 17-5-18.
 */

public class DbManager {

    static final String TABLE_FAVORITE = "favorites";
    static final String TABLE_ALL_APPS = "applications";
    static final String TABLE_HIDDEN_APPS = "hidden_apps";

    public interface Columns extends BaseColumns {
        @COLUMN(TYPE.INTEGER)
        String CELL_X = "cellX";
        @COLUMN(TYPE.INTEGER)
        String CELL_Y = "cellY";
        @COLUMN(TYPE.INTEGER)
        String SPAN_X = "spanX";
        @COLUMN(TYPE.INTEGER)
        String SPAN_Y = "spanY";
        @COLUMN(TYPE.INTEGER)
        String SCREEN = "screen";
        @COLUMN(TYPE.INTEGER)
        String CONTAINER = "container";
        @COLUMN(TYPE.INTEGER)
        String ITEM_TYPE = "itemType";
        @COLUMN(TYPE.INTEGER)
        String CATEGORY = "category";

        int CONTAINER_DESKTOP = -100;
        int CONTAINER_DOCKBAR = -101;

        int ITEM_TYPE_APPLICATION = 1;
        int ITEM_TYPE_SHORTCUT = 2;
        int ITEM_TYPE_APP_WIDGET = 3;
        int ITEM_TYPE_FOLDER = 4;
        int ITEM_TYPE_WIDGET_VIEW = 5;
        int NO_ID = -1;
    }

    public interface Favorites extends Columns {
        @COLUMN(TYPE.TEXT)
        String INTENT = "intent";
        @COLUMN(TYPE.BLOB)
        String ICON = "icon";
        @COLUMN(TYPE.TEXT)
        String TITLE = "title";
        @COLUMN(TYPE.BLOB)
        String CUSTOM_ICON = "customIcon";
        @COLUMN(TYPE.TEXT)
        String CUSTOM_TITLE = "customTitle";
        @COLUMN(TYPE.TEXT)
        String TITLE_RESOURCE = "titleResource";
        @COLUMN(TYPE.TEXT)
        String TITLE_PACKAGE = "titlePackage";
        @COLUMN(TYPE.TEXT)
        String ICON_RESOURCE = "iconResource";
        @COLUMN(TYPE.TEXT)
        String ICON_PACKAGE = "iconPackage";
        @COLUMN(TYPE.INTEGER)
        String SYSTEM = "system";
        @COLUMN(TYPE.INTEGER)
        String WIDGET_ID = "widgetId";
    }

    public interface Applications extends BaseColumns {

        String TITLE = "title";
        String INTENT = "intent";
        String ITEM_TYPE = "itemType";
        String STORAGE = "storate";
        String SYSTEM = "system";
    }

    public interface HiddenApplications extends BaseColumns {

    }

    private enum TYPE {
        TEXT("TEXT"), INTEGER("INTEGER"), BLOB("BLOB");
        TYPE(String value) {
            this.value = value;
        }
        String value;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    private @interface COLUMN {
        TYPE value() default TYPE.TEXT;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    private @interface PRIMARY_KEY {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    private @interface NOT_NULL {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    private @interface DEFAULT_VALUE {
        String value() default "";
    }

    public static String generateSqlCreation(Class<?> cls) {
        if (!BaseColumns.class.isAssignableFrom(cls)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        try {
            DumpState state = new DumpState();
            dumpColumns(cls, sb, state);
            if (state.primary == null && state.primaryCandidate != null) {
                if (state.first) {
                    sb.append("_id INTEGER PRIMARY KEY");
                } else {
                    sb.insert(0, "_id INTEGER PRIMARY KEY,");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return sb.toString();
    }

    private static void dumpColumns(Class<?> cls, StringBuilder sb,
                                    DumpState state) throws Exception {
        Class<?>[] clss = cls.getInterfaces();
        if (clss != null) {
            for (Class<?> c : clss) {
                if (BaseColumns.class.isAssignableFrom(c)) {
                    dumpColumns(c, sb, state);
                }
            }
        }
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            if (!String.class.isAssignableFrom(f.getType())) {
                continue;
            }
            String columnName = (String) f.get(cls);
            if (state.primaryCandidate == null && "_id".equals(columnName)) {
                state.primaryCandidate = f;
                continue;
            }
            COLUMN column = f.getAnnotation(COLUMN.class);
            if (column == null) {
                continue;
            }
            String columnType = column.value().value;

            boolean primaryKey = f.getAnnotation(PRIMARY_KEY.class) != null;
            if (primaryKey) {
                if (state.primary != null) {
                    throw new Exception();
                }
                state.primary = f;
                if (state.primaryCandidate != null) {
                    if (state.first) {
                        state.first = false;
                        sb.append("_id INTEGER");
                    } else {
                        sb.insert(0, "_id INTEGER");
                    }
                    state.primaryCandidate = null;
                }
            }

            boolean notNull = f.getAnnotation(NOT_NULL.class) != null;

            String columnDefault = "";
            DEFAULT_VALUE defaultValue = f.getAnnotation(DEFAULT_VALUE.class);
            if (defaultValue != null) {
                columnDefault = defaultValue.value();
            }

            if (state.first) {
                state.first = false;
            } else {
                sb.append(",");
            }
            sb.append(columnName + " " + columnType + columnDefault);
            if (primaryKey) {
                sb.append(" PRIMARY KEY");
            }
            if (notNull) {
                sb.append(" NOT NULL");
            }
            if (columnDefault != null && columnDefault.length() > 0) {
                sb.append(" DEFAULT ").append(columnDefault);
            }
        }
    }

    private static class DumpState {
        boolean first = true;
        Field primary;
        Field primaryCandidate;
    }
}
