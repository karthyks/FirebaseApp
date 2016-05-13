package com.github.karthyks.firebaseapp.transaction;

import com.firebase.client.Firebase;
import com.github.karthyks.firebaseapp.model.UserModel;

public class UserTransaction {

  public static final String URL = "https://blazing-inferno-8424.firebaseio.com/";

  public static void setUser(UserModel user) {
    Firebase ref = new Firebase(URL);
    ref.child("users").child(user.getUserId()).setValue(user);
  }
}
