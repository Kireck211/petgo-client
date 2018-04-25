package mx.iteso.petgo.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mx.iteso.petgo.R;
import mx.iteso.petgo.adapters.AdapterTrip;
import mx.iteso.petgo.beans.Trip;
import mx.iteso.petgo.beans.User;
import mx.iteso.petgo.utils.Constants;

import static mx.iteso.petgo.utils.Constants.PARCELABLE_USER;

public class FragmentTrips extends Fragment {
    private User mUser;

    public static FragmentTrips newInstance (User user) {
        FragmentTrips fragmentTrips = new FragmentTrips();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCELABLE_USER, user);
        fragmentTrips.setArguments(bundle);

        return fragmentTrips;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trips, container, false);
        mUser = (User) getArguments().getParcelable(PARCELABLE_USER);

        RecyclerView recyclerView = rootView.findViewById(R.id.rv_fragment_trips);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        List<Trip> trips = new ArrayList<>();
        AdapterTrip adapterTrip = new AdapterTrip(trips);

        recyclerView.setAdapter(adapterTrip);

        return rootView;
    }
}
