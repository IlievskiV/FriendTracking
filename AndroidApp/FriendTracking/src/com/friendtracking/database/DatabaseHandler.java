package com.friendtracking.database;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper implements
		DatabaseUtilities {

	// Database version
	private static final int DATABASE_VERSION = 1;

	// Database name
	private static final String DATABASE_NAME = "friend_tracker_api";

	// Login table name
	private static final String TABLE_LOGIN = "login";

	// Login table columns name
	private static final String KEY_ID = "id";
	private static final String KEY_USER_NAME = "user_name";
	private static final String KEY_UID = "uid";
	private static final String KEY_CREATED_AT = "created_at";

	private static final String CREATE_LOGIN_TABLE = "CREATE TABLE "
			+ TABLE_LOGIN + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_USER_NAME + " TEXT UNIQUE," + KEY_UID + " TEXT,"
			+ KEY_CREATED_AT + " TEXT" + ")";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_LOGIN_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

		onCreate(db);
	}

	/**
	 * Storing user details in database
	 */
	@Override
	public void addUser(String userName, String uid, String createdAt) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_USER_NAME, userName);
		values.put(KEY_UID, uid);
		values.put(KEY_CREATED_AT, createdAt);

		// Inserting row
		db.insert(TABLE_LOGIN, null, values);
		db.close();
	}

	/**
	 * Getting user data from database
	 */
	@Override
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT * FROM " + TABLE_LOGIN;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			user.put("userName",
					cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)));

			user.put("uid", cursor.getString(cursor.getColumnIndex(KEY_UID)));

			user.put("created_at",
					cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));
		}

		cursor.close();
		db.close();

		return user;
	}

	/**
	 * Getting user login status return true if rows are there in table
	 */
	@Override
	public int getRowCount() {
		String countQuery = "SELECT * FROM " + TABLE_LOGIN;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();
		db.close();
		cursor.close();

		return rowCount;
	}

	/**
	 * Recreate database Delete all tables and create them again
	 */
	@Override
	public void resetTables() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_LOGIN, null, null);
		db.close();
	}
}
