package com.fengniao.fnbluetooth.bluetooth.utils;

import android.util.Log;

import com.fengniao.fnbluetooth.BuildConfig;


/**
 * Created by songdanqi on 2016/8/27.
 * <p>
 * 统一管理日志
 */
public final class MLog {

    private static boolean DEBUG = BuildConfig.DEBUG;
//    private static boolean DEBUG = true;
    private static final String TAG = "mfengniao";

    private MLog() {
    }

    public static void out(String msg) {
        System.out.println(TAG + ": " + msg);
    }


    public static void v(String msg) {
        if (DEBUG) {
            Log.v(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void i(String msg) {
        if (DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (DEBUG) {
            Log.w(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (DEBUG) {
            Log.v(tag, msg);
        }
    }


    public static void d(String tag, String msg) {

        if (DEBUG) {
            Log.d(tag, msg);
        }
    }


    public static void i(String tag, String msg) {

        if (DEBUG) {
            Log.i(tag, msg);
        }
    }


    public static void w(String tag, String msg) {

        if (DEBUG) {
            Log.w(tag, msg);
        }
    }


    public static void e(String tag, String msg) {

        if (DEBUG) {
            Log.e(tag, msg);
        }
    }


}
