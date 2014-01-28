package com.gocoder.arun.tweetxpress;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gocoder.arun.tweetxpress.models.LoggedInUser;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeTweetActivity extends Activity {

	EditText etMessage;
	TextView tvUser;
	ImageView ivUser;
	private final int TWEET_LENGTH = 140;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_tweet);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		etMessage = (EditText) findViewById(R.id.etMessage);
		tvUser = (TextView) findViewById(R.id.tvUser);
		ivUser = (ImageView) findViewById(R.id.ivUser);
		LoggedInUser user = (LoggedInUser) getIntent().getSerializableExtra("user");
		setTitle(user.getFormattedScreenName());
		tvUser.setText(user.getFormattedScreenName());
		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivUser);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.compose_tweet, menu);
			return true;
	}
	
	
	public void onCancel(View v){
		finish();
	}
	
	public void onTweet(View v){
		String tweet = etMessage.getText().toString();
		if(!tweet.isEmpty()){
			if(tweet.length()>TWEET_LENGTH){
				tweet = tweet.substring(0, TWEET_LENGTH);
			}
			TwitterClientApp.getRestClient().postTweet(tweet, new JsonHttpResponseHandler(){
				public void onSuccess(JSONObject jsonResponse){
					Log.d("DEBUG",jsonResponse.toString());
					Toast.makeText(getApplicationContext(), "Posted", Toast.LENGTH_SHORT).show();
					Intent data = new Intent();
					data.putExtra("status", "POSTED");
					setResult(RESULT_OK, data);
					finish();
				}
			});
		}
		else{
			Toast.makeText(this, "Please enter status to update", Toast.LENGTH_SHORT).show();
		}
	}

}
