package com.example.mvvmbase;

import android.app.Activity;

import androidx.annotation.NonNull;

import java.net.MalformedURLException;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Description:
 * Author:bWang
 * Date:2020/12/9 9:56
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


    public void requestPermissions(Activity activity, String message, int requestCode, String[] permissions, PermissionsResultCallback permissionsResult) throws MalformedURLException {
        if (EasyPermissions.hasPermissions(activity, permissions)) {
            permissionsResult.hasPermissions();
            return;
        }
        EasyPermissions.requestPermissions(activity, message, requestCode, permissions);
        this.requestCode = requestCode;
        this.rationale = message;
        this.mPermissionsResult = permissionsResult;

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        try {
            mPermissionsResult.onPermissionsGranted();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        mPermissionsResult.onPermissionsDenied(perms);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public interface PermissionsResultCallback {
        void hasPermissions() throws MalformedURLException;

        void onPermissionsGranted() throws MalformedURLException;

        void onPermissionsDenied(List<String> perms);
    }
}
