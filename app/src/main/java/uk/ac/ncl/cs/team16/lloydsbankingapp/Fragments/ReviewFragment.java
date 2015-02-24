/**
 * Review payments. Maybe we have a vague idea!
 * @author Aleksander Antoniewicz, James Mountain
 * @version 0.0.0.0.1
 */

package uk.ac.ncl.cs.team16.lloydsbankingapp.fragments;

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

import uk.ac.ncl.cs.team16.lloydsbankingapp.models.Payment;
import uk.ac.ncl.cs.team16.lloydsbankingapp.R;


public class ReviewFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private List<Payment> payeePayments;
    private List<Payment> standingPayments;
    private List<Payment> debitPayments;

    public ReviewFragment() {
        // Required empty public constructor
    }

    private void populatePaymentList() {
        payeePayments = new ArrayList<Payment>();
        payeePayments.add(new Payment(20, "Thomas Tommyson", new GregorianCalendar(2010, 03, 16), "11.57"));
        payeePayments.add(new Payment(34, "Rob Robertson", new GregorianCalendar(2013, 05, 10), "33.00"));
        payeePayments.add(new Payment(60, "Simon Simmonson", new GregorianCalendar(2014, 11, 03), "21.93"));

        standingPayments = new ArrayList<Payment>();
        standingPayments.add(new Payment(21, "Mr asdasda", new GregorianCalendar(2014, 01, 10), "3.90"));
        standingPayments.add(new Payment(99, "Company 123", new GregorianCalendar(2015, 02, 12), "51.00"));
        standingPayments.add(new Payment(34, "Mr fgsdhs", new GregorianCalendar(2011, 10, 03), "105.50"));

        debitPayments = new ArrayList<Payment>();
        debitPayments.add(new Payment(121, "Electric Bill Company", new GregorianCalendar(2006, 07, 13), "99.31"));
        debitPayments.add(new Payment(131, "Student Finance 9912", new GregorianCalendar(2014, 11, 25), "410.00"));
        debitPayments.add(new Payment(159, "Newcastle University", new GregorianCalendar(2013, 06, 22), "899.99"));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View reviewView = inflater.inflate(R.layout.fragment_review, container, false);
        ListView paymentsListView = (ListView) reviewView.findViewById(R.id.payeeListView); // I could implement these as a list, but is it worth it?
        ListView standingListView = (ListView) reviewView.findViewById(R.id.standingListView);
        ListView debitListView = (ListView) reviewView.findViewById(R.id.debitListView);

        final TabHost tabHost = (TabHost) reviewView.findViewById(R.id.tabHost);
        tabHost.setup();
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    View tabWidgetChild = tabHost.getTabWidget().getChildAt(i);
                    TextView tabWidgetChildText = (TextView) tabWidgetChild.findViewById(android.R.id.title);
                    tabWidgetChild.setBackgroundColor(getResources().getColor(R.color.almost_white));
                    tabWidgetChildText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                }

                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(getResources().getColor(R.color.lloyds_green));
            }
        });

        tabHost.addTab(tabHost.newTabSpec("payees").setIndicator("Payees").setContent(R.id.payeeTab));
        tabHost.addTab(tabHost.newTabSpec("standingorders").setIndicator("Standing Orders").setContent(R.id.standingTab));
        tabHost.addTab(tabHost.newTabSpec("directdebits").setIndicator("Direct Debits").setContent(R.id.debitTab));

        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            View tabWidgetChild = tabHost.getTabWidget().getChildAt(i);
            TextView tabWidgetChildText = (TextView) tabWidgetChild.findViewById(android.R.id.title);
            tabWidgetChild.setBackgroundColor(getResources().getColor(R.color.almost_white));
            tabWidgetChildText.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
        tabHost.getTabWidget().getChildAt(0).setBackgroundColor(getResources().getColor(R.color.lloyds_green));

        populatePaymentList();

        paymentsListView.setAdapter(new PaymentAdapter(payeePayments, "Last: "));
        standingListView.setAdapter(new PaymentAdapter(standingPayments, "Next: "));
        debitListView.setAdapter(new PaymentAdapter(debitPayments, "Last: "));

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
