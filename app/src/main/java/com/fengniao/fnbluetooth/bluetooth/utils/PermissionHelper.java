package com.fengniao.fnbluetooth.bluetooth.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * Created by songdanqi on 2015/12/23.
 */
public final class PermissionHelper {


    /**
     * 检测一项权限
     *
     * @param activity
     * @param permission
     * @return
     */
    public static boolean checkPermission(@NonNull Activity activity, @NonNull String permission) {
//        if (Build.VERSION.SDK_INT < 23) {
//            return true;
//        }
        boolean is = (ContextCompat.checkSelfPermission(activity,
                permission) == PackageManager.PERMISSION_GRANTED);
        return is;
    }

    /**
     * 检测多项权限
     *
     * @param activity
     * @param permissions
     * @return 返回未同意的权限，全部同意时候返回null
     */
    public static String[] checkPermissions(@NonNull Activity activity, @NonNull String[] permissions) {
//        if (Build.VERSION.SDK_INT < 23) {
//            return true;
//        }
        ArrayList<String> noAgreeList = null;
        String[] noAgree = null;
        for (int i = 0; i < permissions.length; i++) {
            if (!checkPermission(activity, permissions[i])) {
                if (noAgreeList == null) {
                    noAgreeList = new ArrayList();
                }
                noAgreeList.add(permissions[i]);
            }
        }
        if (noAgreeList != null && noAgreeList.size() != 0) {
            noAgree = new String[noAgreeList.size()];
            noAgreeList.toArray(noAgree);
        }

        return noAgree;
    }


    /**
     * 检测某权限，如果app尚未允许,在Activity中申请一项权限
     *
     * @param activity
     * @param permission
     * @param requestCode
     * @return 权限已通过返回true
     */
    public static boolean checkAndRequest(@NonNull Activity activity, @NonNull String permission, @NonNull int requestCode) {
        if (!checkPermission(activity, permission)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{permission},
                    requestCode);
            return false;
        }
        return true;
    }


    /**
     * 检测某权限，如果app尚未允许,在Activity中申请多项权限
     *
     * @param activity
     * @param permissions
     * @param requestCode
     * @return 权限已通过返回true
     */
    public static boolean checkAndRequest(@NonNull Activity activity, @NonNull String[] permissions, @NonNull int requestCode) {
        String[] noAgreePermission = checkPermissions(activity, permissions);
        if (noAgreePermission != null) {
            ActivityCompat.requestPermissions(activity,
                    noAgreePermission,
                    requestCode);
            return false;
        }
        return true;
    }

    /**
     * 检测某权限，如果app尚未允许,在Fragment中请求一项权限
     *
     * @param fragment
     * @param permission
     * @param requestCode
     * @return 权限已通过返回true
     */
    public static boolean checkAndRequest(@NonNull Fragment fragment, @NonNull String permission, @NonNull int requestCode) {
        if (!checkPermission(fragment.getActivity(), permission)) {
            fragment.requestPermissions(new String[]{permission},
                    requestCode);
            return false;
        }
        return true;
    }

    /**
     * 检测某权限，如果app尚未允许,在Fragment中请求多项权限
     *
     * @param fragment
     * @param permissions
     * @param requestCode
     * @return 权限已通过返回true
     */
    public static boolean checkAndRequest(@NonNull Fragment fragment, @NonNull String[] permissions, @NonNull int requestCode) {
        String[] noAgreePermission = checkPermissions(fragment.getActivity(), permissions);
        if (noAgreePermission != null) {
            fragment.requestPermissions(noAgreePermission,
                    requestCode);
            return false;
        }
        return true;
    }


}
