package com.example.android.common.adapter;

import android.app.ActionBar;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.swiperefreshlayoutbasic.R;

/**
 * Created by Sola_MBP on 9/30/15.
 */
public class NewsAdapter extends BaseAdapter {
    private final Context mContext;
    private final int mCount = 1;
    private final String mUrl;
    private final boolean isNetworkAvailable;
    static WebView mWebView;

    public NewsAdapter(Context context, String url, boolean network) {
        mContext = context;
        mUrl = url;
        isNetworkAvailable = network;
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
            mWebView = new WebView(mContext);
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//            int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, mContext.getResources().getDisplayMetrics());
//            params.setMargins(margin, margin, margin, margin);
//            mWebView.setLayoutParams(params);

            // Load from cache if offline
            String appCachePath = mContext.getApplicationContext().getCacheDir().getAbsolutePath();
            System.out.println("appCachePath = " + appCachePath);
            mWebView.getSettings().setAppCacheMaxSize(5 * 1024 * 1024); // 5MB
            mWebView.getSettings().setAppCachePath(appCachePath);
            mWebView.getSettings().setAllowFileAccess(true);
            mWebView.getSettings().setAppCacheEnabled(true);
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // load online by default

            mWebView.getSettings().setLoadsImagesAutomatically(true);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

            if ( isNetworkAvailable ) {
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
                        view.setEnabled(true);
                    }
                }
            });
            mWebView.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    mWebView.loadUrl("file:///android_asset/myerrorpage.html");
                }
            });

            mWebView.loadUrl(mUrl);
            v = mWebView;
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
//            mTitle = (TextView) base.findViewById(R.id.tv_title);
        }
    }

    public static void loadWebView(String url) {
        mWebView.loadUrl(url);
    }

    public static void smallerFont() {
        mWebView.getSettings().setTextSize(WebSettings.TextSize.SMALLEST);
    }

    public static void biggeerFont() {
        mWebView.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
    }
}
