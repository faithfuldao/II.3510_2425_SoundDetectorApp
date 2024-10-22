package com.example.sounddetector;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

/**
 * ViewPagerAdapter is used to manage fragments in a ViewPager2 for swiping between tabs.
 * It stores fragments and their titles and provides them to the ViewPager2 for display.
 */
public class ViewPagerAdapter extends FragmentStateAdapter {

    private final ArrayList<Fragment> fragments = new ArrayList<>(); // List of fragments to display
    private final ArrayList<String> fragmentTitles = new ArrayList<>(); // Titles for each fragment

    /**
     * Constructor for ViewPagerAdapter.
     *
     * @param fragmentActivity The FragmentActivity that hosts the ViewPager2.
     */
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    /**
     * Creates and returns the fragment for the given position in the ViewPager.
     *
     * @param position The position of the fragment in the ViewPager.
     * @return The Fragment at the specified position.
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    /**
     * Returns the total number of fragments in the ViewPager.
     *
     * @return The number of fragments in the adapter.
     */
    @Override
    public int getItemCount() {
        return fragments.size();
    }

    /**
     * Adds a fragment and its corresponding title to the adapter.
     *
     * @param fragment The fragment to be added.
     * @param title The title corresponding to the fragment.
     */
    public void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        fragmentTitles.add(title);
    }

    /**
     * Returns the title of the fragment at the specified position.
     *
     * @param position The position of the fragment in the adapter.
     * @return The title of the fragment at the specified position.
     */
    public String getFragmentTitle(int position) {
        return fragmentTitles.get(position);
    }
}
