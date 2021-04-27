package com.example.mvvm_base;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.List;

/**
 * desc:动画工具类
 * date:2021-4-25 11:04
 * author:bWang
 * <p>
 * 动画分为帧动画、补间动画、属性动画
 * 帧动画：帧动画是最容易实现的一种动画，这种动画更多的依赖于完善的UI资源，他的原理就是将一张张单独的图片连贯的进行播放，
 * 从而在视觉上产生一种动画的效果；有点类似于某些软件制作gif动画的方式（电影原理一样）
 * 补间动画：分为四种：alpha(淡入淡出)、translate(位移)、scale(缩放大小)、rotate(旋转) 注：只是改变了View的显示效果而已，并不会真正的改变View的属性
 * 属性动画：补间动画的升级 注：会改变控件真实的坐标
 */
public class AnimationUtil {

    private static AnimationUtil instance;

    public static AnimationUtil getInstance() {
        if (instance == null) {
            instance = new AnimationUtil();
        }
        return instance;
    }

    private AnimationDrawable mAnimationDrawable;

    /**
     * @param context        获取drawable上下文
     * @param imageView      动画对象
     * @param drawableIdList 图片合集
     * @param duration       每张图片时间间隔
     * @param isLoop         true不循环 false循环
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startAnimationDrawable(Context context, ImageView imageView, int[] drawableIdList, int duration, boolean isLoop) {
        if (mAnimationDrawable == null)
            mAnimationDrawable = new AnimationDrawable();
        for (int i = 0; i < drawableIdList.length; i++) {
            mAnimationDrawable.addFrame(context.getDrawable(drawableIdList[i]), duration);
        }
        mAnimationDrawable.setOneShot(isLoop);
        imageView.setImageDrawable(mAnimationDrawable);
        mAnimationDrawable.start();
    }

    /**
     * 结束帧动画
     *
     * @param imageView
     */
    public void stopAnimationDrawable(ImageView imageView) {
        if (mAnimationDrawable != null)
            mAnimationDrawable.stop();
    }

    private AlphaAnimation mAlphaAnimation;

    /**
     * 开始alpha动画
     *
     * @param view         动画对象
     * @param interpolator 用来控制动画的变化速度，可以理解成动画渲染器
     *                     (1.LinearInterpolator：动画以均匀的速度改变。
     *                     2.AccelerateInterpolator：在动画开始的地方改变速度较慢，然后开始加速。
     *                     3.AccelerateDecelerateInterpolator：在动画开始、结束的地方改变速度较慢，中间时加速。
     *                     4.CycleInterpolator：动画循环播放特定次数，变化速度按正弦曲线改变
     *                     Math.sin(2 * mCycles * Math.PI * input)。
     *                     5.DecelerateInterpolator：在动画开始的地方改变速度较快，然后开始减速。
     *                     6.AnticipateInterpolator：反向，先向相反方向改变一段再加速播放。
     *                     7.AnticipateOvershootInterpolator：开始的时候向后然后向前甩一定值后返回最后的值。
     *                     8.BounceInterpolator： 跳跃，快到目的值时值会跳跃，如目的值100，后面的值可能依次为85，77，70，80，90，100。
     *                     9.OvershootInterpolator：回弹，最后超出目的值然后缓慢改变到目的值。)
     * @param fromAlpha    起始透明度
     * @param toAlpha      终止透明度
     * @param duration     时间间隔
     * @param fillAfter    设置结束时状态，true保持结束时状态，false变回原先的状态
     * @param repeatCount  重复次数 -1为无限 0为1次依次增加
     * @param repeatMode   重复模式,Animation.RESTART:从头开始，Animation.REVERSE:逆序
     */
    public void startAlphaAnimation(View view, Interpolator interpolator, float fromAlpha, float toAlpha, int duration, boolean fillAfter, int repeatCount, int repeatMode) {
        if (mAlphaAnimation == null)
            mAlphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        mAlphaAnimation.setInterpolator(interpolator);
        mAlphaAnimation.setDuration(duration);
        mAlphaAnimation.setRepeatCount(repeatCount);
        mAlphaAnimation.setRepeatMode(repeatMode);
        mAlphaAnimation.setFillAfter(fillAfter);
        view.startAnimation(mAlphaAnimation);
    }

