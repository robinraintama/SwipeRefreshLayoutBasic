package com.example.android.swiperefreshlayoutbasic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.android.common.adapter.HomeAdapter;
import com.google.ads.conversiontracking.AdWordsConversionReporter;


public class Home extends Activity implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener{

    private int lastTopValue = 0;
    private int lastAlphaValue = 0;

    private BaseAdapter adapter;
    private ListView lv;
    private ImageView iv_bg;
    private ImageView iv_logo;
    private LinearLayout ll_bar;
    private View first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ll_bar = (LinearLayout) findViewById(R.id.ll_bar);

        lv = (ListView) findViewById(R.id.lv);

        adapter = new HomeAdapter(this, 5);
        lv.setAdapter(adapter);

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_home, lv, false);
        lv.addHeaderView(header, null, false);

        iv_bg = (ImageView) header.findViewById(R.id.iv_bg);
        iv_logo = (ImageView) header.findViewById(R.id.iv_logo);

        lv.setOnScrollListener(this);
        lv.setOnItemClickListener(this);

        // Product
        // Google Android in-app conversion tracking snippet
        // Add this code to the event you'd like to track in your app.
        // See code examples and learn how to add advanced features like app deep links at:
        // https://developers.google.com/app-conversion-tracking/android/#track_in-app_events_driven_by_advertising

        AdWordsConversionReporter.reportWithConversionId(this.getApplicationContext(),
                "941816430", "TB8UCJaVkmAQ7vSLwQM", "1000.00", true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        /* Check if the first item is already reached to top.*/
        Rect rect = new Rect();
        iv_bg.getLocalVisibleRect(rect);
        System.out.println("rect.height() = " + rect.height());
        System.out.println("rect.top = " + rect.top);

        if (lastTopValue != rect.top) {
            lastTopValue = rect.top;
            iv_bg.setY((float) (rect.top / 2.0));
        }

        first = lv.getChildAt(0);
        if (first != null) {
            System.out.println("first = " + first.getTop());
            System.out.println("ll_bar = " + ll_bar.getBottom());
            System.out.println("ll_bar = " + ll_bar.getTop());
        }

        if (rect.height() < ll_bar.getHeight()) {
            int gap = ll_bar.getHeight() - rect.height();
            System.out.println("gap = " + gap);
            ll_bar.setY((float) (0-gap));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(this, NewsActivity.class));
    }
}
