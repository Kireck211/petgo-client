package mx.iteso.petgo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import mx.iteso.petgo.beans.Trip;
import mx.iteso.petgo.beans.User;
import mx.iteso.petgo.databinding.ActivityMainBinding;
import mx.iteso.petgo.fragments.FragmentHome;
import mx.iteso.petgo.fragments.FragmentProfile;
import mx.iteso.petgo.fragments.FragmentTrips;

import static mx.iteso.petgo.utils.Constants.ADD_SCHEDULE_TRIP;
import static mx.iteso.petgo.utils.Constants.PARCELABLE_TRIP;
import static mx.iteso.petgo.utils.Constants.PARCELABLE_USER;

public class ActivityMain extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private Fragment mSelectedFragment;
    private FragmentHome mFragmentHome;
    private FragmentProfile mFragmentProfile;
    private FragmentTrips mFragmentTrips;
    private User mUser;

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            mSelectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_trips:
                    mSelectedFragment = getTripsInstance();
                    break;
                case R.id.navigation_petgo:
                    mSelectedFragment = getHomeInstance();
                    break;
                case R.id.navigation_profile:
                    mSelectedFragment = getProfileInstance();
                    break;
                default:
                    mSelectedFragment = getHomeInstance();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, mSelectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

         Intent intent = getIntent();
        if (intent != null) {
            mUser = intent.getParcelableExtra(PARCELABLE_USER);
        }

        BottomNavigationView navigation = mBinding.navigation;
        navigation.setOnNavigationItemSelectedListener(navListener);
        navigation.setSelectedItemId(R.id.navigation_petgo);

        if (savedInstanceState == null) {
            mFragmentHome = getHomeInstance();
            mSelectedFragment = mFragmentHome;
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, mSelectedFragment).commit();
        }
    }

    private FragmentHome getHomeInstance() {
        if (mFragmentHome == null)
            mFragmentHome = FragmentHome.newInstance(mUser);
        return mFragmentHome;
    }

    private FragmentProfile getProfileInstance() {
        if (mFragmentProfile == null)
            mFragmentProfile = FragmentProfile.newInstance(mUser);
        return mFragmentProfile;
    }

    private FragmentTrips getTripsInstance() {
        if (mFragmentTrips == null)
            mFragmentTrips = FragmentTrips.newInstance(mUser);
        return mFragmentTrips;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_SCHEDULE_TRIP:
                if (resultCode == RESULT_OK && data != null) {
                    Trip trip = data.getParcelableExtra(PARCELABLE_TRIP);
                    if (trip != null) {
                        onTripScheduled();
                    }
                } else {
                    // TODO Toast with error
                }
                break;
        }
    }

    private void onTripScheduled() {
        // TODO add toast
    }
}
