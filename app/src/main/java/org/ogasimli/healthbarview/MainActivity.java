/*
 * Copyright (c) 2018 Orkhan Gasimli - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * This file is proprietary and confidential.
 * Written by Orkhan Gasimli <orkhan.gasimli@gmail.com> in 2018.
 */

package org.ogasimli.healthbarview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HealthBarView view = findViewById(R.id.healthbarview_java);

        // Setting min value resets value to min value. Therefore, min value should be set before
        view.setShowMinValue(true);
        view.setMinValue(-20);
        view.setMinValueTextColor(R.color.colorPrimaryDark);
        view.setMinValueTextSize(16);
        view.setMinValueFont(R.font.lato_light_italic);
        //view.setMinValueFont(Typeface.create(Typeface.SERIF, Typeface.ITALIC));
        view.setMinValueSuffix(" pts");
        view.setMinValueDecimalFormat(new DecimalFormat("0.0"));

        view.setMaxValue(34);
        view.setShowMaxValue(true);
        view.setMaxValueTextColor(R.color.colorPrimaryDark);
        view.setMaxValueTextSize(16);
        view.setMaxValueFont(R.font.lato_light_italic);
        //view.setMaxValueFont(Typeface.create(Typeface.SERIF, Typeface.ITALIC));
        view.setMaxValueSuffix(" pts");
        view.setMaxValueDecimalFormat(new DecimalFormat("0.0"));

        // Bar Stroke
        view.setStrokeWidth(1);
        view.setStrokeColor(R.color.colorPrimaryDark);

        // Bar Fill
        view.setStartColor(R.color.colorPrimary);
        view.setEndColor(R.color.colorAccent);

        // Bar Indicator
        view.setIndicatorWidth(0.5f);
        view.setIndicatorColor(R.color.colorPrimaryDark);
        view.setIndicatorTopOverflow(15);
        view.setIndicatorBottomOverflow(15);

        // Value
        view.setShowValue(true);
        view.setValueTextColor(R.color.colorPrimaryDark);
        view.setValueTextSize(16);
        view.setValueFont(R.font.lato_light_italic);
        //view.setValueFont(Typeface.create(Typeface.SERIF, Typeface.ITALIC));
        view.setValueSuffix(" pts");
        view.setValueDecimalFormat(new DecimalFormat("0.0"));
        // Animation
        view.setAnimated(true);
        view.setAnimationDuration(4000);
        view.setValue(16.1f); // Set value after setting all other attributes of value element

        // Label
        view.setShowLabel(true);
        view.setLabelTextColor(R.color.colorPrimaryDark);
        view.setLabelTextSize(16);
        view.setLabelFont(R.font.lato_light_italic);
        //view.setLabelFont(Typeface.create(Typeface.SERIF, Typeface.ITALIC));
        view.setLabels(new String[]{"Poor","Below Average","Average","Above Average","Good","Excellent"});
        //view.setLabels("Poor,Below Average,Average,Above Average,Good,Excellent", Pattern.quote(","));
        view.setLabelsRange(new double[]{-10D,0D,10D,15D,28D,34D});
        //view.setLabelsRange("-10,0,10,15,28,34", Pattern.quote(","));
    }
}