package com.outlook.gonzasosa.basicapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.security.Permission;
import java.security.Permissions;

public class MainActivity extends Activity {
    public static final String NAME = "name";
    public static final String LASTNAME = "lastName";
    public static final String AGE = "age";
    public static final String ADDRESS = "address";
    public static final String PICTURE = "picture";
    public static final String TAG = "MyFirstApp";
    public static final int REQUEST_CODE_CALL_PHONE = 1001;

    EditText edtName, edtLastname, edtAge, edtAddress;
    ImageView picture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_main);

        // setting up fields references
        edtName = findViewById (R.id.edtName);
        edtLastname = findViewById (R.id.edtLastname);
        edtAge = findViewById (R.id.edtAge);
        edtAddress = findViewById (R.id.edtAddress);
        picture = findViewById (R.id.picture);
        picture.setImageResource (R.mipmap.ic_launcher_round);

        // navigating to second activity using explicit intents
        Button btnInformation = findViewById (R.id.btnInformation);
        btnInformation.setOnClickListener (view -> {
            Intent intent = new Intent (getBaseContext (), SecondActivity.class);
            startActivity (intent);
        });

        // invoking phone call dialer using implicit intents
        Button btnPhoneCall = findViewById (R.id.btnPhoneCall);
        btnPhoneCall.setOnClickListener (view -> {

            // validate if user has grant permission of call to the app
            int permission = checkSelfPermission (Manifest.permission.CALL_PHONE);
            if (permission != PackageManager.PERMISSION_GRANTED) { // if not, request it
                requestPermissions (new String [] { Manifest.permission.CALL_PHONE },
                        REQUEST_CODE_CALL_PHONE);

                return;
            }

            // only when the user has permmited phone calls the app is enable to do that
            doPhoneCall ();
        });

        Log.i (TAG, "OnCreate");
    }

    private void doPhoneCall () {
        String phone = "tel: 1234567890";
        Uri uri = Uri.parse (phone);

        Intent intent = new Intent (Intent.ACTION_CALL);
        intent.setData (uri);

        startActivity (intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i (TAG, "OnSaveInstanceState");

        outState.putString (NAME, edtName.getText().toString ());
        outState.putString (LASTNAME, edtLastname.getText().toString ());
        outState.putString (AGE, edtAge.getText().toString ());
        outState.putString (ADDRESS, edtAddress.getText().toString ());

        byte [] barray = convertImage2ByteArray (picture);
        outState.putByteArray (PICTURE, barray);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i (TAG, "OnRestoreInstanceState");

        String name = savedInstanceState.getString (NAME);
        edtName.setText (name);

        String lastName = savedInstanceState.getString (LASTNAME);
        edtLastname.setText (lastName);

        String age = savedInstanceState.getString (AGE);
        edtAge.setText (age);

        String address = savedInstanceState.getString (ADDRESS);
        edtAddress.setText (address);

        byte [] barray = savedInstanceState.getByteArray (PICTURE);
        Bitmap bitmap = BitmapFactory.decodeByteArray (barray, 0, barray.length);
        picture.setImageBitmap (bitmap);
    }

    /**
     * Convierte la imagen contenida en el componente ImageView en su representación
     * de arreglo de bytes de acuerdo a un formato de imagen determinado.
     *
     * @param imageView componente que contiene la imagen deseada.
     * @return arreglo de bytes que representan a la imagen.
     */
    private byte [] convertImage2ByteArray (ImageView imageView) {
        // obtiene la imagen desde el canvas sobre el que está dibujada
        Drawable drawable = imageView.getDrawable ();
        Bitmap bitmap = getBitmapFromDrawable (drawable); // obtiene un objeto Bitmap a partir del lienzo
        // se requiere un objeto que almacena en memoria los bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream ();
        // copia los bytes al flujo indicado, usando compresión (representación) de acuerdo al formato
        // indicado
        bitmap.compress (Bitmap.CompressFormat.PNG, 100, baos);

        return baos.toByteArray (); // devuelve el arreglo
    }

    /**
     * Obtiene un objeto de mapa de bits a partir del objeto Drawable (canvas) recibido.
     *
     * @param drble Drawable que contiene la imagen deseada.
     * @return objeto de mapa de bits con la estructura de la imagen.
     */
    private Bitmap getBitmapFromDrawable (Drawable drble) {
        // debido a la forma que el sistema dibuja una imagen en un el sistema gráfico
        // es necearios realizar comprobaciones para saber del tipo de objeto Drawable
        // con que se está trabajando.
        //
        // si el objeto recibido es del tipo BitmapDrawable no se requieren más conversiones
        if (drble instanceof BitmapDrawable) {
            return  ((BitmapDrawable) drble).getBitmap ();
        }

        // en caso contrario, se crea un nuevo objeto Bitmap a partir del contenido
        // del objeto Drawable
        Bitmap bitmap = Bitmap.createBitmap (drble.getIntrinsicWidth (), drble.getIntrinsicHeight (), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas (bitmap);
        drble.setBounds (0, 0, canvas.getWidth (), canvas.getHeight ());
        drble.draw (canvas);

        return bitmap;
    }

    /**
     * Callback called after user grant or deny permissions requested.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // validate requestCode and permissions resultset
        if (grantResults.length > 0 && requestCode == REQUEST_CODE_CALL_PHONE) {
            if (grantResults [0] == PackageManager.PERMISSION_GRANTED) {
                doPhoneCall ();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i (TAG, "OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i (TAG, "OnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i (TAG, "OnPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i (TAG, "OnStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i (TAG, "OnDestroy");
    }

    // time -> resources
    // JVM -> Dalvik / ART -> APK
}
