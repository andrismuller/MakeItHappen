package hu.bme.andrismulller.makeithappen_withfriends.Functions.Homescreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import hu.bme.andrismulller.makeithappen_withfriends.Functions.Homescreen.applications.ApplicationsFragment;
import hu.bme.andrismulller.makeithappen_withfriends.Functions.Homescreen.duties.DutiesFragment;

/**
 * Created by Muller Andras on 9/27/2017.
 */

class HomeScreenPagerAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_PAGES = 2;
    private long id;

    public HomeScreenPagerAdapter(FragmentManager supportFragmentManager, long id) {
        super(supportFragmentManager);
        this.id = id;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new DutiesFragment();
            case 1: return ApplicationsFragment.newInstance(id);
            default: return new DutiesFragment();
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
