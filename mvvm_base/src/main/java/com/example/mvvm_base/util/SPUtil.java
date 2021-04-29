package com.example.mvvm_base.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharePreference 工具类
 */
public final class SPUtil {

    public static void putString(Context context, String key, String value) {
        SharedPreferences share = context.getSharedPreferences("LoginInfos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("LoginInfos", Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences share = context.getSharedPreferences("LoginInfos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("LoginInfos", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences share = context.getSharedPreferences("LoginInfos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("LoginInfos", Context.MODE_PRIVATE);
        return sp.getInt(key, -1);
    }

    /**
     * 清除信息
     *
     * @param context
     */
    public static void clearInfo(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginInfos",
                Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

}
