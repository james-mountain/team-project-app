/**
 * Display help information about the application
 * @author Aleksander Antoniewicz
 * @version 1.0
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
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.ac.ncl.cs.team16.lloydsbankingapp.adapters.HelpExpandableListAdapter;
import uk.ac.ncl.cs.team16.lloydsbankingapp.R;


/**
 * A simple {@link Fragment} subclass.
 * activities that contain this fragment must implement the
 * {@link HelpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class HelpFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private List<String> listHeaders;
    private HashMap<String, List<String>> listItems;

    public HelpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View helpView = inflater.inflate(R.layout.fragment_help, container, false);

        prepareListData();
		ExpandableListAdapter listAdapter = new HelpExpandableListAdapter(getActivity(), listItems, listHeaders);
		ExpandableListView listView = (ExpandableListView) helpView.findViewById(R.id.helpListView);
        listView.setAdapter(listAdapter);

        return helpView;
    }

    private void prepareListData() {

        listHeaders = new ArrayList<String>();
        listItems = new HashMap<String, List<String>>();

        // TODO: Import some kind of help content or agree to some fixed content between web/app

        listHeaders.add("How to reset online login credentials");
        listHeaders.add("How to log out");
        listHeaders.add("Support");

        List<String> reset = new ArrayList<String>();
        reset.add("For security reasons, you cannot reset your login credentials using this app.\n\n" +
                "If you wish to do this, you will need to speak with your local branch in person.\n\n" +
                "Please note that you may be asked to show ID in store.");

        List<String> logOut = new ArrayList<String>();
        logOut.add("To log out, simply use the back button on your device to take the app back " +
                "to the login screen.");

        List<String> support = new ArrayList<String>();
        support.add("If you have encountered any technical faults with this application or need " +
                "help of any kind, contact the support team on 0800 000 0000");

        listItems.put(listHeaders.get(0), reset);
        listItems.put(listHeaders.get(1), logOut);
        listItems.put(listHeaders.get(2), support);
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
        public void onFragmentInteraction(Uri uri);
    }

}
