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

import java.util.ArrayList;
import java.util.List;

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
import mx.iteso.petgo.utils.Constants;

import static mx.iteso.petgo.utils.Constants.FACEBOOK_PROVIDER;
import static mx.iteso.petgo.utils.Constants.GOOGLE_PROVIDER;
import static mx.iteso.petgo.utils.Constants.PARCELABLE_USER;
import static mx.iteso.petgo.utils.Constants.USER_PREFERENCES;

public class FragmentProfile extends Fragment {
    private FragmentProfileBinding mBinding;
    private User mUser;
    private GoogleSignInClient mGoogleSignInClient;

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

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        mUser = getArguments().getParcelable(PARCELABLE_USER);

        LinearLayoutManager layoutAddress = new LinearLayoutManager(getActivity());
        LinearLayoutManager layoutPhone = new LinearLayoutManager(getActivity());
        LinearLayoutManager layoutPet = new LinearLayoutManager(getActivity());

        List<Address> address = new ArrayList<>();
        List<Phone> phones = new ArrayList<>();
        List<Pet> pets = new ArrayList<>();

        RecyclerView rvAddress = mBinding.rvAddressProfile;
        RecyclerView rvPhone = mBinding.rvPhoneProfile;
        RecyclerView rvPet = mBinding.rvPetProfile;

        rvAddress.setLayoutManager(layoutAddress);
        rvPhone.setLayoutManager(layoutPhone);
        rvPet.setLayoutManager(layoutPet);

        AdapterAddress adapterAddress = new AdapterAddress(address);
        AdapterPhone adapterPhone = new AdapterPhone(phones);
        AdapterPet adapterPet = new AdapterPet(pets);

        rvAddress.setAdapter(adapterAddress);
        rvPhone.setAdapter(adapterPhone);
        rvPet.setAdapter(adapterPet);

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
}
