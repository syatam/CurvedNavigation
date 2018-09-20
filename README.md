# CurvedNavigation

NavigationView from android design support library with curved edge

<img src="https://github.com/syatam/CurvedNavigation/blob/master/media/insideCurve.png" width="303"> 	<img src="https://github.com/syatam/CurvedNavigation/blob/master/media/outsideCurve.png" width="303">

# Sample

## Outside Curve

```xml
<android.support.v4.widget.DrawerLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/drawer_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true"
    tools:openDrawer="start">
    
    ...
    
    <com.syatam.curvednavigation.CurvedNavigation
        android:id="@+id/nav_view"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_gravity="start"
        android:background="@android:color/white"
        android:fitsSystemWindows="true"
        app:itemBackground="@android:color/white"
        app:headerLayout="@layout/nav_header_main"
        app:menu="@menu/activity_main_drawer"
        app:curve_direction="curveOutside"
        app:curve_width="96dp"/>
        
</android.support.v4.widget.DrawerLayout>
```

<img src="https://github.com/syatam/CurvedNavigation/blob/master/media/outsideCurve.png" width="303">


## Inside Curve

```xml
<android.support.v4.widget.DrawerLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/drawer_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true"
    tools:openDrawer="start">
    
    ...
    
    <com.syatam.curvednavigation.CurvedNavigation
        android:id="@+id/nav_view"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_gravity="start"
        android:background="@android:color/white"
        android:fitsSystemWindows="true"
        app:itemBackground="@android:color/white"
        app:headerLayout="@layout/nav_header_main"
        app:menu="@menu/activity_main_drawer"
        app:curve_direction="curveInside"
        app:curve_width="96dp"/>
</android.support.v4.widget.DrawerLayout>
```
<img src="https://github.com/syatam/CurvedNavigation/blob/master/media/insideCurve.png" width="303">


## Translucent status or navigation bar

Simply add next lines to your ```styles-v21``` folder

```xml
<style name="AppTheme" parent="AppTheme.Base">
    <item name="android:windowTranslucentNavigation">true</item>
    <item name="android:windowTranslucentStatus">true</item>
</style>
```

# Download
In your module's build.gradle file:

```groovy
dependencies {
    compile 'com.syatam:curvenavigation:1.2.0'
}
```

# Additionally

```CurveNavigation``` also supports end|right gravity mode for displaying it on the right side of the screen. To prevent child views from cutting I recommend to support right-to-left direction. For that you need:

1. Don't forget to support right-to-left mode by adding ```android:supportsRtl="true"``` inside your ```<application/>``` tag in ```AndroidManifest.xml```.
2. Add ```android:layoutDirection="rtl"``` to ```CurveNavigation```.

You can look how to implement this more closely in the [sample app](https://github.com/syatam/CurvedNavigation/tree/master/app)


License
--------

    Copyright 2018 Sreenu Yatam.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
