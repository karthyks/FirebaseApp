package com.github.karthyks.firebaseapp.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.karthyks.firebaseapp.BaseAppCompatActivity;
import com.github.karthyks.firebaseapp.FirebaseApp;
import com.github.karthyks.firebaseapp.R;
import com.github.karthyks.firebaseapp.model.PostModel;
import com.github.karthyks.firebaseapp.model.UserModel;
import com.github.karthyks.firebaseapp.transaction.PostTransaction;
import com.github.karthyks.firebaseapp.transaction.UserTransaction;
import com.github.karthyks.firebaseapp.utils.PermissionUtils;

public class MainActivity extends BaseAppCompatActivity implements IMainView {

  private static final String TAG = MainActivity.class.getSimpleName();
  private TextView hello;
  private Button btnContact;
  private boolean mPermissionDenied = false;

  private static final int CONTACTS_CODE = 101;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    injectViews();
  }

  private void injectViews() {
    hello = (TextView) findViewById(R.id.hello);
    assert hello != null;
    hello.setText(FirebaseApp.getGoogleUtils().getGoogleSignInAccount().getDisplayName());
    btnContact = (Button) findViewById(R.id.btn_add_contact);
    assert btnContact != null;
    btnContact.setOnClickListener(this);
    new UploadUser().execute(UserModel.fromAccount(
        FirebaseApp.getGoogleUtils().getGoogleSignInAccount()));
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_add_contact:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
              != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.initWith(PermissionUtils.PERMISSION_CONTACTS);
            PermissionUtils.requestPermission(this, PermissionUtils.PERMISSION_CONTACTS,
                Manifest.permission.READ_CONTACTS, true);
          } else {
            openContact();
          }
        } else {
          openContact();
        }
        break;
      default:
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    if (requestCode != PermissionUtils.PERMISSION_CONTACTS) {
      return;
    }

    if (PermissionUtils.isPermissionGranted(permissions, grantResults,
        Manifest.permission.READ_CONTACTS)) {
      openContact();
    } else {
      mPermissionDenied = true;
    }
  }


  @Override
  public void openContact() {
    new UploadPost().execute(new PostModel(FirebaseApp.getGoogleUtils().getGoogleSignInAccount()
        .getId(), "Postssssssssssssssssssssssssssssssssssssssssssss"));
  }

  @Override
  protected void onResumeFragments() {
    super.onResumeFragments();
    if (mPermissionDenied) {
      // Permission was not granted, display error dialog.
      showMissingPermissionError();
      mPermissionDenied = false;
    }
  }


  private void showMissingPermissionError() {
    PermissionUtils.PermissionDeniedDialog
        .newInstance(true).show(getSupportFragmentManager(), "dialog");
  }

  private class UploadUser extends AsyncTask<UserModel, Void, Void> {

    private final String TAG = UploadUser.class.getSimpleName();

    @Override protected Void doInBackground(UserModel... params) {
      try {
        UserTransaction.setUser(params[0]);
      } catch (Exception e) {
        Log.d(TAG, "doInBackground: " + e.toString());
      }
      return null;
    }
  }

  private class UploadPost extends AsyncTask<PostModel, Void, Void> {

    private final String TAG = UploadUser.class.getSimpleName();

    @Override protected Void doInBackground(PostModel... params) {
      try {
        PostTransaction.pushPost(params[0]);
      } catch (Exception e) {
        Log.d(TAG, "doInBackground: " + e.toString());
      }
      return null;
    }
  }
}
