/**
 * Display the dictionary
 * @author James Mountain, Aleksander Antoniewicz
 * @version 0.9
 *
 */

package uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import uk.ac.ncl.cs.team16.lloydsbankingapp.Activities.DictionaryEntryActivity;
import uk.ac.ncl.cs.team16.lloydsbankingapp.Models.DictionaryEntry;
import uk.ac.ncl.cs.team16.lloydsbankingapp.R;

public class DictionaryFragment extends Fragment {

    private final Collection<DictionaryEntry> dictionaryEntries = new ArrayList<DictionaryEntry>(); //This is a fixed set for now
    private OnFragmentInteractionListener mListener;

    public DictionaryFragment() {
        // Required empty public constructor
    }

    /*
    Creates a List of DictionaryEntry that will get used by the adapter to bring all the entries into the ListView, according to search
     */
    private List<DictionaryEntry> rebuildDictionary(String searchPattern) {
        List<DictionaryEntry> searchEntryResults = new ArrayList<DictionaryEntry>();

        for (DictionaryEntry entry : dictionaryEntries) {
            String entryName = entry.getEntryName();
            int searchLength = Math.min(searchPattern.length(), entryName.length()); // Failure to define this can result in a null pointer exception
            if (!searchPattern.isEmpty()) { // A dictionary search of sorts, like a RadixSort
                if (!searchPattern.toLowerCase().equals(entryName.toLowerCase().substring(0, searchLength))) {
                    continue;
                }
            }

           searchEntryResults.add(entry);
        }

        // Finally sort the new list of entries, use comparator since there is no default sort
        Collections.sort(searchEntryResults, new Comparator<DictionaryEntry>() {
            @Override
            public int compare(DictionaryEntry dictionaryEntry, DictionaryEntry dictionaryEntry2) {
                return dictionaryEntry.getEntryName().compareTo(dictionaryEntry2.getEntryName());
            }
        });

        return searchEntryResults;
    }

    // Some temp entries, backend data of the dictionary will be pulled in here, if the backend is going to be used with dictionary.
    /*
    Add all the entries to the list of entries to be used by the dictionary.
     */

    // TODO: Change this to accomodate new dictionary content that will be on the backend
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
        final ListView searchRsltView = (ListView) dictionaryView.findViewById(R.id.dictionarySearchRes);// Set it to the entire dictionary list, initially
        populateDictionary();

        searchRsltView.setAdapter(new DictionaryEntryAdapter(rebuildDictionary("")));
        searchRsltView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent entryIntent = new Intent(getActivity(), DictionaryEntryActivity.class);

                // Temp way to do this. Dictionary needs to be moved to a resource in the main activity ideally.

                TextView entryTextView = (TextView) view.findViewById(R.id.dictionaryEntryName);
                TextView entryDescView = (TextView) view.findViewById(R.id.dictionaryEntryDesc);
                entryIntent.putExtra("entryName", entryTextView.getText());
                entryIntent.putExtra("entryDesc", entryDescView.getText());
                startActivity(entryIntent);
            }
        });

        final EditText searchBar = (EditText) dictionaryView.findViewById(R.id.dictionarySearchBar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence cs, int i, int i2, int i3) {
                // This is a required override, but isn't used
            }

            @Override
            public void onTextChanged(CharSequence cs, int i, int i2, int i3) {
                // Here we actually do the rebuilding of the list, the rebuild dictionary method is called and it regenerates the map to be used with the adapter.
                searchRsltView.setAdapter(new DictionaryEntryAdapter(rebuildDictionary(searchBar.getText().toString())));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This is a required override, but isn't used
            }
        });

        return dictionaryView;
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

    private class DictionaryEntryAdapter extends ArrayAdapter<DictionaryEntry> {
        private final List<DictionaryEntry> dictionaryEntrySet;

        DictionaryEntryAdapter(List<DictionaryEntry> dictionaryEntrySet){
            super(getActivity(), R.layout.row_dictionary, R.id.dictionaryEntryName, dictionaryEntrySet);
            this.dictionaryEntrySet = dictionaryEntrySet;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View entryRow = super.getView(position, convertView, parent);
            TextView entryText = (TextView) entryRow.findViewById(R.id.dictionaryEntryName);
            TextView entryDesc = (TextView) entryRow.findViewById(R.id.dictionaryEntryDesc);

            entryText.setText(dictionaryEntrySet.get(position).getEntryName());
            entryDesc.setText(dictionaryEntrySet.get(position).getEntryDescription());
            return entryRow;
        }
    }

}
