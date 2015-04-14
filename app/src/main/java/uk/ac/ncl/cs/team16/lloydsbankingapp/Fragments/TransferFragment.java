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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import uk.ac.ncl.cs.team16.lloydsbankingapp.R;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.AuthHandler;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.DefaultErrorListener;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.VolleySingleton;


public class TransferFragment extends Fragment {

    private static final String ACCOUNTS_URL_BASE = "http://csc2022api.sitedev9.co.uk/money/transfer";
    private OnFragmentInteractionListener mListener;
    private int from = -1;
    private int to = -1;
    private String amount;

    private TextView amountTV;
    public TransferFragment() {
        // Required empty public constructor
    }

    // TODO: Add tabs for the different transfers - import from current review fragment?

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_transfer, container, false);
        //setup the spinners
        final Spinner accountFromChoice = (Spinner) v.findViewById(R.id.account_from_choice);
        final Spinner accountToChoice = (Spinner) v.findViewById(R.id.account_to_choice);

        Button transferButton = (Button) v.findViewById(R.id.transfer_btn);

        amountTV = (TextView) v.findViewById(R.id.transfer_amount_textfield);

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
        String toAccount = AccountsFragment.accountList.get(to).getId();

        final AuthHandler authHandler = AuthHandler.getInstance();

        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        params.put("from_account", fromAccount);
        params.put("to_account", toAccount);
        params.put("amount", amount);

		String requestString = authHandler.handleAuthentication(params);

        RequestQueue networkQueue = VolleySingleton.getInstance().getRequestQueue();
        JsonObjectRequest transferRequest = new JsonObjectRequest(Request.Method.POST, ACCOUNTS_URL_BASE, requestString, new Response.Listener<JSONObject>() {
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
        }, new DefaultErrorListener()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("API-SESSION-ID", authHandler.obtainSessionID(getActivity()));
                return headers;
            }
        };
        networkQueue.add(transferRequest);
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
