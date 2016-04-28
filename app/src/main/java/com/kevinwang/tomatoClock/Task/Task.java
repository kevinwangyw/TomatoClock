package com.kevinwang.tomatoClock.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by lenovo on 2016/4/20.
 */
public class Task {

    private UUID mId;
    private String mContent;
    private boolean mFinished;

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_FINISH = "finish";

    public Task () {
        mId = UUID.randomUUID();
        mFinished = false;
    }

    public Task (JSONObject json) throws JSONException {  //通过Jason数据对Task实例进行初始化
        mId = UUID.fromString(json.getString(JSON_ID));
        mContent = json.getString(JSON_TITLE);
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

    public String getContent() {
        return mContent;
    }

    public void setContent(String mTitle) {
        this.mContent = mTitle;
    }

    @Override
    public String toString() {
        return mContent;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID,mId.toString());
        json.put(JSON_TITLE, mContent);
        json.put(JSON_FINISH,mFinished);
        return json;
    }
}

