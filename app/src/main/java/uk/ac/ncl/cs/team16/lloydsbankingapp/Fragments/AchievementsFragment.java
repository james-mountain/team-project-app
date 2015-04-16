package uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
}
