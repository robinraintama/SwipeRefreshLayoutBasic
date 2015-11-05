package com.example.android.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.swiperefreshlayoutbasic.R;

import java.util.List;

/**
 * Created by Sola_MBP on 10/27/15.
 */
public class ListAlphabetAdapter extends BaseAdapter {
    private List rows;

    public void setRows(List rows) {
        this.rows = rows;
    }

    @Override
    public int getCount() {
        return rows.size();
    }

    @Override
    public Object getItem(int position) {
        return rows.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof Section) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (getItemViewType(position) == 0) { // Item
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (LinearLayout) inflater.inflate(R.layout.component_item, parent, false);
            }

            Item item = (Item) getItem(position);
            TextView textView = (TextView) view.findViewById(R.id.textView1);
            textView.setText(item.text);
        } else { // Section
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (LinearLayout) inflater.inflate(R.layout.component_section, parent, false);
            }

            Section section = (Section) getItem(position);
            TextView textView = (TextView) view.findViewById(R.id.textView1);
            textView.setText(section.text);
        }

        return view;
    }

    public static abstract class Row {}

    public static final class Section extends Row {
        public final String text;

        public Section(String text) {
            this.text = text;
        }
    }

    public static final class Item extends Row {
        public final String text;

        public Item(String text) {
            this.text = text;
        }
    }
}