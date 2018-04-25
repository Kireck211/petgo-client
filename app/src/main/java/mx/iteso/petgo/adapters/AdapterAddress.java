package mx.iteso.petgo.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mx.iteso.petgo.R;
import mx.iteso.petgo.beans.Address;
import mx.iteso.petgo.databinding.ItemAddressBinding;

public class AdapterAddress extends RecyclerView.Adapter<AdapterAddress.ViewHolder> {
    private List<Address> mAddress;
    private ItemAddressBinding mBinding;

    public AdapterAddress(List<Address> address) {
        mAddress = address;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        mBinding = DataBindingUtil.inflate(inflater, R.layout.item_address, parent, false);
        return new ViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Address address = mAddress.get(position);
        mBinding.tvAddressItem.setText(address.getAddress());
    }

    @Override
    public int getItemCount() {
        if (mAddress == null)
            return 0;
        return mAddress.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
