/**
 * Transfer funds to another account
 * @author Aleksander Antoniewicz
 * @version 0.5
 */

package uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

import uk.ac.ncl.cs.team16.lloydsbankingapp.Models.Payment;
import uk.ac.ncl.cs.team16.lloydsbankingapp.R;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.AuthHandler;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.DefaultErrorListener;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.JsonArrayPostRequest;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.JsonCustomObjectRequest;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.VolleySingleton;

public class TransferFragment extends Fragment {

    private static final String ACCOUNTS_URL_BASE = "http://csc2022api.sitedev9.co.uk/money";
    private static final String REVIEW_URL_BASE = "http://csc2022api.sitedev9.co.uk/account/payee";
    private OnFragmentInteractionListener mListener;
    private ArrayList<Payment> payees;
    private Spinner ppayeePayeeChoice;

    public TransferFragment() {
        // Required empty public constructor
    }

    private void colorTabs(TabHost tabHost) {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            View tabWidgetChild = tabHost.getTabWidget().getChildAt(i);
            TextView tabWidgetChildText = (TextView) tabWidgetChild.findViewById(android.R.id.title);
            tabWidgetChild.setBackgroundColor(getResources().getColor(R.color.almost_white));
            tabWidgetChildText.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }

        View tabWidgetChild = tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab());
        TextView tabWidgetChildText = (TextView) tabWidgetChild.findViewById(android.R.id.title);
        tabWidgetChild.setBackgroundColor(getResources().getColor(R.color.lloyds_green));
        tabWidgetChildText.setTextColor(getResources().getColor(android.R.color.white));
    }

    private void setupTabs(View transferView) {
        final TabHost tabHost = (TabHost) transferView.findViewById(R.id.tabHost);
        tabHost.setup();
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                colorTabs(tabHost);
            }
        });

        tabHost.addTab(tabHost.newTabSpec("transfer").setIndicator("Transfer Money").setContent(R.id.transfertab));
        tabHost.addTab(tabHost.newTabSpec("paypayee").setIndicator("Pay Payee").setContent(R.id.paypayeetab));
        tabHost.addTab(tabHost.newTabSpec("payaccount").setIndicator("Pay Account").setContent(R.id.payaccounttab));
        colorTabs(tabHost);

        tabHost.setCurrentTab(0);
    }

    private void payeesRequest() {
        AuthHandler authHandler = AuthHandler.getInstance();

        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        String requestString = authHandler.handleAuthentication(params);

        RequestQueue networkQueue = VolleySingleton.getInstance().getRequestQueue();
        JsonArrayPostRequest reviewArrayRequest = new JsonArrayPostRequest(REVIEW_URL_BASE, requestString, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                payees = new ArrayList<Payment>();
                ArrayList<String> payeesString = new ArrayList<String>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject payeeJSONObject = response.getJSONObject(i);
                        String payeeID = payeeJSONObject.getString("PayeeID");
                        String payeeName = payeeJSONObject.getString("PayeeName");
                        String payeeAmount = payeeJSONObject.getString("LastPaymentAmount");

                        payees.add(new Payment(Integer.parseInt(payeeID), payeeName, new GregorianCalendar(2014, 01, 10), payeeAmount));
                        payeesString.add(payeeName);
                    } catch (JSONException e) {

                    }
                }

                ppayeePayeeChoice.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, payeesString));
            }
        }, new DefaultErrorListener());
        networkQueue.add(reviewArrayRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View transferView = inflater.inflate(R.layout.fragment_transfer, container, false);

        Button transferButton = (Button) transferView.findViewById(R.id.transfer_btn);
        Button payPayeeButton = (Button) transferView.findViewById(R.id.ppayee_btn);
        Button payAccountButton = (Button) transferView.findViewById(R.id.pacc_btn);

        setupTabs(transferView);

        final Spinner accountFromChoice = (Spinner) transferView.findViewById(R.id.transfer_from_choice);
        final Spinner accountToChoice = (Spinner) transferView.findViewById(R.id.transfer_to_choice);
        final Spinner ppayeeFromChoice = (Spinner) transferView.findViewById(R.id.ppayee_from_choice);
        ppayeePayeeChoice = (Spinner) transferView.findViewById(R.id.ppayee_payee_choice);
        final Spinner paccFromChoice = (Spinner) transferView.findViewById(R.id.pacc_from_choice);

        ArrayAdapter<String> fromAccountAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, AccountsFragment.accountNames);
        accountFromChoice.setAdapter(fromAccountAdapter);
        accountToChoice.setAdapter(fromAccountAdapter);
        ppayeeFromChoice.setAdapter(fromAccountAdapter);
        paccFromChoice.setAdapter(fromAccountAdapter);

        payeesRequest();

        final TextView transferAmountField = (TextView) transferView.findViewById(R.id.transfer_amount_field);

        final TextView ppayeeRefField = (TextView) transferView.findViewById(R.id.ppayee_ref_field);
        final TextView ppayeeAmountField = (TextView) transferView.findViewById(R.id.ppayee_amount_field);

        final TextView paccSortField = (TextView) transferView.findViewById(R.id.pacc_sort_field);
        final TextView paccAccField = (TextView) transferView.findViewById(R.id.pacc_acc_field);
        final TextView paccRefField = (TextView) transferView.findViewById(R.id.pacc_ref_field);
        final TextView paccAmountField = (TextView) transferView.findViewById(R.id.pacc_amount_field);

        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int fromAccountPos = accountFromChoice.getSelectedItemPosition();
                int toAccountPos = accountToChoice.getSelectedItemPosition();

                String fromAccount = AccountsFragment.accountList.get(fromAccountPos).getId();
                String toAccount = AccountsFragment.accountList.get(toAccountPos).getId();
                String transferAmount = transferAmountField.getText().toString();

                transferMoney(fromAccount, toAccount, transferAmount);
            }
        });

        payPayeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int fromAccountPos = ppayeeFromChoice.getSelectedItemPosition();
                int payeePos = ppayeePayeeChoice.getSelectedItemPosition();

                String fromAccount = AccountsFragment.accountList.get(fromAccountPos).getId();
                String payee = Integer.toString(payees.get(payeePos).getPaymentNumber());
                String reference = ppayeeRefField.getText().toString();
                String transferAmount = ppayeeAmountField.getText().toString();

                payPayeeRequest(fromAccount, payee, reference, transferAmount);
            }
        });

        payAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int fromAccountPos = paccFromChoice.getSelectedItemPosition();

                String fromAccount = AccountsFragment.accountList.get(fromAccountPos).getId();
                String sortCode = paccSortField.getText().toString();
                String accountNo = paccAccField.getText().toString();
                String reference = paccRefField.getText().toString();
                String transferAmount = paccAmountField.getText().toString();

                payAccountRequest(fromAccount, sortCode, accountNo, reference, transferAmount);
            }
        });

        return transferView;
    }

    private void transferMoney(String fromAccount, String toAccount, String amount) {
        AuthHandler authHandler = AuthHandler.getInstance();

        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        params.put("from_account", fromAccount);
        params.put("to_account", toAccount);
        params.put("amount", amount);

		String requestString = authHandler.handleAuthentication(params);

        RequestQueue networkQueue = VolleySingleton.getInstance().getRequestQueue();
        JsonCustomObjectRequest transferRequest = new JsonCustomObjectRequest(ACCOUNTS_URL_BASE + "/transfer", requestString, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("Status") == 1) {
                        Toast.makeText(getActivity(), "Transfered money.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Failed.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {

                }
            }
        }, new DefaultErrorListener());
        networkQueue.add(transferRequest);
    }

	private void payPayeeRequest(String fromAccount, String payee, String reference, String amount) {
		AuthHandler authHandler = AuthHandler.getInstance();

		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put("from_account", fromAccount);
		params.put("payeeid", payee);
		params.put("reference", reference);
		params.put("amount", amount);

        Log.d("from_account", fromAccount);
        Log.d("payeeid", payee);
        Log.d("reference", reference);
        Log.d("amount", amount);

		String requestString = authHandler.handleAuthentication(params);

		RequestQueue networkQueue = VolleySingleton.getInstance().getRequestQueue();
		JsonCustomObjectRequest payPayeeRequest = new JsonCustomObjectRequest(ACCOUNTS_URL_BASE + "/pay/payee", requestString, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					if (response.getInt("Status") == 1) {
						Toast.makeText(getActivity(), "Transfered money to payee.", Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getActivity(), "Failed.", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
                    e.printStackTrace();
				}
			}
		}, new DefaultErrorListener());
		networkQueue.add(payPayeeRequest);
	}

	private void payAccountRequest(String account, String sortCode, String accountNo, String reference, String amount) {
		AuthHandler authHandler = AuthHandler.getInstance();

		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put("from_account", account);
		params.put("sort_code", sortCode);
		params.put("account_no", accountNo);
		params.put("reference", reference);
		params.put("amount", amount);

		String requestString = authHandler.handleAuthentication(params);

		RequestQueue networkQueue = VolleySingleton.getInstance().getRequestQueue();
		JsonCustomObjectRequest payPayeeRequest = new JsonCustomObjectRequest(ACCOUNTS_URL_BASE + "/pay/account", requestString, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					if (response.getInt("Status") == 1) {
						Toast.makeText(getActivity(), "Transfered money to account.", Toast.LENGTH_LONG).show();
                    } else {
						Toast.makeText(getActivity(), "Failed.", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
                    Log.d("ERROR", e.getMessage());
				}
			}
		}, new DefaultErrorListener());
		networkQueue.add(payPayeeRequest);
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

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }
}
