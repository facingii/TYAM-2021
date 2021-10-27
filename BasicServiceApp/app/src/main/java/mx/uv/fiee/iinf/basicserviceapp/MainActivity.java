package mx.uv.fiee.iinf.basicserviceapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MainActivity extends Activity {
    Intent intent;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        findViewById (R.id.startService).setOnClickListener (view -> {
            intent = new Intent (getBaseContext (), MyOsomService.class);
            intent.putExtra ("TIME", System.currentTimeMillis ());

            startService (intent);
        });

        findViewById (R.id.stopService).setOnClickListener (view -> {
            if (intent != null) stopService (intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver (receiver, new IntentFilter ("MYINTENTFILTER"));
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (receiver != null) {
            unregisterReceiver (receiver);
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver () {
        @Override
        public void onReceive (Context context, Intent intent) {
            Log.d ("TAG", "Receiving...");
            if (intent == null) return;

            long timeFinish = intent.getLongExtra ("FINISH", 0);
            Toast.makeText (getBaseContext(), "Finish time is " + timeFinish, Toast.LENGTH_LONG).show();
        }
    };
}
