package mx.iteso.petgo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

import mx.iteso.petgo.beans.User;
import mx.iteso.petgo.databinding.ActivityOnTripBinding;

import static mx.iteso.petgo.utils.Constants.PARCELABLE_USER;
import static mx.iteso.petgo.utils.Constants.PERMISSION_LOCATION;

public class ActivityOnTrip extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    private ActivityOnTripBinding mBinding;
    private User mUser;
    private MapView mMapView;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_on_trip);
        Intent intent = getIntent();
        if (intent != null) {
            mUser = intent.getParcelableExtra(PARCELABLE_USER);
        }

        mMapView = mBinding.mapOnTrip;
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        // TODO shared preferences onTrip
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.iv_phone_icon_on_trip:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"));
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }
}
