package com.bignerdranch.android.crimeintentrev.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class Photo {
		
	private static final String FILE_NAME_KEY = "fileName";
	
	private String fileName;
	
	public Photo(){
		
	}
	
	public Photo(JSONObject jsonObject) throws JSONException{
		fileName = jsonObject.getString(FILE_NAME_KEY);
	}
	
	public JSONObject toJson() throws JSONException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(FILE_NAME_KEY, fileName);
		return jsonObject;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
