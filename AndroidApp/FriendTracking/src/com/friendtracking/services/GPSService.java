package com.friendtracking.services;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.friendtracking.MainActivity;
import com.friendtracking.database.TagDao;
import com.friendtracking.model.Tag;
import com.friendtracking.receivers.PinReceiver;
import com.friendtracking.tasks.PublishTagsTask;
import com.friendtracking.utils.UserFunctionsImpl;

public class GPSService extends Service {

	public static final int MSG_SOMETHING = 1;
	private static LocationManager locationManager;
	private static LocationListener locationListener;

	// Binder given to clients
	private final IBinder mBinder = new LocalBinder();

	/**
	 * Class used for the client Binder. Because we know this service always
	 * runs in the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder {
		public GPSService getService() {
			// Return this instance of LocalService so clients can call public
			// methods
			return GPSService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public void stopGPS() {
		if (locationManager != null && locationListener != null) {
			locationManager.removeUpdates(locationListener);
			Log.v("HH", "ZAPREV APDEJTI GPS");
		}
	}
	
	public void startGPS(final Context context) {
		// Don't initialize location manager, retrieve it from system services.
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		locationListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {

			}

			@Override
			public void onProviderEnabled(String provider) {
				Toast.makeText(context, "Provider enabled: " + provider,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onProviderDisabled(String provider) {
				Toast.makeText(context, "Provider disabled: " + provider,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLocationChanged(Location location) {
				// Do work with new location. Implementation of this method will
				// be covered later.
				// doWorkWithNewLocation(location);

				/*
				 * Toast.makeText(context, "FILIP SMENIV LOKACIJA",
				 * Toast.LENGTH_SHORT) .show();
				 */

				TagDao td = new TagDao(context, MainActivity.Language);

				Calendar c = Calendar.getInstance();
				Timestamp t = new Timestamp(c.getTimeInMillis());
				Tag tag = new Tag(1, -1, 1, location.getLongitude(),
						location.getLatitude(), t);
				boolean b = td.addTag(tag);

				if (b) {
					Intent intent = new Intent(PinReceiver.NOTIFICATION);
					sendBroadcast(intent);
				}
			}
		};

		long minTime = 15 * 1000; // Minimum time interval for update in seconds,
									// i.e. 5 seconds.
		long minDistance = 10; // Minimum distance change for update in meters,
								// i.e. 10 meters.

		// Assign LocationListener to LocationManager in order to receive
		// location updates.
		// Acquiring provider that is used for location updates will also be
		// covered later.
		// Instead of LocationListener, PendingIntent can be assigned, also
		// instead of
		// provider name, criteria can be used, but we won't use those
		// approaches now.
		locationManager.requestLocationUpdates(getProviderName(context),
				minTime, minDistance, locationListener);
	}

	String getProviderName(Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM); // Chose your
																// desired power
																// consumption
																// level.
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // Choose your accuracy
														// requirement.
		criteria.setSpeedRequired(true); // Chose if speed for first location
											// fix is required.
		criteria.setAltitudeRequired(false); // Choose if you use altitude.
		criteria.setBearingRequired(false); // Choose if you use bearing.
		criteria.setCostAllowed(false); // Choose if this provider can waste
										// money :-)

		// Provide your criteria and flag enabledOnly that tells
		// LocationManager only to return active providers.
		return locationManager.getBestProvider(criteria, true);
	}

}