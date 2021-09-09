package com.example.mvvm_base.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {

    public static final int TYPE_HEADER = -1;
    public static final int TYPE_FOOTER = -2;
    public static final int TYPE_Empty = -3;
    boolean isEmpty = true;

    public T getItems(int position) {
        return items.get(position);
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        isEmpty = (items == null || items.size() == 0) ? true : false;
        this.items = new ArrayList<>();
        if (isHeaderExist()) this.items.add(0, null);
        if (isEmptyExist()) this.items.add(this.items.size(), null);
        this.items.addAll(items);
        if (isFooterExist()) this.items.add(this.items.size(), null);
    }

    protected List<T> items;
    protected final Context mContext;
    protected LayoutInflater mInflater;
    private OnItemClickListener mClickListener;


    /**
     * 通过构造函数能够得到需要加载的数据
     *
     * @param ctx
     * @param list
     */
    public BaseRecyclerAdapter(Context ctx, List<T> list) {
        items = (list != null) ? list : new ArrayList<T>();
        isEmpty = (items == null || items.size() == 0) ? true : false;
        mContext = ctx;
        mInflater = LayoutInflater.from(ctx);
        //判断是否加载头部，同时对数据源做处理，给头部和底部预留出显得位置
        if (isHeaderExist()) items.add(0, null);
        if (isEmptyExist()) items.add(items.size(), null);
        if (isFooterExist()) items.add(items.size(), null);
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
        if (isHeaderExist() && position == getHeaderPosition())
            return TYPE_HEADER;
        if (isFooterExist() && position == getFooterPosition())
            return TYPE_FOOTER;
        if (isEmptyExist() && position == getEmptyPosition())
            return TYPE_Empty;
        return position;
    }

    /**
     * 该方法创建item的 在调用该方法之前会调用getItemViewType()方法，
     *
     * @param parent
     * @param viewType 这个方法就是getItemViewType返回的值 同时判断该值加载不同的布局，
     *                 需要理解的是没加载一个布局这个类，以及这里面的方法都不调用一次
     * @return 通过判断需要加载的布局的holder对象
     */
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;
        switch (viewType) {
            case TYPE_HEADER:
                //这个方法只用在使用加载头部布局的时候会使用，但是这个方法是在该类的子类（实现类）里面
                layoutId = getHeaderLayoutId();
                break;
            case TYPE_FOOTER:
                //这个方法只用在使用加载底部布局的时候会使用，但是这个方法是在该类的子类（实现类）里面
                layoutId = getFooterLayoutId();
                break;
            case TYPE_Empty:
                //加载正常部分，就是主要的数据展示区的数据
                layoutId = getEmptyLayoutId();
                break;
            default:
                layoutId = getItemLayoutId();
                break;
        }


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
        if (mClickListener != null && viewType != TYPE_HEADER && viewType != TYPE_FOOTER) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //判断如果当前加载的布局里面不包含有头部的时候调用的点击事件
                    mClickListener.onItemClick(holder.itemView, holder.getLayoutPosition());
                }
            });
        }

        return holder;
    }


    /**
     * 在加载底部布局的时候需要实现该方法，同时返回底部布局的layoutID
     *
     * @return
     */
    protected int getFooterLayoutId() {
        return -1;
    }

    /**
     * 判断是否存在底部的布局
     *
     * @return
     */
    final public boolean isFooterExist() {
        return getFooterLayoutId() != -1;
    }

    /**
     * 在加载头部布局的时候需要实现该方法，同时返回头部布局的layoutID
     *
     * @return
     */
    protected int getHeaderLayoutId() {
        return -1;
    }

    /**
     * 判断当前头部是否存在不同的布局
     *
     * @return
     */
    final public boolean isHeaderExist() {
        return getHeaderLayoutId() != -1;
    }

    /**
     * 当头部存在的时候返回头部的位置
     *
     * @return
     */
    final public int getHeaderPosition() {
        if (isHeaderExist()) return 0;
        return -1;
    }

    /**
     * 当底部存在的时候返回底部的位置
     *
     * @return
     */
    final public int getFooterPosition() {
        if (isFooterExist()) return getItemCount() - 1;
        return -2;
    }

    /**
     * 是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return isEmpty;
    }

    /**
     * 是否存在空布局
     *
     * @return
     */
    final public boolean isEmptyExist() {
        return isEmpty && getEmptyLayoutId() != -1;
    }

    /**
     * 返回空布局layoutId
     *
     * @return
     */
    protected int getEmptyLayoutId() {
        return -1;
    }

    /**
     * 返回空布局position
     *
     * @return
     */
    final public int getEmptyPosition() {
        if (isEmpty())
            return getHeaderPosition() + 1;
        return -3;
    }

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
        if (position == getFooterPosition()) {
            bindFooter(holder, position);//调用绑定底部数据的方法
        } else if (position == getHeaderPosition()) {
            bindHeader(holder, position);//调用头部数据的方法
        } else {
            //调用中间部分（就是数据展示部分的）的绑定数据的方法
            if (items != null && items.size() > 0) {
                bindData(holder, position, items.get(position));
            } else {
                bindData(holder, position, null);
            }
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
        return items.size();
    }

    /**
     * 添加数据到特定的位置，该位置已考虑header和footer
     *
     * @param pos
     * @param item
     */
    public void add(int pos, T item) {
        if (pos != getHeaderPosition() && pos != getFooterPosition() + 1) {
            items.add(pos, item);
            notifyItemInserted(pos);
        } else {
            try {
                throw new Exception("your position to add or delete should consider the header and footer.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 在特定位置删除数据，该位置已考虑header和footer
     *
     * @param pos
     */
    public void delete(int pos) {
        if (pos != getHeaderPosition() && pos != getFooterPosition()) {
            items.remove(pos);
            notifyItemRemoved(pos);
        } else {
            try {
                throw new Exception("your position to add or delete should consider the header and footer.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 在特定的位置之间交换位置，该位置已考虑header和footer
     *
     * @param fromPosition
     * @param toPosition
     */
    public void swap(int fromPosition, int toPosition) {
        int head = getHeaderPosition();
        int foot = getFooterPosition();
        if (fromPosition != head && toPosition != head && fromPosition != foot && toPosition != foot) {
            Collections.swap(items, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
        } else {
            try {
                throw new Exception("your position to swap should consider the header and footer.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
     * @return
     */
    abstract protected int getItemLayoutId();

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


}
