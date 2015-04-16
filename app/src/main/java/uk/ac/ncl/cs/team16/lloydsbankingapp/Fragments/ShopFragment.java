package uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.ncl.cs.team16.lloydsbankingapp.Models.Reward;
import uk.ac.ncl.cs.team16.lloydsbankingapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends Fragment {

    private List<Reward> yourRewards = new ArrayList<Reward>();
    private List<Reward> availableRewards = new ArrayList<Reward>();
    private int pointsBalance = 0;

    public ShopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View achievementsView = inflater.inflate(R.layout.fragment_achievements, container, false);

        ListView yourRewards = (ListView) achievementsView.findViewById(R.id.yourRewardsListView);
        ListView availableRewards = (ListView) achievementsView.findViewById(R.id.availableRewardsListView);




        return achievementsView;
    }

    private void populateYourRewards() {


    }

    private void populateAvailableRewards() {


    }


}
