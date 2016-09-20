package com.bignerdranch.android.crimeintentrev.activity;

import java.util.List;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.bignerdranch.android.crimeintentrev.R;
import com.bignerdranch.android.crimeintentrev.domain.Crime;
import com.bignerdranch.android.crimeintentrev.fragment.CrimeFragment;
import com.bignerdranch.android.crimeintentrev.utils.CrimeLab;
import com.bignerdranch.android.crimeintentrev.utils.LogUtil;

public class CrimePagerActivity extends ActionBarActivity 
	implements CrimeFragment.OnCrimeUpdateCallback{
	
	private static final String TAG = "CrimePagerActivity";

	private ViewPager viewPager;
	
	private List<Crime> crimeList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		viewPager = new ViewPager(this);
		viewPager.setId(R.id.vp_crime_pager_crime_container);
		
		setContentView(viewPager);
		
		crimeList = CrimeLab.getInstance(this).getCrimeList();
		
		//set adapter
		FragmentManager fragmentManager = getSupportFragmentManager();
		
		viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
			
			@Override
			public int getCount() {
				return crimeList.size();
			}
			
			@Override
			public Fragment getItem(int position) {
				
				Crime crime = crimeList.get(position);
				return CrimeFragment.newInstance(crime.getCrimeId());
			}
		});
				
		//set current position
		UUID crimeId = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		LogUtil.d(TAG, "crimeId:"+crimeId);
		
		int currPosition = 0;
		for(int i=0;i<crimeList.size();i++){
			
			Crime crime = crimeList.get(i);
			if(crime.getCrimeId().equals(crimeId)){
				currPosition = i;
				break;
			}
		}
		
//		LogUtil.d(TAG, "currPosition:"+currPosition);		
		viewPager.setCurrentItem(currPosition);
		
		//set current crime title
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {

				LogUtil.d(TAG,"onPageSelected--->> position:"+position);
				
				Crime crime = crimeList.get(position);
				CrimePagerActivity.this.setTitle(crime.getCrimeTitle());
			}
			
			@Override
			public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {
				
//				LogUtil.d(TAG,"onPageScrolled--->> pos:"+pos+".posOffset:"+posOffset
//						+".posOffsetPixels:"+posOffsetPixels);
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				
//				LogUtil.d(TAG,"onPageScrollStateChanged--->> state:"+state);
			}
		});
	}

	@Override
	public void onCrimeUpdate() {

		//不做任何事情，CrimeListFragment.onResume会进行更新
	}
	
}
