package com.example.mvvm_base.binding.viewadapter.textview;

import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

/**
 * desc:
 * date:2021/8/3
 * author:bWang
 * <p>
 */
public class ViewAdapter {

    /**
     * 背景
     */
    @BindingAdapter(value = {"txtColor"}, requireAll = false)
    public static void setTxtColor(TextView textView, int colorRes) {
        if (colorRes != 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView.setTextColor(textView.getContext().getColor(colorRes));
            }

    }
}
