package com.bignerdranch.android.crimeintentrev.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;

import android.content.Context;
import android.util.Log;

import com.bignerdranch.android.crimeintentrev.domain.Crime;

public class CrimeLab {
	
	private static final String TAG = "CrimeLab";

	private static CrimeLab instance = null;
	
	private Context applicationContext;
	
	private List<Crime> crimeList;
	
	private CriminalIntentJsonSerializer criminalIntentJsonSerializer;
	
	private CrimeLab(Context context){
		
		Log.d(TAG, "inital CrimeLab");
		applicationContext = context.getApplicationContext();
		
		criminalIntentJsonSerializer = new CriminalIntentJsonSerializer(applicationContext);
		try {
			crimeList = criminalIntentJsonSerializer.loadCrimeList();
		} catch (IOException e) {
			e.printStackTrace();
			Log.d(TAG, "json read error",e);
			crimeList = new ArrayList<Crime>();
		} catch (JSONException e) {
			e.printStackTrace();
			Log.d(TAG, "json read error",e);
			crimeList = new ArrayList<Crime>();
		}
	}
	
	public static CrimeLab getInstance(Context context){
		if(instance == null){
			instance = new CrimeLab(context);
		}
		return instance;
	}
	
	public Crime getCrimeById(UUID crimeId){
		
		for(Crime crime:crimeList){
			if(crimeId.equals(crime.getCrimeId())){
				return crime;
			}
		}
		return null;
	}
	
	public List<Crime> getCrimeList(){
		return crimeList;
	}
	
	public void addCrime(Crime crime){
		crimeList.add(crime);
	}
	
	public boolean saveCrimeList(){
		
		try {
			criminalIntentJsonSerializer.saveCrimeList(crimeList);
			Log.d(TAG, "crime list save to file");
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			Log.d(TAG, "crime save error",e);
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			Log.d(TAG, "crime save error",e);
			return false;
		}
	}
	
	/**
	 * ¸ù¾ÝidÉ¾³ýcrime
	 * @param crimeId
	 */
	public void deleteCrimeById(UUID crimeId){
		
		for(Crime crime:crimeList){
			if(crime.getCrimeId().equals(crimeId)){
				crimeList.remove(crime);
				break;
			}
		}
	}
	
}
