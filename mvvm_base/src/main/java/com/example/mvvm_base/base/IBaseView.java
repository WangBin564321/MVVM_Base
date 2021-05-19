package com.example.mvvm_base.base;

/**
 * desc: IBaseView
 * date:2017/6/15
 * author:goldze
 */
public interface IBaseView {
    /**
     * 初始化界面传递参数
     */
    void initParam();
    /**
     * 初始化数据
     */
    void initData();

    /**
     * 初始化界面观察者的监听
     */
    void initViewObservable();
}
