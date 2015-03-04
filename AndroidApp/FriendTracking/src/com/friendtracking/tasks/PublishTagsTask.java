package com.friendtracking.tasks;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.friendtracking.database.TagDao;
import com.friendtracking.model.Tag;
import com.friendtracking.utils.UserFunctions;
import com.friendtracking.utils.UserFunctionsImpl;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PublishTagsTask extends AsyncTask<Void, Void, Void> {

	// JSON response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_USER_TAGS = "user_tags";
	private static String KEY_LATITUDE = "latitude";
	private static String KEY_LONGITUDE = "longitude";
	
	
	// the context of the application
	private Context context;
	// user name
	private String userName;
	// does the post request is successful
	private boolean isSuccessful;
	
	// list of unpublished tags
	private List<Tag> tags;
	

	public PublishTagsTask(Context context, String userName, List<Tag> tags) {
		this.context = context;
		this.userName = userName;
		this.isSuccessful = true;
		
		this.tags = tags;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		// get JSON format of all tags
		UserFunctions userFunctions = new UserFunctionsImpl();
		JSONObject json = userFunctions.publishTags(userName, tags);

		try {
			if (json.getString(KEY_SUCCESS) != null) {
				String res = json.getString(KEY_SUCCESS);
				if (Integer.parseInt(res) == 1) {
					// get JSON array of tags
					JSONArray tags = json.getJSONArray(KEY_USER_TAGS);
					
					for (int i = 0; i < tags.length(); i++) {
						JSONObject tag = tags.getJSONObject(i);
						Tag t = new Tag();
						
						
						// TODO: Update in DB
						
						
					}
				}
				isSuccessful = true;
			} else {
				isSuccessful = false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {	
		
	}
}
