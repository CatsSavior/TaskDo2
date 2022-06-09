package com.example.android.taskdo;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.android.taskdo.Tabs.FridayFragment;
import com.example.android.taskdo.Tabs.MondayFragment;
import com.example.android.taskdo.Tabs.NotesFragment;
import com.example.android.taskdo.Tabs.SaturdayFragment;
import com.example.android.taskdo.Tabs.SundayFragment;
import com.example.android.taskdo.Tabs.ThursdayFragment;
import com.example.android.taskdo.Tabs.TuesdayFragment;
import com.example.android.taskdo.Tabs.WednesdayFragment;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    public SimpleFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0: return new NotesFragment(0);
            case 1: return new MondayFragment(1);
            case 2: return new TuesdayFragment(2);
            case 3: return new WednesdayFragment(3);
            case 4: return new ThursdayFragment(4);
            case 5: return new FridayFragment(5);
            case 6: return new SaturdayFragment(6);
            case 7: return new SundayFragment(7);


            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 8;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0: return "Все";
            case 1: return "Пн";
            case 2: return "Вт";
            case 3: return "Ср";
            case 4: return "Чт";
            case 5: return "Пт";
            case 6: return "Сб";
            case 7: return "Вс";
            default: return "DEF_PAGE_TITLE";
        }
    }
}
