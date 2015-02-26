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
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import uk.ac.ncl.cs.team16.lloydsbankingapp.R;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.VolleySingleton;
import uk.ac.ncl.cs.team16.lloydsbankingapp.Models.Transaction;


public class AccountsFragment extends Fragment {

    private ListView lv;
    private OnFragmentInteractionListener mListener;

    //account owner name
    private String name = "John";

    //temp balance var
    private int balance = 120;
    private GregorianCalendar c;

    //temporarily one transaction list
    private List<Transaction> transactionList;
    private static final String URL = "http://csc2022api.sitedev9.co.uk/Account/Payee";


    public AccountsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        transactionList = new ArrayList<Transaction>();
        addToList();
        setupMenu();

        c = new GregorianCalendar();

        View v = inflater.inflate(R.layout.fragment_accounts, container, false);
        lv = (ListView) v.findViewById(R.id.transaction_listview);
        lv.setAdapter(new CustomAdapter());

        setupWelcomeScreen(v);
        setupSpinner();

        setupRequest();

        return v;
    }

    private void setupRequest() {
        RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();
        JsonArrayRequest request = new JsonArrayRequest(URL, new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response) {
                parseResponse(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(request);
    }

    private void parseResponse(JSONArray response) {
        if(!(response == null || response.length() == 0)){
            try{
                JSONArray  array = response.getJSONArray(0);

                for(int i=0; i<array.length(); i++){
                    JSONObject o = array.getJSONObject(i);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }


    }


    /**
     * Set the spinner in the actionbar
     */
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
            TextView payee = (TextView) row.findViewById(R.id.payee_name);
            payee.setText(transactionList.get(position).getPayee());
            TextView date = (TextView) row.findViewById(R.id.transaction_date);
            date.setText(transactionList.get(position).getDate());
            TextView balance = (TextView) row.findViewById(R.id.after_balance);
            balance.setText("£" + transactionList.get(position).getBalance());
            TextView value = (TextView) row.findViewById(R.id.transaction_value);
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

        welcomeTV.setText("Welcome, " + name);
        balanceTV.setText("Current balance: £" + balance);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        dateTv.setText(df.format(c.getTime()));
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
    public void setupMenu() {
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
