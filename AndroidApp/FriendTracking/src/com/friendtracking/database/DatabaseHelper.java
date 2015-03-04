package com.friendtracking.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

	//LogCat tag
	private static final String LOG = "DatabaseHelper";
	
	// Database Version
	private static final int DATABASE_VERSION = 2;
		
	// Database Name
	private static final String DATABASE_NAME = "ftracker5.db";

	// Table Names
	public static final String TABLE_USERS = "users";
	public static final String TABLE_TAGS = "tags";
	public static final String TABLE_ACTIVITIES = "activities";
	public static final String TABLE_LOGIN = "login";
	
	// Common column names
	public static final String KEY_ID = "id";
	public static final String KEY_REMOTE_ID = "remote_id";
	public static final String KEY_CREATED_AT = "created_at";
	
	// USERS Table - column names
	public static final String KEY_USERNAME = "username";
	public static final String KEY_FIRST_NAME = "first_name";
	public static final String KEY_LAST_NAME = "last_name";
	public static final String KEY_AGE = "age";
	public static final String KEY_HOMETOWN = "hometown";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_GENDER = "gender";
	
	// TAGS Table - column names
	public static final String KEY_ACTIVITY_ID = "activity_id";
	public static final String KEY_LONGITUDE = "longitude";
	public static final String KEY_LATITUDE = "latitude";
	// created_at
	
	// ACTIVITIES Table - column names
	public static final String KEY_USER_ID = "user_id";
	public static final String KEY_TITLE = "title";
	//created_at

	// LOGIN Table
	public static final String KEY_UID = "uid";

	
	// Table Create Statements
	
    // USERS table create statement
    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + "(" 
    		+ KEY_ID + " INTEGER PRIMARY KEY,"
    		+ KEY_USERNAME + " TEXT," 
            + KEY_FIRST_NAME + " TEXT,"
            + KEY_LAST_NAME + " TEXT,"
            + KEY_AGE + " INTEGER,"
            + KEY_HOMETOWN + " TEXT,"
            + KEY_EMAIL + " TEXT,"
            + KEY_GENDER + " TEXT"
            + ")";
    
    // TAGS table create statement
    private static final String CREATE_TABLE_TAGS = "CREATE TABLE "
            + TABLE_TAGS + "(" 
    		+ KEY_ID + " INTEGER PRIMARY KEY,"
    		+ KEY_REMOTE_ID + " INTEGER DEFAULT -1,"
    		+ KEY_ACTIVITY_ID + " INTEGER," 
            + KEY_LONGITUDE + " TEXT,"
            + KEY_LATITUDE + " TEXT,"
            + KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";
    
    // ACTIVITIES table create statement
    private static final String CREATE_TABLE_ACTIVITIES = "CREATE TABLE "
            + TABLE_ACTIVITIES + "(" 
    		+ KEY_ID + " INTEGER PRIMARY KEY,"
    		+ KEY_REMOTE_ID + " INTEGER,"
    		+ KEY_USER_ID + " INTEGER," 
    		+ KEY_CREATED_AT + " TIMESTAMP,"
            + KEY_TITLE + " TEXT"
            + ")";
    
    
    // LOGIN table create statement
    
	private static final String CREATE_TABLE_LOGIN = "CREATE TABLE "
			+ TABLE_LOGIN + "(" 
    		+ KEY_ID + " INTEGER PRIMARY KEY,"
    		+ KEY_USERNAME + " TEXT UNIQUE,"
    		+ KEY_UID + " TEXT," 
    		+ KEY_CREATED_AT + " TEXT"
            + ")";
			
	
	
	public DatabaseHelper(Context context, String lang) {
		super(context, String.format(DATABASE_NAME, lang), null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
        // creating required tables	
		//database.execSQL(CREATE_TABLE_USERS);
		//database.execSQL(CREATE_TABLE_ACTIVITIES);
		database.execSQL(CREATE_TABLE_TAGS);
		//database.execSQL(CREATE_TABLE_LOGIN);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		//db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_USERS));
		//db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_ACTIVITIES));
		db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_TAGS));
		//db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_LOGIN));
		
		// create new tables
		onCreate(db);
	}

}
