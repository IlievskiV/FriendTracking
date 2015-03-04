package com.friendtracking.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.friendtracking.MainActivity;
import com.friendtracking.R;
import com.friendtracking.adapters.SearchFriendsAdapter;

public class SearchResultsFragment extends Fragment {

	private SearchFriendsAdapter mAdapter;
	private ListView searchFriendsList;

	private MainActivity mainActivity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mainActivity = (MainActivity) activity;
		} catch (ClassCastException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new SearchFriendsAdapter(mainActivity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.search_friends_fragment,
				container, false);
		searchFriendsList = (ListView)view.findViewById(R.id.searchResults);
		searchFriendsList.setAdapter(mAdapter);
		return view;
	}
}
