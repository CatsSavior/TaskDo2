package com.example.android.taskdo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MainActivity.this, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //DATABASE
        final AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, "database")
                .allowMainThreadQueries().build();


        final ViewPager viewPager = findViewById(R.id.viewpager);

        final SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Find the tab layout that shows the tabs
        TabLayout tabLayout = findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);


        FloatingActionButton addTaskFab = findViewById(R.id.add_task_fab);
        addTaskFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Starts intent to add a new Task in that day!
                addTaskIntent(viewPager.getCurrentItem());
            }
        });

        //Finds FAButton to remove all tasks
        FloatingActionButton removeTasksFab = findViewById(R.id.remove_tasks_fab);
        removeTasksFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NukeAllTasks(db, viewPager);
            }
        });

    }

    private void addTaskIntent(int currentPage) {
        Intent addIntent = new Intent(this, AddTaskActivity.class);
        addIntent.putExtra("currentPage", currentPage);
        startActivity(addIntent);
    }


    private void NukeAllTasks(final AppDatabase db, final ViewPager viewPager) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.warning));
        builder.setMessage(getString(R.string.nukeConfirmation));
        builder.setPositiveButton(getString(R.string.deleteAll), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db.TaskDao().nukeTable();
                recreate();
            }
        });
        builder.setNeutralButton(getString(R.string.deleteSelection), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db.TaskDao().deleteTasksByDay(viewPager.getCurrentItem());
                recreate();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}


