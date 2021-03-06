package com.example.android.taskdo;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddTaskActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "AddTaskActivity";

    private String taskName = "";
    private int hour = 25;
    private int minute = 61;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Intent starterIntent = getIntent();
        final int currentDay = starterIntent.getIntExtra("currentPage", 0);
        Log.d(TAG, "Current page which has pressed add is : " + currentDay);




        //Find the the editable text view and save the name of the task into taskName string
        final EditText taskNameEdit = findViewById(R.id.task_name_edit);

        //Find the fab to add the Task
        FloatingActionButton addFab = findViewById(R.id.add_fab);

        //Find the TextView for the time preview and fills it if time is set
        final TextView timePreviewTextView = findViewById(R.id.time_preview);
        timePreviewTextView.setText(getString(R.string.timeNotSet));

        //DATABASE
        final AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database").allowMainThreadQueries().build();


        //Listener to take user new task info
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Take the name of the task from the EditText, casts to String and trims whitespaces
                taskName = taskNameEdit.getText().toString().trim();
                if (TextUtils.isEmpty(taskName) || (taskName.length()) < 1) {
                    Toast.makeText(AddTaskActivity.this, getString(R.string.addNameToast), Toast.LENGTH_SHORT).show();
                } else {
                    //If the new task is valid, then it gets added into our database
                    db.TaskDao().insertTasks(new Task(taskName, false, hour, minute, currentDay));
                    //The user is then redirected back to the Main Activity
                    startActivity(new Intent(AddTaskActivity.this, MainActivity.class));
                    finish();
                }
            }
        });


        //Finds the FAB for inserting the time of the task
        FloatingActionButton timeFab = findViewById(R.id.add_time_fab);
        timeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creating an instance of the DialogFragment for the time Picker
                DialogFragment timePicker = new TimePickerFragment();
                //Calling show to display it
                timePicker.show(getSupportFragmentManager(), "timePicker");
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddTaskActivity.this, MainActivity.class));
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
        TextView timePreview = findViewById(R.id.time_preview);
        timePreview.setText(formatTimePreview(hourOfDay, minute, DateFormat.is24HourFormat(this)));
        timePreview.setTextColor(getResources().getColor(R.color.primaryTextColor));
    }


    private String formatTimePreview(int hour, int minute, boolean is24HourFormat) {
        String hourStr = String.valueOf(hour);
        String minuteStr = String.valueOf(minute);
        String meridiem;

        if (is24HourFormat)
            return hourStr + " : " + minuteStr;
        else {
            if (hour < 13)
                meridiem = "AM";
            else
                meridiem = "PM";

            if (hour < 10)
                hourStr = "0" + hour;
            if (minute < 10)
                minuteStr = "0" + minute;
            return hourStr + " : " + minuteStr + " " + meridiem;
        }
    }
}
