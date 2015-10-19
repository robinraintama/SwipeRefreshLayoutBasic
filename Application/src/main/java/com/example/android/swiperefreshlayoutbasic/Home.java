package com.example.android.swiperefreshlayoutbasic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.android.common.adapter.HomeAdapter;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.ads.conversiontracking.AdWordsConversionReporter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;
import java.util.Vector;


public class Home extends Activity implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private int lastTopValue = 0;
    private int lastAlphaValue = 0;

    private BaseAdapter adapter;
    private BaseAdapter adapterEco;
    private ListView lv;
    private ListView lvEconomic;
    private ImageView iv_bg;
    private ImageView iv_logo;
    private ImageView iv_bar;
    private View first;
    private ViewPager vf;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_home);

        iv_bar = (ImageView) findViewById(R.id.ll_bar);

        /*lv = new ListView(this);
        lvEconomic = new ListView(this);

        adapter = new HomeAdapter(this, 5);
        adapterEco = new HomeAdapter(this, 2);

        lv.setAdapter(adapter);

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_home, lv, false);
        lv.addHeaderView(header, null, false);

        lvEconomic.setAdapter(adapterEco);

        inflater = getLayoutInflater();
        ViewGroup header2 = (ViewGroup) inflater.inflate(R.layout.header_home, lvEconomic, false);
        lvEconomic.addHeaderView(header2, null, false);

        Vector<View> pages = new Vector<View>();
        pages.add(lv);
        pages.add(lvEconomic);

        iv_bg = (ImageView) header.findViewById(R.id.iv_bg);*/

        ViewPager vp = (ViewPager) findViewById(R.id.vf);
        MyPagerAdapter adapter = new MyPagerAdapter();
        vp.setAdapter(adapter);

        PagerTitleStrip pagerTitleStrip = (PagerTitleStrip)findViewById(R.id.titlestrip);
        pagerTitleStrip.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        pagerTitleStrip.setNonPrimaryAlpha(0.3f);
        System.out.println("pagerTitleStrip.getChildCount = " + pagerTitleStrip.getChildCount());

        // Product
        // Google Android in-app conversion tracking snippet
        // Add this code to the event you'd like to track in your app.
        // See code examples and learn how to add advanced features like app deep links at:
        // https://developers.google.com/app-conversion-tracking/android/#track_in-app_events_driven_by_advertising

        AdWordsConversionReporter.reportWithConversionId(this.getApplicationContext(),
                "941816430", "TB8UCJaVkmAQ7vSLwQM", "1000.00", true);



        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        Bundle parameters = new Bundle();
        parameters.putString(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT, "HOME");
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "product");
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, "HDFU-8452");

        logger.logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT,
                1000,
                parameters);

        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
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

        // Create rect for background
        Rect rect = new Rect();
        iv_bg.getLocalVisibleRect(rect);
        System.out.println("rect.height() = " + rect.height());
        System.out.println("rect.top = " + rect.top);

        // Parallax effect for background
        if (lastTopValue != rect.top) {
            lastTopValue = rect.top;
            int y = rect.top/2;
            System.out.println("y = " + y);
            iv_bg.setY((float) y);
        }

        first = lv.getChildAt(0);
        if (first != null) {
//            System.out.println("first = " + first.getTop());
//            System.out.println("iv_bar = " + iv_bar.getBottom());
//            System.out.println("iv_bar = " + iv_bar.getTop());
        }

        // Push bar if touch the list
        /*if (rect.height() < iv_bar.getHeight()) {
            int gap = iv_bar.getHeight() - rect.height();
            System.out.println("gap = " + gap);
            iv_bar.setY((float) (0-gap));
        }*/

        // Fade bar
        float fade = ((float)(100 - (lastTopValue / 3))) / 100;
        System.out.println("fade = " + fade);
        if (firstVisibleItem == 0) {
            if (lastTopValue < 300 && lastTopValue >= -1) iv_bar.setAlpha(fade);
            else iv_bar.setAlpha(0f);
        } else {
            iv_bar.setAlpha(0f);
        }
    }

    private class MyPagerAdapter extends PagerAdapter {

        int NumberOfPages = 2;

        String[] title = {
                "ALL",
                "Economy",
                "paGe 3",
                "pagE 4",
                "page 5"};

        @Override
        public int getCount() {
            return NumberOfPages;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            lv = new ListView(Home.this);

            adapter = new HomeAdapter(Home.this, 5);
            lv.setAdapter(adapter);

            LayoutInflater inflater = getLayoutInflater();
            ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_home, lv, false);
            lv.addHeaderView(header, null, false);

            if (position == 0) {
                iv_bg = (ImageView) header.findViewById(R.id.iv_bg);
                lv.setOnScrollListener(Home.this);
            }

            lv.setOnItemClickListener(Home.this);

            container.addView(lv);
            return lv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ListView) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        System.out.println("Home.startLocationUpdates");
    }

    protected void track(Location mCurrentLocation) {
        if (mCurrentLocation != null) {
            System.out.println("mCurrentLocation.getProvider() = " + mCurrentLocation.getProvider());
            System.out.println("mCurrentLocation.getAccuracy() = " + mCurrentLocation.getAccuracy());
            System.out.println(mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude());
        }
        else {
            System.out.println("Location null");
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        System.out.println("Home.stopLocationUpdates");
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("Home.onLocationChanged");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        track(mCurrentLocation);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
        stopLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);

        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            System.out.println("Location resume");
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("Home.onConnectionSuspended");
        System.out.println("i = " + i);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(this, NewsActivity.class));

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("Home.onConnectionFailed");
        System.out.println("connectionResult.getErrorMessage() = " + connectionResult.getErrorMessage());
    }


    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
}
