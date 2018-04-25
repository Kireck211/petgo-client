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

public class FragmentTrips extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trips, container, false);

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
