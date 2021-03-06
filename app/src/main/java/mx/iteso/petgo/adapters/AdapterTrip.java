package mx.iteso.petgo.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mx.iteso.petgo.R;
import mx.iteso.petgo.beans.Trip;
import mx.iteso.petgo.databinding.ItemTripBinding;

public class AdapterTrip extends RecyclerView.Adapter<AdapterTrip.ViewHolder> {
    private List<Trip> mTrips;
    private ItemTripBinding mBinding;

    public AdapterTrip(List<Trip> trips) {
        mTrips = trips;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        mBinding = DataBindingUtil.inflate(inflater, R.layout.item_trip, parent, false);
        return new ViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Trip trip = mTrips.get(position);
        mBinding.tvCostTrip.setText(String.valueOf(trip.getAmount()));
        mBinding.tvTackerNameTrip.setText(trip.getUser().getName());
        mBinding.tvDateTrip.setText(trip.getDate_hour().toString());
    }

    @Override
    public int getItemCount() {
        if (mTrips == null)
            return 0;
        return mTrips.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
