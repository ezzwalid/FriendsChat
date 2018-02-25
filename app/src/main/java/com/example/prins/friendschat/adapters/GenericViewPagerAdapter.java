package com.example.prins.friendschat.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by prins on 12/7/2017.
 */

public class GenericViewPagerAdapter extends FragmentStatePagerAdapter {
    //===================================================================
    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();
    //===================================================================
    public GenericViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    //===================================================================
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
    //===================================================================
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
    //===================================================================
    @Override
    public int getCount() {
        return fragments.size();
    }
    //===================================================================
    public void addPage(Fragment fragment, String title){
        fragments.add(fragment);
        titles.add(title);
        notifyDataSetChanged();
    }
    //===================================================================
}
