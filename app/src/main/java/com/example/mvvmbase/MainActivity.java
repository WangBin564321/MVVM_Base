package com.example.mvvmbase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mvvm_base.util.DownloadUtil;
import com.example.mvvm_base.util.PermissionUtil;

import java.io.File;
import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_test:
                Log.e(TAG, "onClick: =============>");
                try {
                    DownloadUtil.getInstance().checkToDownload(this, "test", "test.apk", "http://192.168.10.10/eomupload/apk/upgrade/app-release.apk", new DownloadUtil.DownLoadListener() {
                        @Override
                        public void onDownloading(int progress) {
                            Log.e(TAG, "onDownloading: =============>" + progress);
                        }

                        @Override
                        public void onDownloadSuccess(File file) {
                            Log.e(TAG, "onDownloadSuccess: =============>");
                        }

                        @Override
                        public void onDownloadFailed(Exception e) {
                            Log.e(TAG, "onDownloadFailed: =============>");
                        }

                        @Override
                        public void noStorageError() {

                        }
                    });
                } catch (MalformedURLException e) {
                    Log.e(TAG, "onClick: =============>" + e.getMessage());
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}