package com.kevinwang.tomatoClock.history;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kevinwang.tomatoClock.R;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by lenovo on 2016/4/7.
 */
public class TaskHistoryFragment extends Fragment {

    private ArrayList<History> historyLab;
    private DAO historyDAO;
    private StickyListHeadersListView historyList;
    private StickyListHeadersAdapter historyAdapter;

    public static TaskHistoryFragment newInstance(Bundle bundle) {
        TaskHistoryFragment taskHistoryFragment = new TaskHistoryFragment();
        taskHistoryFragment.setArguments(bundle);
        return taskHistoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        System.out.println("TaskHistoryFragment-------> onCreate()");
        super.onCreate(savedInstanceState);

        historyDAO = new DAO(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("TaskHistoryFragment-------> onResume()");
        historyDAO.reopen();
        historyLab = historyDAO.getHistory();
        historyAdapter = new HistoryAdapter (getActivity());
        historyList.setAdapter(historyAdapter);
    }

    @Override
    public void onPause() {
        System.out.println("TaskHistoryFragment-------> onPause()");
        super.onPause();
        historyDAO.close();
    }

    @Override
    public void onStop() {
        System.out.println("TaskHistoryFragment-------> onStop()");
        super.onStop();
        historyDAO.close();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);
        System.out.println("TaskHistoryFragment-------> onCreateView()");
        historyList = (StickyListHeadersListView) view.findViewById(R.id.historyList);
        return view;
    }

    private class HistoryAdapter extends ArrayAdapter<History> implements StickyListHeadersAdapter {
        LayoutInflater layoutInflater;

        public HistoryAdapter(Context context) {
            super(context, 0, historyLab);
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.history_list_item, parent, false);
                holder.time_record = (TextView)convertView.findViewById(R.id.timeRecord);
                holder.content = (TextView)convertView.findViewById(R.id.content);
                holder.delete_history_img = (ImageView)convertView.findViewById(R.id.history_delete_image);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.time_record.setText(historyLab.get(position).getStartTime() + " ~ " + historyLab.get(position).getEndTime());
            holder.content.setText(historyLab.get(position).getContent());
            holder.delete_history_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("删除土豆记录在list中的位置 #" + position);
                    System.out.println("删除土豆记录的id ： #" + historyLab.get(position).getId());
                    System.out.println("删除之前的历史数 ： " + historyDAO.getHistory().size());
                    System.out.println("--------------------------------");
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    AlertDialog alertDialog = alertDialogBuilder.setMessage("确定要删除土豆记录吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    historyDAO.deleteHistory((historyLab.get(position)).getId());
                                    historyLab.remove(historyLab.get(position));
                                    notifyDataSetChanged();
                                }
                            }).setNegativeButton("取消", null).create();
                    alertDialog.show();
                }
            });

            return convertView;

        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            HeaderViewHolder holder;
            if (convertView == null) {
                System.out.println("getHeaderView");
                holder = new HeaderViewHolder();
                convertView = layoutInflater.inflate(R.layout.history_header, parent, false);
                holder.date = (TextView) convertView.findViewById(R.id.header);
                convertView.setTag(holder);
            }else {
                holder = (HeaderViewHolder)convertView.getTag();
            }

            holder.date.setText(historyLab.get(position).getDate());

            return convertView;
        }

        @Override
        public long getHeaderId(int position) {
            return historyLab.get(position).getListHeaderPosition();
        }

        class HeaderViewHolder {
            TextView date;
        }

        class ViewHolder {
            TextView time_record;
            TextView content;
            ImageView delete_history_img;
        }
    }
}
