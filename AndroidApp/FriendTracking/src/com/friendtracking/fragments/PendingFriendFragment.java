package com.friendtracking.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.friendtracking.MainActivity;
import com.friendtracking.R;
import com.friendtracking.adapters.PendingFriendAdapter;
import com.friendtracking.model.Friend;
import com.friendtracking.model.Friend.Sex;
import com.friendtracking.utils.UserFunctions;
import com.friendtracking.utils.UserFunctionsImpl;

/**
 * A class representing the fragment that shows all of the requests for
 * friendship.
 */
public class PendingFriendFragment extends Fragment {
	// JSON response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_USER_NAME = "username";
	private static String KEY_USERS = "users";
	private static String KEY_FIRST_NAME = "first_name";
	private static String KEY_LAST_NAME = "last_name";
	private static String KEY_AGE = "age";
	private static String KEY_GENDER = "gender";
	private static String KEY_HOME_TOWN = "hometown";
	private static String KEY_EMAIL = "email";

	// list of pending friendships
	private ListView pendingFriendsList;
	private PendingFriendAdapter mAdapter;

	private MainActivity mainActivity;

	private UserFunctions userFunctions;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mainActivity = (MainActivity) activity;
		} catch (ClassCastException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mAdapter = new PendingFriendAdapter(mainActivity);
		userFunctions = new UserFunctionsImpl();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pending_friend_fragment,
				container, false);
		pendingFriendsList = (ListView) view
				.findViewById(R.id.pendingFriendsList);
		pendingFriendsList.setAdapter(mAdapter);
		new PendingFriendTask(mainActivity.getApplicationContext(),
				userFunctions.getUserName(mainActivity.getApplicationContext()))
				.execute();
		return view;
	}

	private class PendingFriendTask extends AsyncTask<Void, Void, Void> {
		// the context of the application
		private Context context;
		// user name
		private String userName;
		// does the post request is successful
		private boolean isSuccessful;
		// the resulting list after post
		private List<Friend> result;

		public PendingFriendTask(Context context, String userName) {
			this.context = context;
			this.userName = userName;
			isSuccessful = true;
			result = new ArrayList<Friend>();
		}

		@Override
		protected Void doInBackground(Void... params) {

			JSONObject json = userFunctions.pendingFriends(userName);

			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						JSONArray users = json.getJSONArray(KEY_USERS);

						if (users.length() != 0) {
							for (int i = 0; i < users.length(); i++) {
								// current json object
								JSONObject temp = users.getJSONObject(i);

								// determining the gender of the user
								Sex sex;
								if (temp.getString(KEY_GENDER).equals("m")) {
									sex = Sex.MALE;
								} else {
									sex = Sex.FEMALE;
								}

								result.add(new Friend(temp
										.getString(KEY_USER_NAME), temp
										.getString(KEY_FIRST_NAME), temp
										.getString(KEY_LAST_NAME), temp
										.getInt(KEY_AGE), sex, temp
										.getString(KEY_HOME_TOWN), temp
										.getString(KEY_EMAIL)));
							}
						}
					} else {
						isSuccessful = false;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (isSuccessful) {
				mAdapter.clear();
				mAdapter.addAll(this.result);
			} else {
				Toast.makeText(context, "No results", Toast.LENGTH_LONG).show();
			}
		}
	}
}
