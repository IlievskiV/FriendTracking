package com.friendtracking.database;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.friendtracking.model.Tag;

public class TagDao implements TagInterface {
	// Database fields
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private String[] allColumns = { DatabaseHelper.KEY_ID,
			DatabaseHelper.KEY_REMOTE_ID, DatabaseHelper.KEY_ACTIVITY_ID,
			DatabaseHelper.KEY_LONGITUDE, DatabaseHelper.KEY_LATITUDE,
			DatabaseHelper.KEY_CREATED_AT };

	public TagDao(Context context, String lang) {
		dbHelper = new DatabaseHelper(context, lang);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void openRead() throws SQLException {
		database = dbHelper.getReadableDatabase();
	}

	public void close() {
		database.close();
		// this.close();
	}

	protected ContentValues itemToContentValues(Tag tag) {
		ContentValues values = new ContentValues();
		
		// values.put(DatabaseHelper.KEY_ID, tag.getId());
		values.put(DatabaseHelper.KEY_REMOTE_ID, tag.getRemote_id());
		values.put(DatabaseHelper.KEY_ACTIVITY_ID, tag.getActivity_id());
		values.put(DatabaseHelper.KEY_LONGITUDE,
				String.valueOf(tag.getLongitude()));
		values.put(DatabaseHelper.KEY_LATITUDE,
				String.valueOf(tag.getLatitude()));
		values.put(DatabaseHelper.KEY_CREATED_AT, tag.getCreated_at()
				.toString());
		return values;
	}

	@Override
	public boolean addTag(Tag tag) {
		this.open();
		long insertId = database.insert(DatabaseHelper.TABLE_TAGS, null,
				itemToContentValues(tag));

		this.close();

		if (insertId > 0) {
			// tag.setId(insertId);
			Log.v("OK", "Tag inserted");
			return true;
		} else {
			return false;
		}
	}

	public List<Tag> getAllTags() {
		List<Tag> tags = new ArrayList<Tag>();
		this.open();

		// Select All Query
		String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_TAGS;

		Cursor cursor = database.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Tag tag = new Tag();
				tag.setId(Integer.parseInt(cursor.getString(0)));
				tag.setActivity_id(Integer.parseInt(cursor.getString(2)));
				tag.setRemote_id(Integer.parseInt(cursor.getString(1)));
				tag.setLongitude(Double.parseDouble(cursor.getString(3)));
				tag.setLatitude(Double.parseDouble(cursor.getString(4)));

				/*
				 * String timestamp = cursor.getString(5); Date d = new
				 * Date(timestamp); Timestamp t = new Timestamp(d.getTime());
				 */
				// Timestamp t = new Timestamp(Long.parseLong(timestamp));
				Timestamp t = new Timestamp(123456788);
				tag.setCreated_at(t);

				// Adding contact to list
				tags.add(tag);
			} while (cursor.moveToNext());
		}

