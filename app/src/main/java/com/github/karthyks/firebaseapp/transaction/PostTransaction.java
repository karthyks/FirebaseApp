package com.github.karthyks.firebaseapp.transaction;

import android.util.Log;

import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;
import com.github.karthyks.firebaseapp.model.PostModel;

import java.util.HashMap;
import java.util.Map;

public class PostTransaction {

  public static final String URL = "https://blazing-inferno-8424.firebaseio.com/";
  private static final String TAG = PostTransaction.class.getSimpleName();

  public static void pushPost(PostModel post) {
    Firebase ref = new Firebase(URL);
    Firebase postsRef = ref.child("posts");
    Firebase newPostRef = postsRef.push();
    newPostRef.setValue(post);
    Firebase timeRef = ref.child("posts").child(newPostRef.getKey());
    Map<String, Object> time = new HashMap<>();
    time.put("postedAt", ServerValue.TIMESTAMP);
    timeRef.updateChildren(time);
    Log.d(TAG, "pushPost: " + newPostRef.getKey());
  }
}
