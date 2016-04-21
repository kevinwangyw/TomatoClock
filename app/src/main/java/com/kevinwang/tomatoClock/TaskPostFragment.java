package com.kevinwang.tomatoClock;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/4/21.
 */
public class TaskPostFragment extends ListFragment{
    private ArrayList<Task> mTasks = new ArrayList<Task>();
    private MyListAdapter adapter = null;

    private TextView taskContent;
    private CheckBox checkFinish;
    private ImageView deleteTask;
    private static EditText postEditText;

    public static TaskPostFragment newInstance(Bundle bundle) {
        TaskPostFragment taskPostFragment = new TaskPostFragment();
        taskPostFragment.setArguments(bundle);
        return taskPostFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        for (Task task : TaskLab.get(getActivity()).getTasks()) {
            if (!task.isfinished()) {
                mTasks.add(task);
            }
        }

        adapter = new MyListAdapter (getActivity(), mTasks);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        postEditText = (EditText)view.findViewById(R.id.taskEdit);

        setupUI(view.findViewById(R.id.task_layout_parent));

        return view;
    }

    private class MyListAdapter extends ArrayAdapter<Task> {

        @Override
        public Task getItem(int position) {
            return super.getItem(position);
        }

        public MyListAdapter(Context context, ArrayList<Task> tasks) {
            super(context, 0, tasks);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getActivity().getLayoutInflater().inflate(R.layout.task_list_item, null);

            taskContent = (TextView)convertView.findViewById(R.id.task_content_text);
            taskContent.setText(mTasks.get(position).getContent());

            checkFinish = (CheckBox)convertView.findViewById(R.id.task_checkbox);

            checkFinish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    System.out.println(mTasks.get(position).getContent());
                    if (isChecked) {
                        String str = postEditText.getText().toString() + mTasks.get(position).getContent();
                        postEditText.setText(str);
                    }
                    else {
                        String str = postEditText.getText().toString().replace(mTasks.get(position).getContent(), "");
                        postEditText.setText(str);
                    }
                }
            });

            deleteTask = (ImageView) convertView.findViewById(R.id.task_delete_image);
            deleteTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    AlertDialog alertDialog = alertDialogBuilder.
                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mTasks.remove(mTasks.get(position));
                                    adapter.notifyDataSetChanged();
                                    TaskLab.get(getActivity()).getTasks().remove(mTasks.get(position));
                                    TaskLab.get(getActivity()).saveTasks();
                                }
                            }).setNegativeButton("取消", null).setTitle("删除任务").setMessage("确定要删除番茄任务吗?").create();
                    alertDialog.show();
                }
            });

            return convertView;
        }
    }

    public static void hideSoftKeyboard (Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        postEditText.clearFocus();
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {
            //System.out.println("点击非Edittext区域");
            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(getActivity());
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

    public String getPostTask() {
        return postEditText.getText().toString().trim();
    }
}
