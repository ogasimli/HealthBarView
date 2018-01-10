# HealthBarView

[![Download](https://api.bintray.com/packages/ogasimli/custom_view/HealtBarView/images/download.svg)](https://bintray.com/ogasimli/custom_view/HealtBarView/_latestVersion)

`HealthBarView` is light library to indicate ViewPager's selected page with different animations and ability to customise it as you need.

![](https://raw.githubusercontent.com/ogasimli/HealthBarView/master/misc/assets/main_animation.gif)

## Integration

To add `HealthBarView` to your project, first make sure in root `build.gradle` you have specified the following repository:

  ```groovy
    repositories {
        jcenter()
    }
  ```

>***Note***: by creating new project in Android Studio it will have `jcenter` repository specified by default, so you will not need to add it manually.

Once you make sure you have `jcenter` repository in your project, all you need to do is to add the following line in `dependencies` section of your project `build.gradle`.

See latest library version [![Download](https://api.bintray.com/packages/ogasimli/custom_view/HealtBarView/images/download.svg?version=0.1.0)](https://bintray.com/ogasimli/custom_view/HealtBarView/0.1.0/link)

 ```groovy
compile 'org.ogasimli:healthbarview:X.X.X'
 ```
If your project already use `appcompat-v7` support library, you can omit `HealthBarView` dependencies by adding a single .aar file to your project, that will decrease total amount of methods used in your project.

 ```groovy
 compile 'org.ogasimli:healthbarview:X.X.X@aar'
 ```

Keep in mind, that `HealthBarView` has min [API level 14](https://developer.android.com/about/dashboards/index.html) and these dependencies:

 ```groovy
 compile 'com.android.support:appcompat-v7:27.0.2'
 ```

## Usage Sample

Usage of `HealthBarView` is quite simple. All you need to do is to declare a view in your `layout.xml` - that's it!

`HealthBarView` is fully customizable both from xml and Java.

Customization of HealthBarView from XML layout:

 ```xml
<org.ogasimli.healthbarview.HealthBarView
    android:id="@+id/healthbarview_xml"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_gravity="center"
    android:layout_marginBottom="16dp"
    HealthBarView:hbv_showLabel="true"
    HealthBarView:hbv_labelTextColor="#009688"
    HealthBarView:hbv_labelTextSize="16sp"
    HealthBarView:hbv_labelFont="@font/lato_light_italic"
    HealthBarView:hbv_labels="Poor,Below Average,Average,Above Average,Good,Excellent"
    HealthBarView:hbv_showValue="true"
    HealthBarView:hbv_valueTextColor="#009688"
    HealthBarView:hbv_valueTextSize="16sp"
    HealthBarView:hbv_valueFont="@font/lato_light_italic"
    HealthBarView:hbv_minValue="0"
    HealthBarView:hbv_maxValue="100"
    HealthBarView:hbv_value="90"
    HealthBarView:hbv_valueSuffix="\u00B0"
    HealthBarView:hbv_valueDecimalFormat="0"
    HealthBarView:hbv_indicatorWidth="0.5dp"
    HealthBarView:hbv_indicatorColor="#009688"
    HealthBarView:hbv_strokeWidth="1dp"
    HealthBarView:hbv_strokeColor="#009688"
    HealthBarView:hbv_colorFrom="#ffc200"
    HealthBarView:hbv_colorTo="#7bfbaf"
    HealthBarView:hbv_animated="true"
    HealthBarView:hbv_animationDuration="4000"/>
 ```

Customization of HealthBarView from Java:

 ```java
HealthBarView view = findViewById(R.id.healthBarView);
// Label
view.setShowLabel(true);
view.setLabelTextColor(R.color.colorPrimaryDark);
view.setLabelTextSize(16);
view.setLabelFont(R.font.lato_light_italic);
//view.setLabelFont(Typeface.create(Typeface.SERIF, Typeface.ITALIC));
view.setLabels(new String[]{"Bad", "Good", "Better", "Best"});
//view.setLabels("Bad,Good,Better,Best", Pattern.quote(","));

// Value
view.setShowValue(true);
view.setValueTextColor(R.color.colorPrimaryDark);
view.setValueTextSize(16);
view.setValueFont(R.font.lato_light_italic);
//view.setValueFont(Typeface.create(Typeface.SERIF, Typeface.ITALIC));
view.setValueSuffix("%");
view.setValueDecimalFormat(new DecimalFormat("0.0"));

// Bar Indicator
view.setIndicatorWidth(0.5f);
view.setIndicatorColor(R.color.colorPrimaryDark);

// Bar Stroke
view.setStrokeWidth(1);
view.setStrokeColor(R.color.colorPrimaryDark);

// Bar colors
view.setColorFrom(R.color.colorPrimary);
view.setColorTo(R.color.colorAccent);

// Animation
view.setAnimated(true);
view.setAnimationDuration(4000);

view.setMinValue(-20);
view.setMaxValue(34);
view.setValue(20f);
 ```

>***Note***: Calling `setMinValue()` resets value of `HealthBarView` and sets it equal to `minValue`. Therefore, `setMinValue()` method should be called before `setValue()`.

As you may see from the above code, `HealthBarView` consists of 4 componenets:

    1.Value
    2.Label
    3. Indicator
    4. Bar
    5. Stroke

In below picture you can see the above elements:
![](https://raw.githubusercontent.com/ogasimli/HealthBarView/master/misc/assets/elements.gif)

Each of these componenets has their own properties. In below table you can find the properties of each componenet.

<html>
<head>
</head>
<body>
    <hr>
    <table cellspacing=1 border=1>
        <tr>
            <td>Property</td>
            <td>Type</td>
            <td>Default value</td>
        </tr>
        <tr>
            <td colspan=3>
                <center>Label</td>
        </tr>
        <tr>
            <td>hbv_showLabel</td>
            <td>boolean</td>
            <td>TRUE</td>
        </tr>
        <tr>
            <td>hbv_labelTextColor</td>
            <td>color</td>
            <td>#009688</td>
        </tr>
        <tr>
            <td>hbv_labelTextSize</td>
            <td>dimension</td>
            <td>16sp</td>
        </tr>
        <tr>
            <td>hbv_labelFont</td>
            <td>reference</td>
            <td>Typeface.MONOSPACE</td>
        </tr>
        <tr>
            <td>hbv_labels</td>
            <td>string</td>
            <td>Poor,Below Average,Average,Above Average,Good,Excellent</td>
        </tr>
        <tr>
            <td>hbv_showValue</td>
            <td>boolean</td>
            <td>TRUE</td>
        </tr>
        <tr>
            <td colspan=3>
                <center>Value</td>
        </tr>
        <tr>
            <td>hbv_valueTextColor</td>
            <td>color</td>
            <td>#009688</td>
        </tr>
        <tr>
            <td>hbv_valueTextSize</td>
            <td>dimension</td>
            <td>16sp</td>
        </tr>
        <tr>
            <td>hbv_valueFont</td>
            <td>reference</td>
            <td>Typeface.MONOSPACE</td>
        </tr>
        <tr>
            <td>hbv_minValue</td>
            <td>float</td>
            <td>0</td>
        </tr>
        <tr>
            <td>hbv_maxValue</td>
            <td>float</td>
            <td>100</td>
        </tr>
        <tr>
            <td>hbv_value</td>
            <td>float</td>
            <td>90</td>
        </tr>
        <tr>
            <td>hbv_valueSuffix</td>
            <td>string</td>
            <td></td>
        </tr>
        <tr>
            <td>hbv_valueDecimalFormat</td>
            <td>string</td>
            <td>0</td>
        </tr>
        <tr>
            <td colspan=3>
                <center>Indicator</td>
        </tr>
        <tr>
            <td>hbv_indicatorWidth</td>
            <td>dimension</td>
            <td>0.5dp</td>
        </tr>
        <tr>
            <td>hbv_indicatorColor</td>
            <td>color</td>
            <td>#009688</td>
        </tr>
        <tr>
            <td colspan=3>
                <center>Stroke</td>
        </tr>
        <tr>
            <td>hbv_strokeWidth</td>
            <td>dimension</td>
            <td>1dp</td>
        </tr>
        <tr>
            <td>hbv_strokeColor</td>
            <td>color</td>
            <td>#009688</td>
        </tr>
        <tr>
            <td colspan=3>
                <center>Bar</td>
        </tr>
        <tr>
            <td>hbv_colorFrom</td>
            <td>color</td>
            <td>#ffc200</td>
        </tr>
        <tr>
            <td>hbv_colorTo</td>
            <td>color</td>
            <td>#7bfbaf</td>
        </tr>
        <tr>
            <td colspan=3>
                <center>Animation</td>
        </tr>
        <tr>
            <td>hbv_animated</td>
            <td>boolean</td>
            <td>FALSE</td>
        </tr>
        <tr>
            <td>hbv_animationDuration</td>
            <td>integer</td>
            <td>4000</td>
        </tr>
    </table>
    <hr>
</body>
</html>

## Release Note

See release notes on [github releases](https://github.com/ogasimli/HealthBarView/releases) or [Bintray release notes](https://bintray.com/ogasimli/custom_view/HealtBarView#release).

## License

    Copyright 2018 Orkhan Gasimli

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.