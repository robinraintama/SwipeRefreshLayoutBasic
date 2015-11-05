package com.example.android.swiperefreshlayoutbasic;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by Sola_MBP on 10/26/15.
 */
public class Chart extends Activity {
    private String DESCRIPTION = "Palm Plantation Production";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PieChart pieChart = new PieChart(this);
        pieChart.setBackgroundColor(Color.GRAY);
        pieChart.setDescription(DESCRIPTION);
        pieChart.setDescriptionColor(Color.WHITE);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColorTransparent(true);
        pieChart.setHoleRadius(50);
        pieChart.setTransparentCircleRadius(50);

        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(147531f, 1));
        entries.add(new Entry(17290f, 2));
        entries.add(new Entry(7700f, 3));

        ArrayList<String> label = new ArrayList<>();
        label.add("Matang");
        label.add("Mentah");
        label.add("Overripe");

        PieDataSet dataSet = new PieDataSet(entries, "FFB");
        dataSet.setSliceSpace(5f);
//        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        dataSet.setColors(colors);

        PieData data = new PieData(label, dataSet);
        data.setValueTextColor(Color.BLUE);
        data.setValueTextSize(11f);

        pieChart.setData(data);
        pieChart.highlightValues(null);
        pieChart.animateY(2000, Easing.EasingOption.EaseInCirc);
        pieChart.invalidate();

        setContentView(pieChart);

    }
}
