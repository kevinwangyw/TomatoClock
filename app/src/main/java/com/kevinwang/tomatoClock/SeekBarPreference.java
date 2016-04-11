package com.kevinwang.tomatoClock;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by ywwang on 2016/4/10.
 */
public class SeekBarPreference extends Preference implements SeekBar.OnSeekBarChangeListener {
    private Context context;
    private int mProgress;
    private int mMax = 25;
    private boolean mTrackingTouch;
    private TextView val_text;
    private OnSeekBarPrefsChangeListener mListener = null;
    private String mtype;
    private int seekbar_case;

    public SeekBarPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        switch (getKey()) {
            case "long_rest_interval_count":
                setMax(6);
                seekbar_case = 4;
                break;
            case "long_rest_length":
                setMax(15);
                seekbar_case = 10;
                break;
            case "rest_length":
                setMax(10);
                seekbar_case = 5;
                break;
            case "tomato_length":
                setMax(25);
                seekbar_case = 15;
                break;
        }
        setLayoutResource(R.layout.seekbar_preference);
    }

    public SeekBarPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        //save a value for the setting
    }

    public SeekBarPreference(Context context) {
        this(context, null);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        switch (getKey()) {
            case "long_rest_interval_count":
                setSeekBarInfo("counts");
                break;
            default:
                setSeekBarInfo("mins");
        }

        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekbar);
        seekBar.setMax(mMax);
        seekBar.setProgress(mProgress);
        seekBar.setEnabled(isEnabled());
        seekBar.setOnSeekBarChangeListener(this);
        val_text = (TextView) view.findViewById(R.id.seekbar_info);
        val_text.setText(String.valueOf(mProgress+seekbar_case)+" "+mtype);
    }

    /* @Override
    protected View onCreateView(ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return layoutInflater.inflate(R.layout.seekbar_preference, parent, false);
    }*/

    public void setSeekBarInfo (String type) {
        mtype = type;
        notifyChanged();
    }

    public void setMax(int max) {
        if (mMax != max) {
            mMax = max;
            notifyChanged();
        }
    }

    public void setDefaultValue(int defaultValue) {
        if (getPersistedInt(-1) == -1) {
            //说明没有用户设定的值，所以设置初始值
            setProgress(defaultValue);
        }
    }

    public void setProgress(int progress) {
        setProgress(progress, true);
    }

    //When the system adds your Preference to the screen, it calls onSetInitialValue()
    // to notify you whether the setting has a persisted value.
    // If there is no persisted value, this call provides you the default value.


    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setProgress(restorePersistedValue ? getPersistedInt(mProgress) : (Integer) defaultValue);
       /* Each getPersisted*() method takes an argument that specifies the default value
        to use in case there is actually no persisted value or the key does not exist.*/
    }

    public void setProgress(int progress, boolean notifychanged) {
        if (progress > mMax) {
            progress = mMax;
        }
        if (progress < 0) {
            progress = 0;
        }
        if (progress != mProgress) {
            mProgress = progress;
            persistInt(progress);
            if (notifychanged) {
                notifyChanged();
                //should be called when the data of this preference has been changed
            }
        }
    }

    public int getProgress() {
        return mProgress + seekbar_case;
    }

    public interface OnSeekBarPrefsChangeListener {
        public void onStopTrackingTouch(String key, SeekBar seekBar);

        public void onStartTrackingTouch(String key, SeekBar seekBar);

        public void onProgressChanged(String key, SeekBar seekBar, int progress, boolean fromUser);
    }

    /**
     * Persist the seekBar's progress value if callChangeListener
     * returns true, otherwise set the seekBar's progress to the stored value
     */
    void syncProgress(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        if (progress != mProgress) {
            if (callChangeListener(progress)) {
                //Call this method callChangeListener after the user changes the preference, but before the
                //internal state is set. This allows the client to ignore the user value.
                setProgress(progress, false);
                val_text.setText(String.valueOf(mProgress + seekbar_case) + " " + mtype);
            } else {
                seekBar.setProgress(mProgress);
                val_text.setText(String.valueOf(mProgress + seekbar_case) + " " + mtype);
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mListener != null) {
            mListener.onProgressChanged(getKey(), seekBar, progress, fromUser);
        }
        if (seekBar.getProgress() != mProgress) {
            syncProgress(seekBar);
        }
        if (fromUser && !mTrackingTouch) {
            syncProgress(seekBar);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (mListener != null) {
            mListener.onStartTrackingTouch(getKey(), seekBar);
        }
        /**getKey() method
         * Gets the key for this Preference, which is also the key used for storing
         * values into SharedPreferences.
         */
        mTrackingTouch = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mListener != null) {
            mListener.onStopTrackingTouch(getKey(), seekBar);
        }
        mTrackingTouch = false;
        if (seekBar.getProgress() != mProgress) {
            syncProgress(seekBar);
        }
        notifyHierarchyChanged();
    }

    //To define how your Preference class saves its state, you should extend the Preference.BaseSavedState class.
    private static class SavedState extends BaseSavedState {
        // Member that holds the setting's value
        int max;
        int progress;
        String type;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);
            // Get the current preference's value
            progress = source.readInt();
            max = source.readInt();
            type = source.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            // Write the preference's value
            dest.writeInt(progress);
            dest.writeInt(max);
            dest.writeString(type);
        }

        // Standard creator object using an instance of this class
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {

                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    //With the above implementation of Preference.BaseSavedState added to your app (usually as a subclass of your
    // Preference subclass), you then need to implement the onSaveInstanceState() and onRestoreInstanceState()
    // methods for your Preference subclass.

    @Override
    protected Parcelable onSaveInstanceState() {
        /*
         * Suppose a client uses this preference type without persisting. We
         * must save the instance state so it is able to, for example, survive
         * orientation changes.
         */
        final Parcelable superState = super.onSaveInstanceState();
        // Check whether this Preference is persistent (continually saved)
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }

        // Create instance of custom BaseSavedState
        final SavedState myState = new SavedState(superState);
        // Set the state's value with the class member that holds current
        // setting value
        myState.progress = mProgress;
        myState.max = mMax;
        myState.type = mtype;
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        // Check whether we saved the state in onSaveInstanceState
        if (!state.getClass().equals(SavedState.class)) {
            // Didn't save the state, so call superclass
            super.onRestoreInstanceState(state);
            return;
        }

        // Cast state to custom BaseSavedState and pass to superclass
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        mProgress = myState.progress;
        mMax = myState.max;
        mtype = myState.type;
        notifyChanged();

        // Set this Preference's widget to reflect the restored state
        val_text.setText(String.valueOf(mProgress + seekbar_case) + " " + mtype);
    }
}
