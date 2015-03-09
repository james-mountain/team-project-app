/**
 * Review payments. Maybe we have a vague idea!
 * @author Aleksander Antoniewicz, James Mountain
 * @version 0.0.0.0.1
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
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import uk.ac.ncl.cs.team16.lloydsbankingapp.Activities.HomeActivity;
import uk.ac.ncl.cs.team16.lloydsbankingapp.Models.Payment;
import uk.ac.ncl.cs.team16.lloydsbankingapp.R;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.JsonArrayPostRequest;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.VolleySingleton;


public class ReviewFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private List<Payment> payeePayments = new ArrayList<Payment>();
    private List<Payment> standingPayments = new ArrayList<Payment>();
    private List<Payment> debitPayments = new ArrayList<Payment>();
    private ListView paymentsListView;
    private ListView standingListView;
    private ListView debitListView;

    private static final String reviewURLBase = "http://csc2022api.sitedev9.co.uk/Account/Payee";

    public ReviewFragment() {
        // Required empty public constructor
    }

	private String obtainSessionID() {
		HomeActivity mainActivity = (HomeActivity) this.getActivity();
		return mainActivity.getSessionID();
	}

    private void populatePaymentList() {
		long curTime = new Date().getTime()/1000;
		int nonce = new Random().nextInt(900000) + 100000;

		Map<String, String> params = new LinkedHashMap<String, String>(); // Preserve order
		params.put("nonce", String.valueOf(nonce));
		params.put("timestamp", String.valueOf(curTime));

		String paramString = "";
		for (String param : params.keySet()) {
			paramString = paramString + param + "=" + params.get(param) + "&";
		}
		paramString = paramString.substring(0, paramString.length()-1); // Remove last ampersand.

		Log.d("paramstring", paramString);

		String signature = null;
		try {
			String key = "IgzW60g7VfCvgT8AuZG2tRRgqbx5Cfhj";
			SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");

			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(secretKeySpec);
			byte[] paramStringEncBytes = mac.doFinal(paramString.getBytes());

			signature = new String(Hex.encodeHex(paramStringEncBytes));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		Log.d("signature", signature);
		params.put("signature", signature);

		RequestQueue networkQueue = VolleySingleton.getInstance().getRequestQueue();
        JsonArrayPostRequest reviewArrayRequest = new JsonArrayPostRequest(reviewURLBase, new JSONObject(params), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
				Log.d("response", "response!");

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject payeeJSONObject = response.getJSONObject(i);
                        String payeeID = payeeJSONObject.getString("PayeeID");
                        String payeeName = payeeJSONObject.getString("PayeeName");
                        String payeeAmount = payeeJSONObject.getString("LastPaymentAmount");

						Log.d("iter", "iteration!");

                        payeePayments.add(new Payment(Integer.parseInt(payeeID), payeeName, new GregorianCalendar(2014, 01, 10), payeeAmount));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                paymentsListView.setAdapter(new PaymentAdapter(payeePayments, "Last: "));
                standingListView.setAdapter(new PaymentAdapter(standingPayments, "Next: "));
                debitListView.setAdapter(new PaymentAdapter(debitPayments, "Last: "));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // oops
				Log.d("error", error.getMessage());
            }
        }) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("API-SESSION-ID", obtainSessionID());
				return headers;
			}
		};
        networkQueue.add(reviewArrayRequest);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View reviewView = inflater.inflate(R.layout.fragment_review, container, false);
        paymentsListView = (ListView) reviewView.findViewById(R.id.payeeListView); // I could implement these as a list, but is it worth it?
        standingListView = (ListView) reviewView.findViewById(R.id.standingListView);
        debitListView = (ListView) reviewView.findViewById(R.id.debitListView);

        TabHost tabHost = (TabHost) reviewView.findViewById(R.id.tabHost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("payees").setIndicator("Payees").setContent(R.id.payeeTab));
        tabHost.addTab(tabHost.newTabSpec("standingorders").setIndicator("Standing Orders").setContent(R.id.standingTab));
        tabHost.addTab(tabHost.newTabSpec("directdebits").setIndicator("Direct Debits").setContent(R.id.debitTab));

        populatePaymentList();

        tabHost.setCurrentTab(0);
        return reviewView;
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private class PaymentAdapter extends ArrayAdapter<Payment> {
        private List<Payment> paymentArray;
        private String datePrefix = "";

        PaymentAdapter(List<Payment> paymentArray, String datePrefix){
            super(getActivity(), R.layout.payment_row, R.id.paymentNumberText, paymentArray);
            this.paymentArray = paymentArray;
            this.datePrefix = datePrefix;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View paymentRow = super.getView(position, convertView, parent);
            TextView paymentNumberTextView = (TextView) paymentRow.findViewById(R.id.paymentNumberText);
            TextView payeeTextView = (TextView) paymentRow.findViewById(R.id.payeeText);
            TextView payeeDateTextView = (TextView) paymentRow.findViewById(R.id.payeeDateText);
            DateFormat payeeDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            TextView paymentAmountText = (TextView) paymentRow.findViewById(R.id.paymentAmountText);

            paymentNumberTextView.setText("" + paymentArray.get(position).getPaymentNumber());
            payeeTextView.setText(paymentArray.get(position).getPayee());
            payeeDateTextView.setText(datePrefix + payeeDateFormat.format(paymentArray.get(position).getDate().getTime()));
            paymentAmountText.setText("£" + paymentArray.get(position).getAmount());
            return paymentRow;
        }
    }
}
