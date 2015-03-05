/**
 * Displays information about accounts and their history.
 * @author Aleksander Antoniewicz
 * @version 1.0
 *
 */

package uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import uk.ac.ncl.cs.team16.lloydsbankingapp.R;
import uk.ac.ncl.cs.team16.lloydsbankingapp.Models.Transaction;

public class AccountsFragment extends Fragment {

    private ListView lv;
    private OnFragmentInteractionListener mListener;
    private GregorianCalendar calendar;

    //temporarily one transaction list
    private List<Transaction> transactionList;

    public AccountsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        transactionList = new ArrayList<Transaction>();
        addToList();
        setupMenu();

        calendar = new GregorianCalendar();

        View v = inflater.inflate(R.layout.fragment_accounts, container, false);
        lv = (ListView) v.findViewById(R.id.transaction_listview);
        lv.setAdapter(new CustomAdapter());

        setupWelcomeScreen(v);
        setupSpinner();

        return v;
    }


    /**
     * Set the spinner in the actionbar
     */

    // TODO: The accounts associated with a login should be JSON requested as soon as login is complete
    // TODO: Retrieve further requests from the selected account

    private void setupSpinner() {
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().getActionBar().setDisplayShowCustomEnabled(true);

        LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = inflator.inflate(R.layout.spinner_layout, null);

        Spinner s = (Spinner) vi.findViewById(R.id.spin);


        /******* THIS ARRAYLIST SHOULD BE REMOVED AND REPLACED BY ONE PARSED FROM JSON ************/
        ArrayList<String> spinnerlist = new ArrayList<String>();
        spinnerlist.add("Savers 9000");
        spinnerlist.add("Student spender's");

        /********************************************************************************************/

        ArrayAdapter<String> spinneradapter = new ArrayAdapter<String>(getActivity(), R.layout.actionbar_spinner_item, spinnerlist);

        s.setAdapter(spinneradapter);

        getActivity().getActionBar().setCustomView(vi);

        spinneradapter.notifyDataSetChanged();



        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    /**
     * Add the transaction to a list (works for the mockup only)
     */
    private void addToList() {
        transactionList.add(new Transaction("Homer Simpson Industry Co. Ltd.", "26/10/2014", "1245.10", "+20.00"));
        transactionList.add(new Transaction("Alor Anfosdijgnhnjmpd", "26/11/2014", "20.00", "-20.00"));
        transactionList.add(new Transaction("Szpam Szpamonov", "01/10/2014", "45.10", "-5.00"));
        transactionList.add(new Transaction("I'm sending you money", "05/02/2014", "12.10", "-150.00"));
        transactionList.add(new Transaction("Will", "09/10/2014", "120.00", "-200.29"));
        transactionList.add(new Transaction("Michael", "12/10/2014", "10.00", "+250.29"));
        transactionList.add(new Transaction("WM Morrison Supermarkets PLC", "20/01/2013", "-10.00", "+15.29"));
    }


    /**
     * Custom adapter for the listview of transactions
     * @author Aleksander Antoniewicz
     * @version 1.0
     */
    class CustomAdapter extends ArrayAdapter<Transaction>{
        CustomAdapter(){
            super(getActivity(), R.layout.transaction_row, R.id.payee_name, transactionList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);
            TextView value = (TextView) row.findViewById(R.id.transaction_value);
            TextView payee = (TextView) row.findViewById(R.id.payee_name);
            TextView balance = (TextView) row.findViewById(R.id.after_balance);
            TextView date = (TextView) row.findViewById(R.id.transaction_date);

            payee.setText(transactionList.get(position).getPayee());
            date.setText(transactionList.get(position).getDate());
            balance.setText("£" + transactionList.get(position).getBalance());
            value.setText(transactionList.get(position).getValue());
            return row;
        }
    }


    /**
     * Setup the green welcome screen
     * @param v Accounts fragment xml file
     */
    private void setupWelcomeScreen(View v) {
        TextView welcomeTV, balanceTV, dateTv;
        welcomeTV = (TextView) v.findViewById(R.id.welcome_tv);
        balanceTV = (TextView) v.findViewById(R.id.balance_tv);
        dateTv = (TextView) v.findViewById(R.id.date_tv);

        String name = "John";
        int balance = 120;

        welcomeTV.setText("Welcome, " + name);
        balanceTV.setText("Current balance: £" + balance);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        dateTv.setText(df.format(calendar.getTime()));
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

        //Remove the spinner from the actionbar
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
        getActivity().getActionBar().setDisplayShowCustomEnabled(false);
        super.onDetach();
        mListener = null;
    }

    /**
     * To setup the menu?
     */
    private void setupMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(getActivity());
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
    }
    //
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
