package com.example.android.swiperefreshlayoutbasic;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Environment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Sola_MBP on 10/8/15.
 */
public class MyApp extends Application implements com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private boolean isCacheExist = false;
    private File extStorageAppBasePath;
    private File extStorageAppCachePath;
    public static MyApp mApp;
    public GoogleApiClient mGoogleApiClient;
    public LocationRequest mLocationRequest;
    public Location mCurrentLocation;
    public String mLastUpdateTime;

    @Override
    public void onCreate() {
        super.onCreate();
        // Check if the external storage is writeable
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
        {
            // Retrieve the base path for the application in the external storage
            File externalStorageDir = Environment.getExternalStorageDirectory();

            if (externalStorageDir != null)
            {
                // {SD_PATH}/Android/data/com.devahead.androidwebviewcacheonsd
                extStorageAppBasePath = new File(externalStorageDir.getAbsolutePath() +
                        File.separator + "Android" + File.separator + "data" +
                        File.separator + getPackageName());
            }

            if (extStorageAppBasePath != null)
            {
                // {SD_PATH}/Android/data/com.devahead.androidwebviewcacheonsd/cache
                extStorageAppCachePath = new File(extStorageAppBasePath.getAbsolutePath() +
                        File.separator + "cache");

                boolean isCachePathAvailable = true;

                if (!extStorageAppCachePath.exists())
                {
                    // Create the cache path on the external storage
                    isCachePathAvailable = extStorageAppCachePath.mkdirs();
                }

                if (!isCachePathAvailable)
                {
                    // Unable to create the cache path
                    extStorageAppCachePath = null;
                }
            }
        }

        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        mApp = this;
    }

    @Override
    public File getCacheDir()
    {
        // NOTE: this method is used in Android 2.2 and higher

        if (extStorageAppCachePath != null)
        {
            // Use the external storage for the cache
            return extStorageAppCachePath;
        }
        else
        {
            // /data/data/com.devahead.androidwebviewcacheonsd/cache
            return super.getCacheDir();
        }
    }

    public void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        System.out.println("Home.startLocationUpdates");
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        System.out.println("Home.stopLocationUpdates");
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("Home.onLocationChanged");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        System.out.println("mCurrentLocation.getProvider() = " + mCurrentLocation.getProvider());
        System.out.println("mCurrentLocation.getAccuracy() = " + mCurrentLocation.getAccuracy());
        System.out.println(mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude());
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("connectionResult.getErrorMessage() = " + connectionResult.getErrorMessage());
    }
}
