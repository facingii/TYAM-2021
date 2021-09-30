package com.outlook.gonzasosa.basicapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class SecondActivity extends Activity {
    String name, lastName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        if (intent == null) return;

        name = intent.getStringExtra(MainActivity.NAME);
        lastName = intent.getStringExtra(MainActivity.LASTNAME);
        String age = intent.getStringExtra(MainActivity.AGE);
        String address = intent.getStringExtra(MainActivity.ADDRESS);

        TextView tv1 = findViewById(R.id.welcome_1);
        TextView tv2 = findViewById(R.id.welcome_2);
        TextView tv3 = findViewById(R.id.welcome_3);


        tv1.setText(name + lastName);
        tv2.setText(age);
        tv3.setText(address);
    }

    @Override
    protected void onStop () {
        Intent intent1 = new Intent();
        intent1.putExtra("FULLNAME", name + " " + lastName);
        setResult(RESULT_OK, intent1);

        super.onStop();

    }
}
