/**
 * Enable user to search for a branch or ATM based on Google Maps and current locations
 *
 * @author Aleksander Antoniewicz
 * @version 0.1
 *
 */

package uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.ncl.cs.team16.lloydsbankingapp.R;

public class ATMFragment extends Fragment {

    // TODO: Whole ton of stuff!

    // TODO: API keys, permissions (android studio can do this), just getting a basic map layout to work.
    // TODO: I think a new branch would be ideal for this (atms/maps branch)
    // TODO: JSON request for ATM/branch data.

    private OnFragmentInteractionListener mListener;

    public ATMFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setRetainInstance(true);
        return inflater.inflate(R.layout.fragment_atm, container, false);
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
