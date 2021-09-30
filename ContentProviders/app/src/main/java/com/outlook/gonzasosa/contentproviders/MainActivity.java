package com.outlook.gonzasosa.contentproviders;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

public class MainActivity extends Activity {
    public static final int REQUEST_CODE_EXTERNAL_STORAGE = 1001;
    public static final int REQUEST_CODE_WRITE_PERMISSION = 1002;
    public static final int REQUEST_CODE = 2001;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        int perm = getBaseContext ().checkSelfPermission (Manifest.permission.READ_EXTERNAL_STORAGE);
//        if (perm != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions (
//                    new String [] { Manifest.permission.READ_EXTERNAL_STORAGE },
//                    REQUEST_CODE_EXTERNAL_STORAGE
//            );
//
//            return;
//        }
//
//        loadAudios ();

        if (!hasWritePermission ()) {
            requestPermissions (
                    new String [] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    REQUEST_CODE_WRITE_PERMISSION
            );

            return;
        }

        try {
            insertAudio ();
        } catch (Exception ex) {
            ex.printStackTrace ();
        }
    }

    private boolean hasWritePermission () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            return true;

        int permission = getBaseContext().checkSelfPermission (Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    // consultas
    void loadAudios () {
        // información a recuperar
        String[] columns = {MediaStore.Audio.Media._ID, MediaStore.Audio.Artists.ARTIST, MediaStore.Audio.Media.DISPLAY_NAME};
        String order = MediaStore.Audio.Media.DEFAULT_SORT_ORDER; // orden
        String selection = MediaStore.Audio.Media.DISPLAY_NAME + " LIKE ?";
        String [] selectionArgs = { "%Lake%" };

        // SELECT MediaStore.Audio.Artists.ARTIST, MediaStore.Audio.Media.ALBUM
        // FROM MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ORDER BY MediaStore.Audio.Media.DEFAULT_SORT_ORDER;
        Cursor cursor = getBaseContext ()
                .getContentResolver ()
                .query (MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columns, selection, selectionArgs, order);

        if (cursor == null) return;

        LinkedList<String> artists = new LinkedList<>();

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            int index = cursor.getColumnIndexOrThrow (MediaStore.Audio.Media.DISPLAY_NAME);
            String artist = cursor.getString (index);

            artists.add (artist);
        }

        cursor.close ();

        for (String s: artists) {
            Log.i ("MY_MUSIC", s);
        }
    }

    void insertAudio () throws FileNotFoundException, IOException {
        String filename = "Go_Robot_3.mp3";

        ContentValues values = new ContentValues ();
        values.put (MediaStore.Audio.AudioColumns.TITLE, filename);
        values.put (MediaStore.Audio.AudioColumns.DISPLAY_NAME, filename);
        values.put (MediaStore.Audio.AudioColumns.MIME_TYPE, "audio/mpeg");
        values.put (MediaStore.Audio.AudioColumns.DATE_ADDED, System.currentTimeMillis ());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put (MediaStore.Audio.AudioColumns.DATE_TAKEN, System.currentTimeMillis ());
            values.put (MediaStore.Audio.Media.RELATIVE_PATH, Environment.DIRECTORY_MUSIC);
            values.put (MediaStore.Audio.Media.IS_PENDING, true); // importante!
        } else {
            String musicDirectory = String.format ("%s/%s",
                    Environment.getExternalStorageDirectory (), Environment.DIRECTORY_MUSIC);
            String fullPath = String.format ("%s/%s", musicDirectory, filename);

            values.put (MediaStore.Audio.AudioColumns.DATA, fullPath);
        }

        // paso 1
        Uri uriObject = getContentResolver().insert (MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
        if (uriObject != null) {
            // paso 2
            OutputStream fos = getContentResolver().openOutputStream (uriObject);
            InputStream fis = getResources().openRawResource (R.raw.go_robot);

            int bytesRead;
            int bufferSize = 1024;
            byte [] buffer = new byte [bufferSize];

            while ((bytesRead = fis.read (buffer, 0, bufferSize)) > -1) {
                fos.write (buffer, 0, bytesRead);
            }

            fos.flush ();
            fos.close ();

            // paso 3 (API 29+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.clear ();
                values.put (MediaStore.Audio.AudioColumns.IS_PENDING, false);

                getContentResolver().update(uriObject, values, null, null);
            }

            Log.i("MY_MUSIC", "Inserted!");
        } else {
            Log.e ("MY_MUSIC", "Failed!");
        }
    }

    void deleteAudio () {
        String selection = MediaStore.Audio.Media.DISPLAY_NAME + " = ";
        String [] selectionArgs = { "Go_Robot_3.mp3" };

        // DELETE FROM MediaStore.Audio.Media.EXTERNAL_CONTENT_URI WHERE MediaStore.Audio.Media.DISPLAY_NAME = 'Go_Robot_3.mp3'
        int rows = getContentResolver().delete (MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);

        if (rows > 0) {
            Log.i ("MY_MUSIC", "Deleted!");
        } else {
            Log.i ("MY_MUSIC", "Not found!");
        }
    }

    /**
     * Callback de la solicitud de permisos realizada en cualquier punto de la actividad.
     *
     * @param requestCode código de verificación de la solicitud
     * @param permissions conjunto de permisos solicitados
     * @param grantResults conjunto de resultados, permisos otorgados o denegados
     */
    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText (getBaseContext(),"Permission Granted!", Toast.LENGTH_LONG).show ();
                }
                break;
            case REQUEST_CODE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED) {
                    loadAudios ();
                }
                break;
            case REQUEST_CODE_WRITE_PERMISSION:
                try {
                    insertAudio ();
                } catch (Exception ex) {
                    ex.printStackTrace ();
                }
                break;
        }
    }

}
