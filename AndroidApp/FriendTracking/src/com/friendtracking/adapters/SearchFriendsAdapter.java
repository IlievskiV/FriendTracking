package com.friendtracking.adapters;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.friendtracking.model.Friend;
import com.friendtracking.model.Friend.Sex;
import com.friendtracking.utils.UserFunctions;
import com.friendtracking.utils.UserFunctionsImpl;

import com.friendtracking.R;

import android.app.Service;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SearchFriendsAdapter extends BaseAdapter {

	// JSON response node names
	private static String KEY_SUCCESS = "success";

	// list of users in the list as a result of searching
	private List<Friend> friends;
	// the context of the application
	private Context context;

	private LayoutInflater inflater;

	// user functions for adding friends
	private UserFunctions userFunctions;

	public SearchFriendsAdapter(Context context) {
		friends = new ArrayList<Friend>();
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		userFunctions = new UserFunctionsImpl();
	}

	public SearchFriendsAdapter(Context context, List<Friend> friends) {
		this.context = context;
		this.friends = friends;
		inflater = (LayoutInflater) context
				.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
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

	class SearchFriendHolder {
		public LinearLayout layout;
		public ImageView searchFriendPhoto;
		public TextView searchFriendUsername;
		public Button addButton;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		Friend f = friends.get(position);
		SearchFriendHolder holder = null;

		if (convertView == null) {
			holder = new SearchFriendHolder();

			holder.layout = (LinearLayout) inflater.inflate(
					R.layout.search_friend_item, null);

			holder.searchFriendPhoto = (ImageView) holder.layout
					.findViewById(R.id.searchFriendPhoto);

			holder.searchFriendUsername = (TextView) holder.layout
					.findViewById(R.id.searchFriendUsername);

			holder.addButton = (Button) holder.layout
					.findViewById(R.id.addButton);

			convertView = holder.layout;
			convertView.setTag(holder);
		}

		holder = (SearchFriendHolder) convertView.getTag();

		if (f.getSex() == Sex.MALE) {
			holder.searchFriendPhoto.setImageResource(R.drawable.male);
		} else {
			holder.searchFriendPhoto.setImageResource(R.drawable.female);
		}

		holder.searchFriendUsername.setText(f.getUsername());

		holder.addButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new AddFriendTask(context, userFunctions.getUserName(context),
						friends.get(position).getUsername()).execute();
			}
		});

		return convertView;
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

	private class AddFriendTask extends AsyncTask<Void, Void, Void> {

		// the context of the application
		private Context context;
		// is the operation of adding the friend successful
		private boolean isSuccessful;

		private String userName;
		private String friendUserName;

		public AddFriendTask(Context context, String userName,
				String friendUserName) {
			this.context = context;
			this.userName = userName;
			this.friendUserName = friendUserName;
			isSuccessful = true;
		}

		@Override
		protected Void doInBackground(Void... params) {
			JSONObject json = userFunctions.addFriend(userName, friendUserName);

			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					if (Integer.parseInt(res) != 1) {
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
				Toast.makeText(context, "User added", Toast.LENGTH_LONG).show();
			}
		}
	}
}
