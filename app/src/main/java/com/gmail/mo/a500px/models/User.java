package com.gmail.mo.a500px.models;

import com.google.gson.annotations.SerializedName;

public class User {
  private long id;
  private String username;
  private String firstname;
  private String lastname;
  private String city;
  private String country;
  private String fullname;
  @SerializedName("userpic_url") private String userpicUrl;
  @SerializedName("upgrade_status") private int upgradeStatus;
}
