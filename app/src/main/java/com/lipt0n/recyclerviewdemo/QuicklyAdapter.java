package com.lipt0n.recyclerviewdemo;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Granden on 2017/8/22.
 */

public class QuicklyAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    public QuicklyAdapter(@LayoutRes int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {
        holder.setText(R.id.content,item);
    }
}
