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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;

import uk.ac.ncl.cs.team16.lloydsbankingapp.Activities.HomeActivity;
import uk.ac.ncl.cs.team16.lloydsbankingapp.Models.Payment;
import uk.ac.ncl.cs.team16.lloydsbankingapp.R;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.AuthHandler;
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
		Map<String, String> params = AuthHandler.handleAuthentication(null);

		RequestQueue networkQueue = VolleySingleton.getInstance().getRequestQueue();
        JsonArrayPostRequest reviewArrayRequest = new JsonArrayPostRequest(reviewURLBase, new JSONObject(params), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject payeeJSONObject = response.getJSONObject(i);
                        String payeeID = payeeJSONObject.getString("PayeeID");
                        String payeeName = payeeJSONObject.getString("PayeeName");
                        String payeeAmount = payeeJSONObject.getString("LastPaymentAmount");

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

    // TODO: Add new payee button. Will require a new activity.
    // TODO: That payee activity will require JSON request too
    // TODO: Probably need to add the account spinner, since multiple accounts have different payees

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View reviewView = inflater.inflate(R.layout.fragment_review, container, false);
        paymentsListView = (ListView) reviewView.findViewById(R.id.payeeListView); // I could implement these as a list, but is it worth it?
        standingListView = (ListView) reviewView.findViewById(R.id.standingListView);
        debitListView = (ListView) reviewView.findViewById(R.id.debitListView);

        final TabHost tabHost = (TabHost) reviewView.findViewById(R.id.tabHost);
        tabHost.setup();
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                colorTabs(tabHost);
            }
        });

        tabHost.addTab(tabHost.newTabSpec("payees").setIndicator("Payees").setContent(R.id.payeeTab));
        tabHost.addTab(tabHost.newTabSpec("standingorders").setIndicator("Standing Orders").setContent(R.id.standingTab));
        tabHost.addTab(tabHost.newTabSpec("directdebits").setIndicator("Direct Debits").setContent(R.id.debitTab));

        colorTabs(tabHost);
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
        private final List<Payment> paymentArray;
        private String datePrefix = "";

        PaymentAdapter(List<Payment> paymentArray, String datePrefix){
            super(getActivity(), R.layout.payment_row, R.id.paymentNumberText, paymentArray);
            this.paymentArray = paymentArray;
            this.datePrefix = datePrefix;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View paymentRow = super.getView(position, convertView, parent);
            TextView pytNumberText = (TextView) paymentRow.findViewById(R.id.paymentNumberText);
            TextView pytPayeeText = (TextView) paymentRow.findViewById(R.id.payeeText);
            TextView pytDateText = (TextView) paymentRow.findViewById(R.id.payeeDateText);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
            TextView pytAmountText = (TextView) paymentRow.findViewById(R.id.paymentAmountText);

            pytNumberText.setText("" + paymentArray.get(position).getPaymentNumber());
            pytPayeeText.setText(paymentArray.get(position).getPayee());
            pytDateText.setText(datePrefix + dateFormat.format(paymentArray.get(position).getDate().getTime()));
            pytAmountText.setText("£" + paymentArray.get(position).getAmount());
            return paymentRow;
        }
    }
}
