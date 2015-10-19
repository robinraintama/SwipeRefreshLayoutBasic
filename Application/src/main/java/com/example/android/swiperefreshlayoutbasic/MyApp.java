package com.example.android.swiperefreshlayoutbasic;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Environment;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.File;

/**
 * Created by Sola_MBP on 10/8/15.
 */
public class MyApp extends Application implements
        LocationListener {

    private boolean isCacheExist = false;
    private File extStorageAppBasePath;
    private File extStorageAppCachePath;

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
}
