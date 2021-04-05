package com.example.myfamilymap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Request.LoginRequest;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate(Bundle) called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
        }

        // Create a Fragment in an Activity
        FragmentManager fm = this.getSupportFragmentManager();
        loginFragment = (LoginFragment) fm.findFragmentById(R.id.loginFragmentFrame);
        if (loginFragment == null) {
            loginFragment = new LoginFragment();
            fm.beginTransaction()
                    .add(R.id.loginFragmentFrame, loginFragment)
                    .commit();
        }
    }

}

//            loginFragment = createLoginFragment(getString(R.string.loginTitle));
//
//    private LoginFragment createLoginFragment(String title){
//        LoginFragment fragment = new LoginFragment();
//
//        // if you don't need to pass anything to it, you don't need these 3lines
//        Bundle args = new Bundle();
//        args.putString(LoginFragment.ARG_TITLE,title);
//        fragment.setArguments(args);
//
//        return fragment;
//    }
