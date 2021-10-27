package mx.uv.fiee.iinf.basicserviceapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

// foreground service
public class MyOsomService extends Service {
    public static final String TAG = "MyAwesomeService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d (TAG, "onBind");
        return null;
    }

    @Override
    public void onCreate () {
        super.onCreate();
        Log.d (TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d (TAG, "onStartCommand");

        long startTime = intent.getLongExtra ("TIME", 0);
        doDownload (startTime);

        return START_STICKY;
        //return super.onStartCommand (intent, flags, startId);
    }

    // long process
    void doDownload (long time) {
        Log.d ("TAG", "On download method...");
        try {
            Thread.sleep(5000); // detener el hilo
        } catch (Exception ex) { ex.printStackTrace (); }

        Intent intent = new Intent ("MYINTENTFILTER"); //package result
        intent.putExtra ("FINISH", System.currentTimeMillis ());
        sendBroadcast (intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d (TAG, "onDestroy");
    }
}