    /**
     * @param interpolator 用来控制动画的变化速度，可以理解成动画渲染器
     *                     (1.LinearInterpolator：动画以均匀的速度改变。
     *                     2.AccelerateInterpolator：在动画开始的地方改变速度较慢，然后开始加速。
     *                     3.AccelerateDecelerateInterpolator：在动画开始、结束的地方改变速度较慢，中间时加速。
     *                     4.CycleInterpolator：动画循环播放特定次数，变化速度按正弦曲线改变
     *                     Math.sin(2 * mCycles * Math.PI * input)。
     *                     5.DecelerateInterpolator：在动画开始的地方改变速度较快，然后开始减速。
     *                     6.AnticipateInterpolator：反向，先向相反方向改变一段再加速播放。
     *                     7.AnticipateOvershootInterpolator：开始的时候向后然后向前甩一定值后返回最后的值。
     *                     8.BounceInterpolator： 跳跃，快到目的值时值会跳跃，如目的值100，后面的值可能依次为85，77，70，80，90，100。
     *                     9.OvershootInterpolator：回弹，最后超出目的值然后缓慢改变到目的值。)
     * @param fromAlpha    起始透明度
     * @param toAlpha      最终透明度
     * @param duration     时间间隔
     * @param fillAfter    设置结束时状态，true保持结束时状态，false变回原先的状态
     * @param repeatCount  重复次数 -1为无限 0为1次依次增加
     * @param repeatMode   重复模式,Animation.RESTART:从头开始，Animation.REVERSE:逆序
     * @return AlphaAnimation
     */
    public AlphaAnimation getAlphaAnimation(Interpolator interpolator, float fromAlpha, float toAlpha, int duration, boolean fillAfter, int repeatCount, int repeatMode) {
        if (mAlphaAnimation == null)
            mAlphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        mAlphaAnimation.setInterpolator(interpolator);
        mAlphaAnimation.setDuration(duration);
        mAlphaAnimation.setRepeatCount(repeatCount);
        mAlphaAnimation.setRepeatMode(repeatMode);
        mAlphaAnimation.setFillAfter(fillAfter);
        return mAlphaAnimation;
    }

    private TranslateAnimation mTranslateAnimation;

    /**
     * 开始位移动画
     *
     * @param view        动画对象
     * @param fromX       起始X坐标
     * @param toX         终止X坐标
     * @param fromY       起始Y坐标
     * @param toY         起始Y坐标
     * @param duration    时间间隔
     * @param fillAfter   设置结束时状态，true保持结束时状态，false变回原先的状态
     * @param repeatCount 重复次数 -1为无限 0为1次依次增加
     * @param repeatMode  重复模式,Animation.RESTART:从头开始，Animation.REVERSE:相反运动
     */
    public void startTranslateAnimation(View view, float fromX, float toX, float fromY, float toY, int duration, boolean fillAfter, int repeatCount, int repeatMode) {
        if (mTranslateAnimation == null)
            mTranslateAnimation = new TranslateAnimation(fromX, toX, fromY, toY);
        mTranslateAnimation.setDuration(duration);
        mTranslateAnimation.setRepeatCount(repeatCount);
        mTranslateAnimation.setRepeatMode(repeatMode);
        mTranslateAnimation.setFillAfter(fillAfter);
        view.startAnimation(mTranslateAnimation);
    }

