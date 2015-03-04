package com.friendtracking.receivers;

import java.util.List;

import com.friendtracking.MainActivity;
import com.friendtracking.database.TagDao;
import com.friendtracking.model.Tag;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class PinReceiver extends BroadcastReceiver {

	public static final String RESULT = "result";
	public static final String NOTIFICATION = "com.friendtracking.receivers.PINRECEIVER";

	private GoogleMap mMap;

	public PinReceiver(GoogleMap mMap) {
		this.mMap = mMap;
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		if (mMap != null) {
			Toast.makeText(context, "Prectraj ja mapata", Toast.LENGTH_LONG)
					.show();
			TagDao td = new TagDao(context, MainActivity.Language);
			List<Tag> tags = td.getAllTags();

			mMap.clear();
			PolylineOptions po = new PolylineOptions();
			for (Tag tag : tags) {
				LatLng ll = new LatLng(tag.getLatitude(), tag.getLongitude());
				po.add(ll);
			}

			mMap.addPolyline(po);
		} else {
			Toast.makeText(context, "Nemam mapa za precrtuvanje",
					Toast.LENGTH_LONG).show();
		}

	}

}
