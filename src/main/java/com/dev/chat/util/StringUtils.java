package com.dev.chat.util;

import org.springframework.lang.Nullable;

public class StringUtils {
    public static boolean isEmpty(@Nullable Object str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(@Nullable Object str) {
        return !isEmpty(str);
    }


}
