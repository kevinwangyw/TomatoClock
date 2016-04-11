package com.kevinwang.tomatoClock;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ywwang on 2016/4/10.
 */
public class SeekBarPreference extends Preference {
    private Context context;

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        persistInt(15);
        //save a value for the setting
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return layoutInflater.inflate(R.layout.seekbar_preference, parent, false);
    }
}
