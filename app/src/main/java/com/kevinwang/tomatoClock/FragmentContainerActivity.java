package com.kevinwang.tomatoClock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;

public class FragmentContainerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        FragmentManager fragmentManager = getSupportFragmentManager();
        ViewPager viewPager = (ViewPager)findViewById(R.id.fragmentPager);
        /* Inside the activity */
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView clock_toolbar_text = (TextView) toolbar.findViewById(R.id.clock_toolbar);

        viewPager.setAdapter(new ViewPagerAdapter(fragmentManager));

        TabPageIndicator tabPageIndicator = (TabPageIndicator)findViewById(R.id.tabsTitle);
        tabPageIndicator.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                View menuItemView = findViewById(R.id.menu_settings);
                PopupMenu popupMenu = new PopupMenu(this,menuItemView);
                popupMenu.getMenu().add(Menu.NONE,1,Menu.NONE,"偏好设置");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case 1:
                                Intent intent = new Intent(FragmentContainerActivity.this,SettingsActivity.class);
                                startActivity(intent);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
                return true;
            case R.id.menu_work:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
