package com.example.mvvm_base.util;

import android.Manifest;
import android.app.Activity;
import android.util.Log;


import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pub.devrel.easypermissions.AppSettingsDialog;


/**
 * desc: 下载工具类
 * date:2020/12/9 9:26
 * author:bWang
 * <p>
 * tip:转至PermissionUtil-tip,下载AndroidManifest记得添加权限
 * exam:
 * try {
 * DownloadUtil.getInstance().checkToDownload(this, "test", "test.apk", "http://192.168.10.10/eomupload/apk/upgrade/app-release.apk", new DownloadUtil.DownLoadListener() {
 *
 * @Override public void onDownloading(int progress) {}
 * @Override public void onDownloadSuccess(File file) {}
 * @Override public void onDownloadFailed(Exception e) {}
 * @Override public void noStorageError() {}
 * });
 * } catch (MalformedURLException e) {
 * e.printStackTrace();
 * }
 */
public class DownloadUtil {

    private static final String TAG = "DownloadUtil";

    private static DownloadUtil instance;

    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};


    public static DownloadUtil getInstance() {
        if (instance == null) { //Single Checked
            synchronized (DownloadUtil.class) {
                if (instance == null) //Double Checked
                    instance = new DownloadUtil();
            }
        }
        return instance;
    }

    public void checkToDownload(Activity activity, String dirName, String fileName, String urlStr, DownLoadListener listener) {
            PermissionUtil.getInstance().requestPermissions(activity, "下载必须权限", 11, permissions, new PermissionUtil.PermissionsResultCallback() {
                @Override
                public void hasPermissions()  {
                    downloadFile(Constants.ABSOLUTE_PATH + "/" + dirName, fileName, urlStr, listener);
                }

                @Override
                public void onPermissionsGranted()  {
                    downloadFile(Constants.ABSOLUTE_PATH + "/" + dirName, fileName, urlStr, listener);
                }

                @Override
                public void onPermissionsDenied(List<String> perms) {
                    new AppSettingsDialog.Builder(activity).setTitle("申请权限").setRequestCode(11).setRationale("下载必须权限").build().show();
                }
            });
    }

    public void checkToDownload(Fragment fragment, String dirName, String fileName, String urlStr, DownLoadListener listener) {
        PermissionUtil.getInstance().requestPermissions(fragment, "下载必须权限", 11, permissions, new PermissionUtil.PermissionsResultCallback() {
            @Override
            public void hasPermissions()  {
                downloadFile(Constants.ABSOLUTE_PATH + "/" + dirName, fileName, urlStr, listener);
            }

            @Override
            public void onPermissionsGranted()  {
                downloadFile(Constants.ABSOLUTE_PATH + "/" + dirName, fileName, urlStr, listener);
            }

            @Override
            public void onPermissionsDenied(List<String> perms) {
                new AppSettingsDialog.Builder(fragment.getActivity()).setTitle("申请权限").setRequestCode(11).setRationale("下载必须权限").build().show();
            }
        });
    }


    public void downloadFile(String dirName, String fileName, String urlStr, DownLoadListener listener) {
        if (exitDir(dirName)) {
            final long startTime = System.currentTimeMillis();
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = null;
            try {
                request = new Request.Builder().url(new URL(urlStr)).build();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // 下载失败
                    Log.e(TAG, "onFailure: =============>" + e.getMessage());
                    listener.onDownloadFailed(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len;
                    FileOutputStream fos = null;
                    // 储存下载文件的目录

                    try {
                        is = response.body().byteStream();
                        long total = response.body().contentLength();
                        File file = createFile(dirName, fileName);
                        fos = new FileOutputStream(file);
                        long sum = 0;
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            sum += len;
                            int progress = (int) (sum * 1.0f / total * 100);
                            // 下载中
                            listener.onDownloading(progress);
                            Log.d(TAG, "onDownloading: =============>");
                        }
                        fos.flush();
                        // 下载完成
                        listener.onDownloadSuccess(file);
                        Log.d(TAG, "onDownloadSuccess: =============>" + (System.currentTimeMillis() - startTime));
                    } catch (Exception e) {
                        e.printStackTrace();
                        listener.onDownloadFailed(e);
                        Log.e(TAG, "onDownloadFailed: =============>" + e.getMessage());
                    } finally {
                        try {
                            if (is != null)
                                is.close();
                        } catch (IOException e) {
                        }
                        try {
                            if (fos != null)
                                fos.close();
                        } catch (IOException e) {
                        }
                    }
                }
            });
        } else
            listener.noStorageError();
    }

    /**
     * 在SD卡的指定目录上创建文件
     *
     * @param fileName
     */
    public File createFile(String dirName, String fileName) {
        File file = new File(dirName, fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public boolean exitDir(String dirName) {
        //判断SD卡是否存在
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            File file = new File(dirName);
            if (!file.exists()) {
                file.mkdir();
            }
        } else
            return false;
        return true;
    }

    public interface DownLoadListener {
        void onDownloading(int progress);

        void onDownloadSuccess(File file);

        void onDownloadFailed(Exception e);

        void noStorageError();
    }


}
