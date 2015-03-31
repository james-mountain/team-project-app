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
import android.util.Log;
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
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import uk.ac.ncl.cs.team16.lloydsbankingapp.R;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.AuthHandler;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.JsonArrayPostRequest;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.VolleySingleton;
import uk.ac.ncl.cs.team16.lloydsbankingapp.Models.Transaction;

public class AccountsFragment extends Fragment {

	private OnFragmentInteractionListener mListener;
	private GregorianCalendar calendar;
	private List<Transaction> transactionList;
	private List<String> accountList;
	private ListView transcationLv;
	private Spinner spinner;

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

		setupWelcomeScreen(accountView);
		setupSpinner();
		transactionsRequest();

		return accountView;
	}

	private void assembleSpinner() {
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.actionbar_spinner_item, accountList);
		spinner.setAdapter(spinnerAdapter);
		spinnerAdapter.notifyDataSetChanged();
	}

	private void accountsRequest() {
		final AuthHandler authHandler = AuthHandler.getInstance();
		Map<String, String> params = authHandler.handleAuthentication(null);

		RequestQueue networkQueue = VolleySingleton.getInstance().getRequestQueue();
		JsonArrayPostRequest accountsArrayRequest = new JsonArrayPostRequest(ACCOUNTS_URL_BASE + "/summary", new JSONObject(params), new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				accountList = new ArrayList<String>();

				for (int i = 0; i < response.length(); i++) {
					try {
						JSONObject accountJSONObject = response.getJSONObject(i);
						String accountName = accountJSONObject.getString("AccountName");

						accountList.add(accountName);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				assembleSpinner();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("error", error.getMessage());
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("API-SESSION-ID", authHandler.obtainSessionID(getActivity()));
				return headers;
			}
		};
		networkQueue.add(accountsArrayRequest);
	}

	private void transactionsRequest() {
		final AuthHandler authHandler = AuthHandler.getInstance();
		Map<String, String> params = authHandler.handleAuthentication(null);

		RequestQueue networkQueue = VolleySingleton.getInstance().getRequestQueue();
		JsonArrayPostRequest transactionsArrayRequest = new JsonArrayPostRequest(ACCOUNTS_URL_BASE + "/transaction", new JSONObject(params), new Response.Listener<JSONArray>() {
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

						transactionList.add(new Transaction(transDesc, transDate, "1000", transAmount));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				transcationLv.setAdapter(new TransactionAdapter());
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("error", error.getMessage());
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("API-SESSION-ID", authHandler.obtainSessionID(getActivity()));
				return headers;
			}
		};
		networkQueue.add(transactionsArrayRequest);
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

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

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
	 *
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
