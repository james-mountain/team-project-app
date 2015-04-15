package uk.ac.ncl.cs.team16.lloydsbankingapp.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by James Mountain on 15/04/2015.
 * Custom class to avoid code duplication.
 */
public class JsonCustomObjectRequest extends JsonObjectRequest {
	public JsonCustomObjectRequest(String url, String requestBody, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
		super(Request.Method.POST, url, requestBody, listener, errorListener);
	}

	public Map<String, String> getHeaders() throws AuthFailureError {
		AuthHandler authHandler = AuthHandler.getInstance();
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("API-SESSION-ID", authHandler.obtainSessionID());
		return headers;
	}
}
