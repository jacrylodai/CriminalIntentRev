package com.bignerdranch.android.crimeintentrev.domain;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Crime {
	
	private static final String CRIME_ID_KEY = "crimeId";
	
	private static final String CRIME_TITLE_KEY = "crimeTitle";
	
	private static final String CRIME_DATE_KEY = "crimeDate";
	
	private static final String CRIME_SOLVED_KEY = "crimeSolved";
	
	private static final String PHOTO_KEY = "photo";

	private UUID crimeId;
	
	private String crimeTitle;
	
	private Date crimeDate;
	
	private boolean crimeSolved;
	
	private Photo photo;
	
	public Crime(){
		crimeId = UUID.randomUUID();
		crimeTitle = "";
		crimeDate = new Date();
		crimeSolved = false;
	}

	public Crime(JSONObject jsonObject) throws JSONException {
		
		crimeId = UUID.fromString(jsonObject.getString(CRIME_ID_KEY));
		crimeTitle = jsonObject.getString(CRIME_TITLE_KEY);
		crimeDate = new Date(jsonObject.getLong(CRIME_DATE_KEY));
		crimeSolved = jsonObject.getBoolean(CRIME_SOLVED_KEY);
		if(jsonObject.has(PHOTO_KEY)){
			photo = new Photo(jsonObject.getJSONObject(PHOTO_KEY));
		}
	}

	public String getCrimeTitle() {
		return crimeTitle;
	}

	public void setCrimeTitle(String crimeTitle) {
		this.crimeTitle = crimeTitle;
	}

	public UUID getCrimeId() {
		return crimeId;
	}

	public Date getCrimeDate() {
		return crimeDate;
	}

	public void setCrimeDate(Date crimeDate) {
		this.crimeDate = crimeDate;
	}

	public boolean isCrimeSolved() {
		return crimeSolved;
	}

	public void setCrimeSolved(boolean crimeSolved) {
		this.crimeSolved = crimeSolved;
	}
	
	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	@Override
	public String toString() {
		return crimeTitle;
	}

	public JSONObject toJson() throws JSONException {
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(CRIME_ID_KEY, crimeId.toString());
		jsonObject.put(CRIME_TITLE_KEY, crimeTitle);
		jsonObject.put(CRIME_DATE_KEY, crimeDate.getTime());
		jsonObject.put(CRIME_SOLVED_KEY, crimeSolved);
		if(photo != null){
			jsonObject.put(PHOTO_KEY, photo.toJson());
		}
		return jsonObject;
	}
	
}
