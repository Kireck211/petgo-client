package mx.iteso.petgo.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mx.iteso.petgo.R;
import mx.iteso.petgo.beans.Phone;
import mx.iteso.petgo.databinding.ItemPhoneBinding;

public class AdapterPhone extends RecyclerView.Adapter<AdapterPhone.ViewHolder> {
    private List<Phone> mPhones;
    private ItemPhoneBinding mBinding;

    public AdapterPhone(List<Phone> phones) {
        mPhones = phones;
    }

    public void swapItems(List<Phone> phones) {
        mPhones = phones;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        mBinding = DataBindingUtil.inflate(inflater, R.layout.item_phone, parent, false);
        return new ViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Phone phone = mPhones.get(position);
        mBinding.tvPhoneItem.setText(phone.getPhone());
    }

    @Override
    public int getItemCount() {
        if (mPhones == null)
            return 0;
        return mPhones.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
