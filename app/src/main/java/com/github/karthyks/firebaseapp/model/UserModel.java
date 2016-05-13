package com.github.karthyks.firebaseapp.model;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class UserModel {
  private static final String TAG = UserModel.class.getSimpleName();
  private String username;
  private String userId;
  private String email;
  private String photoUrl;

  public UserModel(String userId, String username, String email, String photoUrl) {
    this.userId = userId;
    this.username = username;
    this.email = email;
    this.photoUrl = photoUrl;
  }

  public static UserModel fromAccount(GoogleSignInAccount account) {
    String photoUrl = "";
    try {
      if (account.getPhotoUrl() != null) {
        photoUrl = account.getPhotoUrl().toString();
      }
    } catch (Exception e) {
      photoUrl = "";
    }
    return new UserModel(account.getId(), account.getDisplayName(), account.getEmail(), photoUrl);
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }
}
