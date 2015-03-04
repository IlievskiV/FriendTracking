package com.friendtracking.database;

import java.util.HashMap;

public interface DatabaseUtilities {

	public void addUser(String userName, String uid, String createdAt);

	public HashMap<String, String> getUserDetails();

	public int getRowCount();

	public void resetTables();
}
