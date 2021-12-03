package com.outlook.gonzasosa.architecturecomponents.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.outlook.gonzasosa.architecturecomponents.Adapters.MyItemRecyclerViewAdapter;
import com.outlook.gonzasosa.architecturecomponents.databinding.FragmentItemListBinding;
import com.outlook.gonzasosa.architecturecomponents.models.BasketItem;
import com.outlook.gonzasosa.architecturecomponents.models.FormViewModel;
import com.outlook.gonzasosa.architecturecomponents.models.FragmentListViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ListFragment extends Fragment {
    private FragmentItemListBinding binding;
    private FragmentListViewModel viewModel;

    @SuppressWarnings("unused")
    public static ListFragment newInstance(int columnCount) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding =  FragmentItemListBinding.inflate (inflater, container, false);
        return binding.getRoot ();
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.list.setLayoutManager (new LinearLayoutManager (getContext (), LinearLayoutManager.VERTICAL, false));

        viewModel = new ViewModelProvider (this).get (FragmentListViewModel.class);
        viewModel.getBasket ().observe (this, (basket) -> {
            MyItemRecyclerViewAdapter adapter = new MyItemRecyclerViewAdapter (basket);
            binding.list.setAdapter (adapter);
        });
    }

}


