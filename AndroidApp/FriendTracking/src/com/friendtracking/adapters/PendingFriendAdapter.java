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
import com.friendtracking.R.layout;

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

public class PendingFriendAdapter extends BaseAdapter {
	// JSON response node names
	private static String KEY_SUCCESS = "success";

	private List<Friend> friends;
	private Context context;
	private LayoutInflater inflater;

	// user functions for accepting friends
	private UserFunctions userFunctions;

	public PendingFriendAdapter(Context context) {
		friends = new ArrayList<Friend>();
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		userFunctions = new UserFunctionsImpl();
	}

	public PendingFriendAdapter(Context context, List<Friend> pendingFriends) {
		this.context = context;
		this.friends = pendingFriends;
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

	class PendingFriendHolder {
		public LinearLayout layout;
		public ImageView pendingFriendPhoto;
		public TextView pendingFriendUsername;
		public Button confirmButton;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Friend f = friends.get(position);
		PendingFriendHolder holder = null;

		if (convertView == null) {
			holder = new PendingFriendHolder();

			holder.layout = (LinearLayout) inflater.inflate(
					R.layout.pending_friend_item, null);

			holder.pendingFriendPhoto = (ImageView) holder.layout
					.findViewById(R.id.pendingFriendPhoto);

			holder.pendingFriendUsername = (TextView) holder.layout
					.findViewById(R.id.pendingFriendUsername);

			holder.confirmButton = (Button) holder.layout
					.findViewById(R.id.confirmButton);

			convertView = holder.layout;
			convertView.setTag(holder);
		}

		holder = (PendingFriendHolder) convertView.getTag();

		if (f.getSex() == Sex.MALE) {
			holder.pendingFriendPhoto.setImageResource(R.drawable.male);
		} else {
			holder.pendingFriendPhoto.setImageResource(R.drawable.female);
		}

		holder.pendingFriendUsername.setText(f.getUsername());

		holder.confirmButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new AcceptFriendTask(context, userFunctions
						.getUserName(context), friends.get(position)
						.getUsername()).execute();
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

	/**
	 * Asynchronous task for accepting the friend request
	 */
	private class AcceptFriendTask extends AsyncTask<Void, Void, Void> {

		// the context of the application
		private Context context;
		// does the operation of accepting is successful
		private boolean isSuccessful;

		private String userName;
		private String friendUserName;

		public AcceptFriendTask(Context context, String userName,
				String friendUserName) {
			this.context = context;
			isSuccessful = true;
			this.userName = userName;
			this.friendUserName = friendUserName;
		}

		@Override
		protected Void doInBackground(Void... params) {
			JSONObject json = userFunctions.acceptFriend(userName,
					friendUserName);

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
			if (!isSuccessful) {
				Toast.makeText(context, "Something went wrong",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(context, "User accepted", Toast.LENGTH_LONG)
						.show();
			}
		}

	}
}
