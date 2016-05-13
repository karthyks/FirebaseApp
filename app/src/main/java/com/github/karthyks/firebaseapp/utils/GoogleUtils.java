package com.github.karthyks.firebaseapp.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.github.karthyks.firebaseapp.SignInAppCompatActivity;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.IOException;

public class GoogleUtils implements GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks {

  public static final int RC_SIGN_IN = 9001;
  public static final int RC_GOOGLE_LOGIN = 9002;
  private static final String TAG = GoogleUtils.class.getSimpleName();

  private GoogleApiClient mGoogleApiClient;
  private Context mContext;
  private GoogleSignInOptions gso;
  private GoogleSignInAccount googleSignInAccount;

  private static GoogleSignInListener mGoogleSignInListener;

  public GoogleUtils(Context context) {
    this.mContext = context;
  }

  public GoogleUtils buildApiClient(SignInAppCompatActivity activity, Context context) {
    mGoogleApiClient = new GoogleApiClient.Builder(context)
        .enableAutoManage(activity, this)
        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .build();
    return this;
  }

  public GoogleUtils buildGso() {
    gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build();
    return this;
  }

  public static void setSignInListener(GoogleSignInListener googleSignInListener) {
    mGoogleSignInListener = googleSignInListener;
  }

  public GoogleSignInOptions getGoogleSignInOptions() {
    return gso;
  }

  public GoogleApiClient getGoogleApiClient() {
    return mGoogleApiClient;
  }

  public Intent getSignInIntent() {
    return Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
  }

  public void silentSignIn() {
    OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi
        .silentSignIn(mGoogleApiClient);
    if (opr.isDone()) {
      GoogleSignInResult result = opr.get();
      handleSignInResult(result);
      Log.d(TAG, "silentSignIn: Got cached sign in");
    } else {
      mGoogleSignInListener.inProgress();
      Log.d(TAG, "silentSignIn: inProgress");
      opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
        @Override
        public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
          Log.d(TAG, "onResult: ");
          handleSignInResult(googleSignInResult);
        }
      });
    }
  }

  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    if (requestCode == RC_SIGN_IN) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
      handleSignInResult(result);
    } else if (requestCode == RC_GOOGLE_LOGIN) {
      if(resultCode != Activity.RESULT_OK) {
        signOut();
        revokeAccess();
      }
    }
  }

  private void handleSignInResult(GoogleSignInResult result) {
    if (mGoogleSignInListener == null) return;
    Log.d(TAG, "handleSignInResult: " + result.toString());
    if (result.isSuccess()) {
      googleSignInAccount = result.getSignInAccount();
      mGoogleSignInListener.onSignInSuccess();
    } else {
      mGoogleSignInListener.onSignInFailed();
    }
  }

  public void signOut() {
    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
        new ResultCallback<Status>() {
          @Override
          public void onResult(Status status) {

          }
        });
  }

  public void revokeAccess() {
    Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
        new ResultCallback<Status>() {
          @Override
          public void onResult(Status status) {
          }
        });
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

  }

  @Override
  public void onConnected(Bundle bundle) {

  }

  @Override
  public void onConnectionSuspended(int i) {

  }

  public GoogleSignInAccount getGoogleSignInAccount() {
    return googleSignInAccount;
  }


  public void getGoogleOAuthTokenAndLogin() {
    mGoogleSignInListener.inProgress();
    AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
      String errorMessage = null;
      Intent errorIntent = null;

      @Override
      protected String doInBackground(Void... params) {
        String token = null;

        try {
          String scope = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
          AccountManager accountManager = AccountManager.get(mContext);
          for (Account account : accountManager.getAccountsByType(
              GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE)) {
            if (TextUtils.equals(account.name, googleSignInAccount.getEmail())) {
              Log.d(TAG, "doInBackground: " + account.name);
              token = GoogleAuthUtil.getToken(mContext, account, scope);
            }
          }
        } catch (IOException transientEx) {
          errorMessage = "Network error: " + transientEx.getMessage();
        } catch (UserRecoverableAuthException e) {
          errorMessage = null;
          errorIntent = e.getIntent();
          //errorMessage = "Error auth recovery" + e.getMessage();
        } catch (GoogleAuthException authEx) {
          errorMessage = "Error authenticating with Google: " + authEx.getMessage();
        }

        return token;
      }

      @Override
      protected void onPostExecute(String token) {
        if (errorMessage != null) {
          mGoogleSignInListener.onSignInFailed();
          Log.d(TAG, "onPostExecute: " + errorMessage);
        } else if (token != null) {
          mGoogleSignInListener.onAuthToken(token);
        } else {
          mGoogleSignInListener.onErrorPermission(errorIntent);
        }
      }
    };
    task.execute();
  }


  public interface GoogleSignInListener {
    void onSignInSuccess();

    void onSignInFailed();

    void inProgress();

    void onAuthToken(String authToken);

    void onErrorPermission(Intent intent);
  }
}
