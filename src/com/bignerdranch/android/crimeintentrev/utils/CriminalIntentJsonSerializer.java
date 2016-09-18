package com.bignerdranch.android.crimeintentrev.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.bignerdranch.android.crimeintentrev.domain.Crime;

/**
 * Crime Json序列化工具类
 * @author jacrylodai
 *
 */
public class CriminalIntentJsonSerializer {
	
	private static final String TAG = "CriminalIntentJsonSerializer";
	
	private static final String DATA_FILE_NAME = "dataFile.data";
	
	private Context context;
	
	public CriminalIntentJsonSerializer(Context context){
		this.context = context;
	}

	public void saveCrimeList(List<Crime> crimeList) throws JSONException, IOException{
		
		JSONArray jsonArray = new JSONArray();
		
		for(Crime crime:crimeList){
			JSONObject jsonObject = crime.toJson();
			jsonArray.put(jsonObject);
		}
		
		String jsonString = jsonArray.toString();
		Log.d(TAG, "write json to file");
		Log.d(TAG, jsonString);
		
		BufferedWriter bufferedWriter = null;
		try {
			FileOutputStream fos = 
					context.openFileOutput(DATA_FILE_NAME,Activity.MODE_PRIVATE);

			bufferedWriter = new BufferedWriter(new OutputStreamWriter(fos));
			bufferedWriter.write(jsonString);
			
			bufferedWriter.flush();
			
		} finally {
			if(bufferedWriter != null){
				bufferedWriter.close();
			}
		}
	}
	
	public List<Crime> loadCrimeList() throws IOException, JSONException{

		List<Crime> crimeList = new ArrayList<Crime>();
		
		FileInputStream fis = null;
		try{
			fis = context.openFileInput(DATA_FILE_NAME);
		
			String jsonString = StreamTools.readFromStream(fis, "UTF-8");
			Log.d(TAG, "read from file");
			Log.d(TAG, jsonString);
			
			JSONArray jsonArray = new JSONArray(new JSONTokener(jsonString));
			
			for(int i=0;i<jsonArray.length();i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Crime crime = new Crime(jsonObject);
				crimeList.add(crime);
			}

		}catch (FileNotFoundException ex){
			//Ignore this one,It happens when you first run this programe
		}
		return crimeList;
	}
	
}
