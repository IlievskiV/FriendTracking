package com.friendtracking.receivers;

import com.friendtracking.MainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Broadcast receiver that listens for changes in connectivity
 */
public class NetworkReceiver extends BroadcastReceiver {

	// string constants representing the choices of the user
	public static final String WIFI = "Wi-Fi";
	public static final String ANY = "Any";

	// whether the device should update coordinates or not
	public static boolean updateCoordinates = true;

	@Override
	public void onReceive(Context context, Intent intent) {
		// get the connectivity manager from system services
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// get information about the type of network
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		/*
		 * Checks the user preferences and the network connection. Based on the
		 * result, decides whether to update coordinates or not If the user
		 * preferences is Wi-Fi only, checks to see if the device has a Wi-Fi
		 * connection.
		 */
		if (WIFI.equals(MainActivity.sPref) && networkInfo != null
				&& networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			/*
			 * If device has its Wi-Fi connection, sets updateCoordinates to
			 * true. This causes the coordinates to be updated until there is a
			 * connection
			 */
			updateCoordinates = true;
		} else if (ANY.equals(MainActivity.sPref) && networkInfo != null) {
			updateCoordinates = true;
		} else {
			updateCoordinates = false;
		}
	}
}
