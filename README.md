# HealthBarView

[![Download](https://api.bintray.com/packages/ogasimli/custom_view/HealtBarView/images/download.svg)](https://bintray.com/ogasimli/custom_view/HealtBarView/_latestVersion)

Before starting let me admit that I am not very good at choosing the names for my projects, classes, variables, etc. The name of this library - `HealthBarView` doesn't reflect the nature of the library very good. But I think that finding the suitable name for this project will be a pain even for a person with good naming skills. And the reason is the very specific task that this view accomplishes.

So, what is this `HealthBarView` intended for?

To make it clear let's first take a look at below screenshots.

![](https://raw.githubusercontent.com/ogasimli/HealthBarView/master/misc/assets/main_animation.gif)

![](https://raw.githubusercontent.com/ogasimli/HealthBarView/master/misc/assets/main_screen.png)

Briefly, `HealthBarView` is determining the position of the values within a horizontal bar, which represents the range, maps the value to a corresponding label and draws it in a beautiful and customizable manner.

To make it clear let's imagine that you have attended an exam and received 75 points out of possible 100. Also, let's assume that students get A, B, C, D or E mark depending on their points and the ranges for this marks are as following:

* A -> 80-100
* B -> 60-80
* C -> 40-60
* D -> 20-40
* E -> 0-20

So, according to the above, you will get B mark, since your point is within 60-80 range. To show this result in `HealthBarView` you have to set your minValue equal to 0 (default value is 0, therefore setting minValue can be omitted for this case), maxValue equal to 100 (default value is 100, therefore setting maxValue actually can be omitted this time), set value equal to 75 (default value is 0) and finally set your labels set equal to "A, B, C, D, E". The library will do the rest. It will automatically detect the matching label for the provided value, determine the position of the indicator within the bar and draw all necessary components.

>***Note***: Currently the library determines the ranges automatically based on the number of provided labels and the absolute difference between minValue and maxValue. The possibility of adding custom ranges set will be added in future releases. Please see the [TODO](https://raw.githubusercontent.com/ogasimli/HealthBarView/master/TODO.md) file for the full list of intended features.

## Integration

To add `HealthBarView` to your project, first make sure that the following repository is added to you root `build.gradle`:

  ```groovy
    repositories {
        jcenter()
    }
  ```

>***Note***: If you have created the project using the Android Studio, `jcenter` repository probably has been added automatically by the IDE, so you will not need to add it manually.

Once you make sure you have `jcenter` repository in your project, all you need to do is to add the following line in `dependencies` section of your project level `build.gradle`.

See latest library version [![Download](https://api.bintray.com/packages/ogasimli/custom_view/HealtBarView/images/download.svg?version=0.1.0)](https://bintray.com/ogasimli/custom_view/HealtBarView/0.1.0/link)

 ```groovy
compile 'org.ogasimli:healthbarview:X.X.X'
 ```
Keep in mind, that `HealthBarView` has min [API level 14](https://developer.android.com/about/dashboards/index.html) and uses these dependencies:

 ```groovy
 compile 'com.android.support:appcompat-v7:27.0.2'
 ```

## Usage Sample

Usage of `HealthBarView` is quite simple. All you need to do is to declare a view in your `layout.xml` - that's it!

`HealthBarView` is fully customizable both from XML and Java.

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

As you may see from the above code, `HealthBarView` consists of 4 components:

    1.Value
    2.Label
    3.Indicator
    4.Bar
    5.Stroke

In below picture you can see the above elements:
![](https://raw.githubusercontent.com/ogasimli/HealthBarView/master/misc/assets/elements.png)

Each of these components has their own properties. In below table you can find the properties of each component.

| Component | Property               | Type      | Default value                                           |
|-----------|------------------------|-----------|---------------------------------------------------------|
| Label     | hbv_showLabel          | boolean   | TRUE                                                    |
| Label     | hbv_labelTextColor     | color     | #009688                                                 |
| Label     | hbv_labelTextSize      | dimension | 16sp                                                    |
| Label     | hbv_labelFont          | reference | Typeface.MONOSPACE                                      |
| Label     | hbv_labels             | string    | Poor,Below Average,Average,Above Average,Good,Excellent |
| Label     | hbv_showValue          | boolean   | TRUE                                                    |
| Value     | hbv_valueTextColor     | color     | #009688                                                 |
| Value     | hbv_valueTextSize      | dimension | 16sp                                                    |
| Value     | hbv_valueFont          | reference | Typeface.MONOSPACE                                      |
| Value     | hbv_minValue           | float     | 0                                                       |
| Value     | hbv_maxValue           | float     | 100                                                     |
| Value     | hbv_value              | float     | 90                                                      |
| Value     | hbv_valueSuffix        | string    |                                                         |
| Value     | hbv_valueDecimalFormat | string    | 0                                                       |
| Indicator | hbv_indicatorWidth     | dimension | 0.5dp                                                   |
| Indicator | hbv_indicatorColor     | color     | #009688                                                 |
| Stroke    | hbv_strokeWidth        | dimension | 1dp                                                     |
| Stroke    | hbv_strokeColor        | color     | #009688                                                 |
| Bar       | hbv_colorFrom          | color     | #ffc200                                                 |
| Bar       | hbv_colorTo            | color     | #7bfbaf                                                 |
| General   | hbv_animated           | boolean   | FALSE                                                   |
| General   | hbv_animationDuration  | integer   | 4000

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