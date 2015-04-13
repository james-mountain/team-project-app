package uk.ac.ncl.cs.team16.lloydsbankingapp.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments.PayeesFragment;
import uk.ac.ncl.cs.team16.lloydsbankingapp.R;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.AuthHandler;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.VolleySingleton;

public class AddPayeeActivity extends Activity {

	private static final String REVIEW_URL_BASE = "http://csc2022api.sitedev9.co.uk/account/payee";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payee);

		Button addPayeeButton = (Button) findViewById(R.id.addPayeeButtonConfirm);
		final TextView descriptiontv = (TextView) findViewById(R.id.description_field);
		final TextView sortcodetv = (TextView) findViewById(R.id.sort_code_field);
		final TextView accountnotv = (TextView) findViewById(R.id.account_no_field);

		addPayeeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				addPayeeRequest(descriptiontv.getText().toString(), sortcodetv.getText().toString(), accountnotv.getText().toString());
			}
		});
    }

	private void finalisePayeeAdd() {
		PayeesFragment.payeesContext.reviewPayeesRequest();
		finish();
	}

	private void addPayeeRequest(String desc, String sortcode, String accountno) {
		final AuthHandler authHandler = AuthHandler.getInstance();

		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put("description", desc);
		params.put("sortcode", sortcode);
		params.put("account_no", accountno);

		authHandler.handleAuthentication(params);
		Gson gson = new Gson();
		String requestString = gson.toJson(params, LinkedHashMap.class);

		RequestQueue networkQueue = VolleySingleton.getInstance().getRequestQueue();
		JsonObjectRequest deleteRequest = new JsonObjectRequest(Request.Method.POST, REVIEW_URL_BASE + "/add", requestString, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					if (response.getInt("Status") == 1) {
						Toast.makeText(getApplicationContext(), "New payee added.", Toast.LENGTH_LONG).show();

						finalisePayeeAdd();
					} else {
						Toast.makeText(getApplicationContext(), "Failed to add new payee.", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("error", error.getMessage());
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("API-SESSION-ID", authHandler.obtainSessionID(getApplicationContext()));
				return headers;
			}
		};
		networkQueue.add(deleteRequest);
	}

}
