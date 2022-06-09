package com.example.android.taskdo.Tabs;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.android.taskdo.AppDatabase;
import com.example.android.taskdo.R;
import com.example.android.taskdo.TabFragment;
import com.example.android.taskdo.Task;
import com.example.android.taskdo.TaskAdapter;

import java.util.List;

public class FridayFragment extends TabFragment {

    private static final String TAG = "FridayFragment";

    private static int DAY;

    private List<Task> taskList;

    private TaskAdapter taskAdapter;

    private Context mContext;


    public FridayFragment() {
        // Required empty public constructor
    }

    public FridayFragment(int day)
    {
        DAY = day;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.task_list, container, false);

        //Database?!
        final AppDatabase db = Room.databaseBuilder(mContext, AppDatabase.class, "database")
                .allowMainThreadQueries().build();

        //Loads the tasks from the database
        taskList = db.TaskDao().getTasksByDay(DAY);

        taskAdapter = new TaskAdapter(getActivity(), taskList);


        ListView listView = rootView.findViewById(R.id.list);
        listView.setAdapter(taskAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Task currentTask = taskList.get(i);
                currentTask.changeTaskStatus();
                db.TaskDao().updateTaskStatus(currentTask.getTaskStatus(), currentTask.getID());
                taskAdapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showAlertOnDeletion(taskList, db, taskAdapter, i);
                return true;
            }
        });


        return rootView;
    }

}

