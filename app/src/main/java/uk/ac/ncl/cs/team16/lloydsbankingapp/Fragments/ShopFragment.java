package uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import uk.ac.ncl.cs.team16.lloydsbankingapp.Models.Voucher;
import uk.ac.ncl.cs.team16.lloydsbankingapp.R;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.AuthHandler;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.DefaultErrorListener;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.JsonArrayPostRequest;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.JsonCustomObjectRequest;
import uk.ac.ncl.cs.team16.lloydsbankingapp.network.VolleySingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends Fragment {

    private int pointsBalance = 0;
    private final String pound = "\u00A3";
    private List<Voucher> vouchers;
    private ListView yourVouchersView;
    private List<Voucher> availableVouchers;
    private ListView availableVouchersView;
    private TextView pointsTV;
    private static final String REWARDS_URL_BASE = "http://csc2022api.sitedev9.co.uk/rewards";



    public ShopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rewardsView = inflater.inflate(R.layout.fragment_shop, container, false);

        pointsTV = (TextView) rewardsView.findViewById(R.id.pointBalanceShop);
        yourVouchersView = (ListView) rewardsView.findViewById(R.id.yourRewardsListView);
        availableVouchersView = (ListView) rewardsView.findViewById(R.id.availableRewardsListView);

        //Apply the 'your rewards' list to the interface

        fetchPointsRequest();
        fetchVouchers();
        fetchAvailableVouchers();

        return rewardsView;
    }

    private void fetchVouchers() {
        final AuthHandler authHandler = AuthHandler.getInstance();
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        String requestString = authHandler.handleAuthentication(params);

        RequestQueue networkQueue = VolleySingleton.getInstance().getRequestQueue();
        JsonArrayPostRequest transArrayRequest = new JsonArrayPostRequest(REWARDS_URL_BASE + "/vouchers/purchased", requestString, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                vouchers = new ArrayList<Voucher>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        vouchers.add(new Voucher(object.getString("VoucherName"), object.getString("VoucherCode"), object.getInt("VoucherCost"), object.getInt("VoucherID")));
                    } catch (JSONException e) {
                        Toast.makeText(getActivity().getApplicationContext(), "Can't fetch available vouchers.", Toast.LENGTH_LONG).show();
                        Log.d("Vouchers exception: ", e.getMessage());
                    }
                }
                yourVouchersView.setAdapter(new VouchersAdapter(vouchers));
            }
        }, new DefaultErrorListener());
        networkQueue.add(transArrayRequest);
    }

    private void fetchAvailableVouchers() {
        final AuthHandler authHandler = AuthHandler.getInstance();
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        String requestString = authHandler.handleAuthentication(params);

        RequestQueue networkQueue = VolleySingleton.getInstance().getRequestQueue();
        JsonArrayPostRequest transArrayRequest = new JsonArrayPostRequest(REWARDS_URL_BASE + "/vouchers", requestString, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                availableVouchers = new ArrayList<Voucher>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        availableVouchers.add(new Voucher(object.getString("VoucherName"), object.getInt("VoucherCost"), object.getInt("VoucherID")));
                    } catch (JSONException e) {
                        Toast.makeText(getActivity().getApplicationContext(), "Can't fetch available vouchers.", Toast.LENGTH_LONG).show();
                        Log.d("Vouchers exception: ", e.getMessage());
                    }
                }
                availableVouchersView.setAdapter(new VouchersAdapter(availableVouchers));
            }
        }, new DefaultErrorListener());
        networkQueue.add(transArrayRequest);
    }

    private void fetchPointsRequest() {
        final AuthHandler authHandler = AuthHandler.getInstance();
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        String requestString = authHandler.handleAuthentication(params);
        RequestQueue networkQueue = VolleySingleton.getInstance().getRequestQueue();

        JsonCustomObjectRequest deleteRequest = new JsonCustomObjectRequest(REWARDS_URL_BASE + "/points", requestString, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("status") == 1) {
                        pointsTV.setText(response.getString("points"));
                    } else {
                        Toast.makeText(getActivity(), "Failed to fetch points", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.d("JSON parsing problem: ", e.getMessage());
                }
            }
        }, new DefaultErrorListener());
        networkQueue.add(deleteRequest);
    }


    private class VouchersAdapter extends ArrayAdapter<Voucher> {
        VouchersAdapter(List<Voucher> voucherList) {
            super(getActivity(), R.layout.row_reward, R.id.rewardName, voucherList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);
            TextView name = (TextView) row.findViewById(R.id.rewardName);
            TextView cost = (TextView) row.findViewById(R.id.rewardCost);
            TextView code = (TextView) row.findViewById(R.id.rewardCode);

            name.setText(getItem(position).getName());
            cost.setText(Integer.toString(getItem(position).getCost()));
            code.setText(getItem(position).getCode());

            return row;
        }
    }

}
