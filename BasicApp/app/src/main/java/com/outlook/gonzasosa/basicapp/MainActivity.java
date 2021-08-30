package com.outlook.gonzasosa.basicapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activty_main);

        TextView welcome_1 = findViewById (R.id.welcome_1);
        welcome_1.setText ("Changing text...");

        TextView welcome_2 = findViewById (R.id.welcome_2);
        TextView welcome_3 = findViewById (R.id.welcome_3);
    }

}
