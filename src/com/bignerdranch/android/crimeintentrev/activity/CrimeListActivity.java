package com.bignerdranch.android.crimeintentrev.activity;

import java.util.UUID;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.bignerdranch.android.crimeintentrev.R;
import com.bignerdranch.android.crimeintentrev.domain.Crime;
import com.bignerdranch.android.crimeintentrev.fragment.CrimeFragment;
import com.bignerdranch.android.crimeintentrev.fragment.CrimeListFragment;

public class CrimeListActivity extends SingleFragmentActivity
	implements CrimeListFragment.OnCrimeSelectedCallback , CrimeFragment.OnCrimeUpdateCallback{

	@Override
	public Fragment createFragment() {
		return new CrimeListFragment();
	}
	
	@Override
	protected int getLayoutId() {
		return R.layout.activity_master_detail;
	}

	@Override
	public void onCrimeSelected(Crime crime) {

		View flTwoPaneDetailedFragmentContainer = 
				findViewById(R.id.fl_two_pane_detailed_fragment_container);
		if(flTwoPaneDetailedFragmentContainer == null){
			
			UUID crimeId = crime.getCrimeId();
			
			Intent intent = new Intent(this,CrimePagerActivity.class);
			intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crimeId);
			startActivity(intent);
		}else{

			UUID crimeId = crime.getCrimeId();			
			
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction transaction = fm.beginTransaction();
			
			Fragment oldFragment = 
					fm.findFragmentById(R.id.fl_two_pane_detailed_fragment_container);
			if(oldFragment != null){
				transaction.remove(oldFragment);
			}
			
			Fragment newFragment = CrimeFragment.newInstance(crimeId);
			transaction.add(R.id.fl_two_pane_detailed_fragment_container, newFragment);
			
			transaction.commit();
		}
	}

	@Override
	public void onCrimeUpdate() {

		FragmentManager fm = getSupportFragmentManager();
		CrimeListFragment masterFragment = 
				(CrimeListFragment) fm.findFragmentById(R.id.fl_single_fragment_fragment_container);
		masterFragment.updateUI();
	}
	
}
