package com.example.android.swiperefreshlayoutbasic;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class Tracker extends Activity {

    private TextView lonlat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        lonlat = (TextView) findViewById(R.id.lonlat);
    }

}
