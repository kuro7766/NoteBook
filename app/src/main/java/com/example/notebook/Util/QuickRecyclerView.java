package com.example.notebook.Util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class QuickRecyclerView<T> extends RecyclerView {
    private Context context;
    private RvAdapter<T> rvAdapter;
    private int previousLayoutRes=-1;
    private static final String TAG = "QuickRecyclerView";
    public QuickRecyclerView(@NonNull Context context) {
        super(context);
        Log.d(TAG, "QuickRecyclerView() called with: context = [" + context + "]");
        init(context);
    }

    public QuickRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "QuickRecyclerView() called with: context = [" + context + "], attrs = [" + attrs + "]");
        init(context);
    }

    public QuickRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Log.d(TAG, "QuickRecyclerView() called with: context = [" + context + "], attrs = [" + attrs + "], defStyle = [" + defStyle + "]");
        init(context);
    }

    private void init(Context context){
        this.context=context;
        this.setLayoutManager(new LinearLayoutManager(context));
    }

    public QuickRecyclerView<T> setItemLayout(int layoutResId,List<T> data){
        if(layoutResId!=previousLayoutRes){
            rvAdapter=new RvAdapter<>(layoutResId,data==null?new ArrayList<T>():data);
            previousLayoutRes=layoutResId;
        }else {
            rvAdapter.setNewData(data);
        }
        this.setAdapter(rvAdapter);
        return this;
    }

    public QuickRecyclerView<T> describeLayout(RvAdapter.DescribeItem<T> describeItem){
        rvAdapter.describeLayout(describeItem);
        return this;
    }

    public QuickRecyclerView<T> setOnItemClickListener(final OnItemClickListener<T> l){
        if(l==null){
            return this;
        }
        rvAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d(TAG, "" + position);
                l.onItemClick(rvAdapter.getItem(position),position,view,adapter);
            }
        });
        return this;
    }

    @Override
    public BaseQuickAdapter<T, BaseViewHolder> getAdapter() {
        return rvAdapter;
    }

    public interface OnItemClickListener <T>{
        void onItemClick(T item, int position, View view, BaseQuickAdapter adapter);
    }

    public static class RvAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
        private DescribeItem<T> describeItem;

        private RvAdapter(int layoutResId, @Nullable List<T> data) {
            super(layoutResId, data);
        }
        @Override
        protected void convert(BaseViewHolder helper, T item) {
            if (describeItem != null) {
                describeItem.describeItem(helper, item,helper.getAdapterPosition());
            }
        }

        public interface DescribeItem<T> {
            void describeItem(BaseViewHolder helper, T item, int position);
        }

        private void describeLayout(DescribeItem describeItem) {
            this.describeItem = describeItem;
        }
    }
}
