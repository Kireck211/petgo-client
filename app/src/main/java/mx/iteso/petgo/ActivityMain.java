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

import mx.iteso.petgo.beans.User;
import mx.iteso.petgo.databinding.ActivityMainBinding;
import mx.iteso.petgo.fragments.FragmentHome;
import mx.iteso.petgo.fragments.FragmentProfile;
import mx.iteso.petgo.fragments.FragmentTrips;

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
                case R.id.navigation_dashboard:
                    mSelectedFragment = getTripsInstance();
                    break;
                case R.id.navigation_home:
                    mSelectedFragment = getHomeInstance();
                    break;
                case R.id.navigation_notifications:
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

        BottomNavigationView navigation = mBinding.navigation;
        navigation.setOnNavigationItemSelectedListener(navListener);

        Intent intent = getIntent();
        if (intent != null) {
            mUser = intent.getParcelableExtra(PARCELABLE_USER);
        }

        if (savedInstanceState == null) {
            mFragmentHome = getHomeInstance();
            mSelectedFragment = mFragmentHome;
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, mSelectedFragment).commit();
        }
    }

    private FragmentHome getHomeInstance() {
        if (mFragmentHome == null)
            mFragmentHome = new FragmentHome();
        return mFragmentHome;
    }

    private FragmentProfile getProfileInstance() {
        if (mFragmentProfile == null)
            mFragmentProfile = new FragmentProfile();
        return mFragmentProfile;
    }

    private FragmentTrips getTripsInstance() {
        if (mFragmentTrips == null)
            mFragmentTrips = new FragmentTrips();
        return mFragmentTrips;
    }
}
