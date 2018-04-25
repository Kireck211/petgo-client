package mx.iteso.petgo.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import mx.iteso.petgo.R;
import mx.iteso.petgo.adapters.AdapterTrip;
import mx.iteso.petgo.beans.Trip;
import mx.iteso.petgo.beans.User;
import mx.iteso.petgo.databinding.FragmentHomeBinding;
import mx.iteso.petgo.utils.Constants;

import static mx.iteso.petgo.utils.Constants.PARCELABLE_USER;

public class FragmentHome extends Fragment {
    private FragmentHomeBinding mBinding;
    private User mUser;

    public static FragmentHome newInstance(User user) {
        FragmentHome fragmentHome = new FragmentHome();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCELABLE_USER, user);
        fragmentHome.setArguments(bundle);

        return fragmentHome;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        mUser = (User) getArguments().getParcelable(PARCELABLE_USER);

        return mBinding.getRoot();
    }
}
