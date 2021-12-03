package com.outlook.gonzasosa.architecturecomponents.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.outlook.gonzasosa.architecturecomponents.databinding.FragmentItemBinding;
import com.outlook.gonzasosa.architecturecomponents.models.BasketItem;

import java.util.List;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<BasketItem> mValues;

    public MyItemRecyclerViewAdapter(List<BasketItem> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder (FragmentItemBinding.inflate (LayoutInflater.from (parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        BasketItem basketItem = mValues.get (position);
        holder.mIdView.setText (String.valueOf (basketItem.idItem));
        holder.mDescView.setText (basketItem.description);
    }

    @Override
    public int getItemCount () {
        return mValues.size ();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mDescView;

        public ViewHolder (FragmentItemBinding binding) {
            super (binding.getRoot ());

            mIdView = binding.basketItemId;
            mDescView = binding.basketItemDescription;
        }

        @NonNull
        @Override
        public String toString () {
            return super.toString () + " '" + mDescView.getText () + "'";
        }
    }
}

