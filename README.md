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

So, according to the above, you will get B mark, since your point is within 60-80 range. To show this result in `HealthBarView` you have to set your minValue equal to 0 (default value is 0, therefore setting minValue can be omitted for this case), maxValue equal to 100 (default value is 100, therefore setting maxValue actually can be omitted this time), set value equal to 75 (default value is 0) and finally set your labels set equal to "A|B|C|D|E". The library will do the rest. It will automatically detect the matching label for the provided value, determine the position of the indicator within the bar and draw all necessary components.

>***Note***: The library has ability to determine the ranges automatically based on the number of provided labels and the absolute difference between minValue and maxValue. However, there is also an opportunity to determine the custom ranges.

## Integration

To add `HealthBarView` to your project, first make sure that the following repository is added to you root `build.gradle`:

  ```groovy
    repositories {
        jcenter()
    }
  ```

>***Note***: If you have created the project using the Android Studio, `jcenter` repository probably has been added automatically by the IDE, so you will not need to add it manually.

Once you make sure you have `jcenter` repository in your project, all you need to do is to add the following line in `dependencies` section of your project level `build.gradle`.

See latest library version [![Download](https://api.bintray.com/packages/ogasimli/custom_view/HealtBarView/images/download.svg)](https://bintray.com/ogasimli/custom_view/HealtBarView/_latestVersion)

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
    HealthBarView:hbv_minValue="0"
    HealthBarView:hbv_showMinValue="false"
    HealthBarView:hbv_minValueTextColor="#009688"
    HealthBarView:hbv_minValueTextSize="16sp"
    HealthBarView:hbv_minValueFont="@font/lato_light_italic"
    HealthBarView:hbv_maxValue="100"
    HealthBarView:hbv_showMaxValue="false"
    HealthBarView:hbv_maxValueTextColor="#009688"
    HealthBarView:hbv_maxValueTextSize="16sp"
    HealthBarView:hbv_maxValueFont="@font/lato_light_italic"
    HealthBarView:hbv_strokeWidth="1dp"
    HealthBarView:hbv_strokeColor="#009688"
    HealthBarView:hbv_startColor="#ffc200"
    HealthBarView:hbv_endColor="#7bfbaf"
    HealthBarView:hbv_indicatorWidth="0.5dp"
    HealthBarView:hbv_indicatorColor="#009688"
    HealthBarView:hbv_indicatorTopOverflow="5dp"
    HealthBarView:hbv_indicatorBottomOverflow="5dp"
    HealthBarView:hbv_showValue="true"
    HealthBarView:hbv_valueTextColor="#009688"
    HealthBarView:hbv_valueTextSize="16sp"
    HealthBarView:hbv_valueFont="@font/lato_light_italic"
    HealthBarView:hbv_value="81"
    HealthBarView:hbv_valueSuffix="\u00B0"
    HealthBarView:hbv_valueDecimalFormat="0"
    HealthBarView:hbv_animated="true"
    HealthBarView:hbv_animationDuration="4000"
    HealthBarView:hbv_showLabel="true"
    HealthBarView:hbv_labelTextColor="#009688"
    HealthBarView:hbv_labelTextSize="16sp"
    HealthBarView:hbv_labelFont="@font/lato_light_italic"
    HealthBarView:hbv_labels="Cold|Normal|Warm|Hot|Very Hot"
    HealthBarView:hbv_labelsRange="25.5|50|75|85|100"/>
 ```

Customization of HealthBarView from Java:

 ```java
HealthBarView view = findViewById(R.id.healthBarView);

// Min value
view.setShowMinValue(true);
view.setMinValue(-20);
view.setMinValueTextColor(R.color.colorPrimaryDark);
view.setMinValueTextSize(16);
view.setMinValueFont(R.font.lato_light_italic);
//view.setMinValueFont(Typeface.create(Typeface.SERIF, Typeface.ITALIC));
view.setMinValueSuffix(" pts");
view.setMinValueDecimalFormat(new DecimalFormat("0.0"));

// Max value
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
 ```

>***Note***: Calling `setMinValue()` resets value of `HealthBarView` and sets it equal to `minValue`. Therefore, `setMinValue()` method should be called before `setValue()`.

As you may see from the above code, `HealthBarView` consists of 7 components:

    1.MinValue
    2.MaxValue
    3.Stroke
    4.Bar
    5.Indicator
    6.Value
    7.Label

In below picture you can see the above elements:
![](https://raw.githubusercontent.com/ogasimli/HealthBarView/master/misc/assets/elements.png)

Each of these components has their own properties. In below table you can find the properties of each component.

| Component | XML Attribute               | Type      | Default value      | Java method                                                                                      |
|-----------|-----------------------------|-----------|--------------------|--------------------------------------------------------------------------------------------------|
| MinValue  | hbv_showMinValue            | boolean   | FALSE              | setShowMinValue(boolean showMinValue)                                                            |
| MinValue  | hbv_minValueTextColor       | color     | #009688            | setMinValueTextColor(int minValueTextColor)                                                      |
| MinValue  | hbv_minValueTextSize        | dimension | 16sp               | setMinValueTextSize(int minValueTextSize); setMinValueTextSize(float minValueTextSize)           |
| MinValue  | hbv_minValueFont            | reference | Typeface.MONOSPACE | setMinValueFont(Typeface minValueFont); setMinValueFont(@FontRes int minValueFont)               |
| MinValue  | hbv_minValue                | float     | 0                  | setMinValue(double minValue)                                                                     |
| MinValue  | hbv_minValueSuffix          | string    |                    | setMinValueSuffix(String minValueSuffix)                                                         |
| MinValue  | hbv_minValueDecimalFormat   | string    | 0                  | setMinValueDecimalFormat(DecimalFormat minValueDecimalFormat)                                    |
| MaxValue  | hbv_showMaxValue            | boolean   | FALSE              | setShowMaxValue(boolean showMaxValue)                                                            |
| MaxValue  | hbv_maxValueTextColor       | color     | #009688            | setMaxValueTextColor(int maxValueTextColor)                                                      |
| MaxValue  | hbv_maxValueTextSize        | dimension | 16sp               | setMaxValueTextSize(int maxValueTextSize); setMaxValueTextSize(float maxValueTextSize)           |
| MaxValue  | hbv_maxValueFont            | reference | Typeface.MONOSPACE | setMaxValueFont(Typeface maxValueFont); setMaxValueFont(@FontRes int maxValueFont)               |
| MaxValue  | hbv_maxValue                | float     | 100                | setMaxValue(double maxValue)                                                                     |
| MaxValue  | hbv_maxValueSuffix          | string    |                    | setMaxValueSuffix(String maxValueSuffix)                                                         |
| MaxValue  | hbv_maxValueDecimalFormat   | string    | 0                  | setMaxValueDecimalFormat(DecimalFormat maxValueDecimalFormat)                                    |
| Stroke    | hbv_strokeWidth             | dimension | 1dp                | setStrokeWidth(int strokeWidth)setStrokeWidth(float strokeWidth)                                 |
| Stroke    | hbv_strokeColor             | color     | #009688            | setStrokeColor(int strokeColor)                                                                  |
| Bar       | hbv_startColor              | color     | #ffc200            | setStartColor(int startColor)                                                                    |
| Bar       | hbv_endColor                | color     | #7bfbaf            | setEndColor(int endColor)                                                                        |
| Indicator | hbv_indicatorWidth          | dimension | 0.5dp              | setIndicatorWidth(int indicatorWidth); setIndicatorWidth(float indicatorWidth)                   |
| Indicator | hbv_indicatorColor          | color     | #009688            | setIndicatorColor(int indicatorColor)                                                            |
| Indicator | hbv_indicatorTopOverflow    | dimension | 5dp                | setIndicatorTopOverflow(int topOverflow); setIndicatorTopOverflow(float topOverflow)             |
| Indicator | hbv_indicatorBottomOverflow | dimension | 5dp                | setIndicatorBottomOverflow(int bottomOverflow); setIndicatorBottomOverflow(float bottomOverflow) |
| Value     | hbv_showValue               | boolean   | TRUE               | setShowValue(boolean showValue)                                                                  |
| Value     | hbv_valueTextColor          | color     | #009688            | setValueTextColor(int valueTextColor)                                                            |
| Value     | hbv_valueTextSize           | dimension | 16sp               | setValueTextSize(int valueTextSize); setValueTextSize(float valueTextSize)                       |
| Value     | hbv_valueFont               | reference | Typeface.MONOSPACE | setValueFont(Typeface valueFont); setValueFont(@FontRes int valueFont)                           |
| Value     | hbv_valueSuffix             | string    |                    | setValueSuffix(String valueSuffix)                                                               |
| Value     | hbv_valueDecimalFormat      | string    | 0                  | setValueDecimalFormat(DecimalFormat valueDecimalFormat)                                          |
| Value     | hbv_animated                | boolean   | FALSE              | setAnimated(boolean animated)                                                                    |
| Value     | hbv_animationDuration       | integer   | 4000               | setAnimationDuration(long animationDuration)                                                     |
| Value     | hbv_value                   | float     | 0                  | setValue(double value)                                                                           |
| Label     | hbv_showLabel               | boolean   | TRUE               | setShowLabel(boolean showLabel)                                                                  |
| Label     | hbv_labelTextColor          | color     | #009688            | setLabelTextColor(int labelTextColor)                                                            |
| Label     | hbv_labelTextSize           | dimension | 16sp               | setLabelTextSize(int labelTextSize)setLabelTextSize(float labelTextSize)                         |
| Label     | hbv_labelFont               | reference | Typeface.MONOSPACE | setLabelFont(Typeface labelFont); setLabelFont(@FontRes int labelFont)                           |
| Label     | hbv_labels                  | string    | A&#124;B&#124;C&#124;D&#124;E       | setLabels(String[] labels); setLabels(String labelsString, String regex)                         |
| Label     | hbv_labelsRange             | string    |                    | setLabelsRange(double[] labelsRange); setLabelsRange(String labelsRangeString, String regex)     |

>***Note***: While setting labels and label ranges from xml using `HealthBarView:hbv_labels` and `HealthBarView:hbv_labelsRange` attributes use `|` symbol as the delimiter between the values. However, you can use any delimiter that you wish, for setting labels and label ranges from Java via the `setLabels(String labelsString, String regex)` and `setLabelsRange(String labelsRangeString, String regex)` methods.

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