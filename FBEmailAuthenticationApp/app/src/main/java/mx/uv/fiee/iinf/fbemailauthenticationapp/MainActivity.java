package mx.uv.fiee.iinf.fbemailauthenticationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GithubAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;

import mx.uv.fiee.iinf.fbemailauthenticationapp.fragments.GithubOAuth2Fragment;
import mx.uv.fiee.iinf.fbemailauthenticationapp.fragments.LoginFragment;
import mx.uv.fiee.iinf.fbemailauthenticationapp.fragments.RegisterFragment;

public class MainActivity extends AppCompatActivity {
    public static final String GOOGLE_ID_CLIENT_TOKEN = BuildConfig.GOOGLE_ID_CLIENT_TOKEN;
    public static final int GOOGLE_SIGNIN_REQUEST_CODE = 1001;
    private GoogleSignInClient gmsClient;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar ();
        if (actionBar != null) {
            actionBar.setTitle ("Auth Options");
        }

        /* Setup Google Services Client */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder (GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken (GOOGLE_ID_CLIENT_TOKEN)
                .requestEmail ()
                .build ();

        gmsClient = GoogleSignIn.getClient (getBaseContext (), gso);

        getSupportFragmentManager ()
                .beginTransaction ()
                .add (R.id.rootContainer, new LoginFragment())
                .setTransition (FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit ();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater ().inflate (R.menu.main, menu);
        return super.onCreateOptionsMenu (menu);
    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item) {

        int id = item.getItemId ();
        if (id == R.id.mnuRegister) {
            showRegisterView ();
        } else if (id == R.id.mnuGoogleSignin) {
            showGoogleSignInView ();
        } else if (id == R.id.mnuGithubSignin) {
            showGithubSignInView ();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showRegisterView () {
        getSupportFragmentManager ()
                .beginTransaction ()
                .addToBackStack (null)
                .replace (R.id.rootContainer, new RegisterFragment())
                .setTransition (FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit ();
    }

    private void showGithubSignInView () {
        GithubOAuth2Fragment github = new GithubOAuth2Fragment ();
        github.setOnAuthenticationCompleteListener (this::firebaseAuthWithGithubProvider);

        getSupportFragmentManager ()
                .beginTransaction ()
                .add (R.id.rootContainer, github)
                .setTransition (FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit ();
    }

    private void firebaseAuthWithGithubProvider (String token) {
        auth = FirebaseAuth.getInstance ();
        AuthCredential credential = GithubAuthProvider.getCredential (token);

        auth.signInWithCredential (credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful ()) {
                        Toast.makeText (getBaseContext (), "Github SignIn Successful!", Toast.LENGTH_LONG).show ();
                    } else {
                        Toast.makeText (getBaseContext (),
                                "SignIn with Google services failed with exception " +
                                        (task.getException () != null ? task.getException().getMessage () : "None"),
                                Toast.LENGTH_LONG).show ();
                    }
                });
    }

    private void showGoogleSignInView () {
        auth = FirebaseAuth.getInstance ();

        Intent intent = gmsClient.getSignInIntent ();
        myActivityResultLauncher.launch (intent);
        //startActivityForResult (intent, GOOGLE_SIGNIN_REQUEST_CODE);
    }

    ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult (
            new ActivityResultContracts.StartActivityForResult (),
            result -> {
                if (result.getResultCode() == GOOGLE_SIGNIN_REQUEST_CODE) {
                    if (result.getData() == null) return;
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent (result.getData ());

                    GoogleSignInAccount account = task.getResult();
                    if (account != null) firebaseAuthWithGoogleServices (account);
                }
            }
    );

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == GOOGLE_SIGNIN_REQUEST_CODE) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            if (data == null) return;
//
//            GoogleSignInAccount account = task.getResult ();
//            if (account != null) firebaseAuthWithGoogleServices(account);
//        }
//    }

    private void firebaseAuthWithGoogleServices (GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential (account.getIdToken (), null);
        auth.signInWithCredential (credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful ()) {
                        Toast.makeText (getBaseContext (), "Google SignIn Successful!", Toast.LENGTH_LONG).show ();
                    } else {
                        Toast.makeText (getBaseContext (),
                                "SignIn with Google services failed with exception " +
                                        (task.getException () != null ? task.getException().getMessage () : "None"),
                                Toast.LENGTH_LONG).show ();
                    }
                });
    }
}
