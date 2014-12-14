package uk.ac.ncl.cs.team16.lloydsbankingapp;

import android.app.Activity;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;


public class AccountsFragment extends Fragment {

    private ListView lv;
    private OnFragmentInteractionListener mListener;
    private String name = "John";
    private int balance = 120;
    private GregorianCalendar c;
    private List<Transaction> transactionList;


    public AccountsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        transactionList = new ArrayList<Transaction>();
        addToList();
        setupMenu();


        c = new GregorianCalendar();

        View v = inflater.inflate(R.layout.fragment_accounts, container, false);
        lv = (ListView) v.findViewById(R.id.transaction_listview);
        lv.setAdapter(new CustomAdapter());
        setupWelcomeScreen(v);
        return v;
    }

    private void addToList() {
        transactionList.add(new Transaction("Homer Simpson Industry Co. Ltd.", "26/10/2014", "1245.10", "+20.00"));
        transactionList.add(new Transaction("Alor Anfosdijgnhnjmpd", "26/11/2014", "20.00", "-20.00"));
        transactionList.add(new Transaction("Szpam Szpamonov", "01/10/2014", "45.10", "-5.00"));
        transactionList.add(new Transaction("I'm sending you money", "05/02/2014", "12.10", "-150.00"));
        transactionList.add(new Transaction("Will", "09/10/2014", "120.00", "-200.29"));
        transactionList.add(new Transaction("Michael", "12/10/2014", "10.00", "+250.29"));
        transactionList.add(new Transaction("WM Morisson Supermarkets PLC", "20/01/2013", "-10.00", "+15.29"));
    }

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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
