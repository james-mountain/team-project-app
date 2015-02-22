/**
 * Transfer funds to another account
 * @author Aleksander Antoniewicz
 * @version 0.5
 */

package uk.ac.ncl.cs.team16.lloydsbankingapp.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import uk.ac.ncl.cs.team16.lloydsbankingapp.R;


public class TransferFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Spinner accountFromChoice, accountToChoice;

    //Temporary account options array
    private String accountNames[] = {"Savers account 9000", "Student spender's"};
    public TransferFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_transfer, container, false);


        //setup the spinners
        accountFromChoice = (Spinner) v.findViewById(R.id.account_from_choice);

        ArrayAdapter<String> aa = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, accountNames);
        aa.setDropDownViewResource(R.layout.spinner_item);

        accountFromChoice.setAdapter(aa);

        return v;
    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
