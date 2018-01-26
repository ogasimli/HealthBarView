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

        // Label
        view.setShowLabel(true);
        view.setLabelTextColor(R.color.colorPrimaryDark);
        view.setLabelTextSize(16);
        view.setLabelFont(R.font.lato_light_italic);
        //view.setLabelFont(Typeface.create(Typeface.SERIF, Typeface.ITALIC));
        view.setLabels(new String[]{"Bad", "Good", "Better", "Best"});
        //view.setLabels("Bad,Good,Better,Best", Pattern.quote(","));

        // Setting min value resets value to min value. Therefore, min value should be set before
        view.setShowMinValue(true);
        view.setMinValue(-20);
        view.setMinValueTextColor(R.color.colorPrimaryDark);
        view.setMinValueTextSize(16);
        view.setMinValueFont(R.font.lato_light_italic);
        //view.setMinValueFont(Typeface.create(Typeface.SERIF, Typeface.ITALIC));
        view.setMinValueSuffix("%");
        view.setMinValueDecimalFormat(new DecimalFormat("0.0"));

        view.setMaxValue(34);
        view.setShowMaxValue(true);
        view.setMaxValueTextColor(R.color.colorPrimaryDark);
        view.setMaxValueTextSize(16);
        view.setMaxValueFont(R.font.lato_light_italic);
        //view.setMaxValueFont(Typeface.create(Typeface.SERIF, Typeface.ITALIC));
        view.setMaxValueSuffix("%");
        view.setMaxValueDecimalFormat(new DecimalFormat("0.0"));

        // Value
        view.setShowValue(true);
        view.setValue(34f);
        view.setValueTextColor(R.color.colorPrimaryDark);
        view.setValueTextSize(16);
        view.setValueFont(R.font.lato_light_italic);
        //view.setValueFont(Typeface.create(Typeface.SERIF, Typeface.ITALIC));
        view.setValueSuffix("%");
        view.setValueDecimalFormat(new DecimalFormat("0.0"));
        // Animation
        view.setAnimated(true);
        view.setAnimationDuration(4000);

        // Bar Indicator
        view.setIndicatorWidth(0.5f);
        view.setIndicatorColor(R.color.colorPrimaryDark);

        // Bar Stroke
        view.setStrokeWidth(1);
        view.setStrokeColor(R.color.colorPrimaryDark);

        // Bar colors
        view.setStartColor(R.color.colorPrimary);
        view.setEndColor(R.color.colorAccent);
    }
}
