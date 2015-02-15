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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import uk.ac.ncl.cs.team16.lloydsbankingapp.Models.Payment;
import uk.ac.ncl.cs.team16.lloydsbankingapp.R;


public class ReviewFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private List<Payment> payments;

    public ReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View reviewView = inflater.inflate(R.layout.fragment_review, container, false); // This will work by using a spinner to inflate different fragments
        ListView paymentsListView = (ListView) reviewView.findViewById(R.id.payeeListView);
        TabHost tabHost = (TabHost) reviewView.findViewById(R.id.tabHost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("payees").setIndicator("Payees").setContent(R.id.payeeTab));
        tabHost.addTab(tabHost.newTabSpec("standingorders").setIndicator("Standing Orders").setContent(R.id.standingTab));
        tabHost.addTab(tabHost.newTabSpec("directdebits").setIndicator("Direct Debits").setContent(R.id.debitTab));

        payments = new ArrayList<Payment>();
        payments.add(new Payment(342, "Rob Robertson", new GregorianCalendar(2010, 05, 10), "33.00"));
        payments.add(new Payment(20, "Thomas Tommyson", new GregorianCalendar(2013, 03, 16), "11.57"));
        paymentsListView.setAdapter(new PaymentAdapter());

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
        PaymentAdapter(){
            super(getActivity(), R.layout.payment_row, R.id.paymentNumberText, payments);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View paymentRow = super.getView(position, convertView, parent);
            TextView paymentNumberTextView = (TextView) paymentRow.findViewById(R.id.paymentNumberText);
            TextView payeeTextView = (TextView) paymentRow.findViewById(R.id.payeeText);
            TextView payeeDateTextView = (TextView) paymentRow.findViewById(R.id.payeeDateText);
            DateFormat payeeDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            TextView paymentAmountText = (TextView) paymentRow.findViewById(R.id.paymentAmountText);

            paymentNumberTextView.setText("" + payments.get(position).getPaymentNumber());
            payeeTextView.setText(payments.get(position).getPayee());
            payeeDateTextView.setText(payeeDateFormat.format(payments.get(position).getDate().getTime()));
            paymentAmountText.setText("£" + payments.get(position).getAmount());
            return paymentRow;
        }
    }
}
