package uk.ac.ncl.cs.team16.lloydsbankingapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.GregorianCalendar;



public class AccountsFragment extends Fragment {

    private ListView lv;
    private OnFragmentInteractionListener mListener;
    private String name = "John";
    private int balance = 120;
    private GregorianCalendar c;

    public AccountsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setupMenu();
        String[] items = {"fsdfsd","hgfkghkgh","aghgfjkhgj","jgjghfsd","hdgjhfgja",
                "fsdfsd","hgfkghkgh","aghgfjkhgj","jgjghfsd","hdgjhfgja",
                "fsdfsd","hgfkghkgh","aghgfjkhgj","jgjghfsd","hdgjhfgja",
                "fsdfsd","hgfkghkgh","aghgfjkhgj","jgjghfsd","hdgjhfgja"};

        c = new GregorianCalendar();

        View v = inflater.inflate(R.layout.fragment_accounts, container, false);
        lv = (ListView) v.findViewById(R.id.transaction_listview);
        lv.setAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, items));
        setupWelcomeScreen(v);
        return v;
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
