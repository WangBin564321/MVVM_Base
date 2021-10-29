package com.example.mvvm_base.util;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static android.content.ContentValues.TAG;

/**
 * desc: 权限请求工具类
 * author:bWang
 * date:2020/12/9 9:56
 * tip: activity、fragment继承BaseActivity、BaseFragment直接使用
 * 未继承的需添加:
 *
 * @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
 * PermissionUtil.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
 * }
 * exam:
 * PermissionUtil.getInstance().requestPermissions(activity, "下载必须权限", 11, permissions, new PermissionUtil.PermissionsResultCallback() {
 * @Override public void hasPermissions() throws MalformedURLException {}
 * @Override public void onPermissionsGranted() throws MalformedURLException {}
 * @Override public void onPermissionsDenied(List<String> perms) {}
 * });
 */
public class PermissionUtil implements EasyPermissions.PermissionCallbacks {

    int requestCode;
    String rationale;
    private static PermissionUtil instance;
    private PermissionsResultCallback mPermissionsResult;

    public static PermissionUtil getInstance() {
        if (instance == null) { //Single Checked
            synchronized (PermissionUtil.class) {
                if (instance == null) //Double Checked
                    instance = new PermissionUtil();
            }
        }
        return instance;
    }


    public void requestPermissions(Activity activity, String message, int requestCode, String[] permissions, PermissionsResultCallback permissionsResult) {
        if (EasyPermissions.hasPermissions(activity, permissions)) {
            permissionsResult.hasPermissions();
            return;
        }
        EasyPermissions.requestPermissions(activity, message, requestCode, permissions);
        this.requestCode = requestCode;
        this.rationale = message;
        this.mPermissionsResult = permissionsResult;
    }

    public void requestPermissions(Fragment fragment, String message, int requestCode, String[] permissions, PermissionsResultCallback permissionsResult) {
        if (EasyPermissions.hasPermissions(fragment.getContext(), permissions)) {
            permissionsResult.hasPermissions();
            return;
        }
        EasyPermissions.requestPermissions(fragment, message, requestCode, permissions);
        this.requestCode = requestCode;
        this.rationale = message;
        this.mPermissionsResult = permissionsResult;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.e(TAG, "onPermissionsGranted: ===============>");
        mPermissionsResult.onPermissionsGranted();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.e(TAG, "onPermissionsDenied: ===============>");
        mPermissionsResult.onPermissionsDenied(perms);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public interface PermissionsResultCallback {
        void hasPermissions();

        void onPermissionsGranted();

        void onPermissionsDenied(List<String> perms);
    }
}
