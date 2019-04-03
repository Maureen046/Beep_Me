package com.me.beem.beep_me.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.me.beem.beep_me.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginFragment";
    private static final String RED = "#de5750";
    private static final String GREEN = "#89DA91";

    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextIputLayout;
    private TextInputEditText email;
    private TextInputEditText password;
    private Button login;
    private Button forgotPassword;
    private ProgressBar progressBar;

    private Context context;

    private MainActivity mainActivity;

    private CoordinatorLayout coordinatorLayout;

    String token;
    String[] userIDs;
    String[] lastNames;
    String userId;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private TextView loginTextTop;
    Boolean isFromLoginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            onLoginSuccess();
        }
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        setContentView(R.layout.activity_login);

        initStringArrays();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = mAuth.getCurrentUser();
                if (mUser != null) {
                    onLoginSuccess();
                }
            }
        };
        coordinatorLayout = findViewById(R.id.loginCoordinatorLayout);

        checkForNetwork();

        forgotPassword = findViewById(R.id.forgotPasswordLink);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setForgotPassword();
            }
        });

        progressBar = findViewById(R.id.loginActivityProgressBar);
        progressBar.setVisibility(View.INVISIBLE);
        emailTextInputLayout = findViewById(R.id.emailTextInputLayout);
        passwordTextIputLayout = findViewById(R.id.passwordTextInputLayout);
        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);
        login = findViewById(R.id.logInButtton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLogin();
            }
        });
        goLogInDirectKeyboard();
    }

    public void setForgotPassword() {
        String emailString = email.getText().toString().trim();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to change your password?");

        builder.setPositiveButton("I'm sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                passwordTextIputLayout.setError(null);
                if (emailString.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
                    emailTextInputLayout.setError("Enter your registered email address");
                } else if (!hasInternetConnection()) {
                    emailTextInputLayout.setError(null);
                    passwordTextIputLayout.setError(null);
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please connect to a network", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(Color.parseColor(RED));
                    snackbar.show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.sendPasswordResetEmail(emailString)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                                        alertDialogBuilder.setTitle("Email Sent!");
                                        alertDialogBuilder.setMessage("The link to change your account password has been sent. If the email can't be seen, look into your email spam folder");
                                        alertDialogBuilder.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                emailTextInputLayout.setError(null);
                                                passwordTextIputLayout.setError(null);
                                            }
                                        });
                                        alertDialogBuilder.show();
                                    } else {
                                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            emailTextInputLayout.setError("Incorrect email address");
                                            passwordTextIputLayout.setError(null);
                                        }
                                        progressBar.setVisibility(View.INVISIBLE);
                                        emailTextInputLayout.setError(null);
                                        passwordTextIputLayout.setError(null);
                                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Failed to send email", Snackbar.LENGTH_LONG);
                                        View sbView = snackbar.getView();
                                        sbView.setBackgroundColor(Color.parseColor(RED));
                                        snackbar.show();
                                    }
                                }
                            });
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    public void setLogin() {
        Log.d(TAG, "Login");
        if (validate() && hasInternetConnection()) {
            login.setEnabled(false);
            emailTextInputLayout.setError(null);
            passwordTextIputLayout.setError(null);
            progressBar.setVisibility(View.VISIBLE);
            String emailString = email.getText().toString();
            String passwordString = password.getText().toString();

            // TODO: Implement your own authentication logic here.
            mAuth.signInWithEmailAndPassword(emailString, passwordString)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    login.setEnabled(true);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Invalid password or email address", Snackbar.LENGTH_LONG);
                                    View sbView = snackbar.getView();
                                    sbView.setBackgroundColor(Color.parseColor(RED));
                                    snackbar.show();
                                } else {
                                    login.setEnabled(true);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Something went wrong. Please try again", Snackbar.LENGTH_LONG);
                                    View sbView = snackbar.getView();
                                    sbView.setBackgroundColor(Color.parseColor(RED));
                                    snackbar.show();
                                }
                            } else {
                                login.setEnabled(true);
                                progressBar.setVisibility(View.INVISIBLE);
                                onLoginSuccess();
                            }
                        }
                    });
        } else if (!hasInternetConnection()) {
            login.setEnabled(true);
            onNoNetworkConnection();
        } else {
            login.setEnabled(true);
            onLoginFailed();
        }
    }

    private void goLogInDirectKeyboard() {
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    login.performClick();
                }
                return false;
            }
        });
    }


    public void onLoginSuccess() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        mUser = mAuth.getCurrentUser();
                        userId = mUser.getUid();
                        token = task.getResult().getToken();

                        mReference.child("Users").child(userId).child("messagingToken").setValue(token);
                        FirebaseMessaging.getInstance().subscribeToTopic("user_" + mUser.getUid().toString());
                    }
                });


        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("fromLogin", isFromLoginActivity);
        startActivity(intent);
        finish();

    }

    private void onNoNetworkConnection() {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please connect to the internet to log in", Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.parseColor(RED));
        snackbar.show();
    }

    public void onLoginFailed() {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Log in failed", Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.parseColor(RED));
        snackbar.show();
    }

    public boolean validate() {
        boolean valid = true;

        String emailString = email.getText().toString().trim();
        String passwordString = password.getText().toString().trim();

        if (emailString.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            emailTextInputLayout.setError("enter a valid email address");
            email.setText("");
            password.setText("");
            valid = false;
        } else {
            email.setError(null);
        }

        if (passwordString.isEmpty()) {
            passwordTextIputLayout.setError("enter correct password");
            valid = false;
        } else {
            passwordTextIputLayout.setError(null);
        }

        return valid;
    }

    private void checkForNetwork() {
        if (hasInternetConnection()) {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Connected", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.parseColor(GREEN));
            snackbar.show();
        } else {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No network connection found", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.parseColor(RED));
            snackbar.show();
        }
    }

    private boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }
        return false;
    }

    private void initStringArrays() {
        userIDs = new String[]{
                "6rkxkCPG13WCeZraCcg6SEnQ3Sg1",
                "WWZu7mbGdONcLN2LFKC2zaiAuX53",
                "0Rk16YKONNb6TrKLwMWhvp56HMl1",
                "kppaOBFMRsZs6S5O4nKYksDyhnG2",
                "O21YNHCjy9QZTSwQ9k6vxPNCWR13",
                "VnJYUAtO45Oc74CRwocMXVpztNg2",
                "nOSRBiw0vMN8CPucEDFZuTE73PD3",
                "BNjRJ56suJNZzMU1ZFC6GHkbqsx2",
                "MqQHdLi7QmXUdIG9wmdTNcdsqRv1",
                "ixyBntwzSceKKw21HyuKHoq068p2",
                "Ee5iL7XXTlfPmwME3XpvYrXgrI62",
                "glTz63IrVsO2UZ6BS63j8RgQXgs1",
                "jccxtjVArwMqXqD2JNI2Cj0etLm1",
                "XIwGr4DEnNOFpJjzPMMTqPwNTuK2",
                "VZQIHiYA0WZ6eEysQh8sjpSss4B3",
                "kNYooe68dmSjBxYr2rQC80zjgag2",
                "5XDgxpxEwcXhOYc1MABrIROjZ6p1",
                "3QenydwlgIWrAbdHxZDbPK5Sbu42"
        };

        lastNames = new String[]{
                "Abraham",
                "Asia",
                "Barundia",
                "Belarmino",
                "Bete",
                "Bitancor",
                "Calinagan",
                "Elag",
                "Esguerra",
                "Evangelista",
                "Geluz",
                "Logo",
                "Marcelo",
                "Mayuga",
                "Mercado",
                "Narsolis",
                "Ortega",
                "Puyod"
        };
    }


}





