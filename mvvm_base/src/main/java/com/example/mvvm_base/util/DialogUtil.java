package com.example.mvvm_base.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.example.mvvm_base.R;
import com.example.mvvm_base.base.BaseDialog;


/**
 * Dialog 工具类
 */
public class DialogUtil {


    static ProgressDialog progressDlg = null;

    public static void showDialogWait(Context ctx, String strMessage) {
        if (null == progressDlg) {
            if (ctx == null) return;
            progressDlg = new ProgressDialog(ctx);
            //设置进度条样式
            progressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //提示的消息
            progressDlg.setMessage(strMessage);
            progressDlg.setIndeterminate(false);
            progressDlg.setCancelable(true);
            progressDlg.setCanceledOnTouchOutside(false);
            progressDlg.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            progressDlg.show();
        }
    }

    public static void showDialogWait1(Context ctx, String strMessage) {
        if (ctx == null) return;
        progressDlg = new ProgressDialog(ctx);
        //提示的消息
        progressDlg.setMessage(strMessage);
        progressDlg.setCanceledOnTouchOutside(false);
        progressDlg.show();
    }

    private static Dialog dialog;

    /**
     * 结束进度条
     */
    public static void stopProgressDlg() {
        if (null != progressDlg && progressDlg.isShowing()) {
            progressDlg.dismiss();
            progressDlg = null;
        }
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }


    public static void showCallbackDialog(Context context, String message, @NonNull @LayoutRes int layoutId) {
        BaseDialog callbackDialog = new BaseDialog(context, layoutId);
        callbackDialog.getWindow().setLayout(ToolUtil.dp2px(context, 300),
                ToolUtil.dp2px(context, 175));
        callbackDialog.setTextViewContent(R.id.tv_callback, message);
        callbackDialog.getTextView(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackDialog.dismiss();
            }
        });
        callbackDialog.show();
    }

    public static void showCallbackDialog(Context context, String message) {
        BaseDialog callbackDialog = new BaseDialog(context, R.layout.dialog_callback);
        callbackDialog.getWindow().setLayout(ToolUtil.dp2px(context, 300),
                ToolUtil.dp2px(context, 175));
        callbackDialog.setTextViewContent(R.id.tv_callback, message);
        callbackDialog.getTextView(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackDialog.dismiss();
            }
        });
        callbackDialog.show();
    }

}
