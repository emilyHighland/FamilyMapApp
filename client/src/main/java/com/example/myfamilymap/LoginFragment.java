package com.example.myfamilymap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Request.LoginRequest;
import Request.RegisterRequest;

public class LoginFragment extends Fragment {
    private static final String LOG_TAG = "LoginFragment";
    public static final String ARG_TITLE = "title";

    private EditText serverHost;
    private EditText serverPort;
    private EditText username;
    private EditText password;
    private EditText firstName;
    private EditText lastName;
    private EditText email;

    RadioButton radioButton;
    Button loginButton;
    Button registerButton;

    private String title;
    private String serverHostText;
    private String serverPortText;
    private String usernameText;
    private String passwordText;
    private String firstNameText;
    private String lastNameText;
    private String emailText;
    private String genderText;

    DataCache dataCache;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "in onCreate(Bundle)");

        if (getArguments() != null){
            title = getArguments().getString(ARG_TITLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(LOG_TAG, "in onCreateView(Bundle)");
        View view = inflater.inflate(R.layout.fragment_login, container,false);

        serverHost = view.findViewById(R.id.serverHostField);
        serverHost.addTextChangedListener(textWatcher);

        serverPort = view.findViewById(R.id.serverPortField);
        serverPort.addTextChangedListener(textWatcher);

        username = view.findViewById(R.id.usernameField);
        username.addTextChangedListener(textWatcher);

        password = view.findViewById(R.id.passwordField);
        password.addTextChangedListener(textWatcher);

        firstName = view.findViewById(R.id.firstNameField);
        firstName.addTextChangedListener(textWatcher);

        lastName = view.findViewById(R.id.lastNameField);
        lastName.addTextChangedListener(textWatcher);

        email = view.findViewById(R.id.emailField);
        email.addTextChangedListener(textWatcher);

        // event handlers //
        // gender button
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedID = group.getCheckedRadioButtonId();
                radioButton = group.findViewById(selectedID);
                genderText = radioButton.getText().toString();
            }
        });

        // SIGN IN button
        loginButton = view.findViewById(R.id.signInButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Handler uiThreadMessageHandler = new Handler(Looper.getMainLooper()) {
                        @Override
                        public void handleMessage(Message message) {
                            Bundle bundle = message.getData();
                            boolean success = bundle.getBoolean("Success");
                            // back on original thread
                            // check whether login was a success or not to determine success/fail action/toast
                            if (success){
                                getFamilyData();
                            } else {
                                Toast.makeText(getContext(),"Login failed.", Toast.LENGTH_LONG).show();
                            }
                        }
                    };
                    LoginRequest loginRequest = new LoginRequest(usernameText, passwordText);
                    LoginTask logTask = new LoginTask(uiThreadMessageHandler,loginRequest,serverHostText,serverPortText);
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(logTask);

                } catch (Exception e){
                    Log.e(LOG_TAG,e.getMessage(),e);
                }
            }
        });

        // REGISTER button
        registerButton = view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Handler uiThreadMessageHandler = new Handler(Looper.getMainLooper()) {
                        @Override
                        public void handleMessage(Message message) {
                            Bundle bundle = message.getData();
                            boolean success = bundle.getBoolean("Success");
                            // back on original thread
                            // check whether login or register was a success or not to determine success/fail toast
                            if (success){
                                getFamilyData();
                            } else {
                                Toast.makeText(getContext(),"Register failed.", Toast.LENGTH_LONG).show();
                            }
                        }
                    };
                    RegisterRequest registerRequest = new RegisterRequest(usernameText,passwordText,
                            emailText,firstNameText,lastNameText,genderText);
                    RegisterTask regTask = new RegisterTask(uiThreadMessageHandler,registerRequest,serverHostText,serverPortText);
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(regTask);

                } catch (Exception e){
                    Log.e(LOG_TAG,e.getMessage(),e);
                }
            }
        });

        return view;
    }

    private void getFamilyData() {
        try {
            Handler uiThreadMessageHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message message) {
                    Bundle bundle = message.getData();
                    boolean success = bundle.getBoolean("Success");
                    // back on original thread
                    // check whether login or register was a success or not to determine success/fail toast
                    if (success){
                        Toast.makeText(getContext(),firstNameText + " " + lastNameText, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(),"Unable to retrieve family data.",Toast.LENGTH_LONG).show();
                    }
                }
            };
            DataTask getDataTask = new DataTask(uiThreadMessageHandler,serverHostText,serverPortText);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(getDataTask);

        } catch (Exception e){
            Log.e(LOG_TAG,e.getMessage(),e);
        }
    }

    // The SIGN IN button is disabled unless the server host, server port, user name, and password values are filled in.
    private void checkLoginEnabled() {
        if ((serverHost != null) && (serverPort != null) && (username != null) && (password != null)){
            serverHostText = serverPort.getText().toString();
            serverPortText = serverPort.getText().toString();
            usernameText = username.getText().toString();
            passwordText = password.getText().toString();
            loginButton.setEnabled(true);
        } else {
            loginButton.setEnabled(false);
        }
    }

    // The REGISTER button is disabled unless all field values are filled in.
    private void checkRegisterEnabled() {
        if (loginButton.isEnabled()){
            if ((firstName != null) && (lastName != null) && (email != null) && (genderText != null)){
                firstNameText = firstName.getText().toString();
                lastNameText = lastName.getText().toString();
                emailText = email.getText().toString();
                if (genderText == "Female"){ genderText = "f";}
                else { genderText = "m";}
                registerButton.setEnabled(true);
            }
        } else {
            registerButton.setEnabled(false);
        }
    }

    // when text field changes
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            checkLoginEnabled();
            checkRegisterEnabled();
        }
    };

    // TASKS //
    // Login Task
    private class LoginTask implements Runnable {
        private final Handler messageHandler;
        private final LoginRequest loginRequest;
        private String host;
        private String port;

        public LoginTask(Handler messageHandler,LoginRequest loginRequest,String host,String port) {
            this.messageHandler = messageHandler;
            this.loginRequest = loginRequest;
            this.host = host;
            this.port = port;
        }

        @Override
        public void run() {
            ServerProxy proxy = new ServerProxy(host,port);
            proxy.loginProxy(loginRequest);
            sendMessage();
        }

        private void sendMessage() {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            // connect loginResult isSuccess() back to original thread
            messageBundle.putBoolean("Success",dataCache.getLoginResult().isSuccess());
            message.setData(messageBundle);

            messageHandler.sendMessage(message);
        }
    }

    // Register Task
    private class RegisterTask implements Runnable {
        private final Handler messageHandler;
        private final RegisterRequest registerRequest;
        private String host;
        private String port;

        public RegisterTask(Handler messageHandler,RegisterRequest registerRequest,String host,String port) {
            this.messageHandler = messageHandler;
            this.registerRequest = registerRequest;
            this.host = host;
            this.port = port;
        }

        @Override
        public void run() {
            ServerProxy proxy = new ServerProxy(host,port);
            proxy.registerProxy(registerRequest);
            sendMessage();
        }

        private void sendMessage() {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            // connect registerResult isSuccess() back to original thread
            messageBundle.putBoolean("Success",dataCache.getRegisterResult().isSuccess());
            message.setData(messageBundle);

            messageHandler.sendMessage(message);
        }
    }

    // Get Family Data Task
    private class DataTask implements Runnable{
        private final Handler messageHandler;
        private String host;
        private String port;

        public DataTask(Handler messageHandler, String host, String port) {
            this.messageHandler = messageHandler;
            this.host = host;
            this.port = port;
        }

        @Override
        public void run() {
            ServerProxy proxy = new ServerProxy(host,port);
            proxy.getDataProxy();
            sendMessage();
        }

        private void sendMessage() {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            // connect login and register isSuccess() back to original thread
            // TODO: include getData.isSuccess boolean for 2nd parameter
            // could put separate booleans, or
            // create one bool for both case of get all persons success get all events success

            messageBundle.putBoolean("Success",dataCache.dataRetrieved);
            message.setData(messageBundle);

            messageHandler.sendMessage(message);
        }
    }






//    @Override
//    public void onAttachFragment(@NonNull Fragment childFragment) {
//        super.onAttachFragment(childFragment);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(LOG_TAG, "in onSaveInstanceState(Bundle)");
    }
}
