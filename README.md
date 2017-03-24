# DragScrollDetailsLayout

### 使用时候注意FragmentPagerAdapter 继承DragDetailFragmentPagerAdapter

模仿淘宝、京东、蘑菇街商品详情页，可嵌套ListView、WebView、ViewPager、FragmentTabhost等

### 1、支持ScrollView+Webview 
### 2、支持scollView+viewpager(内不可以是listview或者webbiew)
### 3、支持支持ScrollView+listview


等等

<img src="https://github.com/happylishang/DragScrollDetailsLayout/blob/master/video/scrollview%2Bviewpager.gif" width=300></img> 
<img src="https://github.com/happylishang/DragScrollDetailsLayout/blob/master/video/scrollview%2Bfragmenttabhost.gif" width=300></img>
<img src="https://github.com/happylishang/DragScrollDetailsLayout/blob/master/video/scrollview%2Blistview.gif" width=300></img> 
<img src="https://github.com/happylishang/DragScrollDetailsLayout/blob/master/video/scrollview%2Bwebview.gif" width=300></img>

### 使用说明  

**FragmentPagerAdapter请继承DragDetailFragmentPagerAdapter**
 
#### ScrollView+WebView

     <com.snail.labaffinity.view.DragScrollDetailsLayout

        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        >
        <ScrollView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:fillViewport="false">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical">
                <Button
                    android:layout_width="match_parent"
                    android:layout_height="100dp"
                    android:background="#98ff0000"
                    android:text="pull up to show more"/>
            </LinearLayout>
        </ScrollView>
        <WebView
            android:id="@+id/webview"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>
    </com.snail.labaffinity.view.DragScrollDetailsLayout>
    
#### ScrollView+ViewPager

    <com.snail.labaffinity.view.DragScrollDetailsLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <ScrollView
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">
                 <TextView
                    android:layout_width="match_parent"
                    android:layout_height="100dp"
                    android:background="#98ff0000"
                    android:text="pull up to show more"/>
            </LinearLayout>
        </ScrollView>
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical">

            <android.support.v4.app.FragmentTabHost
                android:id="@+id/tablayout"
                android:layout_width="match_parent"
                android:layout_height="wrap_content">

            </android.support.v4.app.FragmentTabHost>

            <FrameLayout
                android:id="@+id/content"
                android:layout_width="match_parent"
                android:layout_height="match_parent">

            </FrameLayout>
        </LinearLayout>

    </com.snail.labaffinity.view.DragScrollDetailsLayout>
    
#### ScrollView+FragmentTabHost

    <com.snail.labaffinity.view.DragScrollDetailsLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <ScrollView
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">
                 <TextView
                    android:layout_width="match_parent"
                    android:layout_height="100dp"
                    android:background="#98ff0000"
                    android:text="pull up to show more"/>
            </LinearLayout>
        </ScrollView>
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical">

            <android.support.v4.app.FragmentTabHost
                android:id="@+id/tablayout"
                android:layout_width="match_parent"
                android:layout_height="wrap_content">

            </android.support.v4.app.FragmentTabHost>

            <FrameLayout
                android:id="@+id/content"
                android:layout_width="match_parent"
                android:layout_height="match_parent">

            </FrameLayout>
        </LinearLayout>

    </com.snail.labaffinity.view.DragScrollDetailsLayout>
