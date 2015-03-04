package com.friendtracking;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.friendtracking.database.TagDao;
import com.friendtracking.fragments.FriendListFragment;
import com.friendtracking.fragments.MapFragment;
import com.friendtracking.fragments.PendingFriendFragment;
import com.friendtracking.model.Tag;
import com.friendtracking.receivers.NetworkReceiver;
import com.friendtracking.services.GPSService;
import com.friendtracking.services.GPSService.LocalBinder;
import com.friendtracking.utils.UserFunctions;
import com.friendtracking.utils.UserFunctionsImpl;

public class MainActivity extends ActionBarActivity {

	public static String Language = "mk";

	// the action bar in the top of the activity
	private ActionBar actionBar;

	// functions for sign in and sign up
	private UserFunctions userFunctions;

	// view pager for switching the fragments
	private ViewPager mViewPager;

	// tab adapter for handling the tabs in the action bar
	private TabsAdapter mTabsAdapter;

	// reference to fragment manager
	public static FragmentManager fragmentManager;

	// The user's current network preference setting.
	public static String sPref = null;

	// Used for GPS
	private GPSService mService;
	private boolean mBound = false;

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

		// Check login status in database
		userFunctions = new UserFunctionsImpl();
		if (userFunctions.isUserLogedIn(getApplicationContext())) {
			// user already logged in
			setContentView(R.layout.activity_main);

			// initialize fragment manager
			fragmentManager = getSupportFragmentManager();

			// initialize pager
			mViewPager = (ViewPager) findViewById(R.id.pager);

			setUpActionBar(savedInstanceState);

		} else {
			// user is not logged in show login screen
			Intent login = new Intent(getApplicationContext(),
					LoginActivity.class);
			login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(login);
			// Closing Main Activity screen
			finish();
		}

	}

	@Override
	protected void onStart() {
		super.onStart();

		// Bind to LocalService
		Intent intent = new Intent(this, GPSService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

		// get the default shared preferences
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		// get last saved preferences about the type of network to be used
		sPref = sharedPrefs.getString("listPref", NetworkReceiver.ANY);
	}

	@Override
	protected void onStop() {
		super.onStop();
		// Unbind from the service
		if (mBound) {
			unbindService(mConnection);
			mBound = false;
		}
	}

	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get
			// LocalService instance
			LocalBinder binder = (LocalBinder) service;
			mService = binder.getService();
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};

	public void startGPS() {
		if (mBound) {
			mService.startGPS(this.getApplicationContext());
		}
	}

	public void stopGPS() {
		TagDao td = new TagDao(this, Language);
		List<Tag> tags = td.getAllTags();

		Log.v("Ima tagovi", tags.size() + " tolku");

		if (mBound) {
			mService.stopGPS();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("tab", getSupportActionBar()
				.getSelectedNavigationIndex());
	}

	/**
	 * Utility function setting up the action bar
	 */
	private void setUpActionBar(Bundle savedInstanceState) {
		// get action bar
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		// instantiate the tabs adapter
		mTabsAdapter = new TabsAdapter(this, mViewPager);

		// add first tab
		mTabsAdapter.addTab(actionBar.newTab().setText("Friends"),
				FriendListFragment.class, null);

		// add second tab
		mTabsAdapter.addTab(actionBar.newTab().setText("Map"),
				MapFragment.class, null);

		// add the third tab
		mTabsAdapter.addTab(actionBar.newTab().setText("Requests"),
				PendingFriendFragment.class, null);

		if (savedInstanceState != null) {
			actionBar.setSelectedNavigationItem(savedInstanceState.getInt(
					"tab", 0));
		}

	}

	/**
	 * Helper class
	 * 
	 */
	public static class TabsAdapter extends FragmentPagerAdapter implements
			ActionBar.TabListener, ViewPager.OnPageChangeListener {

		private final Context mContext;
		private final ActionBar mActionBar;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		static final class TabInfo {
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(Class<?> _class, Bundle _args) {
				clss = _class;
				args = _args;
			}
		}

		public TabsAdapter(ActionBarActivity activity, ViewPager pager) {
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mActionBar = activity.getSupportActionBar();
			mViewPager = pager;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
			TabInfo info = new TabInfo(clss, args);
			tab.setTag(info);
			tab.setTabListener(this);
			mTabs.add(info);
			mActionBar.addTab(tab);
			notifyDataSetChanged();
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			mActionBar.setSelectedNavigationItem(position);
		}

		@Override
		public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.setCustomAnimations(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);

			Object tag = tab.getTag();
			for (int i = 0; i < mTabs.size(); i++) {
				if (mTabs.get(i) == tag) {
					mViewPager.setCurrentItem(i);
				}
			}

		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(),
					info.args);
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

	}
}