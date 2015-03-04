package com.friendtracking;

import java.util.ArrayList;
import java.util.List;

import com.friendtracking.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.friendtracking.utils.UserFunctionsImpl;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Activity showing the map of all tags of one friend
 */
public class FriendTagsActivity extends ActionBarActivity {
	// latitude and longitude of Macedonia
	private static final LatLng MACEDONIA = new LatLng(41.6000, 21.7000);

	// map of all tags
	private GoogleMap mMap;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
			Toast.makeText(getApplicationContext(), "Pressed",
					Toast.LENGTH_LONG).show();
			Intent intent = new Intent(getApplicationContext(),
					SearchResultsActivity.class);
			startActivity(intent);
			break;
		case R.id.action_logout:
			new UserFunctionsImpl().logOutUser(getApplicationContext());
			Intent intent2 = new Intent(getApplicationContext(),
					LoginActivity.class);
			startActivity(intent2);
			finish();
			break;
		default:
			break;
		}

		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_tags);

		mMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.friend_tags_map)).getMap();

		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MACEDONIA, 7));
		
		String friendUserName = getIntent().getStringExtra("friendUserName");
		
		getSupportActionBar().setTitle(friendUserName+"'s tags");

		double[] latitudes = (double[]) getIntent().getDoubleArrayExtra(
				"latitudes");
		double[] longitudes = (double[]) getIntent().getDoubleArrayExtra(
				"longitudes");

		for (int i = 0; i < latitudes.length; i++) {
			mMap.addMarker(new MarkerOptions().position(new LatLng(
					latitudes[i], longitudes[i])));
		}

	}
}
