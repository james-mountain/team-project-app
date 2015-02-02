package uk.ac.ncl.cs.team16.lloydsbankingapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    private List<Map<String,String>> rebuildDictionary(String searchPattern) {
        List<Map<String, String>> dictionaryMapList = new ArrayList<Map<String, String>>();
        for (DictionaryEntry entry : dictionaryEntries) {
            Map<String, String> dictionaryMap = new HashMap<String, String>();
            String entryName = entry.getEntryName();
            int searchLength = Math.min(searchPattern.length(), entryName.length());
            if (!searchPattern.equals("")) {
                if (!searchPattern.toLowerCase().equals(entryName.toLowerCase().substring(0, searchLength))) {
                    continue;
                }
            }

            dictionaryMap.put("name", entry.getEntryName());
            dictionaryMap.put("desc", entry.getEntryDescription());
            dictionaryMapList.add(dictionaryMap);
        }

        Collections.sort(dictionaryMapList, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> map1, Map<String, String> map2) {
                return map1.get("name").compareTo(map2.get("name"));
            }
        });

        return dictionaryMapList;
    }

    private void populateDictionary() {
        dictionaryEntries.add(new DictionaryEntry("Interest", "Interest is a payment by the bank for your deposit."));
        dictionaryEntries.add(new DictionaryEntry("Overdraft", "An extra amount of available money below £0. This is not unlimited."));
        dictionaryEntries.add(new DictionaryEntry("Standing Order", "A set payment (usually with a timescale), which is setup by you."));
        dictionaryEntries.add(new DictionaryEntry("Student Loan", "A sum of money that is paid to the bank holder by the government in order to pay for student expenses."));
        dictionaryEntries.add(new DictionaryEntry("Statement", "A set of transactions put into a summary over a time period, normally one month."));
        dictionaryEntries.add(new DictionaryEntry("ATM", "A machine to provide an interface to get banking services, via ones card."));
        dictionaryEntries.add(new DictionaryEntry("Available Balance", "Calculated value which will take into account not only your balance but your overdraft."));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View dictionaryView = inflater.inflate(R.layout.fragment_dictionary, container, false);
        dictionaryEntries = new ArrayList<DictionaryEntry>();
        populateDictionary();

        dictionarySearchResults = (ListView) dictionaryView.findViewById(R.id.dictionarySearchRes);// Set it to the entire dictionary list, initially
        final String[] resource = {"name", "desc"};
        final int[] location = {android.R.id.text1, android.R.id.text2};
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), rebuildDictionary(""), android.R.layout.simple_list_item_2, resource, location);
        dictionarySearchResults.setAdapter(adapter);

        final EditText searchBar = (EditText) dictionaryView.findViewById(R.id.dictionarySearchBar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence cs, int i, int i2, int i3) {
                // REQUIRED
            }

            @Override
            public void onTextChanged(CharSequence cs, int i, int i2, int i3) {
                SimpleAdapter searchAdapter = new SimpleAdapter(getActivity(), rebuildDictionary(searchBar.getText().toString()), android.R.layout.simple_list_item_2, resource, location);
                dictionarySearchResults.setAdapter(searchAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //ALSO REQUIRED
            }
        });

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
