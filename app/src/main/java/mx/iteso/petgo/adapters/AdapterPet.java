package mx.iteso.petgo.adapters;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import mx.iteso.petgo.R;
import mx.iteso.petgo.beans.Pet;
import mx.iteso.petgo.databinding.ItemPetBinding;

public class AdapterPet extends RecyclerView.Adapter<AdapterPet.ViewHolder>{
    private List<Pet> mPets;
    private ItemPetBinding mBinding;

    public AdapterPet(List<Pet> pets) {
        mPets = pets;
    }

    public void swapItems(List<Pet> pets) {
        mPets = pets;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        mBinding = DataBindingUtil.inflate(inflater, R.layout.item_pet, parent, false);
        return new ViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Pet pet = mPets.get(position);
        mBinding.tvNamePetItem.setText(pet.getName());
        mBinding.tvSizePetItem.setText(pet.getSize());
        mBinding.tvTypePetItem.setText(pet.getType());
        Uri uri = Uri.parse(pet.getPhoto());
        Picasso.get().load(uri).into(mBinding.ivPetItem);
    }

    @Override
    public int getItemCount() {
        if (mPets == null)
            return 0;
        return mPets.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
