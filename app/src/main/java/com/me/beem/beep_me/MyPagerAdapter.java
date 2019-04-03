package com.me.beem.beep_me;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {
    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MainScreenFragment();
            case 1:
                return new HelpPageFragment();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
