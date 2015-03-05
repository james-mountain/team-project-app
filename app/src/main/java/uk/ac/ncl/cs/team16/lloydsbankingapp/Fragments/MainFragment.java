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

import uk.ac.ncl.cs.team16.lloydsbankingapp.Activities.HomeActivity;
import uk.ac.ncl.cs.team16.lloydsbankingapp.R;


public class MainFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button loginButton;
    public MainFragment() {
        // Required empty public constructor
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

    // TODO: Implement a second step process for the unique passkey(?).
    // TODO: This is specified on the API outline that was provided.
    // TODO: Needs to be implemented as another activity perhaps?

    // TODO: Implementation of the two JSON packages that are sent to the server
    // TODO: Use SHA-256 to generate hash of the password to send FIRST.
    // TODO: First data is username and hashed password
    // TODO: Second data is the passkey/uniquekey/identifer ONCE the first data was a success.
    private void listeners(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getActivity(), HomeActivity.class);
                startActivity(a);
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
