package com.github.karthyks.firebaseapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.github.karthyks.firebaseapp.utils.GoogleUtils;

public abstract class SignInAppCompatActivity extends AppCompatActivity implements
    GoogleUtils.GoogleSignInListener, Firebase.AuthResultHandler {

  private ProgressDialog progressDialog;
  private GoogleUtils googleUtils;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    googleUtils = FirebaseApp.getGoogleUtils();
    googleUtils.buildGso().buildApiClient(this, this);
    GoogleUtils.setSignInListener(this);
  }

  protected ProgressDialog getProgressDialog() {
    return progressDialog;
  }

  protected void showProgressDialog(String message) {
    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage(message);
    progressDialog.setIndeterminate(true);
    if(!progressDialog.isShowing())
      progressDialog.show();
  }

  protected GoogleUtils getGoogleUtils() {
    return googleUtils;
  }

  protected void hideProgressDialog() {
    if (progressDialog != null) {
      progressDialog.hide();
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    googleUtils.onActivityResult(requestCode, resultCode, data);
  }
}
