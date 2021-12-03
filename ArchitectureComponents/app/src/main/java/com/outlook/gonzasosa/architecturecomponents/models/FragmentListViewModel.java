package com.outlook.gonzasosa.architecturecomponents.models;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FragmentListViewModel extends ViewModel {
    private String TAG = FragmentListViewModel.class.getSimpleName();
    private MutableLiveData<List<BasketItem>> basket;

    public LiveData<List<BasketItem>> getBasket() {
        if (basket == null) {
            basket = new MutableLiveData<>();
            fillBasket ();
        }

        return basket;
    }

    private void fillBasket () {
        Handler myHandler = new Handler ();
        myHandler.postDelayed (() -> {
            List<BasketItem> items = new ArrayList<>();
            items.add(new BasketItem (1, "Mango"));
            items.add(new BasketItem (2, "Apple"));
            items.add(new BasketItem (3, "Orange"));
            items.add(new BasketItem (4, "Banana"));
            items.add(new BasketItem (5, "Grapes"));
            items.add(new BasketItem (6, "Strawberry"));

            long seed = System.nanoTime ();
            Collections.shuffle (items, new Random (seed));

            basket.setValue (items);
        }, 5000);
    }

    @Override
    protected void onCleared () {
        super.onCleared ();
        Log.d (TAG, "OnCleared Called");
    }
}
