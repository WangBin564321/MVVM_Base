package com.example.mvvmbase;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mvvm_base.base.BaseFragment;
import com.example.mvvm_base.util.DownloadUtil;
import com.example.mvvm_base.util.PermissionUtil;

import java.io.File;

/**
 * desc:
 * date:2021/10/29
 * author:bWang
 * <p>
 */
public class TestFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fg_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.tv_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "onClick: =============>");
                DownloadUtil.getInstance().checkToDownload(TestFragment.this, "test", "test.apk", "http://192.168.10.10/eomupload/apk/upgrade/app-release.apk", new DownloadUtil.DownLoadListener() {
                    @Override
                    public void onDownloading(int progress) {
                        Log.e("TAG", "onDownloading: =============>" + progress);
                    }

                    @Override
                    public void onDownloadSuccess(File file) {
                        Log.e("TAG", "onDownloadSuccess: =============>");
                    }

                    @Override
                    public void onDownloadFailed(Exception e) {
                        Log.e("TAG", "onDownloadFailed: =============>");
                    }

                    @Override
                    public void noStorageError() {

                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
