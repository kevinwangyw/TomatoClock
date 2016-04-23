package com.kevinwang.tomatoClock.history;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.kevinwang.tomatoClock.CircleTimeFragment;
import com.kevinwang.tomatoClock.MainActivity;
import com.kevinwang.tomatoClock.MyCountDown;
import com.kevinwang.tomatoClock.R;

/**
 * Created by lenovo on 2016/4/21.
 */
public class TaskPostActivity extends AppCompatActivity{
    private FragmentManager fragmentManager;
    private DAO historyDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("TaskPostActivity ------------> onCreate()");
        setContentView(R.layout.fragment_not_main_container);

        historyDAO = new DAO(this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_not_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("已完成番茄工作时间");

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.not_main_fragment_container, TaskPostFragment.newInstance(savedInstanceState)).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TaskPostFragment taskPostFragment = (TaskPostFragment)fragmentManager.getFragments().get(0);
        ///////////
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (item.getItemId() == R.id.menu_post_task) {

            String history_content = taskPostFragment.getPostTask();

            if (history_content.trim().length() == 0) {
                Toast.makeText(this, "请输入完成的工作任务", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            }

            historyDAO.createHistory(history_content);
            System.out.println("历史数 ： " + historyDAO.getHistory().size());

            if (MainActivity.getJustStart()) {
                System.out.println("TaskPostFragment-------->onOptionsItemSelected : justStart = " + MainActivity.getJustStart());
                MainActivity.setState(0);
               // MainActivity.setJustStart(false);
                MainActivity.setJustStart(false);
                finish();
            }
            else {
                MainActivity.setState(MainActivity.getState() + 1);
                MyCountDown.getInstance().start();
               // MainActivity.setActive(true);
                Intent intent = getIntent();
                int lastActivity = intent.getIntExtra("lastActivity", 0);
                if (lastActivity == 0) {
                    MainActivity.setActive(true);
                }
                if (lastActivity == 1) {
                    CircleTimeFragment.setActive(true);
                }
                finish();
            }
            //startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("TaskPostActivity ------------> onPause()");
        historyDAO.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("TaskPostActivity ------------> onStop()");
        historyDAO.close();
    }
}