    /**
     * @param fromX       起始X坐标
     * @param toX         终止X坐标
     * @param fromY       起始Y坐标
     * @param toY         起始Y坐标
     * @param duration    时间间隔
     * @param fillAfter   设置结束时状态，true保持结束时状态，false变回原先的状态
     * @param repeatCount 重复次数 -1为无限 0为1次依次增加
     * @param repeatMode  重复模式,Animation.RESTART:从头开始，Animation.REVERSE:相反运动
     * @return TranslateAnimation
     */
    public TranslateAnimation getTranslateAnimation(float fromX, float toX, float fromY, float toY, int duration, boolean fillAfter, int repeatCount, int repeatMode) {
        if (mTranslateAnimation == null)
            mTranslateAnimation = new TranslateAnimation(fromX, toX, fromY, toY);
        mTranslateAnimation.setDuration(duration);
        mTranslateAnimation.setRepeatCount(repeatCount);
        mTranslateAnimation.setRepeatMode(repeatMode);
        mTranslateAnimation.setFillAfter(fillAfter);
        return mTranslateAnimation;
    }

    private ScaleAnimation mScaleAnimation;

    /**
     * 开始缩放动画
     *
     * @param view       动画对象
     * @param fromXScale 起始X轴缩放比例
     * @param toXScale   终止X轴缩放比例
     * @param fromYScale 起始Y轴缩放比例
     * @param toYScale   终止Y轴缩放比例
     * @param pivotXType 动画在X轴相对于物件位置类型
     * @param pivotX     缩放点的X坐标
     * @param pivotYType 动画在Y轴相对于物件位置类型
     * @param pivotY     缩放点的Y坐标
     * @param duration   时间间隔
     */
    public void startScaleAnimation(View view, float fromXScale, float toXScale, float fromYScale, float toYScale, int pivotXType, float pivotX, int pivotYType, float pivotY, int duration) {
        if (mScaleAnimation == null)
            mScaleAnimation = new ScaleAnimation(fromXScale, toXScale, fromYScale, toYScale, pivotXType, pivotX, pivotYType, pivotY);
        mScaleAnimation.setDuration(duration);
        view.startAnimation(mScaleAnimation);
    }

    private RotateAnimation mRotateAnimation;

    /**
     * 开始旋转动画
     *
     * @param view        动画对象
     * @param fromDegrees 起始角度
     * @param toDegrees   终止角度
     * @param pivotXType  动画在X轴相对于物件位置类型
     * @param pivotX      旋转点的X坐标
     * @param pivotYType  动画在Y轴相对于物件位置类型
     * @param pivotY      旋转点的Y坐标
     * @param duration    时间间隔
     * @param repeatCount 重复次数 -1为无限 0为1次依次增加
     * @param repeatMode  重复模式,Animation.RESTART:从头开始，Animation.REVERSE:相反运动
     */
    public void startRotateAnimation(View view, float fromDegrees, float toDegrees, int pivotXType, float pivotX, int pivotYType, float pivotY, int duration, int repeatCount, int repeatMode) {
        if (mRotateAnimation == null)
            mRotateAnimation = new RotateAnimation(fromDegrees, toDegrees, pivotXType, pivotX, pivotYType, pivotY);
        mRotateAnimation.setDuration(duration);
        mRotateAnimation.setRepeatCount(repeatCount);
        mRotateAnimation.setRepeatMode(repeatMode);
        view.startAnimation(mRotateAnimation);
    }

    private AnimationSet mAnimationSet;

    /**
     * 组合动画
     *
     * @param view          动画对象
     * @param animationList 动画合集
     */
    public void startAnimationSet(View view, List<Animation> animationList) {
        if (mAnimationSet == null)
            mAnimationSet = new AnimationSet(true);
        for (int i = 0; i < animationList.size(); i++) {
            mAnimationSet.addAnimation(animationList.get(i));
        }
        view.startAnimation(mAnimationSet);
    }

    private ObjectAnimator mAlphaPropertyAnimator;

