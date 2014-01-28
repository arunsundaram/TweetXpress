package com.gocoder.arun.tweetxpress;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.gocoder.arun.tweetxpress.models.LoggedInUser;
import com.gocoder.arun.tweetxpress.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimeLineActivity extends Activity {
	private final int REQUESTCODE_COMPOSE = 20;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_line);
		if(isOnNetwork()){
			_getUserInfo();
			_getHomeTimeLine();
		}
		else{
			Toast.makeText(this, "no network", Toast.LENGTH_SHORT).show();
		}
	}

	private boolean isOnNetwork() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
	private void _getHomeTimeLine(){
		TwitterClientApp.getRestClient().getHomeTimeLine(new JsonHttpResponseHandler(){
			public void onSuccess(JSONArray jsonTweets){
				Log.d("DEBUG",jsonTweets.toString());
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
				ListView lvTweets = (ListView) findViewById(R.id.lvTweets);
				TweetsAdapter adapter = new TweetsAdapter(getBaseContext(), tweets);
				lvTweets.setAdapter(adapter);
			}
		});
	}

	
	private void _getUserInfo(){
		TwitterClientApp.getRestClient().getUserDetails(new JsonHttpResponseHandler(){
			public void onSuccess(JSONObject jsonResponse){
				LoggedInUser userinfo = LoggedInUser.getInstance();
				userinfo.setUserInfo(jsonResponse);
				setTitle(userinfo.getFormattedScreenName());
				Log.d("DEBUG", jsonResponse.toString());
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.time_line, menu);
		return true;
	}
	
	public void onRefreshTweets(MenuItem mi){
		Toast.makeText(this, "refreshing", Toast.LENGTH_SHORT).show();
		_getHomeTimeLine();
	}
	
	public void onComposeTweet(MenuItem mi){
		Intent i = new Intent(TimeLineActivity.this, ComposeTweetActivity.class);
		i.putExtra("user", LoggedInUser.getInstance());
		startActivityForResult(i,REQUESTCODE_COMPOSE);
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	  if (resultCode == RESULT_OK && requestCode == REQUESTCODE_COMPOSE) {
    		  if(data.getExtras().getString("status").equals("POSTED")){
    			  this._getHomeTimeLine();
    		  }
    	  }
   } 

}
