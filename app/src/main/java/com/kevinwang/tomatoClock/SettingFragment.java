package com.kevinwang.tomatoClock;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
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
public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private SharedPreferences sharedPreferences;
    private static final String KEY_DAY_GOAL = "day_goal";
    private static final String KEY_WEEK_GOAL = "week_goal";
    private static final String KEY_MONTH_GOAL = "month_goal";
    private static final String KEY_RINGTONE_USING_STATE = "ringtone_using_state";
    private static final String KEY_RINGTONE_SETTING = "ringtone_setting";

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //including toolbar in nested PreferenceScreen
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        super.onPreferenceTreeClick(preferenceScreen, preference);

        System.out.println("the root of the clicked preferenceScreen -----> "+ preferenceScreen.getTitle());

        if (preference instanceof PreferenceScreen) {
            setUpNestedScreen((PreferenceScreen) preference);
        }
/*        else if (preference instanceof  SeekBarPreference) {
            switch(preference.getKey()) {
                case "long_rest_interval_count":
                    ((SeekBarPreference) preference).setSeekBarInfo("counts");
                    break;
                default:
                    ((SeekBarPreference) preference).setSeekBarInfo("mins");
            }
        }*/

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
            }else{
                height = toolbar.getHeight();
            }

            content.setPadding(0, height, 0, 0);

            root.addView(content);
            root.addView(toolbar);
        }

        toolbar.setTitle(preferenceScreen.getTitle());
        System.out.println("the name of the clicked preferenceScreen -----> "+ preferenceScreen.getTitle());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        System.out.println("In onSharedPreferenceChanged method and the key is "+key);
        if (key.compareTo(KEY_DAY_GOAL) == 0) {
/*            Integer day_goal = Integer.valueOf(sharedPreferences.getString(KEY_DAY_GOAL,""));
            System.out.println(day_goal);*/
            ((EditTextPreference)findPreference(key)).setSummary(sharedPreferences.getString(KEY_DAY_GOAL,""));
        }
        else if (key.compareTo(KEY_WEEK_GOAL) == 0)
        {
            ((EditTextPreference)findPreference(key)).setSummary(sharedPreferences.getString(KEY_WEEK_GOAL,""));
        }
        else if (key.compareTo(KEY_MONTH_GOAL) == 0)
        {
            ((EditTextPreference)findPreference(key)).setSummary(sharedPreferences.getString(KEY_MONTH_GOAL,""));
        }
        else if (key.compareTo(KEY_RINGTONE_USING_STATE) == 0) {
            //System.out.println(sharedPreferences.getBoolean(key,false));
            if (sharedPreferences.getBoolean(key,false)) {
                findPreference(KEY_RINGTONE_SETTING).setEnabled(true);
            }
            else {
                findPreference(KEY_RINGTONE_SETTING).setEnabled(false);
            }
        }
    }



}
