package com.kevinwang.tomatoClock;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/4/7.
 */
public class TaskListFragment extends ListFragment{
    private static ArrayList<Task> mTasks;
    private DragListAdapter adapter = null;
    private ListView dragListView;

    private TextView taskContent;
    private CheckBox checkFinish;
    private ImageView deleteTask;
    private static EditText taskEditText;

    private ImageView dragImageView;//被拖拽的项，其实就是一个ImageView
    private int dragSrcPosition;//手指拖动项原始在列表中的位置
    private int dragPosition;//手指拖动的时候，当前拖动项在列表中的位置

    private int dragPoint;//在当前数据项中的位置
    private int dragOffset;//当前视图和屏幕的距离(这里只使用了y方向上)

    private WindowManager windowManager;//windows窗口控制类
    private WindowManager.LayoutParams windowParams;//用于控制拖拽项的显示的参数

    private int scaledTouchSlop;//判断滑动的一个距离
    private int upScrollBounce;//拖动的时候，开始向上滚动的边界
    private int downScrollBounce;//拖动的时候，开始向下滚动的边界

    private GestureDetector gestureDetector;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        System.out.println("TaskListFragment-------->onCreate");
        super.onCreate(savedInstanceState);

        mTasks = TaskLab.get(getActivity()).getTasks();

        scaledTouchSlop = ViewConfiguration.get(getActivity()).getScaledTouchSlop();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("TaskListFragment-------->onCreateView");
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        dragListView = (ListView)view.findViewById(android.R.id.list);

        setupUI(view.findViewById(R.id.task_layout_parent));
        taskEditText = (EditText)view.findViewById(R.id.taskEdit);
        taskEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    View dialogView = inflater.inflate(R.layout.dialog_task_edit,null);
                    final EditText dialogTaskEdit = (EditText)dialogView.findViewById(R.id.dialog_task_edittext);
                    AlertDialog alertDialog = alertDialogBuilder.setView(dialogView).
                            setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    taskEditText.clearFocus();
                                    String task_content = dialogTaskEdit.getText().toString().trim();
                                    if (task_content.length() > 0) {
                                        Task task = new Task();
                                        task.setContent(task_content);
                                        TaskLab.get(getActivity()).addTask(task);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                    }).create();
                    alertDialog.show();
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        System.out.println("TaskListFragment-------->onResume");
        super.onResume();
        adapter = new DragListAdapter(getContext(), mTasks);
        setListAdapter(adapter);
        gestureDetector = new GestureDetector(getActivity(), new MyGestureDetector());
        dragListView.setOnTouchListener(new MyTouchListener());
    }

    @Override
    public void onStop() {
        System.out.println("TaskListFragment-------->onStop");
        super.onStop();
    }

    @Override
    public void onPause() {
        System.out.println("TaskListFragment-------->onPause");
        super.onPause();
/*        Iterator<Task> iterator = mTasks.iterator();
        while (iterator.hasNext()) {
            Task t = iterator.next();
            if (t.isfinished()) {
                iterator.remove();
            }
        }  //退出程序后只要勾选完成了的任务就会被删除*/
        TaskLab.get(getActivity()).saveTasks();
    }

