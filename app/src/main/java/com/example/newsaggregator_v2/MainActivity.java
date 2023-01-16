package com.example.newsaggregator_v2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ArrayList<Source> sourceList = new ArrayList<>();
    private HashMap<String, ArrayList<Source>> sourceData = new HashMap<>();
    private ArrayList<Story> storyList = new ArrayList<>();

    private Menu opt_menu;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayAdapter<Source> arrayAdapter;

    private StoryAdapter storyAdapter;
    ViewPager2 viewPager2;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.drawer_list);

        SourceLoader.downloadSource(this);

        // drawelist listerner
        mDrawerList.setOnItemClickListener(
                (parent, view, position, id) -> {
                    Source s = sourceList.get(position);
                    setTitle(s.getName());
                    StoryLoader.downloadStory(this, s.getId());
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
        );
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

    }

    public void updateSourceData(ArrayList<Source> listIn) {
        // update sourceData
        if(listIn == null) {
            Toast.makeText(this,
                    "Developer accounts are limited to 100 requests over a 24 hour period", Toast.LENGTH_LONG).show();
            /*
            "status": "error",
            "code": "rateLimited",
            "message": "You have made too many requests recently.
            Developer accounts are limited to 100 requests over a 24 hour period (50 requests available every 12 hours).
            Please upgrade to a paid plan if you need more requests."
            */
            return;
        }
        for (Source s : listIn) {
            if (!sourceData.containsKey(s.getCategory())) {
                sourceData.put(s.getCategory(), new ArrayList<>());
            }
            ArrayList<Source> slist = sourceData.get(s.getCategory());
            if (slist != null) {
               slist.add(s);
            }
        }

        // All
        sourceData.put("All", listIn);

        // all the category
        ArrayList<String> tempList = new ArrayList<>(sourceData.keySet());
        Collections.sort(tempList);
        for (String s : tempList) {
            opt_menu.add(s);
        }

        sourceList.addAll(listIn);
        arrayAdapter = new ArrayAdapter<>(this, R.layout.drawer_item, sourceList);
        mDrawerList.setAdapter(arrayAdapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    public void updateStoryData(ArrayList<Story> listIn) {

        storyList.clear();
        storyList.addAll(listIn);

        storyAdapter = new StoryAdapter(this, storyList);
        storyAdapter.notifyDataSetChanged();

        viewPager2 = findViewById(R.id.view_pager2);
        viewPager2.setAdapter(storyAdapter);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager2.setBackgroundColor(getResources().getColor(R.color.white));

    }


    // functions to make the drawer-toggle work properly:
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }

        setTitle(item.getTitle().toString().toUpperCase(Locale.ROOT));

        sourceList.clear();
        ArrayList<Source> slist = sourceData.get(item.getTitle().toString());
        if (slist != null) {
            sourceList.addAll(slist);
        }

        arrayAdapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        opt_menu = menu;
        return true;
    }

    public void urlClicked(View v) {
        int position = viewPager2.getCurrentItem();
        Story s = storyList.get(position);
        String url = s.getUrl();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

}