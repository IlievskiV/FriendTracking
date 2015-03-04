package com.friendtracking.fragments;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.friendtracking.MainActivity;
import com.friendtracking.R;
import com.friendtracking.database.TagDao;
import com.friendtracking.model.Tag;
import com.friendtracking.receivers.PinReceiver;
import com.friendtracking.services.GPSService;
import com.friendtracking.tasks.PublishTagsTask;
import com.friendtracking.utils.UserFunctionsImpl;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * Fragment for showing the map
 * 
 */
public class MapFragment extends Fragment {

	// latitude and longitude of Macedonia
	private static final LatLng MACEDONIA = new LatLng(41.6000, 21.7000);

	// the view for the fragment
	private static View view;
	// button for checking
	private ImageButton checkInBtn;
	// map for drawing
	private static GoogleMap mMap;
	// main activity of the application
	private MainActivity mainActivity;
	// user functions for tagging
	// private UserFunctions userFunctions;
	// service for getting the latitude and longitude
	// private static GPSService gps;
	// does the button for updating coordinates is checked
	public static boolean isChecked = false;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mainActivity = (MainActivity) activity;
		} catch (ClassCastException ex) {
			ex.printStackTrace();
		}
	}

	private PinReceiver pinReceiver;

	@Override
	public void onResume() {
		super.onResume();
		if (mainActivity != null)
			mainActivity.registerReceiver(pinReceiver, new IntentFilter(
					PinReceiver.NOTIFICATION));
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mainActivity != null)
			mainActivity.unregisterReceiver(pinReceiver);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// initialize user functions
		// userFunctions = new UserFunctionsImpl();
		// initialize GPS service
		// gps = new GPSService(mainActivity.getApplicationContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = (FrameLayout) inflater.inflate(R.layout.map_fragment, container,
				false);

		setUpMap();

		pinReceiver = new PinReceiver(mMap);

		checkInBtn = (ImageButton) view.findViewById(R.id.checkInBtn);
		checkInBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// to start the cycle of updating coordinates
				if (!isChecked) {

					mainActivity.startGPS();
					// mainActivity.sendBroadcast(intent);
					Toast.makeText(mainActivity.getApplicationContext(),
							"GPS Service Started", Toast.LENGTH_LONG).show();
				} else {
					mainActivity.stopGPS();

					Toast.makeText(mainActivity.getApplicationContext(),
							"GPS Service Stopped", Toast.LENGTH_LONG).show();

					TagDao td = new TagDao(
							mainActivity.getApplicationContext(),
							MainActivity.Language);
					// TODO: IF INTERNET IS ACTIVE SEND REQUEST TO SERVER
					List<Tag> unpublishedTags = td.getUnpublishedTags();

					// ASYNC Task
					if (isOnline()) {
						// send async task with unpublshedTags
						UserFunctionsImpl ufi = new UserFunctionsImpl();
						String userName = ufi.getUserName(mainActivity
								.getApplicationContext());
						PublishTagsTask ptt = new PublishTagsTask(mainActivity
								.getApplicationContext(), userName,
								unpublishedTags);
						ptt.execute();
					}

				}
				isChecked = !isChecked;
			}
		});
		return view;
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) mainActivity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	/**
	 * Utility function for setting up the map with all tags of the friends
	 */
	private void setUpMap() {
		mMap = ((SupportMapFragment) MainActivity.fragmentManager
				.findFragmentById(R.id.location_map)).getMap();

		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MACEDONIA, 7));

		/*
		 * new GetMyTagsTask(mainActivity.getApplicationContext(),
		 * userFunctions.getUserName(mainActivity.getApplicationContext()))
		 * .execute();
		 */

		// TODO: Get And Draw Tags From Latest Activity

	}

}