/*    @Override
    public void onDestroy() {
        super.onDestroy();

        TaskLab.get(getActivity()).saveTasks();
    }*/

    public static void hideSoftKeyboard (Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        taskEditText.clearFocus();
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

    private class DragListAdapter extends ArrayAdapter<Task> {
        public DragListAdapter(Context context, ArrayList<Task> tasks) {
            super(context, 0, tasks);
        }

        public ArrayList<Task> getList() { return mTasks;}

        @Override
        public int getCount() {
            return mTasks.size();
        }

        @Override
        public Task getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getActivity().getLayoutInflater().inflate(R.layout.task_list_item, null);

            final Task task = mTasks.get(position);

            taskContent = (TextView)convertView.findViewById(R.id.task_content_text);
            taskContent.setText(task.getContent());

            checkFinish = (CheckBox)convertView.findViewById(R.id.task_checkbox);
            if (task.isfinished()) {
                checkFinish.setChecked(true);
            }
            checkFinish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        task.setFinished(true);
                    }
                    else {
                        task.setFinished(false);
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
                                    mTasks.remove(task);
                                    adapter.notifyDataSetChanged();
                                }
                    }).setNegativeButton("取消", null).setTitle("删除任务").setMessage("确定要删除番茄任务吗?").create();
                    alertDialog.show();
                }
            });

            return convertView;
        }
    }

    class MyTouchListener implements View.OnTouchListener {


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(!gestureDetector.onTouchEvent(event)) {
                //Manually handle the event.
                if (event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    //Check if user is actually longpressing, not slow-moving
                    // if current position differs much then press positon then discard whole thing
                    // If position change is minimal then after 0.5s that is a longpress. You can now process your other gestures
                    Log.e("test","Action move");
                    //System.out.println("MOVE");
                    if (dragPosition != dragListView.INVALID_POSITION && dragImageView != null) {
                        int moveY = (int)event.getY();
                        onDrag(moveY);
                        return true;
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    //Get the time and position and check what that was :)
                    Log.e("test","Action down");
                    if (dragPosition != dragListView.INVALID_POSITION && dragImageView != null) {
                        System.out.println("onUP");
                        int upY = (int) event.getY();
                        stopDrag();
                        onDrop(upY);
                    }
                }
                return false;
            }
            return gestureDetector.onTouchEvent(event);
        }
    }

    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            System.out.println("onSingleTapUp");
            int x = (int) e.getX();
            int y = (int) e.getY();

            dragPosition = dragListView.pointToPosition(x, y);  //map a point to a position in the list
            System.out.println("dragPosition: " + dragPosition);
            if (dragPosition == AdapterView.INVALID_POSITION) {
                //return super.onInterceptTouchEvent(ev);
                return super.onSingleTapUp(e);// super.onDown(e);
            }

            ViewGroup itemView = (ViewGroup) dragListView.getChildAt(dragPosition - dragListView.getFirstVisiblePosition());

            TextView dragger = (TextView)itemView.findViewById(R.id.task_content_text);

            if (dragger != null && x > dragger.getLeft() && x < dragger.getRight()) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = layoutInflater.inflate(R.layout.dialog_task_edit,null);
                final EditText dialogTaskEdit = (EditText)dialogView.findViewById(R.id.dialog_task_edittext);
                dialogTaskEdit.setText(dragger.getText());
                AlertDialog alertDialog = alertDialogBuilder.setView(dialogView).
                        setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                //taskEditText.clearFocus();
                                String task_content = dialogTaskEdit.getText().toString().trim();
                                if (task_content.length() > 0) {
                                    Task task = new Task();
                                    task.setContent(task_content);
                                    TaskLab.get(getActivity()).addTask(task);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }).create();
                alertDialog.show();
            }

            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //System.out.println("onScroll");
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (dragPosition != dragListView.INVALID_POSITION && dragImageView != null) {
                System.out.println("onUP");
                int upY = (int) e2.getY();
                stopDrag();
                onDrop(upY);
                return true;
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            System.out.println("onLongPress(MotionEvent e)");
            int x = (int) e.getX();
            int y = (int) e.getY();

            dragSrcPosition = dragPosition = dragListView.pointToPosition(x, y);  //map a point to a position in the list
            System.out.println("dragPosition: " + dragPosition);
            if (dragPosition == AdapterView.INVALID_POSITION) {
                //return super.onInterceptTouchEvent(ev);
                return;// super.onDown(e);
            }
            //getChildAt() eturns the view at the specified position in the group.
            //getFirstVisiblePosition() returns the position within the adapter's
            // data set for the first item displayed on screen.
            System.out.println("dragListView.getFirstVisiblePosition() : " + dragListView.getFirstVisiblePosition());
            ViewGroup itemView = (ViewGroup) dragListView.getChildAt(dragPosition - dragListView.getFirstVisiblePosition());
            dragPoint = y - itemView.getTop();  //Top position of this view relative to its parent.
            System.out.println("Top position of this view relative to its parent : " + dragPoint);
            System.out.println("getRawY() : " + e.getRawY());
            dragOffset = (int) (e.getRawY() - y);
            // getRawY() returns the original raw Y coordinate of this event.
            //当前视图和屏幕的距离(这里只使用了y方向上)
            View dragger = itemView.findViewById(R.id.task_content_text);
            System.out.println("text of drag item : " + ((TextView)dragger).getText());
            if (dragger != null && x > dragger.getLeft() && x < dragger.getRight()) {
                //scaledTouchSlop 判断滑动的一个距离
                //upScrollBounce 拖动的时候，开始向上滚动的边界
                upScrollBounce = Math.min(y - scaledTouchSlop, dragListView.getHeight() / 3);
                downScrollBounce = Math.max(y + scaledTouchSlop, dragListView.getHeight() * 2 / 3);
                //downScrollBounce 拖动的时候，开始向下滚动的边界
                itemView.setDrawingCacheEnabled(true);
                Bitmap bm = Bitmap.createBitmap(itemView.getDrawingCache());
                startDrag(bm, y);
                //return true;
            }
        }
    }

    /**
     * 准备拖动，初始化拖动项的图像
     *
     * @param bm
     * @param y
     */
    public void startDrag(Bitmap bm, int y) {
        stopDrag();

        windowParams = new WindowManager.LayoutParams();
        windowParams.gravity = Gravity.TOP;
        windowParams.x = 0;
        windowParams.y = y - dragPoint + dragOffset;
        windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        windowParams.format = PixelFormat.TRANSLUCENT;
        windowParams.windowAnimations = 0;

        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(bm);
        windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(imageView, windowParams);
        dragImageView = imageView;
    }

    /**
     * 停止拖动，去除拖动项的头像
     */
    public void stopDrag() {
        if (dragImageView != null) {
            System.out.println("stopDrag() : ");
            windowManager.removeView(dragImageView);
            dragImageView = null;
        }
    }

    /**
     * 拖动执行，在Move方法中执行
     *
     * @param y
     */
    public void onDrag(int y) {
        if (dragImageView != null) {
            windowParams.alpha = 0.8f;
            windowParams.y = y - dragPoint + dragOffset;
            windowManager.updateViewLayout(dragImageView, windowParams);
        }
        //为了避免滑动到分割线的时候，返回-1的问题
        int tempPosition = dragListView.pointToPosition(0, y);
        if (tempPosition != dragListView.INVALID_POSITION) {
            dragPosition = tempPosition;
        }

        //滚动
        int scrollHeight = 0;
        if (y < upScrollBounce) {
            scrollHeight = 8;//定义向上滚动8个像素，如果可以向上滚动的话
        } else if (y > downScrollBounce) {
            scrollHeight = -8;//定义向下滚动8个像素，，如果可以向上滚动的话
        }

        if (scrollHeight != 0) {
            //真正滚动的方法setSelectionFromTop()
            dragListView.setSelectionFromTop(dragPosition, dragListView.getChildAt(dragPosition - dragListView.getFirstVisiblePosition()).getTop() +
                    scrollHeight);
        }
    }

    /**
     * 拖动放下的时候
     *
     * @param y
     */
    public void onDrop(int y) {

        //为了避免滑动到分割线的时候，返回-1的问题
        int tempPosition = dragListView.pointToPosition(0, y);
        if (tempPosition != dragListView.INVALID_POSITION) {
            dragPosition = tempPosition;
        }

        //超出边界处理
        if (y < dragListView.getChildAt(0).getTop()) {
            //超出上边界
            dragPosition = 0;
        } else if (y > dragListView.getChildAt(dragListView.getChildCount() - 1).getBottom()) {
            //超出下边界
            dragPosition = dragListView.getAdapter().getCount() - 1;
        }

        //数据交换
        if (dragPosition >= 0 && dragPosition < dragListView.getAdapter().getCount()) {
            @SuppressWarnings("unchecked")
            DragListAdapter adapter = (DragListAdapter) dragListView.getAdapter();
            Task dragItem = adapter.getItem(dragSrcPosition);
            //TaskLab.get(getActivity()).deleteTask(dragItem);
            //TaskLab.get(getActivity()).addTask(dragPosition, dragItem);
            adapter.remove(dragItem);
            adapter.insert(dragItem, dragPosition);
            //adapter.notifyDataSetChanged();
        }

    }
}
