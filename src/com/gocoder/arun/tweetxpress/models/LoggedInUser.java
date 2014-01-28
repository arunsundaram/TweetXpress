package com.gocoder.arun.tweetxpress.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class LoggedInUser implements Serializable{

	private static final long serialVersionUID = 5971848094938937963L;
	private String name;
	private String screenName;
	private String profileImageUrl;
	private static LoggedInUser instance;
	
	private LoggedInUser(){
		name="";
		screenName="";
	}
	
	public String getName() {
		return name;
	}
	public String getScreenName() {
		return screenName;
	}
	public String getProfileImageUrl() {
		return profileImageUrl;
	}
	public String getFormattedScreenName(){
		return "@"+screenName;
	}

	public static LoggedInUser getInstance(){
		if(instance == null){
			instance  = new LoggedInUser();
		}
		return instance;
	}
	
	public void setUserInfo(JSONObject json){
		try {
			name = json.getString("name");
			screenName = json.getString("screen_name");
			profileImageUrl = json.getString("profile_image_url");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
