package com.kevinwang.tomatoClock;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by lenovo on 2016/4/20.
 */
public class TaskJSONSerializer {
    private Context mContext;
    private String mFileName;

    public TaskJSONSerializer(Context mContext,String mFileName ) {
        this.mFileName = mFileName;
        this.mContext = mContext;
    }

    public void saveTasks(ArrayList<Task> Tasks) throws JSONException,IOException {
        JSONArray array = new JSONArray();
        for (Task task : Tasks) {
            array.put(task.toJSON());
        }

        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFileName,Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        }finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public ArrayList<Task> loadTasks() throws IOException,JSONException {
        ArrayList<Task> Tasks = new ArrayList<Task>();
        BufferedReader reader = null;
        try {
            InputStream in = mContext.openFileInput(mFileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            JSONArray array = (JSONArray)new JSONTokener(jsonString.toString()).nextValue();
            for (int i = 0; i < array.length(); i++) {
                Tasks.add(new Task(array.getJSONObject(i)));
            }
        }catch (FileNotFoundException e) {

        }finally {
            if (reader != null) {
                reader.close();
            }
        }
        return Tasks;
    }
}
