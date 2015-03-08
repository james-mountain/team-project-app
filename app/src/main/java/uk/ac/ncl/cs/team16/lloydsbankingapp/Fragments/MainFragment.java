/**
 * Supply login information to access the restricted part of the app
 * @author Shawkat Al-Baghdadi, Aleksander Antoniewicz
 * @version 1.0
 */
package uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import uk.ac.ncl.cs.team16.lloydsbankingapp.Activities.HomeActivity;
import uk.ac.ncl.cs.team16.lloydsbankingapp.R;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.VolleySingleton;

public class MainFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button loginButton;
    private static final String AUTH_URL_BASE = "http://csc2022api.sitedev9.co.uk/authentication";
    private static final String APPKEY = "FZpQ25vZsV8X1btAn59VgA2Bfe37kXMP";
    private static String sessionToken = null;

    public MainFragment() {
        // Required empty public constructor
    }

    private void getSessionToken() {
        RequestQueue networkQueue = VolleySingleton.getInstance().getRequestQueue();
        Map<String, String> appKeyParams = new HashMap<String, String>();
        appKeyParams.put("app_key", APPKEY);

        JsonObjectRequest sessTokenRequest = new JsonObjectRequest(Request.Method.POST, AUTH_URL_BASE + "/session", new JSONObject(appKeyParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    sessionToken = response.getString("session_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                requestLogin();
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // oops, error handling can be sorted later.
            }
        });
        networkQueue.add(sessTokenRequest);
    }

    private void requestLogin() {
        String userID = "1";
        String password = "password123";

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
                    String status = response.getString("status");
                    if (status.equals("Success")) {
                        requestAuth();
                    } else {
                        Toast.makeText(getActivity(), "Incorrect username or password.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // oops, error handling can be sorted later.
            }
        }) {
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
                    String status = response.getString("status");
                    if (status.equals("Success")) {
                        Toast.makeText(getActivity(), "SUCCESSFULLY LOGGED IN", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Incorrect memorable word.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // oops, error handling can be sorted later.
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("API-SESSION-ID", sessionToken);
                return headers;
            }
        };
        networkQueue.add(authRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        loginButton = (Button) v.findViewById(R.id.login_button);

        //separate the listeners from onCreateView
        listeners();

        return v;
    }

    public void listeners(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSessionToken();
                /*Intent a = new Intent(getActivity(), HomeActivity.class);
                startActivity(a);*/
            }
        });
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
