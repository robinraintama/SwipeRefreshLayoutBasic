package com.example.android.swiperefreshlayoutbasic;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Tracker extends Activity {

    private TextView lonlat;
    private Button bTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        lonlat = (TextView) findViewById(R.id.lonlat);
        bTrack = (Button) findViewById(R.id.bTrack);
        bTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location mCurrentLocation = MyApp.mApp.mCurrentLocation;
                if (mCurrentLocation != null) {
                    String location = mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude();
                    lonlat.setText(lonlat.getText().toString() + "\n" + location);
                }
                else Toast.makeText(getBaseContext(),"Location not found, Please try agai",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
