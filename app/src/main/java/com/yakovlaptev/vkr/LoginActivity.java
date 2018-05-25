package com.yakovlaptev.vkr;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yakovlaptev.vkr.Models.User;
import com.yakovlaptev.vkr.Services.JSONController;
import com.yakovlaptev.vkr.Services.PostTaskListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;


public class LoginActivity extends BaseActivity implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";

    //private TextView mStatusTextView;
    //private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    // [END declare_auth]
    private User curUser;

    PostTaskListener<JSONArray> postTaskListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //mStatusTextView = findViewById(R.id.status);
        //mDetailTextView = findViewById(R.id.detail);
        mEmailField = findViewById(R.id.email);
        mPasswordField = findViewById(R.id.password);

        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);
        //findViewById(R.id.sign_out_button).setOnClickListener(this);
        //findViewById(R.id.verify_email_button).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        postTaskListener = new PostTaskListener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onPostTask(JSONArray result) {
                Log.d("JSON RES USER", result.toString());
                try {
                    curUser = User.parseJsonData((JSONObject) result.get(0));
                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this,"Error of getting current user", Toast.LENGTH_SHORT).show();
                    Log.e("JSON Reciever", "Error getting data " + e.toString());
                } catch (ParseException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        user = mAuth.getCurrentUser();
        updateUI();
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed. " +
                                            task.getException().toString().substring(task.getException().toString().indexOf(':') + 1),
                                    Toast.LENGTH_LONG).show();
                            user = null;
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }
        showProgressDialog();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            user = mAuth.getCurrentUser();
                            request();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed. " +
                                            task.getException().toString().substring(task.getException().toString().indexOf(':') + 1),
                                    Toast.LENGTH_LONG).show();
                            user = null;
                        }
                        if (!task.isSuccessful()) {
                            //mStatusTextView.setText(R.string.auth_failed);
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        user = null;
        updateUI();
    }

    private void sendEmailVerification() {
        // Disable button
        //findViewById(R.id.verify_email_button).setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        findViewById(R.id.verify_email_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(LoginActivity.this,
                                    "Failed to send verification email. " + task.getException(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }


    public void request() {
        User us = new User();
        us.setEmail(user.getEmail());

        try {
            new JSONController("http://192.168.137.103:8080/users/get_by_email/", User.getJsonData(us), "POST", postTaskListener).execute(null, null, null);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        //new JSONController("http://192.168.137.103:8080/users/my_requests/1", new JSONObject(), "GET", postTaskListener).execute(null, null, null);

        Log.d("JSON INFO ", "+++++++++");
    }

    private void updateUI() {
        hideProgressDialog();
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("user", (Serializable) curUser);
            startActivity(intent);
            //mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
            //       user.getEmail(), user.isEmailVerified()));
            //mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

//            findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
//            findViewById(R.id.email_password_fields).setVisibility(View.GONE);
//            findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);
//
//            findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());
        } else {
            //mStatusTextView.setText(R.string.signed_out);
            //mDetailTextView.setText(null);

//            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
//            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            //findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_create_account_button) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.email_sign_in_button) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
//        } else if (i == R.id.sign_out_button) {
//            signOut();
//        } else if (i == R.id.verify_email_button) {
//            sendEmailVerification();
//        }
    }
}


