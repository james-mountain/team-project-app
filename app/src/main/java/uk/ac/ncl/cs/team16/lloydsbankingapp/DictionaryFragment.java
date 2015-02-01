package uk.ac.ncl.cs.team16.lloydsbankingapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DictionaryFragment extends Fragment {

    private ListView dictionarySearchResults;
    private List<DictionaryEntry> dictionaryEntries;
    private OnFragmentInteractionListener mListener;

    public DictionaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View dictionaryView = inflater.inflate(R.layout.fragment_dictionary, container, false);
        dictionaryEntries = new ArrayList<DictionaryEntry>();
        dictionaryEntries.add(new DictionaryEntry("Interest", "Interest is a payment by the bank for your deposit."));
        dictionaryEntries.add(new DictionaryEntry("Overdraft", "An extra amount of available money below £0. This is not unlimited."));
        dictionaryEntries.add(new DictionaryEntry("Standing Order", "A set payment (usually with a timescale), which is setup by you."));

        List<Map<String, String>> dictionaryMapList = new ArrayList<Map<String, String>>(); // I'll clean this up later, it's a mess
        for (DictionaryEntry entry : dictionaryEntries) {
            Map<String, String> dictionaryMap = new HashMap<String, String>();
            dictionaryMap.put("name", entry.getEntryName());
            dictionaryMap.put("desc", entry.getEntryDescription());
            dictionaryMapList.add(dictionaryMap);
        }

        dictionarySearchResults = (ListView) dictionaryView.findViewById(R.id.dictionarySearchRes);// Set it to the entire dictionary list, initially
        String[] resource = {"name", "desc"};
        int[] location = {android.R.id.text1, android.R.id.text2};
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), dictionaryMapList, android.R.layout.simple_list_item_2, resource, location);
        dictionarySearchResults.setAdapter(adapter);

        return dictionaryView;
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
