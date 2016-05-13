package com.github.karthyks.firebaseapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.client.AuthData;
import com.firebase.client.FirebaseError;
import com.github.karthyks.firebaseapp.R;
import com.github.karthyks.firebaseapp.SignInAppCompatActivity;
import com.github.karthyks.firebaseapp.main.MainActivity;
import com.github.karthyks.firebaseapp.utils.FirebaseUtils;
import com.github.karthyks.firebaseapp.utils.GoogleUtils;
import com.google.android.gms.common.SignInButton;

public class LoginActivity extends SignInAppCompatActivity implements View.OnClickListener {

  private static final String TAG = LoginActivity.class.getSimpleName();
  private SignInButton signInButton;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    injectViews();
  }

  private void injectViews() {
    signInButton = (SignInButton) findViewById(R.id.sign_in_button);
    signInButton.setSize(SignInButton.SIZE_STANDARD);
    signInButton.setScopes(getGoogleUtils().getGoogleSignInOptions().getScopeArray());
    signInButton.setOnClickListener(this);
  }

  @Override public void onAuthenticated(AuthData authData) {
    hideProgressDialog();
    Log.d(TAG, "onAuthenticated:");
    startActivity(new Intent(this, MainActivity.class));
    finish();
  }

  @Override public void onAuthenticationError(FirebaseError firebaseError) {
    hideProgressDialog();
  }

  @Override public void onSignInSuccess() {
    getGoogleUtils().getGoogleOAuthTokenAndLogin();
  }

  @Override public void onSignInFailed() {
    hideProgressDialog();
  }

  @Override public void inProgress() {
    showProgressDialog("Logging in...");
  }

  @Override public void onAuthToken(String authToken) {
    FirebaseUtils.checkLogin(authToken, this);
  }

  @Override public void onErrorPermission(Intent intent) {
    startActivityForResult(intent, GoogleUtils.RC_GOOGLE_LOGIN);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.sign_in_button:
        startActivityForResult(getGoogleUtils().getSignInIntent(), GoogleUtils.RC_SIGN_IN);
        break;
      default:
    }
  }
}
