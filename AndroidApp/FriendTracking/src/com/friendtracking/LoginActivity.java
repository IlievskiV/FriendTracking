package com.friendtracking;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.friendtracking.database.DatabaseHandler;
import com.friendtracking.utils.UserFunctions;
import com.friendtracking.utils.UserFunctionsImpl;

import com.friendtracking.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	// UI elements
	private EditText usernameEditText;
	private EditText passwordEditText;

	// JSON response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_TOKEN = "token";
	private static String KEY_USER_NAME = "username";
	private static String KEY_CREATED_AT = "created_at";
	private static String KEY_USER = "user";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);

		usernameEditText = (EditText) findViewById(R.id.usernameEditText);
		passwordEditText = (EditText) findViewById(R.id.passwordEditText);
	}

	/**
	 * Click event for the sign in button
	 * 
	 * @param view
	 */
	public void signIn(View view) {
		new LoginUserTask(getApplicationContext()).execute();
	}

	/**
	 * Click event for the sign up button
	 * 
	 * @param view
	 */
	public void signUp(View view) {
		Intent intent = new Intent(getApplicationContext(),
				SignUpActivity.class);
		startActivity(intent);

		finish();
	}

	private class LoginUserTask extends AsyncTask<Void, Void, Void> {

		// the context of the application
		private Context context;

		// flag indicating that user is logged in successfully
		private boolean isSuccessfull;

		public LoginUserTask(Context context) {
			this.context = context;
			isSuccessfull = true;
		}

		@Override
		protected Void doInBackground(Void... params) {
			String userName = usernameEditText.getText().toString();
			String password = passwordEditText.getText().toString();
			UserFunctions userFunction = new UserFunctionsImpl();
			JSONObject json = userFunction.loginUser(userName, password);

			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						// User successfully logged in
						// Store user details in SQLite Database
						DatabaseHandler db = new DatabaseHandler(
								getApplicationContext());
						JSONObject json_user = json.getJSONObject(KEY_USER);

						// Clear all previous data in database
						userFunction.logOutUser(getApplicationContext());
						db.addUser(json_user.getString(KEY_USER_NAME),
								json.getString(KEY_TOKEN),
								json_user.getString(KEY_CREATED_AT));

						// Launch Main Activity screen
						Intent intent = new Intent(getApplicationContext(),
								MainActivity.class);

						// Close all views before launching Main Activity
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);

						// Close Sign In screen
						finish();

					} else {
						isSuccessfull = false;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (!isSuccessfull) {
				Toast.makeText(context, "Incorect username/password",
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
