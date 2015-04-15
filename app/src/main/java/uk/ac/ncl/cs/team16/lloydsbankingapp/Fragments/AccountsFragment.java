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

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import uk.ac.ncl.cs.team16.lloydsbankingapp.Models.Account;
import uk.ac.ncl.cs.team16.lloydsbankingapp.R;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.AuthHandler;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.DefaultErrorListener;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.JsonArrayPostRequest;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.VolleySingleton;
import uk.ac.ncl.cs.team16.lloydsbankingapp.Models.Transaction;

public class AccountsFragment extends Fragment {

	private OnFragmentInteractionListener mListener;
	private GregorianCalendar calendar;
	private List<Transaction> transactionList;
	public static List<Account> accountList;
    public static List<String> accountNames;
	private ListView transcationLv;
	private Spinner spinner;
    private View overallView;

	private static final String ACCOUNTS_URL_BASE = "http://csc2022api.sitedev9.co.uk/account";

	public AccountsFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		transactionList = new ArrayList<Transaction>();
		setupMenu();

		calendar = new GregorianCalendar();

		View accountView = inflater.inflate(R.layout.fragment_accounts, container, false);
		transcationLv = (ListView) accountView.findViewById(R.id.transaction_listview);
        overallView = accountView;
		setupSpinner();
		return accountView;
	}

	private void assembleSpinner() {
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.actionbar_spinner_item, accountNames);
		spinner.setAdapter(spinnerAdapter);
		spinnerAdapter.notifyDataSetChanged();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setupWelcomeScreen(i);
                transactionsRequest(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
	}

	private void accountsRequest() {
		final AuthHandler authHandler = AuthHandler.getInstance();

		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		String requestString = authHandler.handleAuthentication(params);

        RequestQueue networkQueue = VolleySingleton.getInstance().getRequestQueue();
		JsonArrayPostRequest accountsArrayRequest = new JsonArrayPostRequest(ACCOUNTS_URL_BASE + "/summary", requestString, new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				accountList = new ArrayList<Account>();
                accountNames = new ArrayList<String>();

				for (int i = 0; i < response.length(); i++) {
					try {
						JSONObject accountJSONObject = response.getJSONObject(i);
						accountList.add(new Account(accountJSONObject.getString("AccountID"),
                       	 	accountJSONObject.getString("AccountName"),
                        	accountJSONObject.getString("AccountType"),
                        	accountJSONObject.getString("AccountBalance")));
					} catch (JSONException e) {

					}
				}

                for(Account a : accountList){
                    accountNames.add(a.getName());
                }
				assembleSpinner();
			}
		}, new DefaultErrorListener());
		networkQueue.add(accountsArrayRequest);
	}

	private void transactionsRequest(int index) {
        String accountID = accountList.get(index).getId();
		final AuthHandler authHandler = AuthHandler.getInstance();

        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        params.put("accountid", accountID);

        String requestString = authHandler.handleAuthentication(params);

		RequestQueue networkQueue = VolleySingleton.getInstance().getRequestQueue();
		JsonArrayPostRequest transArrayRequest = new JsonArrayPostRequest(ACCOUNTS_URL_BASE + "/transaction", requestString, new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				transactionList = new ArrayList<Transaction>();
				for (int i = 0; i < response.length(); i++) {
					try {
						JSONObject transactionObject = response.getJSONObject(i);
						String transDesc = transactionObject.getString("TransDesc");
						String transAmount = transactionObject.getString("TransAmout");
						JSONObject transDateObject = transactionObject.getJSONObject("TransDate");
						String transDate = transDateObject.getString("date");
						String accountBalance = transactionObject.getString("AccountBalance");

						transactionList.add(new Transaction(transDesc, transDate, accountBalance, transAmount));
					} catch (JSONException e) {

					}
				}

				transcationLv.setAdapter(new TransactionAdapter());
			}
		}, new DefaultErrorListener());
		networkQueue.add(transArrayRequest);
	}

	/**
	 * Set the spinner in the actionbar
	 */
	private void setupSpinner() {
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().getActionBar().setDisplayShowCustomEnabled(true);

		LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View spinnerLayout = inflator.inflate(R.layout.spinner_layout, null);
		spinner = (Spinner) spinnerLayout.findViewById(R.id.spin);
		getActivity().getActionBar().setCustomView(spinnerLayout);

		accountsRequest();
	}

	/**
	 * Custom adapter for the listview of transactions
	 *
	 * @author Aleksander Antoniewicz
	 * @version 1.0
	 */
	class TransactionAdapter extends ArrayAdapter<Transaction> {
		TransactionAdapter() {
			super(getActivity(), R.layout.row_transaction, R.id.payee_name, transactionList);
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
			balance.setText(transactionList.get(position).getBalance() + " GBP");
			value.setText(transactionList.get(position).getValue());
			return row;
		}
	}

	/**
	 * Setup the green welcome screen
	 *
	 *
	 */
	private void setupWelcomeScreen(int i) {
		TextView welcomeTV, balanceTV, dateTv;

		welcomeTV = (TextView) overallView.findViewById(R.id.welcome_tv);
		balanceTV = (TextView) overallView.findViewById(R.id.balance_tv);
		dateTv = (TextView) overallView.findViewById(R.id.date_tv);

		welcomeTV.setText("Welcome");
		balanceTV.setText("Current balance: £" + accountList.get(i).getBalance());
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
		public void onFragmentInteraction(Uri uri);
	}
}
