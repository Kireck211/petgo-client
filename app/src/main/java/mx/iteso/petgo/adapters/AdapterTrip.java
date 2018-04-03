package mx.iteso.petgo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mx.iteso.petgo.beans.Trip;

public class AdapterTrip extends RecyclerView.Adapter<AdapterTrip.ViewHolder> {
    private ArrayList<Trip> mTrips;

    public AdapterTrip(ArrayList<Trip> trips) {
        mTrips = trips;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
