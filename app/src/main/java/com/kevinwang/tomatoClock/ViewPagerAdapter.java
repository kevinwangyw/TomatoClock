package com.kevinwang.tomatoClock;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by lenovo on 2016/4/7.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    // Tab Titles
    private String tabtitles[] = new String[] { "土豆列表", "历史"};
    Context context;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            // Open FragmentTab1.java
            case 0:
                TaskListFragment fragmenttab1 = new TaskListFragment();
                return fragmenttab1;

            // Open FragmentTab2.java
            case 1:
                TaskHistoryFragment fragmenttab2 = new TaskHistoryFragment();
                return fragmenttab2;
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }
}
