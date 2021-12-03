package com.outlook.gonzasosa.architecturecomponents.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.outlook.gonzasosa.architecturecomponents.R;
import com.outlook.gonzasosa.architecturecomponents.models.DataModel;
import com.outlook.gonzasosa.architecturecomponents.models.FormViewModel;
import com.outlook.gonzasosa.architecturecomponents.databinding.FragmentBlank3Binding;

public class Blank3Fragment extends Fragment {
    FragmentBlank3Binding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBlank3Binding.inflate (inflater, container, false);
        return binding.getRoot ();
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        DataModel dataModel = new DataModel ();
        dataModel.name.set ("Sansa");
        dataModel.lastName.set ("Stark");
        dataModel.age.set (21);
        dataModel.address.set ("Winterfell");

        binding.setDataModel (dataModel);

        binding.btnFill.setOnClickListener (v -> {
            dataModel.name.set ("John");
            dataModel.lastName.set ("Snow");
            dataModel.age.set (33);
            dataModel.address.set ("Winterfell");
        });

        binding.btnFragmentList.setOnClickListener (v -> {
            Navigation.findNavController(view).navigate (R.id.action_blank3Fragment_to_listFragment);
        });
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        binding = null;
    }
}

