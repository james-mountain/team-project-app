package uk.ac.ncl.cs.team16.lloydsbankingapp.Activities;

import android.app.Activity;
import android.os.Bundle;

import uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments.ShopFragment;
import uk.ac.ncl.cs.team16.lloydsbankingapp.R;

public class ShopActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        if (savedInstanceState == null) {
            ShopFragment mf = new ShopFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mf)
                    .commit();
        }
    }
}
