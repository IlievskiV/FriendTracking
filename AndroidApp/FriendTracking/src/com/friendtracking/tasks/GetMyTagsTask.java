package com.friendtracking.tasks;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.friendtracking.utils.UserFunctions;
import com.friendtracking.utils.UserFunctionsImpl;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GetMyTagsTask extends AsyncTask<Void, Void, Void> {

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
	// list of all coordinates
	private List<LatLng> coordinates;
	
	private GoogleMap mMap;

	public GetMyTagsTask(Context context, String userName, GoogleMap mMap) {
		this.context = context;
		this.userName = userName;
		this.isSuccessful = true;
		coordinates = new ArrayList<LatLng>();
		this.mMap = mMap;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		// get JSON format of all tags
		UserFunctions userFunctions = new UserFunctionsImpl();
		JSONObject json = userFunctions.getTags(userName);

		try {
			if (json.getString(KEY_SUCCESS) != null) {
				String res = json.getString(KEY_SUCCESS);
				if (Integer.parseInt(res) == 1) {
					// get JSON array of tags
					JSONArray tags = json.getJSONArray(KEY_USER_TAGS);

					for (int i = 0; i < tags.length(); i++) {
						JSONObject tag = tags.getJSONObject(i);

						coordinates.add(new LatLng(tag
								.getDouble(KEY_LATITUDE), tag
								.getDouble(KEY_LONGITUDE)));
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
		for (int i = 0; i < coordinates.size(); i++) {
			mMap.addMarker(new MarkerOptions().position(coordinates.get(i)));
		}
	}
}
