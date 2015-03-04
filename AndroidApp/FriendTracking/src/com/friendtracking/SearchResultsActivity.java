package com.friendtracking;

import java.util.ArrayList;
import java.util.List;

import com.friendtracking.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.friendtracking.adapters.SearchFriendsAdapter;
import com.friendtracking.model.Friend;
import com.friendtracking.model.Friend.Sex;
import com.friendtracking.utils.UserFunctions;
import com.friendtracking.utils.UserFunctionsImpl;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Activity for searching and showing users that are not friends of the current
 * user.
 * 
 */
public class SearchResultsActivity extends ActionBarActivity {

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

	// the action bar at the top of the activity
	private ActionBar actionBar;

	// custom adapter for searching results
	private SearchFriendsAdapter mAdapter;

	// list view where the search results are shown
	private ListView searchFriendsList;

	// user functions for getting the user name
	private UserFunctions userFunctions;

	// the edit text for entering the text for searching
	private EditText searchEditText;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_search, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_results);
	    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mAdapter = new SearchFriendsAdapter(getApplicationContext());
		searchFriendsList = (ListView) findViewById(R.id.searchResultsList);
		searchFriendsList.setAdapter(mAdapter);

		userFunctions = new UserFunctionsImpl();
		searchEditText = (EditText) findViewById(R.id.searchEditText);

		setUpActionBar();
	}

	/**
	 * Utility function setting up the action bar
	 */
	private void setUpActionBar() {
		// get action bar
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
	}

	// event listener for clicking the button
	public void searchFriends(View view) {
		new SearchFriendsTask(getApplicationContext(),
				userFunctions.getUserName(getApplicationContext()).trim(),
				searchEditText.getText().toString().trim()).execute();
	}

	private class SearchFriendsTask extends AsyncTask<Void, Void, Void> {

		// the context of the application
		private Context context;

		// does the search is successful
		private boolean isSuccessful;

		// the user name
		private String userName;

		// query for the searching
		private String searchQuery;

		// the resulting list after searching
		private List<Friend> result;

		public SearchFriendsTask(Context context, String userName,
				String searchQuery) {
			this.context = context;
			isSuccessful = true;
			this.userName = userName;
			this.searchQuery = searchQuery;
			result = new ArrayList<Friend>();
		}

		@Override
		protected Void doInBackground(Void... params) {
			JSONObject json = userFunctions.searchForFriends(userName,
					searchQuery);

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