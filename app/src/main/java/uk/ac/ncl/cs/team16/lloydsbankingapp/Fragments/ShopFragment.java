package uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
        View rewardsView = inflater.inflate(R.layout.fragment_shop, container, false);

        ListView yourRewardsView = (ListView) rewardsView.findViewById(R.id.yourRewardsListView);
        ListView availableRewardsView = (ListView) rewardsView.findViewById(R.id.availableRewardsListView);

        populateAvailableRewards();
        populateYourRewards();

        //Apply the 'your rewards' list to the interface
        yourRewardsView.setAdapter(new RewardAdapter(yourRewards));
        availableRewardsView.setAdapter(new RewardAdapter(availableRewards));

        return rewardsView;
    }

    private void populateYourRewards() {

        yourRewards.add(new Reward("Tesco Voucher - £20","5asdf0923klj42".toUpperCase(),1500));
    }

    private void populateAvailableRewards() {


        availableRewards.add(new Reward("Tesco Voucher - £20","",1000));
        availableRewards.add(new Reward("2 small pizzas for 1 at Dominoes","",2000));
    }

    private class RewardAdapter extends ArrayAdapter<Reward> {

        private List<Reward> rewardsSet;

        //Adapter constructor
        RewardAdapter(List<Reward> rewardsSet){

            super(getActivity(), R.layout.row_reward, R.id.rewardName, rewardsSet);
            this.rewardsSet = rewardsSet;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //Connects each detail of the achievement object with the relevant section of the achievement row
            View entryRow = super.getView(position, convertView, parent);
            TextView entryName = (TextView) entryRow.findViewById(R.id.rewardName);
            TextView entryDesc = (TextView) entryRow.findViewById(R.id.rewardCode);
            TextView entryValue = (TextView) entryRow.findViewById(R.id.rewardCost);

            entryName.setText(rewardsSet.get(position).getName());
            entryDesc.setText(rewardsSet.get(position).getCode());
            entryValue.setText(rewardsSet.get(position).getCost() + "pts");

            return entryRow;
        }
    }

}
