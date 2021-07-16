package com.example.mvvm_base.base;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * <p>
 * 该类的使用的时候是每加载一个布局就是调用一次该类
 * 该类里面最重要的方法有三
 * onCreateViewHolder  创建显示数据的item的布局
 * onBindViewHolder  对每个item绑定数据
 * getItemViewType  在加载不同布局的时候需要调用该方法，
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {

    public static final int TYPE_ITEM = 0; //正常布局
    public static final int TYPE_EMPTY = -1; //空布局

    protected final Context mContext;
    protected LayoutInflater mInflater;
    protected List<T> items;

    public T getItems(int position) {
        return items.get(position);
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    private OnItemClickListener mClickListener;
    private OnItemLongClickListener mLongClickListener;


    /**
     * 通过构造函数能够得到需要加载的数据
     *
     * @param ctx
     * @param list
     */
    public BaseRecyclerAdapter(Context ctx, List<T> list) {
        items = (list != null) ? list : new ArrayList<T>();
        mContext = ctx;
        mInflater = LayoutInflater.from(ctx);
    }

    /**
     * 构造函数
     *
     * @param ctx
     * @param list
     * @param headIsFormItems 头部数据来自于整体的数据
     */
    public BaseRecyclerAdapter(Context ctx, List<T> list, boolean headIsFormItems) {
        items = (list != null) ? list : new ArrayList<T>();
        mContext = ctx;
        mInflater = LayoutInflater.from(ctx);

    }

    /**
     * 如果需要在子类重写该方法，建议参照此形式
     * 该方法就是实现加载不同布局的最重要的方法
     * 通过返回不同的值加载不同的布局
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (getEmptyLayoutId() == 0)
            return TYPE_ITEM;
        else if (items == null || items.size() == 0)
            return TYPE_EMPTY;
        return TYPE_ITEM;
    }

    /**
     * 该方法创建item的 在调用该方法之前会调用getItemViewType()方法，
     *
     * @param parent
     * @param viewType 这个方法就是getItemViewType返回的值 同时判断该值加载不同的布局，
     *                 需要理解的是没加载一个布局这个类，以及这里面的方法都不调用一次
     * @return 通过判断需要加载的布局的holder对象
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;
        switch (viewType) {
            case TYPE_ITEM:
                //加载正常部分，就是主要的数据展示区的数据
                layoutId = getItemLayoutId(viewType);
                break;
            case TYPE_EMPTY:
                layoutId = getEmptyLayoutId();
                break;
            default:
                //加入都没有加载布局，出现运行是异常
                throw new RuntimeException("illegal viewType!");
        }
        //同理是没有加载布局，不管是头部 中间和底部都没有加载布局，给出一个运行时异常
        if (layoutId == -1)
            throw new RuntimeException("The method getHeaderLayoutId() return_material the wrong id, you should override it and return_material the correct id");


        //为解决复用而存在的一个类，同时在该类里面可以获得每个item对象， holder里面就包含有item View的对象
        //就是说通过holder就能获得item布局里面的所有控件
        //同时该方法返回的就是就是该对象
        final RecyclerViewHolder holder = new RecyclerViewHolder(mContext,
                mInflater.inflate(layoutId, parent, false));
        /*
         * 在这里实现每个item的点击事件，但是对于是否有头部需要进行不同的判断，（这里需要主要看构造函数里面的两个if语句）
         * 在if语句里面对加载头部布局，和底部布局是对数据源进行了处理，所以在点击相应的item 的时候会对考虑不同的数据源的问题
         */
        //这里的判断就是点击事件监听器不为空，当前不是同步，也不是底部
        if (mClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //判断如果当前加载的布局里面不包含有头部的时候调用的点击事件

                    mClickListener.onItemClick(holder.itemView, holder.getLayoutPosition());

                }
            });
        }


        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongClickListener.onItemLongClick(holder.itemView, holder.getLayoutPosition());
                    return true;
                }
            });
        }
        return holder;
    }

    protected abstract int getEmptyLayoutId();


    /**
     * 这是最根本的加载数据的方法，是实现接口以后必须实现的方法，最重要的方法之一
     * 理解就是绑定数据，让数据与item布局进行数据绑定
     * 在这里考虑了整个Recyclerview的头部和底部加载不同的布局
     * 同时对不同的部分采用不同的绑定数据的方法，绑定数据的方法需要早自己实现的适配器里面去实现
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        //调用中间部分（就是数据展示部分的）的绑定数据的方法
        if (items != null && items.size() > 0) {
            bindData(holder, position, items.get(position));
        } else {
            bindData(holder, position, null);
        }
    }

    /**
     * 得到加载的全部item数量
     * 如果头部用了链表的第0个数据，则需要进行处理
     *
     * @return
     */
    @Override
    final public int getItemCount() {
        if (getEmptyLayoutId() == 0)
            return items.size() == 0 ? 0 : items.size();
        else
            return items.size() == 0 ? 1 : items.size();
    }

    /**
     * 添加数据到特定的位置，该位置已考虑header和footer
     *
     * @param pos
     * @param item
     */
    public void add(int pos, T item) {
        items.add(pos, item);
        notifyItemInserted(pos);
    }

    /**
     * 在特定位置删除数据，该位置已考虑header和footer
     *
     * @param pos
     */
    public void delete(int pos) {
        items.remove(pos);
        notifyItemRemoved(pos);
    }

    private int type;

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 在特定的位置之间交换位置，该位置已考虑header和footer
     *
     * @param fromPosition
     * @param toPosition
     */
    public void swap(int fromPosition, int toPosition) {
        Collections.swap(items, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * 调用item的点击事件就是调用这个方法，然后实现监听事件的接口
     *
     * @param listener
     */
    final public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    /**
     * 调用item长的点击事件就是调用这个方法，然后实现监听事件的接口
     *
     * @param listener
     */
    final public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mLongClickListener = listener;
    }

    /**
     * 重写该方法进行header视图的数据绑定
     *
     * @param holder
     * @param position
     */
    protected void bindHeader(RecyclerViewHolder holder, int position) {
    }

    /**
     * 重写该方法进行footer视图的数据绑定
     *
     * @param holder
     * @param position
     */

    protected void bindFooter(RecyclerViewHolder holder, int position) {
    }

    /**
     * 重写该方法，根据viewType设置item的layout
     * 相当于关联每一个item的布局
     *
     * @param viewType 通过重写getItemViewType（）设置，默认item是0
     * @return
     */
    abstract protected int getItemLayoutId(int viewType);

    /**
     * 重写该方法进行item数据项视图的数据绑定
     * 就是把活动的数据添加到每一个item上
     *
     * @param holder   通过holder获得item中的子View，进行数据绑定
     * @param position 该item的position
     * @param item     映射到该item的数据
     */
    abstract protected void bindData(RecyclerViewHolder holder, int position, T item);

    /**
     * 实现每一个item的点击事件需要实现的方法
     */
    public interface OnItemClickListener {
        void onItemClick(View itemView, int pos);
    }

    /**
     * 实现item的长顶啊及事件需要实现的方法
     */
    public interface OnItemLongClickListener {
        void onItemLongClick(View itemView, int pos);
    }

}
