package com.outlook.gonzasosa.architecturecomponents.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.outlook.gonzasosa.architecturecomponents.models.FormViewModel;
import com.outlook.gonzasosa.architecturecomponents.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Blank1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Blank1Fragment extends Fragment {
    FormViewModel mViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Blank1Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static Blank1Fragment newInstance(String param1, String param2) {
        Blank1Fragment fragment = new Blank1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mViewModel = new ViewModelProvider (this).get(FormViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank1, container, false);
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentActivity activity = getActivity ();
        if (activity == null) return;

        EditText name = activity.findViewById (R.id.editTextTextPersonName2);
        name.addTextChangedListener (new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mViewModel.Name = charSequence.toString ();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        EditText lastName = activity.findViewById (R.id.editTextTextPersonName3);
        EditText age = activity.findViewById (R.id.editTextTextPersonName4);
        EditText address = activity.findViewById (R.id.editTextTextPersonName5);

        name.setText (mViewModel.Name);
        lastName.setText (mViewModel.LastName);
        age.setText (mViewModel.Age > 0 ? String.valueOf (mViewModel.Age) : "");
        address.setText (mViewModel.Adress);
    }
}