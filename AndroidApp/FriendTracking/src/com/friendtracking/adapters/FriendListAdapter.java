package com.friendtracking.adapters;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.friendtracking.FriendTagsActivity;
import com.friendtracking.model.Friend;
import com.friendtracking.model.Friend.Sex;
import com.friendtracking.utils.UserFunctions;
import com.friendtracking.utils.UserFunctionsImpl;
import com.google.android.gms.internal.fr;
import com.google.android.gms.maps.model.LatLng;

import com.friendtracking.R;
import com.friendtracking.R.layout;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FriendListAdapter extends BaseAdapter implements
		OnItemClickListener {
	// JSON response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_USER_TAGS = "user_tags";
	private static String KEY_LATITUDE = "latitude";
	private static String KEY_LONGITUDE = "longitude";

	// list of all friends
	private List<Friend> friends;
	// the context of the application
	private Context context;
	private LayoutInflater inflater;

	// user functions for getting the tags
	private UserFunctions userFunctions;

	public FriendListAdapter(Context context) {
		friends = new ArrayList<Friend>();
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		userFunctions = new UserFunctionsImpl();
	}

	public FriendListAdapter(Context context, List<Friend> friends) {
		this.context = context;
		this.friends = friends;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		userFunctions = new UserFunctionsImpl();
	}

	@Override
	public int getCount() {
		return friends.size();
	}

	@Override
	public Object getItem(int position) {
		return friends.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class FriendHolder {
		public LinearLayout layout;
		public ImageView friendPhoto;
		public TextView username;
		public TextView firstLastName;
		public TextView onlineSince;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Friend f = friends.get(position);
		FriendHolder holder = null;

		if (convertView == null) {
			holder = new FriendHolder();

			holder.layout = (LinearLayout) inflater.inflate(
					R.layout.friend_list_item, null);

			holder.friendPhoto = (ImageView) holder.layout
					.findViewById(R.id.friend_photo);

			holder.username = (TextView) holder.layout
					.findViewById(R.id.friend_username);

			holder.firstLastName = (TextView) holder.layout
					.findViewById(R.id.friend_first_last_name);

			holder.onlineSince = (TextView) holder.layout
					.findViewById(R.id.online_since);

			convertView = holder.layout;
			convertView.setTag(holder);
		}

		holder = (FriendHolder) convertView.getTag();

		if (f.getSex() == Sex.MALE) {
			holder.friendPhoto.setImageResource(R.drawable.male);
		} else {
			holder.friendPhoto.setImageResource(R.drawable.female);
		}

		holder.username.setText(f.getUsername());
		holder.firstLastName.setText(f.getFirstName() + " " + f.getLastName());
		holder.onlineSince.setText("Last time online: ");

		return convertView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		new GetFriendAllTagsTask(friends.get(position).getUsername().trim())
				.execute();
	}

	public void add(Friend friend) {
		friends.add(friend);
		notifyDataSetChanged();
	}

	public void addAll(List<Friend> friendsList) {
		friends.addAll(friendsList);
		notifyDataSetChanged();
	}

	public void clear() {
		friends.clear();
		notifyDataSetChanged();
	}

	/**
	 * Asynchronous task for getting the tags of one friend
	 */
	private class GetFriendAllTagsTask extends AsyncTask<Void, Void, Void> {
		// user name of the friend
		private String friendUserName;
		// coordinates of all tags of the friend
		private ArrayList<LatLng> coordinates;
		// does the operation is successful
		private boolean isSuccessful;

		public GetFriendAllTagsTask(String friendUserName) {
			this.friendUserName = friendUserName;
			coordinates = new ArrayList<LatLng>();
			isSuccessful = true;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// json object representing all tags if the friend
			JSONObject json = userFunctions.getTags(friendUserName);
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						// get JSON array of tags
						JSONArray tags = json.getJSONArray(KEY_USER_TAGS);

						if (tags.length() != 0) {
							// iterate all tags
							for (int i = 0; i < tags.length(); i++) {
								JSONObject tag = tags.getJSONObject(i);
								// add last coordinate to the list of
								// coordinates
								coordinates.add(new LatLng(tag
										.getDouble(KEY_LATITUDE), tag
										.getDouble(KEY_LONGITUDE)));
							}
						}
					}
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
			Intent intent = new Intent(context, FriendTagsActivity.class);
			
			double[] latutudes = new double[coordinates.size()];
			double[] longitudes = new double[coordinates.size()];
			
			for(int i = 0; i < coordinates.size(); i++){
				latutudes[i] = coordinates.get(i).latitude;
				longitudes[i] = coordinates.get(i).longitude;
			}
			
			intent.putExtra("latitudes", latutudes);
			intent.putExtra("longitudes", longitudes);
			intent.putExtra("friendUserName", friendUserName);
			context.startActivity(intent);
		}
	}
}
