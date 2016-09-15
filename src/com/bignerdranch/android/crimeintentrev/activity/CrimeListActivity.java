package com.bignerdranch.android.crimeintentrev.activity;

import android.support.v4.app.Fragment;

import com.bignerdranch.android.crimeintentrev.fragment.CrimeListFragment;

public class CrimeListActivity extends SingleFragmentActivity{

	@Override
	public Fragment createFragment() {
		return new CrimeListFragment();
	}
	
}
