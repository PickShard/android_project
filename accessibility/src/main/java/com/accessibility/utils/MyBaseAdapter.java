package com.accessibility.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import java.util.List;

/**
 * @描述： 通用適配器
 * @作者： SuCheng
 * @时间： 2016/12/12 11:31
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter {

    protected List<T> mList;
    protected Context mContext;

    public MyBaseAdapter(Context context,List<T> list) {
        mContext=context;
        mList=list;
    }

    /**
     * 清空并添加新的数据
     * @param list
     */
    public void setNewData(List<T> list){
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 添加新的数据
     * @param list
     */
    public void addData(List<T> list){
        mList.addAll(list);
        notifyDataSetChanged();
    }



    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public final View getView(int position, View convertView, ViewGroup parent) {

        BaseViewHolder holder=null;

        if (convertView==null){
            holder =new BaseViewHolder(mContext);

            getView(holder,getItem(position),position);

            holder.getConvertView().setTag(holder);

        }else {
            holder= (BaseViewHolder) convertView.getTag();

            getView(holder,getItem(position),position);
        }

        return holder.getConvertView();
    }


    protected abstract void getView(BaseViewHolder holder, T t, int position);
}
