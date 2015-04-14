package uk.ac.ncl.cs.team16.lloydsbankingapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments.MainFragment;
import uk.ac.ncl.cs.team16.lloydsbankingapp.R;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.DefaultErrorListener;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.VolleySingleton;

public class MainActivity extends Activity implements MainFragment.OnFragmentInteractionListener {
	private static final String AUTH_URL_BASE = "http://csc2022api.sitedev9.co.uk/authentication";
	private static final String APPKEY = "xlYzpVTNVr4NWvsSR9WUmkW4kZhMYigz";
	private String sessionToken = null;

	private String userID = "";
	private String password = "";
	private boolean fullyAuthToken = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			MainFragment mf = new MainFragment();
			getFragmentManager().beginTransaction()
					.add(R.id.container, mf)
					.commit();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		getNewSessionToken(); // We get the session token here. Getting it onCreate apparently doesn't work, oh well
	}

	private void getNewSessionToken() {
		RequestQueue networkQueue = VolleySingleton.getInstance().getRequestQueue();
		Map<String, String> appKeyParams = new HashMap<String, String>();
		appKeyParams.put("app_key", APPKEY);

		JsonObjectRequest sessTokenRequest = new JsonObjectRequest(Request.Method.POST, AUTH_URL_BASE + "/session", new JSONObject(appKeyParams), new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					sessionToken = response.getString("session_id");
				} catch (JSONException e) {

				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// oops, error handling can be sorted later.
			}
		});
		networkQueue.add(sessTokenRequest);
	}

	private void requestLogin() {
		RequestQueue networkQueue = VolleySingleton.getInstance().getRequestQueue();
		Map<String, String> loginParams = new HashMap<String, String>();
		String passwordHex = new String(Hex.encodeHex(DigestUtils.sha256(password))); // Apache Common Codec - DigestUtils lib
		String combinedHex = new String(Hex.encodeHex(DigestUtils.sha256(sessionToken + passwordHex)));

		loginParams.put("userid", userID);
		loginParams.put("password", combinedHex);

		JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, AUTH_URL_BASE + "/login", new JSONObject(loginParams), new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					if (response.getInt("Status") == 1) {
						requestAuth();
					} else {
						Toast.makeText(getApplicationContext(), "Incorrect username or password.", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {

				}
			}
		}, new DefaultErrorListener()) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("API-SESSION-ID", sessionToken);
				return headers;
			}
		};
		networkQueue.add(loginRequest);
	}

	private void requestAuth() {
		String memorable = "password123";

		RequestQueue networkQueue = VolleySingleton.getInstance().getRequestQueue();
		Map<String, String> loginParams = new HashMap<String, String>();
		String memHex = new String(Hex.encodeHex(DigestUtils.sha256(memorable))); // Apache Common Codec - DigestUtils lib
		String combinedHex = new String(Hex.encodeHex(DigestUtils.sha256(sessionToken + memHex)));

		loginParams.put("memorable", combinedHex);

		JsonObjectRequest authRequest = new JsonObjectRequest(Request.Method.POST, AUTH_URL_BASE + "/authorize", new JSONObject(loginParams), new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					if (response.getInt("Status") == 1) {
						fullyAuthToken = true;

						Toast.makeText(getApplicationContext(), "Login successful.", Toast.LENGTH_LONG).show();
						attemptLogin();
					} else {
						Toast.makeText(getApplicationContext(), "Incorrect memorable word.", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {

				}
			}
		}, new DefaultErrorListener()) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("API-SESSION-ID", sessionToken);
				return headers;
			}
		};
		networkQueue.add(authRequest);
	}

	private void attemptLogin() {
		if (fullyAuthToken) { // We use this as a barrier because the method is public.
			Intent homeIntent = new Intent(this, HomeActivity.class);
			startActivity(homeIntent);

			String location = "uk.ac.ncl.cs.team16.lloydsbankingapp";
			SharedPreferences sharedPref = this.getSharedPreferences(location, Context.MODE_PRIVATE);
			sharedPref.edit().putString(location + ".session", sessionToken).apply(); // This should be secure enough
		}
	}

	@Override
	public void onFragmentInteraction(String inputUserID, String inputPassword) {
		this.userID = inputUserID;
		this.password = inputPassword;

		if (sessionToken == null) {
			Toast.makeText(getApplicationContext(), "Failed to get a session, please try again", Toast.LENGTH_LONG).show();
			getNewSessionToken();
		} else {
			requestLogin();
		}
	}
}
