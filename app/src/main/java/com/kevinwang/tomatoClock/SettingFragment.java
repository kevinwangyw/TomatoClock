package com.kevinwang.tomatoClock;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by lenovo on 2016/4/9.
 */
public class SettingFragment extends PreferenceFragment implements SharedPreferences
        .OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private SharedPreferences sharedPreferences;
    private static final String KEY_DAY_GOAL = "day_goal";
    private static final String KEY_WEEK_GOAL = "week_goal";
    private static final String KEY_MONTH_GOAL = "month_goal";
    private static final String KEY_RINGTONE_USING_STATE = "ringtone_using_state";
    private static final String KEY_RINGTONE_SETTING = "ringtone_setting";
    private static final String KEY_IS_VIBRATE = "is_vibrate";
    private static final String KEY_IS_LONG_VIBRATE = "is_long_vibrate";
    private static final String KEY_TOMATO_LENGTH = "tomato_length";
    private static final String KEY_REST_LENGTH = "rest_length";
    private static final String KEY_LONG_REST_LENGTH = "long_rest_length";
    private static final String KEY_COUNT_INTERVAL = "long_rest_interval_count";
    private RingtonePreference mRingTone;
    private SwitchPreference switch_is_vibrate;
    private SwitchPreference switch_is_long_vibrate;


    public static SettingFragment newInstance(Bundle bundle) {
        SettingFragment settingFragment = new SettingFragment();
        settingFragment.setArguments(bundle);
        return settingFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings_preference);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        initialPreference(sharedPreferences);

        System.out.println(sharedPreferences.getInt(KEY_LONG_REST_LENGTH,1));
    }

    private void initialPreference (SharedPreferences sharedPreferences) {
        ((EditTextPreference) findPreference(KEY_DAY_GOAL)).setSummary(sharedPreferences.getString(KEY_DAY_GOAL, ""));
        ((EditTextPreference) findPreference(KEY_WEEK_GOAL)).setSummary(sharedPreferences.getString(KEY_WEEK_GOAL, ""));
        ((EditTextPreference) findPreference(KEY_MONTH_GOAL)).setSummary(sharedPreferences.getString(KEY_MONTH_GOAL,
                ""));

        mRingTone = (RingtonePreference) findPreference(KEY_RINGTONE_SETTING);
        if (sharedPreferences.getBoolean(KEY_RINGTONE_USING_STATE, false)) {
            mRingTone.setEnabled(true);
        } else {
            sharedPreferences.edit().putString(KEY_RINGTONE_SETTING, "".toString()).commit();
            mRingTone.setEnabled(false);
        }
        mRingTone.setSummary(getRingtoneName(Uri.parse(sharedPreferences.getString(KEY_RINGTONE_SETTING, ""))));
        mRingTone.setOnPreferenceChangeListener(this);

        switch_is_vibrate = (SwitchPreference) findPreference(KEY_IS_VIBRATE);
        switch_is_long_vibrate = (SwitchPreference) findPreference(KEY_IS_LONG_VIBRATE);
        if (sharedPreferences.getBoolean(KEY_IS_VIBRATE, false)) {
            findPreference(KEY_IS_LONG_VIBRATE).setEnabled(true);
        } else {
            sharedPreferences.edit().putBoolean(KEY_IS_LONG_VIBRATE, false).commit();
            switch_is_long_vibrate.setChecked(false);
            findPreference(KEY_IS_LONG_VIBRATE).setEnabled(false);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //including toolbar in nested PreferenceScreen
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        super.onPreferenceTreeClick(preferenceScreen, preference);

        if (preference instanceof PreferenceScreen) {
            setUpNestedScreen((PreferenceScreen) preference);
        }

        return false;
    }


    //PreferenceScreens are based as a wrapper dialog,
    // so we need to capture the dialog layout to add the toolbar to it.
    private void setUpNestedScreen(PreferenceScreen preferenceScreen) {
        final Dialog dialog = preferenceScreen.getDialog();

        Toolbar toolbar;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            LinearLayout root = (LinearLayout) dialog.findViewById(android.R.id.list).getParent();
            toolbar = (Toolbar) LayoutInflater.from(getActivity()).inflate(R.layout.settings_toolbar, root, false);
            root.addView(toolbar, 0);
        } else {
            ViewGroup root = (ViewGroup) dialog.findViewById(android.R.id.content);
            ListView content = (ListView) root.getChildAt(0);

            root.removeAllViews();

            toolbar = (Toolbar) LayoutInflater.from(getActivity()).inflate(R.layout.settings_toolbar, root, false);

            int height;
            TypedValue tv = new TypedValue();
            if (getActivity().getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
                height = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            } else {
                height = toolbar.getHeight();
            }

            content.setPadding(0, height, 0, 0);

            root.addView(content);
            root.addView(toolbar);
        }

        toolbar.setTitle(preferenceScreen.getTitle());
        System.out.println("the name of the clicked preferenceScreen -----> " + preferenceScreen.getTitle());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        //--------------------
        if (key.compareTo(KEY_DAY_GOAL) == 0) {
/*            Integer day_goal = Integer.valueOf(sharedPreferences.getString(KEY_DAY_GOAL,""));
            System.out.println(day_goal);*/
            ((EditTextPreference) findPreference(key)).setSummary(sharedPreferences.getString(KEY_DAY_GOAL, ""));
        }
        if (key.compareTo(KEY_WEEK_GOAL) == 0) {
            ((EditTextPreference) findPreference(key)).setSummary(sharedPreferences.getString(KEY_WEEK_GOAL, ""));
        }
        if (key.compareTo(KEY_MONTH_GOAL) == 0) {
            ((EditTextPreference) findPreference(key)).setSummary(sharedPreferences.getString(KEY_MONTH_GOAL, ""));
        }
        //--------------------
        if (key.compareTo(KEY_RINGTONE_USING_STATE) == 0) {
            //System.out.println(sharedPreferences.getBoolean(key,false));
            if (sharedPreferences.getBoolean(key, false)) {
                mRingTone.setEnabled(true);
            } else {
                sharedPreferences.edit().putString(KEY_RINGTONE_SETTING, "".toString()).commit();
                mRingTone.setSummary(getRingtoneName(Uri.parse(sharedPreferences.getString(KEY_RINGTONE_SETTING, ""))));
                mRingTone.setEnabled(false);
            }
        }
        //--------------------
        if (key.compareTo(KEY_IS_VIBRATE) == 0) {
            if (sharedPreferences.getBoolean(key, false)) {
                findPreference(KEY_IS_LONG_VIBRATE).setEnabled(true);
                vibrator.vibrate(1000);
            } else {
                vibrator.cancel();
                sharedPreferences.edit().putBoolean(KEY_IS_LONG_VIBRATE, false).commit();
                switch_is_long_vibrate.setChecked(false);
                findPreference(KEY_IS_LONG_VIBRATE).setEnabled(false);
            }
        }
        if (key.compareTo(KEY_IS_LONG_VIBRATE) == 0) {
            if (sharedPreferences.getBoolean(key, false)) {
                // The following numbers represent millisecond lengths
                int dash = 500;     // Length of a Morse Code "dash" in milliseconds
                int short_gap = 250;    // Length of Gap Between dots/dashes
                long[] pattern = {
                        0,  // Start immediately
                        dash, short_gap, dash, short_gap, dash,
                        short_gap, dash, short_gap, dash,short_gap,
                        dash, short_gap, dash
                };
                // Only perform this pattern one time (-1 means "do not repeat")
                vibrator.vibrate(pattern, -1);
            }
            else {
                vibrator.cancel();
            }
        }
        //--------------------
        if (key.compareTo(KEY_TOMATO_LENGTH) == 0) {

        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Uri ringtoneUri = Uri.parse((String) newValue);
        String strSummary = getRingtoneName(ringtoneUri);
        preference.setSummary(strSummary);
        // 此处必须加上，否则不会保存
        sharedPreferences.edit().putString(preference.getKey(), (String) newValue).commit();

        return false;
    }


    // 获取提示音名称
    public String getRingtoneName(Uri uri) {
        if (uri.toString().compareTo("") == 0) {
            return "静音";
        } else {
            Ringtone r = RingtoneManager.getRingtone(getActivity(), uri);
            return r.getTitle(getActivity());
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }
}
