package com.outlook.gonzasosa.basicapp;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;
import dagger.android.DaggerApplication;

public class ThirdActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationGraph applicationGraph = DaggerApplicationGraph.create ();

        UserRepository userRepository1 = applicationGraph.userRepository ();
        System.out.println (userRepository1.toString ());

        UserRepository userRepository2 = applicationGraph.userRepository ();
        System.out.println (userRepository2.toString ());
    }
}

// preparar las clases para ID
class UserLocaDataSource {
    @Inject
    public UserLocaDataSource () {}
}

class UserRemoteDataSource {
    @Inject
    public UserRemoteDataSource () {}
}

class UserRepository {
    private final UserLocaDataSource userLocaDataSource;
    private final UserRemoteDataSource userRemoteDataSource;

    @Inject
    public UserRepository (UserLocaDataSource userLocaDataSource, UserRemoteDataSource userRemoteDataSource) {
        this.userLocaDataSource = userLocaDataSource;
        this.userRemoteDataSource = userRemoteDataSource;
    }
}

@Singleton
@Component
interface ApplicationGraph {
    UserRepository userRepository ();
}