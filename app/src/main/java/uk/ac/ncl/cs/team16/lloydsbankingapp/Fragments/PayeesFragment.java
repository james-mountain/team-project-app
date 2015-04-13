/**
 * Review payments. Maybe we have a vague idea!
 * @author Aleksander Antoniewicz, James Mountain
 * @version 0.0.0.0.1
 */

package uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;

import uk.ac.ncl.cs.team16.lloydsbankingapp.Activities.AddPayeeActivity;
import uk.ac.ncl.cs.team16.lloydsbankingapp.Models.Payment;
import uk.ac.ncl.cs.team16.lloydsbankingapp.R;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.AuthHandler;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.JsonArrayPostRequest;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.VolleySingleton;


public class PayeesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private List<Payment> payeePayments = new ArrayList<Payment>();
    private List<Payment> standingPayments = new ArrayList<Payment>();
    private List<Payment> debitPayments = new ArrayList<Payment>();
    private ListView paymentsListView;
    //private ListView standingListView;
    //private ListView debitListView;

    private static final String REVIEW_URL_BASE = "http://csc2022api.sitedev9.co.uk/account/payee";

    public PayeesFragment() {
        // Required empty public constructor
    }

	private void reloadAdapters() {
		paymentsListView.setAdapter(new PaymentAdapter(payeePayments, "Last: "));
		//standingListView.setAdapter(new PaymentAdapter(standingPayments, "Next: "));
		//debitListView.setAdapter(new PaymentAdapter(debitPayments, "Last: "));
	}

    private void reviewPayeesRequest() {
		final AuthHandler authHandler = AuthHandler.getInstance();
		Map<String, String> params = authHandler.handleAuthentication(null);

        Gson gson = new Gson();
        String requestString = gson.toJson(params, LinkedHashMap.class);

        RequestQueue networkQueue = VolleySingleton.getInstance().getRequestQueue();
        JsonArrayPostRequest reviewArrayRequest = new JsonArrayPostRequest(REVIEW_URL_BASE, requestString, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
				payeePayments = new ArrayList<Payment>();

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

				reloadAdapters();
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
        networkQueue.add(reviewArrayRequest);
    }

	private void deletePayeeRequest(int payeeID) {
		final AuthHandler authHandler = AuthHandler.getInstance();

		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put("payeeid", Integer.toString(payeeID));

		authHandler.handleAuthentication(params);
		Gson gson = new Gson();
		String requestString = gson.toJson(params, LinkedHashMap.class);

		RequestQueue networkQueue = VolleySingleton.getInstance().getRequestQueue();
		JsonObjectRequest deleteRequest = new JsonObjectRequest(Request.Method.POST, REVIEW_URL_BASE + "/delete", requestString, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					if (response.getString("Status").equals("Success, payee deleted")) { // This is a mouthful to check for, it should just be success
						Toast.makeText(getActivity(), "Payee deleted.", Toast.LENGTH_LONG).show();

						reviewPayeesRequest(); // This has to be done as a separate request, otherwise more parameters to this method
					} else {
						Toast.makeText(getActivity(), "Failed to delete payee.", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
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
		networkQueue.add(deleteRequest);
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
        View reviewView = inflater.inflate(R.layout.fragment_payees, container, false);
        paymentsListView = (ListView) reviewView.findViewById(R.id.payeeListView); // I could implement these as a list, but is it worth it?
        //standingListView = (ListView) reviewView.findViewById(R.id.standingListView);
        //debitListView = (ListView) reviewView.findViewById(R.id.debitListView);

		registerForContextMenu(paymentsListView);
		addPayeeButtonSetup();

        /*
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
        */
        reviewPayeesRequest();

        //tabHost.setCurrentTab(0);
        return reviewView;
    }

	public void addPayeeButtonSetup() {
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().getActionBar().setDisplayShowCustomEnabled(true);

		LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View addLayout = inflator.inflate(R.layout.add_payee_layout, null);
		getActivity().getActionBar().setCustomView(addLayout);

		Button addPayeeButton = (Button) addLayout.findViewById(R.id.addPayeeButton);
		addPayeeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent addPayeeIntent = new Intent(getActivity(), AddPayeeActivity.class);
				startActivity(addPayeeIntent);
			}
		});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, view, menuInfo);
		if (view.getId() == R.id.payeeListView) {
			MenuInflater menuInflater = getActivity().getMenuInflater();
			menuInflater.inflate(R.menu.menu_review_item, menu);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		if (item.getItemId() == R.id.delete) {
			deletePayeeRequest(payeePayments.get(info.position).getPaymentNumber());
			return true;
		}

		return false;
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