    /**
     * 开始alpha属性动画
     *
     * @param view        动画对象
     * @param values      过程透明度
     * @param duration    时间间隔
     * @param repeatCount 重复次数 -1为无限 0为1次依次增加
     * @param repeatMode  重复模式,Animation.RESTART:从头开始，Animation.REVERSE:相反运动
     */
    public void startAlphaPropertyAnimator(View view, int duration, int repeatCount, int repeatMode, float... values) {
        if (mAlphaPropertyAnimator == null)
            mAlphaPropertyAnimator = ObjectAnimator.ofFloat(view, "alpha", values);
        mAlphaPropertyAnimator.setDuration(duration);
        mAlphaPropertyAnimator.setRepeatCount(repeatCount);
        mAlphaPropertyAnimator.setRepeatMode(repeatMode);
        mAlphaPropertyAnimator.start();
    }

    private ObjectAnimator mTranslatePropertyAnimator;

    /**
     * 开始translate属性动画
     *
     * @param view       动画对象
     * @param type       位移方向1：X轴，2：Y轴
     * @param values     过程位移
     * @param duration   时间间隔
     * @param repeatMode 重复模式,Animation.RESTART:从头开始，Animation.REVERSE:相反运动
     */
    @SuppressLint("ObjectAnimatorBinding")
    public void startTranslatePropertyAnimator(View view, @NonNull int type, int duration, int repeatMode, float... values) {
        String propertyName = "";
        switch (type) {
            case 1:
                propertyName = "translationX";
                break;
            case 2:
                propertyName = "translationY";
                break;
        }
        if (mTranslatePropertyAnimator == null)
            mTranslatePropertyAnimator = ObjectAnimator.ofFloat(view, propertyName, values);
        mTranslatePropertyAnimator.setDuration(duration);
        mTranslatePropertyAnimator.setRepeatMode(repeatMode);
        mTranslatePropertyAnimator.start();
    }

    private ObjectAnimator mScalePropertyAnimator;

    /**
     * 开始scale属性动画
     *
     * @param view       动画对象
     * @param type       缩放方向1：X轴，2：Y轴
     * @param values     过程缩放大小
     * @param duration   时间间隔
     * @param repeatMode 重复模式,Animation.RESTART:从头开始，Animation.REVERSE:相反运动
     */
    @SuppressLint("ObjectAnimatorBinding")
    public void startScalePropertyAnimator(View view, @NonNull int type, int duration, int repeatMode, float... values) {
        String propertyName = "";
        switch (type) {
            case 1:
                propertyName = "scaleX";
                break;
            case 2:
                propertyName = "scaleY";
                break;
        }
        if (mScalePropertyAnimator == null)
            mScalePropertyAnimator = ObjectAnimator.ofFloat(view, propertyName, values);
        mScalePropertyAnimator.setDuration(duration);
        mScalePropertyAnimator.setRepeatMode(repeatMode);
        mScalePropertyAnimator.start();
    }

    private ObjectAnimator mRotatePropertyAnimator;

    /**
     * 开始rotate属性动画
     *
     * @param view       动画对象
     * @param values     过程角度
     * @param duration   时间间隔
     * @param repeatMode 重复模式,Animation.RESTART:从头开始，Animation.REVERSE:相反运动
     */
    @SuppressLint("ObjectAnimatorBinding")
    public void startRotatePropertyAnimator(View view, int duration, int repeatMode, float... values) {
        if (mRotatePropertyAnimator == null)
            mRotatePropertyAnimator = ObjectAnimator.ofFloat(view, "rotation", values);
        mRotatePropertyAnimator.setDuration(duration);
        mRotatePropertyAnimator.setRepeatMode(repeatMode);
        mRotatePropertyAnimator.start();
    }

    private AnimatorSet mAnimatorSet;

    public void startAnimatorSet(List<Animator> animatorList) {
        if (mAnimatorSet == null)
            mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animatorList);
        mAnimatorSet.start();
    }
}
