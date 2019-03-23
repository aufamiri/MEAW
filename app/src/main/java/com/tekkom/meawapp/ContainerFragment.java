package com.tekkom.meawapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ContainerFragment extends Fragment {

    private TabLayoutSetupCallback mToolbarSetupCallback;
    private List<String> mTabNamesList;

    public ContainerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ActivityHome) {
            mToolbarSetupCallback = (TabLayoutSetupCallback) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement TabLayoutSetupCallback");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTabNamesList = new ArrayList<>();
        mTabNamesList.add("HOME");
        mTabNamesList.add("UPLOAD BOOK");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_container, container, false);
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new ItemsPagerAdapter(getChildFragmentManager(), mTabNamesList));
        mToolbarSetupCallback.setupTabLayout(viewPager);

        return view;
    }


    public static class ItemsPagerAdapter extends FragmentStatePagerAdapter {

        private List<String> mTabs = new ArrayList<>();

        public ItemsPagerAdapter(FragmentManager fm, List<String> tabNames) {
            super(fm);

            mTabs = tabNames;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FragmentHome.newInstance();
                case 1:
                    return FragmentUpload.newInstance();
            }
            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs.get(position);
        }
    }

    public interface TabLayoutSetupCallback {
        void setupTabLayout(ViewPager viewPager);
    }
}
