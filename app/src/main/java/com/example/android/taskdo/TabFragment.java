package com.example.android.taskdo;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import java.util.List;

@SuppressWarnings("unused")
public class TabFragment extends Fragment {

    private static final String TAG = "TabFragment";

    private static int DAY;

    private List<Task> taskList;

    private TaskAdapter taskAdapter;

    private Context mContext;


    public TabFragment() {
        // Required empty public constructor
    }

    public TabFragment(int day) {
        if (day <= 7 && day >= 0) {
            DAY = day;
        }
        else
        {
            Log.d(TAG,"Не правильно " +
                    "(должен быть от 0 до 7)");
        }
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


        // Get a reference to the ListView, and attach the adapter to the listView.
        ListView listView = rootView.findViewById(R.id.list);
        listView.setAdapter(taskAdapter);


        //Sets an onClickListener on each item of the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Task currentTask = taskList.get(i);
                //Since the user tapped on an item, its status is changed.
                currentTask.changeTaskStatus();
                //Updates the Database to reflect the change just made
                db.TaskDao().updateTaskStatus(currentTask.getTaskStatus(), currentTask.getID());
                //Notifies the adapter that the dataset has changed and refreshes it
                taskAdapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Shows the user an Alert before deleting an item from the list
                showAlertOnDeletion(taskList, db, taskAdapter, i);
                return true;
            }
        });


        return rootView;
    }

    protected void showAlertOnDeletion(final List<Task> taskList, final AppDatabase db,
                                     final TaskAdapter taskAdapter, final int position) {

        if (getActivity() != null) {

            //Retrieve currentTask from adapter
            final Task currentTask = taskList.get(position);

            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            //Set title and message for the warning
            builder.setTitle(getString(R.string.warning));
            builder.setMessage(getString(R.string.deleteConfirmation) + currentTask.getName() + " ?");
            // add the buttons
            builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //remove current entry from the database and from the List
                    db.TaskDao().deleteTasksById(currentTask.getID());
                    taskList.remove(position);
                    taskAdapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton(getString(R.string.cancel), null);
            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


}

