package uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        final View achievementsView = inflater.inflate(R.layout.fragment_achievements, container, false);
        final ListView achievementsListView = (ListView) achievementsView.findViewById(R.id.achievementsListView);

        populateAchievementsList();

        achievementsListView.setAdapter(new AchievementAdapter(achievements));

        // Inflate the layout for this fragment
        return achievementsView;
    }

    public void populateAchievementsList() {

        achievements.add(new Achievement("Login Regularly (weekly)", "Login at least once on five different days, each week", 10));
        achievements.add(new Achievement("Login Regularly (monthly)", "Obtain the weekly regular login award, four times in a row", 60));
    }

    private class AchievementAdapter extends ArrayAdapter<Achievement> {

        private final List<Achievement> achievementSet;

        AchievementAdapter(List<Achievement> achievementSet){
            super(getActivity(), R.layout.achievement_row, R.id.achievementName, achievementSet);
            this.achievementSet = achievementSet;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View entryRow = super.getView(position, convertView, parent);
            TextView entryName = (TextView) entryRow.findViewById(R.id.achievementName);
            TextView entryDesc = (TextView) entryRow.findViewById(R.id.achievementDescription);

            entryName.setText(achievementSet.get(position).getName());
            entryDesc.setText(achievementSet.get(position).getDescription());
            return entryRow;
        }
    }
}
