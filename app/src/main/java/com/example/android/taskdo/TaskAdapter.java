package com.example.android.taskdo;

import android.app.Activity;
import android.graphics.Paint;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {

    private static final String TAG = "TaskAdapter";

    private final Boolean is24HourFormat = DateFormat.is24HourFormat(getContext());


    public TaskAdapter(Activity context, List<Task> tasks) {
        super(context, 0, tasks);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        //If first item is not yet present, inflate it
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.task_item, parent, false);
        }

        //Store the current Task in a task Object named currentTask
        final Task currentTask = getItem(position);


        //Finds the View for the time and sets it
        TextView timeTextView = listItemView.findViewById(R.id.time_textview);
        //Find amPm textView
        TextView amPm = listItemView.findViewById(R.id.am_pm);

        //variables to simplify notation
        int currentHour;
        int currentMinute;
        if (currentTask != null) {
            currentHour = currentTask.getHour();
            currentMinute = currentTask.getMinute();
        } else {
            //TODO need to handle this exception
            currentHour = 0;
            currentMinute = 0;
        }

        if (is24HourFormat) {
            //Removes am_pm TextView from the layout since user is using 24h format
            amPm.setVisibility(View.GONE);

        } else {
            if (currentHour > 12 && currentHour < 25)
                amPm.setText(R.string.pm);
            else if (currentHour >= 0 && currentHour < 12)
                amPm.setText(R.string.am);
            else {
                amPm.setVisibility(View.GONE);
            }
        }

        String formattedTime = formatTime(currentHour, currentMinute);

        //Sets time in timeTextView
        timeTextView.setText(formattedTime);
        //Check whether the time is valid, if not valid, set its TextView to be invisible
        if (!(currentHour < 25 && currentHour >= 0 &&
                currentMinute < 61 && currentMinute >= 0)) {
            timeTextView.setVisibility(View.INVISIBLE);
        }

        //Sets the current taskName in the task_name TextView
        TextView taskName = listItemView.findViewById(R.id.task_name);
        //ImageView
        ImageView statusImageView = listItemView.findViewById(R.id.status_image);

        //Fixes the view according to the status of the task
        if (currentTask != null) {
            taskName.setText(currentTask.getName());
            if (currentTask.getTaskStatus()) {
                taskName.setPaintFlags(taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                statusImageView.setImageResource(R.drawable.outline_assignment_turned_in_black_24);
            } else {
                taskName.setPaintFlags(taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                taskName.setPaintFlags(taskName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                statusImageView.setImageResource(R.drawable.outline_assignment_24);
            }
        }
        return listItemView;

    }

    private static String formatTime(int hour, int minute) {
        String hourStr = String.valueOf(hour);
        String minuteStr = String.valueOf(minute);

        if (hour < 10)
            hourStr = "0" + hour;
        if (minute < 10)
            minuteStr = "0" + minute;
        return hourStr + " : " + minuteStr;
    }


}
