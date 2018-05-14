package mx.iteso.petgo.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import mx.iteso.petgo.ActivityOnTrip;
import mx.iteso.petgo.R;
import mx.iteso.petgo.beans.MyLocation;
import mx.iteso.petgo.beans.Trip;
import mx.iteso.petgo.beans.User;
import mx.iteso.petgo.databinding.FragmentHomeBinding;
import mx.iteso.petgo.utils.Parser;

import static mx.iteso.petgo.utils.Constants.PARCELABLE_USER;
import static mx.iteso.petgo.utils.Constants.PERMISSION_LOCATION;

public class FragmentHome extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    private FragmentHomeBinding mBinding;
    private User mUser;
    private MapView mMapView;
    private GoogleMap mMap;
    protected int minimumDistanceBetweenUpdates = 20;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback mLocationCallback;
    private Location mLocation;
    private DatabaseReference mReference;
    private DatabaseReference mTackersReference;
    private ChildEventListener mListener;
    private ArrayList<User> mUsers;
    private Pair<Integer, User> mTacker;
    private String mTripId;

    public static FragmentHome newInstance(User user) {
        FragmentHome fragmentHome = new FragmentHome();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCELABLE_USER, user);
        fragmentHome.setArguments(bundle);

        return fragmentHome;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        mUser = (User) getArguments().getParcelable(PARCELABLE_USER);

        mMapView = mBinding.map;
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        mBinding.btnRequestTripHome.setOnClickListener(this);
        mBinding.btnScheduleTripHome.setOnClickListener(this);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setSmallestDisplacement(minimumDistanceBetweenUpdates);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                mLocation = locationResult.getLastLocation();
            }
        };

        mListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (mUsers != null && mUsers.size() > 0) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user == null) return;
                    user.setKeyDatabase(dataSnapshot.getKey());
                    if (user.getKeyDatabase().equals(mTacker.second.getKeyDatabase())) {
                        Trip trip = user.getTrips().get(mTripId);
                        if (trip != null) {
                            if (trip.getStatus().equals("active")) { // Start onTripView
                                Intent intent = new Intent(getActivity(), ActivityOnTrip.class);
                                intent.putExtra(PARCELABLE_USER, mUser);
                                startActivity(intent);
                            }
                        } else {
                            int newTakerIndex = mTacker.first + 1;
                            if (newTakerIndex < mUsers.size()) {
                                mTacker = new Pair<>(newTakerIndex, mUsers.get(newTakerIndex));
                                sendNotification(mTacker);
                            } else {
                                noTackersAvailable();
                            }
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mUsers = new ArrayList<>();

        mReference = FirebaseDatabase.getInstance().getReference();
        mTackersReference = mReference
                .child("users");

        return mBinding.getRoot();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("")
                        .setMessage("")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            PERMISSION_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_LOCATION);
            }
        } else {
            mMap.setMyLocationEnabled(true);
            startLocationUpdates();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED)
                        try {
                            mMap.setMyLocationEnabled(true);
                            startLocationUpdates();
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }
                }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        mTackersReference.addChildEventListener(mListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        stopLocationUpdates();
        if (mTackersReference != null) {
            mTackersReference.removeEventListener(mListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request_trip_home:
                requestTrip();
                break;
            case R.id.btn_schedule_trip_home:
                scheduleTrip();
                break;
        }
    }

    private void scheduleTrip() {

    }

    private void requestTrip() {
        Query query = mReference.child("users")
                .orderByChild("availability")
                .equalTo(true);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    User user;
                    try {
                        user = snapshot.getValue(User.class);
                        if (user == null)
                            continue;
                        if (user.getType().equals("tacker")) {
                            user.setKeyDatabase(snapshot.getKey());
                            mUsers.add(user);
                        }
                    } catch (DatabaseException e) {
                        e.printStackTrace();
                    }
                }
                if (mUsers.size() > 0) {
                    mTacker = new Pair<>(0, mUsers.get(0));
                    sendNotification(mTacker);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void startLocationUpdates() {
        try {
            mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, null);
        } catch (SecurityException e) {

        }
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void noTackersAvailable() {
        // TODO view
        // TODO reinitialize variables
    }

    private void sendNotification(Pair<Integer, User> mTacker) {
        String tripId = mReference.child("users/" + mTacker.second.getKeyDatabase() + "/trips").push().getKey();
        Trip trip = new Trip(150, Parser.parseFromDate(Calendar.getInstance().getTime()) , "notification", 90, "");
        Map<String, MyLocation> locationHashMap = new HashMap<>();
        final Location location = mLocation;
        if (location != null)
            locationHashMap.put("asdf234124kl", new MyLocation(location.getLatitude(), location.getLongitude()));
        else
            locationHashMap.put("asdf234124kl", new MyLocation(1234, 12345));
        trip.setAddress(locationHashMap);
        trip.setUser(mUser);
        mReference.child("users/"+ mTacker.second.getKeyDatabase()+"/trips").child(tripId).setValue(trip);
        mTripId = tripId;
    }
}
