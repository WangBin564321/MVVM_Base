package com.example.mvvm_base.binding.viewadapter.image;


import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by goldze on 2017/6/18.
 */
public final class ViewAdapter {
    @BindingAdapter(value = {"url", "placeholderRes"}, requireAll = false)
    public static void setImageUri(ImageView imageView, String url, int placeholderRes) {
        if (!TextUtils.isEmpty(url)) {
            //使用Glide框架加载图片
            Glide.with(imageView.getContext())
                    .load(url)
                    .apply(new RequestOptions().placeholder(placeholderRes))
                    .into(imageView);
        }
    }

    @BindingAdapter(value = {"avatar", "defaultRes"}, requireAll = false)
    public static void setImageResource(ImageView imageView, String avatarUrl, int defaultRes) {
        Glide.with(imageView.getContext()).load(avatarUrl).error(defaultRes)//异常时候显示的图片
                .placeholder(defaultRes) //加载成功前显示的图片
                .fallback(defaultRes) //url为空的时候,显示的图片
                .into(imageView);
    }
}

