package uk.ac.ncl.cs.team16.lloydsbankingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import uk.ac.ncl.cs.team16.lloydsbankingapp.R;

/**
 * Custom navigation drawer listview adapter
 * @author Aleksander Antoniewicz
 * @version 1.0
 *
 */
public class NavigationAdapter extends BaseAdapter {
    private Context context;
    private String[] navDrawerElements;
    private int[] icons= {R.drawable.ic_accounts, R.drawable.ic_reviewpayment, R.drawable.ic_transfer,
                R.drawable.ic_findatm, R.drawable.ic_dictionary, R.drawable.ic_help, R.drawable.gift};

    public NavigationAdapter(Context context){
        navDrawerElements = context.getResources().getStringArray(R.array.nav_drawer_elements);
        this.context = context;
    }

    @Override
    public int getCount() {
        return navDrawerElements.length;
    }

    @Override
    public Object getItem(int i) {
        return navDrawerElements[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row;
        if (view==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.navdrawer_row, viewGroup, false);
        } else {
            row = view;
        }

        TextView titleTV = (TextView) row.findViewById(R.id.nav_drawer_tv);
        ImageView titleIV = (ImageView) row.findViewById(R.id.nav_drawer_iv);

        titleTV.setText(navDrawerElements[i]);
        titleIV.setImageResource(icons[i]);
        return row;
    }
}
