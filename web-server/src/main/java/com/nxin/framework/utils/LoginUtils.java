package com.nxin.framework.utils;

public class LoginUtils {
    private static final ThreadLocal<String> USERNAME = new ThreadLocal<>();

    public static final String DEFAULT_VALUE = "SYSTEM";

    public static void setUsername(String username) {
        USERNAME.set(username);
    }

    public static String getUsername() {
        return USERNAME.get() == null ? DEFAULT_VALUE : USERNAME.get();
    }

    public static void remove() {
        USERNAME.remove();
    }
}
