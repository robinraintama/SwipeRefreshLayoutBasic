/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.swiperefreshlayoutbasic;

import com.example.android.common.adapter.NewsAdapter;
import com.example.android.common.logger.Log;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

/**
 * A basic sample that shows how to use {@link android.support.v4.widget.SwipeRefreshLayout} to add
 * the 'swipe-to-refresh' gesture to a layout. In this sample, SwipeRefreshLayout contains a
 * scrollable {@link android.widget.ListView} as its only child.
 *
 * <p>To provide an accessible way to trigger the refresh, this app also provides a refresh
 * action item.
 *
 * <p>In this sample app, the refresh updates the ListView with a random set of new items.
 */
public class SwipeRefreshLayoutBasicFragment extends Fragment {

    private static final String LOG_TAG = SwipeRefreshLayoutBasicFragment.class.getSimpleName();

    private static final int LIST_ITEM_COUNT = 20;

    /**
     * The {@link android.support.v4.widget.SwipeRefreshLayout} that detects swipe gestures and
     * triggers callbacks in the app.
     */
    private SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * The {@link android.webkit.WebView} that displays the content that should be refreshed.
     */

    /**
     * The link that load by mWebView.
     */
    private String url[] = {"file:///android_asset/saturnv.html", "http://www.bbc.com/future/story/20150925-blindsight-the-strangest-form-of-consciousness"};

    /**
     * The last url.
     */
    private String mUrl = "http://www.bbc.com/future/story/20150415-the-plane-that-can-fly-backwards";
    private int mText = 1;
    private ListView lv;
    private NewsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Notify the system to allow an options menu for this fragment.
        setHasOptionsMenu(true);
    }

    // BEGIN_INCLUDE (inflate_view)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sample, container, false);

        // Retrieve the SwipeRefreshLayout and WebView instances
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        // BEGIN_INCLUDE (change_colors)
        // Set the color scheme of the SwipeRefreshLayout by providing 4 color resource ids
        mSwipeRefreshLayout.setColorScheme(
                R.color.swipe_color_1, R.color.swipe_color_2,
                R.color.swipe_color_3, R.color.swipe_color_4);
        // END_INCLUDE (change_colors)

        // Initiate List
        lv = (ListView) view.findViewById(R.id.lv);
        lv.setDividerHeight(0);
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_news, lv, false);
        lv.addHeaderView(header, null, false);

        adapter = new NewsAdapter(getActivity(),url[0],isNetworkAvailable());
        lv.setAdapter(adapter);

        return view;
    }
    // END_INCLUDE (inflate_view)

    // BEGIN_INCLUDE (setup_views)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // BEGIN_INCLUDE (setup_refreshlistener)
        /**
         * Implement {@link SwipeRefreshLayout.OnRefreshListener}. When users do the "swipe to
         * refresh" gesture, SwipeRefreshLayout invokes
         * {@link SwipeRefreshLayout.OnRefreshListener#onRefresh onRefresh()}. In
         * {@link SwipeRefreshLayout.OnRefreshListener#onRefresh onRefresh()}, call a method that
         * refreshes the content. Call the same method in response to the Refresh action from the
         * action bar.
         */
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");

                initiateRefresh();
                mSwipeRefreshLayout.setRefreshing(false);

                adapter.loadWebView(url[0]);
                adapter.notifyDataSetChanged();
            }
        });
        // END_INCLUDE (setup_refreshlistener)
    }

    // END_INCLUDE (setup_views)

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    // BEGIN_INCLUDE (setup_refresh_menu_listener)
    /**
     * Respond to the user's selection of the Refresh action item. Start the SwipeRefreshLayout
     * progress bar, then initiate the background task that refreshes the content.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                Log.i(LOG_TAG, "Refresh menu item selected");

                // We make sure that the SwipeRefreshLayout is displaying it's refreshing indicator
                if (!mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(true);
                }

                // Start our refresh background task
                initiateRefresh();

                return true;
            case R.id.menu_text_size:
                if (mText == 1) {
//                    mWebView.getSettings().setTextSize(WebSettings.TextSize.SMALLEST);
                    adapter.smallerFont();
                    adapter.notifyDataSetChanged();
                    mText = 2;
                }
                else {
//                    mWebView.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
                    adapter.biggeerFont();
                    adapter.notifyDataSetChanged();
                    mText = 1;
                }
                break;
            case R.id.menu_share:
                initShareIntent("mail");
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    // END_INCLUDE (setup_refresh_menu_listener)

    // BEGIN_INCLUDE (initiate_refresh)
    /**
     * By abstracting the refresh process to a single method, the app allows both the
     * SwipeGestureLayout onRefresh() method and the Refresh action item to refresh the content.
     */
    private void initiateRefresh() {
        Log.i(LOG_TAG, "initiateRefresh");

        /**
         * Change the url.
         */
        if (url[0].equals(mUrl)) mUrl = url[1];
        else mUrl = url[0];

    }
    // END_INCLUDE (initiate_refresh)


    private void initShareIntent(String type) {
        boolean found = false;
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("image/jpeg");

        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo = getContext().getPackageManager().queryIntentActivities(share, 0);
        if (!resInfo.isEmpty()){
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains(type) ||
                        info.activityInfo.name.toLowerCase().contains(type) ) {
                    share.putExtra(Intent.EXTRA_SUBJECT,  "subject");
                    share.putExtra(Intent.EXTRA_TEXT,     "your text");
//                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(myPath)) ); // Optional, just if you wanna share an image.
                    share.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }
            }
            if (!found)
                return;

            startActivity(Intent.createChooser(share, "Select"));
        }
    }
}
