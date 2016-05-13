package com.github.karthyks.firebaseapp.model;

public class PostModel {

  private String userId;
  private String post;
  private long postedAt;

  public PostModel(String userId, String post) {
    this.userId = userId;
    this.post = post;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getPost() {
    return post;
  }

  public void setPosts(String posts) {
    this.post = posts;
  }

  public long getPostedAt() {
    return postedAt;
  }

  public void setPostedAt(long postedAt) {
    this.postedAt = postedAt;
  }
}