		this.close();
		return tags;
	}

	@Override
	public List<Tag> getTagsByActivity(long activity_id) {
		this.open();

		// Select All Query
		String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_TAGS
				+ " WHERE " + DatabaseHelper.KEY_ACTIVITY_ID + " = "
				+ activity_id;

		Cursor cursor = database.rawQuery(selectQuery, null);
		List<Tag> tags = getTagsFromCursor(cursor);
		
		this.close();
		return tags;
	}
	
	private List<Tag> getTagsFromCursor(Cursor cursor) {
		List<Tag> tags = new ArrayList<Tag>();
		// looping through all rows and adding to list
				if (cursor.moveToFirst()) {
					do {
						Tag tag = new Tag();
						tag.setId(Integer.parseInt(cursor.getString(0)));
						tag.setActivity_id(Integer.parseInt(cursor.getString(2)));
						tag.setRemote_id(Integer.parseInt(cursor.getString(1)));
						tag.setLongitude(Double.parseDouble(cursor.getString(3)));
						tag.setLatitude(Double.parseDouble(cursor.getString(4)));

						/*
						 * String timestamp = cursor.getString(5); Date d = new
						 * Date(timestamp); Timestamp t = new Timestamp(d.getTime());
						 */
						// Timestamp t = new Timestamp(Long.parseLong(timestamp));
						Timestamp t = new Timestamp(123456788);
						tag.setCreated_at(t);

						// Adding contact to list
						tags.add(tag);
					} while (cursor.moveToNext());
				}
		return tags;
	}

	@Override
	public List<Tag> getUnpublishedTags() {
		this.open();

		String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_TAGS
				+ " WHERE " + DatabaseHelper.KEY_REMOTE_ID + " = -1";

		Cursor cursor = database.rawQuery(selectQuery, null);
		List<Tag> tags = getTagsFromCursor(cursor);
		
		this.close();
		return tags;
	}

	@Override
	public boolean updateTags(List<Tag> tags) {
		boolean updated = true;
		for (Tag tag : tags) {
			updated = updated && updateTag(tag);
		}
		return updated;
	}

	@Override
	public boolean updateTag(Tag tag) {
		this.open();
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_REMOTE_ID, tag.getRemote_id());
		
		int updated = database.update(DatabaseHelper.TABLE_TAGS, values, DatabaseHelper.KEY_ID + " = ? ",
				new String [] {String.valueOf(tag.getId())});
		
		this.close();
		return updated == 1;
	}

	/*
	 * public boolean update(Song song) { long numRowsAffected =
	 * database.update(SongDbOpenHelper.TABLE_NAME, itemToContentValues(song),
	 * SongDbOpenHelper.COLUMN_ID + " = " + song.getId(), null); return
	 * numRowsAffected > 0; }
	 * 
	 * public List<Song> getAllItems() { List<Song> items = new
	 * ArrayList<Song>();
	 * 
	 * Cursor cursor = database.query(SongDbOpenHelper.TABLE_NAME, allColumns,
	 * null, null, null, null, null, null);
	 * 
	 * if (cursor.moveToFirst()) { do { items.add(cursorToSong(cursor)); } while
	 * (cursor.moveToNext()); } cursor.close(); return items; }
	 * 
	 * public List<Song> getSearchItems(String s) { List<Song> items = new
	 * ArrayList<Song>(); String query = "SELECT * FROM " +
	 * SongDbOpenHelper.TABLE_NAME + " WHERE ("; query+= allColumns[1] +
	 * " like '%" + s + "%' OR "; query+= allColumns[2] + " like '%" + s +
	 * "%' OR "; query+= allColumns[3] + " like '%" + s + "%')" ;
	 * 
	 * Cursor cursor = database.rawQuery(query, null);
	 * 
	 * if (cursor.moveToFirst()) { do { items.add(cursorToSong(cursor)); } while
	 * (cursor.moveToNext()); } cursor.close(); return items; }
	 * 
	 * public Song getById(long id) {
	 * 
	 * Cursor cursor = database.query(SongDbOpenHelper.TABLE_NAME, allColumns,
	 * SongDbOpenHelper.COLUMN_ID + " = " + id, null, null, null, null, null);
	 * try { if (cursor.moveToFirst()) { return cursorToSong(cursor); } else {
	 * // no items found return null; } } catch (Exception ex) {
	 * ex.printStackTrace(); return null; } finally { cursor.close(); }
	 * 
	 * }
	 * 
	 * public Song getNext(long id) {
	 * 
	 * Cursor cursor = database.query(SongDbOpenHelper.TABLE_NAME, allColumns,
	 * null, null, null, null, null, null);
	 * 
	 * while (cursor.moveToNext()) { long currentID = cursor.getLong(cursor
	 * .getColumnIndex(SongDbOpenHelper.COLUMN_ID)); if (currentID == id) {
	 * break; } }
	 * 
	 * try { if (cursor.moveToNext()) { return cursorToSong(cursor); } else if
	 * (cursor.moveToFirst()) { return cursorToSong(cursor); } else { return
	 * null; } } catch (Exception ex) { ex.printStackTrace(); return null; }
	 * finally { cursor.close(); } }
	 * 
	 * public Song getPrevious(long id) { Cursor cursor =
	 * database.query(SongDbOpenHelper.TABLE_NAME, allColumns, null, null, null,
	 * null, null, null);
	 * 
	 * cursor.moveToLast(); long currentID = cursor.getLong(cursor
	 * .getColumnIndex(SongDbOpenHelper.COLUMN_ID));
	 * 
	 * if (currentID != id) { while (cursor.moveToPrevious()) { currentID =
	 * cursor.getLong(cursor .getColumnIndex(SongDbOpenHelper.COLUMN_ID)); if
	 * (currentID == id) { break; } } }
	 * 
	 * try { if (cursor.moveToPrevious()) { return cursorToSong(cursor); } else
	 * if (cursor.moveToLast()) { return cursorToSong(cursor); } else { return
	 * null; } } catch (Exception ex) { ex.printStackTrace(); return null; }
	 * finally { cursor.close(); } }
	 * 
	 * protected Song cursorToSong(Cursor cursor) { Song song = new Song();
	 * song.setId(cursor.getLong(cursor
	 * .getColumnIndex(SongDbOpenHelper.COLUMN_ID)));
	 * 
	 * song.setAuthor(cursor.getString(cursor
	 * .getColumnIndex(SongDbOpenHelper.COLUMN_AUTHOR)));
	 * 
	 * song.setTitle(cursor.getString(cursor
	 * .getColumnIndex(SongDbOpenHelper.COLUMN_TITLE)));
	 * 
	 * song.setContent(cursor.getString(cursor
	 * .getColumnIndex(SongDbOpenHelper.COLUMN_CONTENT)));
	 * 
	 * song.setCreated(new Timestamp(cursor.getLong(cursor
	 * .getColumnIndex(SongDbOpenHelper.COLUMN_CREATED))));
	 * 
	 * return song; }
	 */

}
