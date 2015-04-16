/**
 * Review payments. Maybe we have a vague idea!
 * @author Aleksander Antoniewicz, James Mountain
 * @version 0.0.0.0.1
 */

package uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import uk.ac.ncl.cs.team16.lloydsbankingapp.Activities.AddPayeeActivity;
import uk.ac.ncl.cs.team16.lloydsbankingapp.Models.Payment;
import uk.ac.ncl.cs.team16.lloydsbankingapp.R;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.AuthHandler;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.DefaultErrorListener;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.JsonArrayPostRequest;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.JsonCustomObjectRequest;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.VolleySingleton;


public class PayeesFragment extends Fragment {

    private List<Payment> payeePayments = new ArrayList<Payment>();
    private ListView paymentsListView;
	public static PayeesFragment payeesContext;

    private static final String REVIEW_URL_BASE = "http://csc2022api.sitedev9.co.uk/account/payee";

    public PayeesFragment() {
        // Required empty public constructor
    }

	private void reloadAdapters() {
		paymentsListView.setAdapter(new PaymentAdapter(payeePayments));
	}

    public void reviewPayeesRequest() {
		final AuthHandler authHandler = AuthHandler.getInstance();

		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		String requestString = authHandler.handleAuthentication(params);

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

                    }
                }

				reloadAdapters();
            }
        }, new DefaultErrorListener());
        networkQueue.add(reviewArrayRequest);
    }

	private void deletePayeeRequest(int payeeID) {
		final AuthHandler authHandler = AuthHandler.getInstance();

		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put("payeeid", Integer.toString(payeeID));

		String requestString = authHandler.handleAuthentication(params);

		RequestQueue networkQueue = VolleySingleton.getInstance().getRequestQueue();
		JsonCustomObjectRequest deleteRequest = new JsonCustomObjectRequest(REVIEW_URL_BASE + "/delete", requestString, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					if (response.getInt("Status") == 1) {
						Toast.makeText(getActivity(), "Payee deleted.", Toast.LENGTH_LONG).show();
						reviewPayeesRequest(); // This has to be done as a separate request, otherwise more parameters to this method
					} else {
						Toast.makeText(getActivity(), "Failed to delete payee.", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {

				}
			}
		}, new DefaultErrorListener());
		networkQueue.add(deleteRequest);
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		payeesContext = this;

        View reviewView = inflater.inflate(R.layout.fragment_payees, container, false);
        paymentsListView = (ListView) reviewView.findViewById(R.id.payeeListView); // I could implement these as a list, but is it worth it?

		registerForContextMenu(paymentsListView);
        reviewPayeesRequest();
        setHasOptionsMenu(true);


        return reviewView;
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
    public void onDetach() {
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		getActivity().getActionBar().setDisplayShowCustomEnabled(false);
        super.onDetach();
    }



    private class PaymentAdapter extends ArrayAdapter<Payment> {
        private final List<Payment> paymentArray;

        PaymentAdapter(List<Payment> paymentArray){
            super(getActivity(), R.layout.row_payment, R.id.paymentNumberText, paymentArray);
            this.paymentArray = paymentArray;
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
            pytDateText.setText("Last: " + dateFormat.format(paymentArray.get(position).getDate().getTime()));
            pytAmountText.setText("£" + paymentArray.get(position).getAmount());
            return paymentRow;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_payees, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_payee) {
            Intent addPayeeIntent = new Intent(getActivity(), AddPayeeActivity.class);
            startActivity(addPayeeIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
