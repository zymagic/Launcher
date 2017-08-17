package com.abs.launcher.util;

import java.util.Collection;

/**
 * Created by zy on 17-8-18.
 */

public class Utils {
    private Utils() {}

    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }
}
