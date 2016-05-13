package com.github.karthyks.firebaseapp.utils;

import com.firebase.client.Firebase;

public class FirebaseUtils {

  public static final String URL = "https://blazing-inferno-8424.firebaseio.com";
  private static final String TAG = FirebaseUtils.class.getSimpleName();

  public static void checkLogin(String token, Firebase.AuthResultHandler handler) {
    Firebase ref = new Firebase(URL);
    ref.authWithOAuthToken("google", token, handler);
  }
}
