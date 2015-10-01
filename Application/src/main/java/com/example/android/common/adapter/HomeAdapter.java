package com.example.android.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.swiperefreshlayoutbasic.R;

/**
 * Created by Sola_MBP on 9/30/15.
 */
public class HomeAdapter extends BaseAdapter {
    private final Context mContext;
    private final int mCount;

    public HomeAdapter(Context context, int i) {
        mContext = context;
        mCount = i;
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        CompleteListViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.adapter_home, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }
        return v;
    }
    class CompleteListViewHolder {
        public TextView mTitle;
        public CompleteListViewHolder(View base) {
            mTitle = (TextView) base.findViewById(R.id.tv_title);
        }
    }
}
