package uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.ncl.cs.team16.lloydsbankingapp.Activities.ShopActivity;
import uk.ac.ncl.cs.team16.lloydsbankingapp.R;
import uk.ac.ncl.cs.team16.lloydsbankingapp.Models.Achievement;

/**
 * A simple {@link Fragment} subclass.
 */
public class AchievementsFragment extends Fragment {

    private List<Achievement> achievements = new ArrayList<Achievement>();

    public AchievementsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View achievementsView = inflater.inflate(R.layout.fragment_achievements, container, false);
        ListView achievementsListView = (ListView) achievementsView.findViewById(R.id.achievementsListView);
        setHasOptionsMenu(true);
        populateAchievementsList();

        //Apply the achievements list to the interface
        achievementsListView.setAdapter(new AchievementAdapter(achievements));

        //set onclick listener for the button leading to the rewards shop fragment
        Button button = (Button) achievementsView.findViewById(R.id.buttonToShop);

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {


            }
        });


        return achievementsView;
    }

    private void populateAchievementsList() {

        //Creates the achievements and adds them to the achievements list
        achievements.add(new Achievement("Login Regularly (weekly)", "Login at least once on five different days, each week", 10));
        achievements.add(new Achievement("Login Regularly (monthly)", "Obtain the weekly regular login award, four weeks in a row", 60));
        achievements.add(new Achievement("Make spending cuts", "This month, spending cuts in entertainment are suggested to ensure more money comes in than out", 20));
    }

    private class AchievementAdapter extends ArrayAdapter<Achievement> {

        private List<Achievement> achievementSet;

        //Adapter constructor
        AchievementAdapter(List<Achievement> achievementSet){
            super(getActivity(), R.layout.row_achievement, R.id.achievementName, achievementSet);
            this.achievementSet = achievementSet;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //Connects each detail of the achievement object with the relevant section of the achievement row
            View entryRow = super.getView(position, convertView, parent);
            TextView entryName = (TextView) entryRow.findViewById(R.id.achievementName);
            TextView entryDesc = (TextView) entryRow.findViewById(R.id.achievementDescription);
            TextView entryValue = (TextView) entryRow.findViewById(R.id.achievementValue);

            entryName.setText(achievementSet.get(position).getName());
            entryDesc.setText(achievementSet.get(position).getDescription());
            entryValue.setText(achievementSet.get(position).getPoints() + "pts");
            return entryRow;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_shop, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_shop) {
            Intent addPayeeIntent = new Intent(getActivity(), ShopActivity.class);
            startActivity(addPayeeIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
