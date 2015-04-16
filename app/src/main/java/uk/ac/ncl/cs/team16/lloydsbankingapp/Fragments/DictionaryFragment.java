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

    /*
    Add all the entries to the list of entries to be used by the dictionary.
     */

    // TODO: Change this to accomodate new dictionary content that will be on the backend
    private void populateDictionary() {
        dictionaryEntries.add(new DictionaryEntry("Accrued Interest", "Interest that has been earned but not yet paid."));
        dictionaryEntries.add(new DictionaryEntry("Annual Percentage Rate", "The cost of credit on a yearly basis, expressed as a percentage."));
        dictionaryEntries.add(new DictionaryEntry("Available Balance", "The balance of an account less any hold, uncollected funds, and restrictions against the account."));
        dictionaryEntries.add(new DictionaryEntry("Balance Transfer", "The process of moving an outstanding balance from one credit card to another. This is usually done to obtain a lower interest rate on the outstanding balance. Transfers are sometimes subjected to a Balance Transfer Fee."));
        dictionaryEntries.add(new DictionaryEntry("Bank Statement", "Periodically the bank provides a statement of a customer's deposit account. It shows all deposits made, all checks paid, and other debits posted during the period (usually one month), as well as the current balance."));
        dictionaryEntries.add(new DictionaryEntry("Billing Date", "The month, date, and year when a periodic or monthly statement is generated. Calculations have been performed for appropriate finance charges, minimum payment due, and new balance."));
        dictionaryEntries.add(new DictionaryEntry("Current Account", "An account at a bank or building society from which money may be drawn on demand."));
		dictionaryEntries.add(new DictionaryEntry("Debit", "A debit may be an account entry representing money you owe a lender or money that has been taken from your deposit account."));
		dictionaryEntries.add(new DictionaryEntry("Debit Card", "A debit card allows the account owner to access their funds electronically. Debit cards may be used to obtain cash from automated teller machines or purchase goods or services using point-of-sale systems. The use of a debit card involves immediate debiting and crediting of consumers' accounts."));
		dictionaryEntries.add(new DictionaryEntry("Electronic Banking", "A service that allows an account holder to obtain account information and manage certain banking transactions through a personal computer via the financial institution's Web site on the Internet. (This is also known as Internet or online banking.)"));
		dictionaryEntries.add(new DictionaryEntry("Electronic Funds Transfer", "The transfer of money between accounts by consumer electronic systems-such as automated teller machines (ATMs) and electronic payment of bills-rather than by check or cash. (Wire transfers, checks, drafts, and paper instruments do not fall into this category.)"));
		dictionaryEntries.add(new DictionaryEntry("Inactive Account", "An account that has little or no activity; neither deposits nor withdrawals having been posted to the account for a significant period of time."));
		dictionaryEntries.add(new DictionaryEntry("Insufficient Funds", "When a depositor's checking account balance is inadequate to pay a check presented for payment."));
		dictionaryEntries.add(new DictionaryEntry("Interest", "The term interest is used to describe the cost of using money, a right, share, or title in property."));
		dictionaryEntries.add(new DictionaryEntry("Interest Rate", "The amount paid by a borrower to a lender in exchange for the use of the lender's money for a certain period of time. Interest is paid on loans or on debt instruments, such as notes or bonds, either at regular intervals or as part of a lump sum payment when the issue matures."));
		dictionaryEntries.add(new DictionaryEntry("Online Banking", "A service that allows an account holder to obtain account information and manage certain banking transactions through a personal computer via the financial institution's web site on the Internet. (This is also known as Internet or electronic banking.)"));
		dictionaryEntries.add(new DictionaryEntry("Overdraft", "When the amount of money withdrawn from a bank account is greater than the amount actually available in the account, the excess is known as an overdraft, and the account is said to be overdrawn."));
		dictionaryEntries.add(new DictionaryEntry("Overdraw", "To write a check for an amount that exceeds the amount on deposit in the account."));
		dictionaryEntries.add(new DictionaryEntry("Payee", "The person or organization to whom a check, draft, or note is made payable."));
		dictionaryEntries.add(new DictionaryEntry("Payor", "The person or organization who pays."));
		dictionaryEntries.add(new DictionaryEntry("Personal Identification Number", "Generally a four-character number or word, the PIN is the secret code given to credit or debit cardholders enabling them to access their accounts. The code is either randomly assigned by the bank or selected by the customer. It is intended to prevent unauthorized use of the card while accessing a financial service terminal."));
		dictionaryEntries.add(new DictionaryEntry("Previous Balance", "The cardholder's account balance as of the previous billing statement."));
		dictionaryEntries.add(new DictionaryEntry("Refund", "An amount paid back because of an overpayment or because of the return of an item previously sold."));
		dictionaryEntries.add(new DictionaryEntry("Savings Account", "A deposit account at a bank or savings and loan which pays interest, but cannot be withdrawn by check writing."));
		dictionaryEntries.add(new DictionaryEntry("Service Charge", "A charge assessed by a depository institution for processing transactions and maintaining accounts."));
		dictionaryEntries.add(new DictionaryEntry("Statement", "A summary of all transactions that occurred over the preceding month and could be associated with a deposit account or a credit card account."));
		dictionaryEntries.add(new DictionaryEntry("Student Loan", "Loans made, insured, or guaranteed under any program authorized by the Higher Education Act. Loan funds are used by the borrower for education purposes."));
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
