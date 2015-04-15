package uk.ac.ncl.cs.team16.lloydsbankingapp.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by James Mountain on 09/03/2015.
 */
public class JsonArrayPostRequest extends JsonRequest<JSONArray> {
	public JsonArrayPostRequest(String url, String jsonRequest, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
		super(Method.POST, url, jsonRequest, listener, errorListener);
	}

	@Override
	protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString =
					new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			return Response.success(new JSONArray(jsonString),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	}

	public Map<String, String> getHeaders() throws AuthFailureError {
		AuthHandler authHandler = AuthHandler.getInstance();
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("API-SESSION-ID", authHandler.obtainSessionID());
		return headers;
	}
}
