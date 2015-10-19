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

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;

import com.example.android.common.logger.Log;

import java.util.List;

/**
 * A basic sample that shows how to use {@link SwipeRefreshLayout} to add
 * the 'swipe-to-refresh' gesture to a layout. In this sample, SwipeRefreshLayout contains a
 * scrollable {@link ListView} as its only child.
 *
 * <p>To provide an accessible way to trigger the refresh, this app also provides a refresh
 * action item.
 *
 * <p>In this sample app, the refresh updates the ListView with a random set of new items.
 */
public class SwipeRefreshLayoutBasicFragmentOld extends Fragment {

    private static final String LOG_TAG = SwipeRefreshLayoutBasicFragmentOld.class.getSimpleName();

    private static final int LIST_ITEM_COUNT = 20;

    /**
     * The {@link SwipeRefreshLayout} that detects swipe gestures and
     * triggers callbacks in the app.
     */
    private SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * The {@link WebView} that displays the content that should be refreshed.
     */
    private WebView mWebView;

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
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_news, lv, false);
        lv.addHeaderView(header, null, false);

        // Retrieve the WebView
        mWebView = new WebView(getActivity());

        // Load from cache if offline
        String appCachePath = getContext().getApplicationContext().getCacheDir().getAbsolutePath();
        System.out.println("appCachePath = " + appCachePath);
        mWebView.getSettings().setAppCacheMaxSize(5 * 1024 * 1024); // 5MB
        mWebView.getSettings().setAppCachePath(appCachePath);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setCacheMode( WebSettings.LOAD_DEFAULT ); // load online by default

        if ( !isNetworkAvailable() ) {
            // loading offline
            mWebView.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );
        }

        //
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
//                getActivity().setProgress(progress*100);
                view.setEnabled(false);

                // The progress meter will automatically disappear when we reach 100%
                if (progress == 100) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    view.setEnabled(true);
                }
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                mWebView.loadUrl("file:///android_asset/myerrorpage.html");
            }
        });

        return view;
    }
    // END_INCLUDE (inflate_view)

    // BEGIN_INCLUDE (setup_views)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadUrl();

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
            }
        });
        // END_INCLUDE (setup_refreshlistener)
    }

    // END_INCLUDE (setup_views)
    private void loadUrl() {
        /**
         * Set WebView Settings. Turn on load images and javascript. Set scrollbar inside and overlay
         */
        // Loading the webview
        mSwipeRefreshLayout.setRefreshing(true);

        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        // Load the WebView.
        mWebView.loadUrl(mUrl);
    }

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
                    mWebView.getSettings().setTextSize(WebSettings.TextSize.SMALLEST);
                    mText = 2;
                }
                else {
                    mWebView.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
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
        loadUrl();
    }
    // END_INCLUDE (initiate_refresh)


    private void initShareIntent(String type) {
        boolean found = false;
        Intent share = new Intent(Intent.ACTION_SEND);
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
