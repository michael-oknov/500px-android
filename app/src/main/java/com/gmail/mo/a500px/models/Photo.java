package com.gmail.mo.a500px.models;

import com.google.gson.annotations.SerializedName;

public class Photo {
  private long id;
  private String name;
  private String description;
  @SerializedName("times_viewed") private int timesViewed;
  private double rating;
  @SerializedName("created_at") private String createdAt;
  private int category;
  private boolean privacy;
  private int width;
  private int height;
  @SerializedName("votes_count") private int votesCount;
  @SerializedName("comments_count") private int commentsCount;
  private boolean nsfw;
  @SerializedName("image_url") private String imageUrl;
  private User user;

  public String getName() {
    return name;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }
}
