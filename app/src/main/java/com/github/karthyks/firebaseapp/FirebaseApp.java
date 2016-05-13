package com.github.karthyks.firebaseapp;

import android.app.Application;

import com.firebase.client.Firebase;
import com.github.karthyks.firebaseapp.utils.GoogleUtils;

public class FirebaseApp extends Application {

  private static GoogleUtils googleUtils;

  @Override public void onCreate() {
    super.onCreate();
    googleUtils = new GoogleUtils(getApplicationContext());
    Firebase.setAndroidContext(getApplicationContext());
  }

  public static GoogleUtils getGoogleUtils() {
    return googleUtils;
  }
}
