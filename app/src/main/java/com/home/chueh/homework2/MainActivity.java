package com.home.chueh.homework2;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

/*

    The sliding tab design is from the follow online tutorial:
    http://www.android4devs.com/2015/01/how-to-make-material-design-sliding-tabs.html

 */
public class MainActivity extends ActionBarActivity implements Tab1.OnTab1Activity, Tab2.OnTab2Activity{

    // Declaring Your View and Variables

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"In-Progress","Completed"};
    public static final String SETTINGS = "Hm2_Settings";
    int Numboftabs =2;
    private Tab1 tab1;
    private Tab2 tab2;

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lifecycle","saving...");
        SharedPreferences settings = getSharedPreferences(SETTINGS,MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("settings1",TextUtils.join("`", tab1.getItemList()));
        editor.putString("settings2",TextUtils.join("`", tab2.getItemList()));
        Log.i("lifecycle", String.format("Saving: %s - %s", TextUtils.join("`", tab1.getItemList()), TextUtils.join("`", tab2.getItemList())));
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lifecycle", "restoring...");
        SharedPreferences settings = getSharedPreferences(SETTINGS,MODE_PRIVATE);

        String str1 = settings.getString("settings1","");
        String[] strArr1 = str1.split("`");
        tab1.setItemList(strArr1);

        String str2 = settings.getString("settings2", "");
        String[] strArr2 = str2.split("`");
        tab2.setItemList(strArr2);


        Log.i("lifecycle", String.format("Retrieved: %s - %s", str1, str2));
        Log.i("lifecycle", String.format("Restored: %s - %s", Arrays.toString(strArr1), Arrays.toString(strArr2)));


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);
        tab1 = new Tab1();
        tab2 = new Tab2();
        adapter.setTab(tab1, 0);
        adapter.setTab(tab2, 1);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void completeItem(String str){
        tab2.addItem(str);
        tab2.notifyUpdate();
    }
    private void uncompleteItem(String str){
        tab1.addItem(str);
        tab1.notifyUpdate();

    }

    @Override
    public void tab1Activity(String str) {
        Log.i("INFO", "tab1Activity");
        completeItem(str);
    }

    @Override
    public void tab2Activity(String str) {
        Log.i("INFO", "tab2Activity");
        uncompleteItem(str);
    }
}