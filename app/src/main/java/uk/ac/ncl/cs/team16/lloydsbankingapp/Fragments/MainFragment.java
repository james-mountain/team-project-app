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
import android.widget.EditText;
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
    private View loginView;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loginView = inflater.inflate(R.layout.fragment_main, container, false);
        loginButton = (Button) loginView.findViewById(R.id.login_button);

        //separate the listeners from onCreateView
        listeners();

        return loginView;
    }

    // TODO: Implement a second step process for the unique passkey(?).
    // TODO: This is specified on the API outline that was provided.
    // TODO: Needs to be implemented as another activity perhaps?
    private void listeners(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText userIDet = (EditText) loginView.findViewById(R.id.idInput);
                EditText passwordet = (EditText) loginView.findViewById(R.id.passwordInput);

                mListener.onFragmentInteraction(userIDet.getText().toString(), passwordet.getText().toString());
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
        public void onFragmentInteraction(String userID, String password);
    }

}
