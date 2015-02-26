package uk.ac.ncl.cs.team16.lloydsbankingapp.Activities;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments.MainFragment;
import uk.ac.ncl.cs.team16.lloydsbankingapp.R;

public class MainActivity extends Activity implements MainFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            MainFragment mf = new MainFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mf)
                    .commit();
        }


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
