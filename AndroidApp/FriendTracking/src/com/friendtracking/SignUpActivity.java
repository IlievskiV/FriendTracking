package com.friendtracking;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.friendtracking.R;

import org.json.JSONException;
import org.json.JSONObject;

import com.friendtracking.database.DatabaseHandler;
import com.friendtracking.utils.UserFunctions;
import com.friendtracking.utils.UserFunctionsImpl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class SignUpActivity extends Activity implements OnItemSelectedListener {

	// UI elements
	private EditText userNameSignUp;
	private EditText firstNameSignUp;
	private EditText secondNameSignUp;
	private Spinner ageSpiner;
	private EditText homeTown;
	private EditText email;
	private EditText password;

	// JSON response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_TOKEN = "token";
	private static String KEY_USER_NAME = "username";
	private static String KEY_CREATED_AT = "created_at";
	private static String KEY_USER = "user";

	//
	private String gender = "";
	private int age = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_activity);

		userNameSignUp = (EditText) findViewById(R.id.usernameSignUp);
		firstNameSignUp = (EditText) findViewById(R.id.firstnameSignUp);
		secondNameSignUp = (EditText) findViewById(R.id.secondnameSignUp);

		ageSpiner = (Spinner) findViewById(R.id.age);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter
				.createFromResource(this, R.array.years_array,
						android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		ageSpiner.setAdapter(adapter);
		ageSpiner.setOnItemSelectedListener(this);

		homeTown = (EditText) findViewById(R.id.homeTown);
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password);
	}

	/**
	 * 
	 */
	public void signUp(View view) {
		if (validate()) {
			new SignUpTask(getApplicationContext()).execute();
		} else {
			Toast.makeText(getApplicationContext(),
					"Fill in all of the fields", Toast.LENGTH_LONG).show();
		}
	}

	public void onRadioButtonClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();

		switch (view.getId()) {
		case R.id.maleOptionRadioBtn:
			if (checked) {
				gender = "male";
			}
			break;

		case R.id.femaleOptionRadioBtn:
			if (checked) {
				gender = "female";
			}
			break;
		default:
			break;
		}
	}

	private boolean validate() {
		if (userNameSignUp.getText().toString().trim().equals("")
				|| firstNameSignUp.getText().toString().trim().equals("")
				|| secondNameSignUp.getText().toString().trim().equals("")
				|| age == 0 || gender.equals("")
				|| homeTown.getText().toString().trim().equals("")
				|| email.getText().toString().trim().equals("")
				|| password.getText().toString().trim().equals("")) {

			return false;
		}

		return true;
	}

	private String generateMD5(String s) throws NoSuchAlgorithmException {
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(s.getBytes(), 0, s.length());
		String encrypted = new BigInteger(1, m.digest()).toString(16);
		String res = String.format("%0" + (32 - encrypted.length()) + "d%s", 0,
				encrypted);
		return res;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		age = Integer.parseInt(parent.getItemAtPosition(position).toString());
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		age = 0;
	}

	private class SignUpTask extends AsyncTask<Void, Void, Void> {

		// the context of the application
		private Context context;

		// flag indicating that user is signed up successfully
		private boolean isSuccessfull;

		public SignUpTask(Context context) {
			this.context = context;
			isSuccessfull = true;
		}

		@Override
		protected Void doInBackground(Void... params) {

			// extracting values from UI elements
			String userName = userNameSignUp.getText().toString();
			String firstName = firstNameSignUp.getText().toString();
			String lastName = secondNameSignUp.getText().toString();
			String mAge = String.valueOf(age);
			String mHomeTown = homeTown.getText().toString();
			String mEmail = email.getText().toString();
			String mPassword = password.getText().toString();

			UserFunctions userFunctions = new UserFunctionsImpl();
			JSONObject json = userFunctions.registerUser(userName, firstName,
					lastName, mAge, gender, mHomeTown, mEmail, mPassword);

			Log.i("json object", json.toString());

			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						// User successfully registered
						// Store user details in SQLite Database
						DatabaseHandler db = new DatabaseHandler(
								getApplicationContext());
						JSONObject json_user = json.getJSONObject(KEY_USER);

						// Clear all previous data in database
						userFunctions.logOutUser(getApplicationContext());
						db.addUser(json_user.getString(KEY_USER_NAME),
								json.getString(KEY_TOKEN),
								json_user.getString(KEY_CREATED_AT));

						// Launch Main Activity screen
						Intent intent = new Intent(getApplicationContext(),
								MainActivity.class);

						// Close all views before launching Dashboard
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);

						// Close Registration Screen
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
				Toast.makeText(getApplicationContext(),
						"Error occured in registration", Toast.LENGTH_LONG)
						.show();
			}
		}
	}
}
