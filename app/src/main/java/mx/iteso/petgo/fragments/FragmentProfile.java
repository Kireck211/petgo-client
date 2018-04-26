package mx.iteso.petgo.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewSwitcher;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import mx.iteso.petgo.ActivityAddPet;
import mx.iteso.petgo.ActivityLogin;
import mx.iteso.petgo.ActivityMain;
import mx.iteso.petgo.R;
import mx.iteso.petgo.adapters.AdapterAddress;
import mx.iteso.petgo.adapters.AdapterPet;
import mx.iteso.petgo.adapters.AdapterPhone;
import mx.iteso.petgo.beans.Address;
import mx.iteso.petgo.beans.Pet;
import mx.iteso.petgo.beans.Phone;
import mx.iteso.petgo.beans.User;
import mx.iteso.petgo.databinding.FragmentProfileBinding;

import static mx.iteso.petgo.utils.Constants.ADD_PET;
import static mx.iteso.petgo.utils.Constants.FACEBOOK_PROVIDER;
import static mx.iteso.petgo.utils.Constants.GOOGLE_PROVIDER;
import static mx.iteso.petgo.utils.Constants.PARCELABLE_USER;
import static mx.iteso.petgo.utils.Constants.USER_PREFERENCES;

public class FragmentProfile extends Fragment {
    private FragmentProfileBinding mBinding;
    private User mUser;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseDatabase database;
    private AdapterPet mAdapterPet;
    private AdapterPhone mAdapterPhone;
    private AdapterAddress mAdapterAddress;
    private List<Pet> pets;
    private List<Phone> phones;
    private List<Address> address;

    public static FragmentProfile newInstance(User user) {
        FragmentProfile fragmentProfile = new FragmentProfile();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCELABLE_USER, user);
        fragmentProfile.setArguments(bundle);

        return fragmentProfile;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        mUser = getArguments().getParcelable(PARCELABLE_USER);

        database = FirebaseDatabase.getInstance();
        DatabaseReference petRef = database.getReference("users/" + mUser.getKeyDatabase() + "/pets");
        petRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                    return;
                pets = new ArrayList<>();
                Pet pet;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    pet = snapshot.getValue(Pet.class);
                    pets.add(pet);
                }
                mAdapterPet.swapItems(pets);
                selectPetView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference phoneRef = database.getReference("users/" + mUser.getKeyDatabase() + "/phone");
        phoneRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                    return;
                phones = new ArrayList<>();
                Phone phone;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    phone = snapshot.getValue(Phone.class);
                    phones.add(phone);
                }
                mAdapterPhone.swapItems(phones);
                selectPhoneView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference addressRef = database.getReference("users" + mUser.getKeyDatabase() + "/address");
        addressRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                    return;
                address = new ArrayList<>();
                Address addr;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    addr = snapshot.getValue(Address.class);
                    address.add(addr);
                }
                mAdapterAddress.swapItems(address);
                selectAddressView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        LinearLayoutManager layoutAddress = new LinearLayoutManager(getActivity());
        LinearLayoutManager layoutPhone = new LinearLayoutManager(getActivity());
        LinearLayoutManager layoutPet = new LinearLayoutManager(getActivity());

        setDataLists();

        RecyclerView rvAddress = mBinding.rvAddressProfile;
        RecyclerView rvPhone = mBinding.rvPhoneProfile;
        RecyclerView rvPet = mBinding.rvPetProfile;

        rvAddress.setLayoutManager(layoutAddress);
        rvPhone.setLayoutManager(layoutPhone);
        rvPet.setLayoutManager(layoutPet);

        setAdapters();

        rvAddress.setAdapter(mAdapterAddress);
        rvPhone.setAdapter(mAdapterPhone);
        rvPet.setAdapter(mAdapterPet);

        Uri uri = Uri.parse(mUser.getPicture());
        mBinding.ivUserProfile.setImageURI(uri);
        mBinding.tvRatingProfile.setText(String.valueOf(mUser.getRating()));
        mBinding.tvUserNameProfile.setText(mUser.getName());

        mBinding.btnLogOutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_log_out_profile:
                        signOut();
                        break;
                }
            }
        });

        mBinding.fabProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityAddPet.class);
                intent.putExtra(PARCELABLE_USER, mUser);
                ((ActivityMain) getContext()).startActivityForResult(intent, ADD_PET);
            }
        });

        selectPetView();
        selectPhoneView();

        return mBinding.getRoot();
    }

    private void signOut() {
        final String provider = mUser.getProvider();
        if (provider.equals(FACEBOOK_PROVIDER)) {
            LoginManager.getInstance().logOut();
            signOutCallback();
        } else if (provider.equals(GOOGLE_PROVIDER)) {
            mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    signOutCallback();
                }
            });
        }
    }

    private void signOutCallback() {
        FirebaseAuth.getInstance().signOut();
        deleteUser();
        Intent intent = new Intent(getContext(), ActivityLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        ((ActivityMain) getContext()).finish();
    }

    private void deleteUser() {
        SharedPreferences sharedPreferences =
                this.getActivity().getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void setDataLists() {
        if (pets == null) {
            pets = new ArrayList<>();
        }
        if (phones == null) {
            phones = new ArrayList<>();
        }
        if (address == null) {
            address = new ArrayList<>();
        }
    }

    private void setAdapters() {
        if (mAdapterPet == null) {
            mAdapterPet = new AdapterPet(pets);
        }
        if (mAdapterPhone == null) {
            mAdapterPhone = new AdapterPhone(phones);
        }
        if (mAdapterAddress == null) {
            mAdapterAddress = new AdapterAddress(address);
        }
    }

    private void selectPetView() {
        ViewSwitcher petSwitcher = mBinding.vsPetProfile;
        if (pets.size() > 0) {
            petSwitcher.setDisplayedChild(1);
        } else {
            petSwitcher.setDisplayedChild(0);
        }
    }

    private void selectPhoneView() {
        ViewSwitcher phoneSwitcher = mBinding.vsPhoneProfile;
        if (phones.size() > 0) {
            phoneSwitcher.setDisplayedChild(1);
        } else {
            phoneSwitcher.setDisplayedChild(0);
        }
    }

    private void selectAddressView() {
        ViewSwitcher addressSwitcher = mBinding.vsAddressProfile;
        if (address.size() > 0) {
            addressSwitcher.setDisplayedChild(1);
        } else {
            addressSwitcher.setDisplayedChild(0);
        }
    }
}
