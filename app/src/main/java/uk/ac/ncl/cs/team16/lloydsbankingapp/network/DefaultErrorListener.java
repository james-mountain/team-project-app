package uk.ac.ncl.cs.team16.lloydsbankingapp.network;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by James Mountain on 14/04/2015.
 */
public class DefaultErrorListener implements Response.ErrorListener{
	@Override
	public void onErrorResponse(VolleyError error) {
		Log.d("error", error.getMessage());
	}
}
