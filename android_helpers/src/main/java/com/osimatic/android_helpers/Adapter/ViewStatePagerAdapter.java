package com.osimatic.android_helpers.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewStatePagerAdapter extends FragmentStatePagerAdapter {
	private final List<Fragment> mFragmentList = new ArrayList<>();

	public ViewStatePagerAdapter(FragmentManager fm) {
		super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
	}

	@Override
	public Fragment getItem(int position) {
		return mFragmentList.get(position);
	}

	@Override
	public int getCount() {
		return mFragmentList.size();
	}

	public void addFragment(Fragment fragment) {
		mFragmentList.add(fragment);
	}

}
