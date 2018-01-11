package com.forms.wjl.rsa.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by bubbly on 2018/1/11.
 */
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private List<String> titleLists;

    public SimpleFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleLists) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleLists = titleLists;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleLists.get(position);
    }
}
