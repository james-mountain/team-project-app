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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

import uk.ac.ncl.cs.team16.lloydsbankingapp.R;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.AuthHandler;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.DefaultErrorListener;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.JsonCustomObjectRequest;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.VolleySingleton;

public class TransferFragment extends Fragment {

    private static final String ACCOUNTS_URL_BASE = "http://csc2022api.sitedev9.co.uk/money";
    private OnFragmentInteractionListener mListener;
    private int from = -1;
    private int to = -1;
    private String amount;
    private TextView amountTV;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_transfer, container, false);
        //setup the spinners
        final Spinner accountFromChoice = (Spinner) v.findViewById(R.id.transfer_from_choice);
        final Spinner accountToChoice = (Spinner) v.findViewById(R.id.transfer_to_choice);

        Button transferButton = (Button) v.findViewById(R.id.transfer_btn);

        final TabHost tabHost = (TabHost) v.findViewById(R.id.tabHost);
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

        amountTV = (TextView) v.findViewById(R.id.transfer_amount_field);

        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = amountTV.getText().toString();
                transferMoney();
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, AccountsFragment.accountNames);
        accountFromChoice.setAdapter(arrayAdapter);
        accountToChoice.setAdapter(arrayAdapter);

        accountFromChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                from = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        accountToChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                to = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return v;
    }

    private void transferMoney() {
        String fromAccount = AccountsFragment.accountList.get(from).getId();
        final String toAccount = AccountsFragment.accountList.get(to).getId();

        final AuthHandler authHandler = AuthHandler.getInstance();

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
                        Toast.makeText(getActivity(), "Transfered money to " + toAccount + ".", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Failed.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {

                }
            }
        }, new DefaultErrorListener());
        networkQueue.add(transferRequest);
    }

	private void payPayeeRequest() {
		String fromAccount = AccountsFragment.accountList.get(from).getId();
		String payeeId = "32";
		String paymentReference = "Test Payee Pay";

		final AuthHandler authHandler = AuthHandler.getInstance();

		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put("from_account", fromAccount);
		params.put("payeeid", payeeId);
		params.put("reference", paymentReference);
		params.put("amount", amount);

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

				}
			}
		}, new DefaultErrorListener());
		networkQueue.add(payPayeeRequest);
	}

	private void payAccountRequest() {
		String fromAccount = AccountsFragment.accountList.get(from).getId();
		String sortCode = "3312432";
		final String accountNo = "42325266";
		String paymentReference = "Test Account Pay";

		final AuthHandler authHandler = AuthHandler.getInstance();

		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put("from_account", fromAccount);
		params.put("sort_code", sortCode);
		params.put("account_no", accountNo);
		params.put("reference", paymentReference);
		params.put("amount", amount);

		String requestString = authHandler.handleAuthentication(params);

		RequestQueue networkQueue = VolleySingleton.getInstance().getRequestQueue();
		JsonCustomObjectRequest payPayeeRequest = new JsonCustomObjectRequest(ACCOUNTS_URL_BASE + "/pay/payee", requestString, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					if (response.getInt("Status") == 1) {
						Toast.makeText(getActivity(), "Transfered money to account no. " + accountNo + ".", Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getActivity(), "Failed.", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {

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
