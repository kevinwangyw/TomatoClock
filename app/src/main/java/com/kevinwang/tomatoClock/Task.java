package com.kevinwang.tomatoClock;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by lenovo on 2016/4/20.
 */
public class Task {

    private UUID mId;
    private String mTitle;
    private boolean mFinished;

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_FINISH = "finish";

    public Task () {
        mId = UUID.randomUUID();
    }

    public Task (JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        mTitle = json.getString(JSON_TITLE);
        mFinished = json.getBoolean(JSON_FINISH);
    }

    public UUID getId() {
        return mId;
    }

    public boolean isfinished() {
        return mFinished;
    }

    public void setFinished(boolean mfinished) {
        this.mFinished = mfinished;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID,mId.toString());
        json.put(JSON_TITLE,mTitle);
        json.put(JSON_FINISH,mFinished);
        return json;
    }
}

