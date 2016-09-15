package com.bignerdranch.android.crimeintentrev.activity;

import com.bignerdranch.android.crimeintentrev.fragment.CrimeCameraFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

public class CrimeCameraActivity extends SingleFragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		super.onCreate(savedInstanceState);
	}

	@Override
	public Fragment createFragment() {
		return new CrimeCameraFragment();
	}

}
