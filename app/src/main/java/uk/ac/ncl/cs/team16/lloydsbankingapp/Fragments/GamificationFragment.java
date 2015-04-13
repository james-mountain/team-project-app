package uk.ac.ncl.cs.team16.lloydsbankingapp.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.ncl.cs.team16.lloydsbankingapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GamificationFragment extends Fragment {


    public GamificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gamification, container, false);
    }


}
