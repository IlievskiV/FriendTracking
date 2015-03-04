package com.friendtracking.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.friendtracking.database.DatabaseHandler;
import com.friendtracking.model.Tag;

public class UserFunctionsImpl implements UserFunctions {

	private JSONParser jsonParser;

	private static final String loginURL = "http://friendtracking.com/api/login.php";
	private static final String registerURL = "http://friendtracking.com/api/register.php";
	private static final String searchingURL = "http://friendtracking.com/api/searchUser.php";
	private static final String addFriendURL = "http://friendtracking.com/api/addFriend.php";
	private static final String pendingFriendsURL = "http://friendtracking.com/api/getPendingRequests.php";
	private static final String friendListURL = "http://friendtracking.com/api/getFriendList.php";
	private static final String acceptFriendURL = "http://friendtracking.com/api/acceptFriend.php";
	private static final String tagURL = "http://friendtracking.com/api/tag.php";
	private static final String getTagsURL = "http://friendtracking.com/api/getFriendTags.php";
	private static final String publishTagsURL = "http://friendtracking.com/api/publishTags.php";
	
	public UserFunctionsImpl() {
		jsonParser = new JSONParser();
	}

	/**
	 * Function make Login Request
	 * 
	 * @param userName
	 * @param password
	 */
	@Override
	public JSONObject loginUser(String userName, String password) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(PARAM_NAME_FOR_USER_NAME, userName));
		params.add(new BasicNameValuePair(PARAM_NAME_FOR_PASSWORD, password));

		JSONObject jObj = jsonParser.getJSONFromUrl(loginURL, params);

		return jObj;
	}

	/**
	 * Function make login request
	 * 
	 * @param userName
	 * @param firstName
	 * @param lastName
	 * @param age
	 * @param gender
	 * @param homeTown
	 * @param email
	 * @param password
	 */
	@Override
	public JSONObject registerUser(String userName, String firstName,
			String lastName, String age, String gender, String homeTown,
			String email, String password) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(PARAM_NAME_FOR_USER_NAME, userName));
		params.add(new BasicNameValuePair(PARAM_NAME_FOR_FIRST_NAME, firstName));
		params.add(new BasicNameValuePair(PARAM_NAME_FOR_LAST_NAME, lastName));
		params.add(new BasicNameValuePair(PARAM_NAME_FOR_AGE, age));
		params.add(new BasicNameValuePair(PARAM_NAME_FOR_GENDER, gender));
		params.add(new BasicNameValuePair(PARAM_NAME_FOR_HOME_TOWN, homeTown));
		params.add(new BasicNameValuePair(PARAM_NAME_FOR_EMAIL, email));
		params.add(new BasicNameValuePair(PARAM_NAME_FOR_PASSWORD, password));

		JSONObject jObj = jsonParser.getJSONFromUrl(registerURL, params);

		return jObj;
	}

	/**
	 * Function for searching friends
	 */
	@Override
	public JSONObject searchForFriends(String userName, String query) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(PARAM_NAME_FOR_USER_NAME, userName));
		params.add(new BasicNameValuePair(PARAM_NAME_FOR_SEARCHING, query));

		JSONObject jObj = jsonParser.getJSONFromUrl(searchingURL, params);

		return jObj;
	}

	/**
	 * Function for adding friend
	 */
	@Override
	public JSONObject addFriend(String userName, String friendUserName) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(PARAM_NAME_FOR_USER_NAME, userName));
		params.add(new BasicNameValuePair(PARAM_NAME_FOR_FRIEND_USER_NAME,
				friendUserName));

		JSONObject jObj = jsonParser.getJSONFromUrl(addFriendURL, params);

		return jObj;
	}

	/**
	 * Function for getting the pending friend requests
	 */
	@Override
	public JSONObject pendingFriends(String userName) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(PARAM_NAME_FOR_USER_NAME, userName));

		JSONObject jObj = jsonParser.getJSONFromUrl(pendingFriendsURL, params);

		return jObj;
	}

	@Override
	public JSONObject friendList(String userName) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(PARAM_NAME_FOR_USER_NAME, userName));

		JSONObject jObj = jsonParser.getJSONFromUrl(friendListURL, params);
		return jObj;
	}

	/**
	 * Function for accepting friendship
	 */
	@Override
	public JSONObject acceptFriend(String userName, String friendUserName) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(PARAM_NAME_FOR_USER_NAME, userName));
		params.add(new BasicNameValuePair(PARAM_NAME_FOR_FRIEND_USER_NAME,
				friendUserName));

		JSONObject jObj = jsonParser.getJSONFromUrl(acceptFriendURL, params);

		return jObj;
	}

	/**
	 * Function for tagging
	 */
	@Override
	public JSONObject tag(String userName, double longitude, double latitude,
			String text) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(PARAM_NAME_FOR_USER_NAME, userName));
		params.add(new BasicNameValuePair(PARAM_NAME_FOR_LONGITUDE, String
				.valueOf(longitude)));
		params.add(new BasicNameValuePair(PARAM_NAME_FOR_LATITUDE, String
				.valueOf(latitude)));
		params.add(new BasicNameValuePair(PARAM_NAME_FOR_TEXT, text));

		JSONObject jObj = jsonParser.getJSONFromUrl(tagURL, params);

		return jObj;
	}

	/**
	 * Function for getting tags of your friend
	 */
	@Override
	public JSONObject getTags(String friendUserName) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(PARAM_NAME_FOR_USER_NAME, friendUserName));
		
		JSONObject jObj = jsonParser.getJSONFromUrl(getTagsURL, params);
		
		return jObj;
	}

	
	@Override
	public JSONObject publishTags(String userName, List<Tag> tags) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(PARAM_NAME_FOR_USER_NAME, userName));
		params.add(new BasicNameValuePair("tags", tags.toString()));
		
		JSONObject jObj = jsonParser.getJSONFromUrl(publishTagsURL, params);
		
		return jObj;
	}
	/**
	 * Function get Login status
	 */
	@Override
	public boolean isUserLogedIn(Context context) {
		DatabaseHandler db = new DatabaseHandler(context);
		int count = db.getRowCount();
		if (count > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Function to logout user Reset Database
	 */
	@Override
	public boolean logOutUser(Context context) {
		DatabaseHandler db = new DatabaseHandler(context);
		db.resetTables();
		return true;
	}

	@Override
	public String getUserName(Context context) {
		DatabaseHandler db = new DatabaseHandler(context);
		HashMap<String, String> user = db.getUserDetails();

		String res = "";

		if (user.containsKey("userName")) {
			res = new String(user.get("userName"));
		}

		return res;
	}

}
