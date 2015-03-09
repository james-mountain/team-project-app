/**
 * Display a proper fragment depending on the user choice in the nav drawer
 * @author Aleksander Antoniewicz
 * @version 1.0
 */

package uk.ac.ncl.cs.team16.lloydsbankingapp.Activities;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments.ATMFragment;
import uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments.AccountsFragment;
import uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments.DictionaryFragment;
import uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments.HelpFragment;
import uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments.NavigationDrawerFragment;
import uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments.ReviewFragment;
import uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments.TransferFragment;
import uk.ac.ncl.cs.team16.lloydsbankingapp.R;


public class HomeActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, AccountsFragment.OnFragmentInteractionListener,
        ReviewFragment.OnFragmentInteractionListener,  TransferFragment.OnFragmentInteractionListener,
        ATMFragment.OnFragmentInteractionListener,  DictionaryFragment.OnFragmentInteractionListener,
        HelpFragment.OnFragmentInteractionListener {

    private NavigationDrawerFragment mNavigationDrawerFragment;
	private String sessionID;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		Bundle intentExtras = getIntent().getExtras();
		if (intentExtras.isEmpty()) {
			finish();
		}
		sessionID = intentExtras.getString("session_id");
		if (sessionID == null) {
			finish();
		}

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getString(R.string.title_drawer_accounts);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing Fragments
        //THIS IS A CAVEMAN APPROACH AND SHALL BE CHANGED BEFORE FINAL SUBMISSION TO NESS
        Fragment fragment = null;
        FragmentManager fragmentManager = getFragmentManager();
        switch (position){
            case 0:
                fragment = new AccountsFragment();
                mTitle = getString(R.string.title_drawer_accounts);
                break;

            case 1:
                fragment = new ReviewFragment();
                mTitle = getString(R.string.title_drawer_review_payments);
                break;

            case 2:
                fragment = new TransferFragment();
                mTitle = getString(R.string.title_drawer_transfer);
                break;

            case 3:
                fragment = new ATMFragment();
                mTitle = getString(R.string.title_drawer_find_atm);
                break;

            case 4:
                fragment = new DictionaryFragment();
                mTitle = getString(R.string.title_drawer_dictionary);
                break;

            case 5:
                fragment = new HelpFragment();
                mTitle = getString(R.string.title_drawer_help);
                break;
            default:
                mTitle = getString(R.string.title_drawer_accounts);
                fragment = new AccountsFragment();
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

	public String getSessionID() {
		return sessionID;
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {

            getMenuInflater().inflate(R.menu.home, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
