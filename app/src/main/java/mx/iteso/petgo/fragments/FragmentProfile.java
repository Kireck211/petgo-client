package mx.iteso.petgo.fragments;

import android.databinding.DataBindingUtil;
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
import mx.iteso.petgo.adapters.AdapterAddress;
import mx.iteso.petgo.adapters.AdapterPet;
import mx.iteso.petgo.adapters.AdapterPhone;
import mx.iteso.petgo.beans.Address;
import mx.iteso.petgo.beans.Pet;
import mx.iteso.petgo.beans.Phone;
import mx.iteso.petgo.databinding.FragmentProfileBinding;

public class FragmentProfile extends Fragment {
    private FragmentProfileBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        List<Address> address = new ArrayList<>();
        List<Phone> phones = new ArrayList<>();
        List<Pet> pets = new ArrayList<>();

        RecyclerView rvAddress = mBinding.rvAddressProfile;
        RecyclerView rvPhone = mBinding.rvPhoneProfile;
        RecyclerView rvPet = mBinding.rvPetProfile;

        rvAddress.setLayoutManager(linearLayoutManager);
        rvPhone.setLayoutManager(linearLayoutManager);
        rvPet.setLayoutManager(linearLayoutManager);

        AdapterAddress adapterAddress = new AdapterAddress(address);
        AdapterPhone adapterPhone = new AdapterPhone(phones);
        AdapterPet adapterPet = new AdapterPet(pets);

        rvAddress.setAdapter(adapterAddress);
        rvPhone.setAdapter(adapterPhone);
        rvPet.setAdapter(adapterPet);

        return mBinding.getRoot();
    }
}
