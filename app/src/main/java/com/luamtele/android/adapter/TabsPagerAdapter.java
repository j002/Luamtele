package com.luamtele.android.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.TabHost;

//import com.luamtele.android.Fragments.CeSoirFragment;
import com.luamtele.android.Fragments.CeSoirFragment;
import com.luamtele.android.Fragments.DeuxiemePartieFragment;
import com.luamtele.android.Fragments.NowFragment;

/**
 * Created by masta on 9/26/15.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public final String[] TAB_TITLES = {"Now", "Ce soir", "2e Partie"};

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);

    }


    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                // Now fragment activity
                return new NowFragment();
            case 1:
                // Ce soir fragment activity
                return new CeSoirFragment();
            case 2:
                // 2ieme partie fragment activity
                return new DeuxiemePartieFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return TAB_TITLES.length;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }
}
