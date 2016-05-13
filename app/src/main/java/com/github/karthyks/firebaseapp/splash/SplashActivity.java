package com.github.karthyks.firebaseapp.splash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.FirebaseError;
import com.github.karthyks.firebaseapp.R;
import com.github.karthyks.firebaseapp.SignInAppCompatActivity;
import com.github.karthyks.firebaseapp.login.LoginActivity;
import com.github.karthyks.firebaseapp.main.MainActivity;
import com.github.karthyks.firebaseapp.utils.FirebaseUtils;
import com.github.karthyks.firebaseapp.utils.GoogleUtils;

public class SplashActivity extends SignInAppCompatActivity {

  private static final String TAG = SplashActivity.class.getSimpleName();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    GoogleUtils.setSignInListener(this);
    getGoogleUtils().silentSignIn();
  }

  @Override
  public void onSignInSuccess() {
    getGoogleUtils().getGoogleOAuthTokenAndLogin();
  }

  @Override
  public void onSignInFailed() {
    startActivity(new Intent(this, LoginActivity.class));
    finish();
  }

  @Override
  public void inProgress() {
    showProgressDialog("Welcome to Firebase...");
  }

  @Override
  public void onAuthToken(String authToken) {
    Log.d(TAG, "onAuthToken: " + authToken);
    FirebaseUtils.checkLogin(authToken, this);
  }

  @Override
  public void onErrorPermission(Intent intent) {
    startActivityForResult(intent, GoogleUtils.RC_GOOGLE_LOGIN);
  }

  @Override
  public void onAuthenticated(AuthData authData) {
    hideProgressDialog();
    startActivity(new Intent(this, MainActivity.class));
    finish();
    Log.d(TAG, "onAuthenticated: " + authData.toString());
  }

  @Override
  public void onAuthenticationError(FirebaseError firebaseError) {
    startActivity(new Intent(this, LoginActivity.class));
    Log.d(TAG, "onAuthenticationError: " + firebaseError.toString());
  }
}
