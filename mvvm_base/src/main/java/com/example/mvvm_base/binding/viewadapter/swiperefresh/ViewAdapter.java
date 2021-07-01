package com.example.mvvm_base.binding.viewadapter.swiperefresh;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import com.example.mvvm_base.binding.command.BindingCommand;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


/**
 * Created by goldze on 2017/6/18.
 */
public class ViewAdapter {
    //下拉刷新命令
    @BindingAdapter({"onRefreshCommand"})
    public static void onRefreshCommand(SmartRefreshLayout smartRefreshLayout, final BindingCommand onRefreshCommand) {
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (onRefreshCommand != null) {
                    onRefreshCommand.execute();
                }
                refreshLayout.finishRefresh(smartRefreshLayout.getState().equals(RefreshState.RefreshFinish));
            }
        });
    }


    //上拉加载命令
    @BindingAdapter({"onLoadMoreCommand"})
    public static void onLoadMoreCommand(SmartRefreshLayout smartRefreshLayout, final BindingCommand onLoadMoreCommand) {
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (onLoadMoreCommand != null) {
                    onLoadMoreCommand.execute();
                }
                refreshLayout.finishLoadMore(smartRefreshLayout.getState().equals(RefreshState.LoadFinish));
            }
        });
    }

}
