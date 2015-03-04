package com.friendtracking.utils;

import java.util.List;

import org.json.JSONObject;

import com.friendtracking.model.Tag;

import android.content.Context;

/**
 * Interface for user functions
 */
public interface UserFunctions {

	public static final String PARAM_NAME_FOR_USER_NAME = "username";

	public static final String PARAM_NAME_FOR_FIRST_NAME = "first_name";

	public static final String PARAM_NAME_FOR_LAST_NAME = "last_name";

	public static final String PARAM_NAME_FOR_AGE = "age";

	public static final String PARAM_NAME_FOR_GENDER = "gender";

	public static final String PARAM_NAME_FOR_HOME_TOWN = "hometown";

	public static final String PARAM_NAME_FOR_EMAIL = "email";

	public static final String PARAM_NAME_FOR_PASSWORD = "password";

	public static final String PARAM_NAME_FOR_SEARCHING = "search_text";

	public static final String PARAM_NAME_FOR_FRIEND_USER_NAME = "friendname";
	
	public static final String PARAM_NAME_FOR_LONGITUDE = "longitude";
	
	public static final String PARAM_NAME_FOR_LATITUDE = "latitude";
	
	public static final String PARAM_NAME_FOR_TEXT = "text";

	public JSONObject loginUser(String userName, String password);

	public JSONObject registerUser(String userName, String firstName,
			String lastName, String age, String gender, String homeTown,
			String email, String password);

	public JSONObject searchForFriends(String userName, String query);

	public JSONObject addFriend(String userName, String friendUserName);

	public JSONObject pendingFriends(String userName);

	public JSONObject friendList(String userName);

	public JSONObject acceptFriend(String userName, String friendUserName);
	
	public JSONObject tag(String userName, double longitude, double latitude, String text);
	
	public JSONObject getTags(String friendUserName);

	public String getUserName(Context context);

	public boolean isUserLogedIn(Context context);

	public boolean logOutUser(Context context);

	JSONObject publishTags(String userName, List<Tag> tags);
}
