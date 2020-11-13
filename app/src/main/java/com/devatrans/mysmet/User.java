package com.devatrans.mysmet;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("success")
    private String Success;

    @SerializedName("name")
    private String Name;

    @SerializedName("email")
    private String Email;

    @SerializedName("password")
    private String Password;

    @SerializedName("id")
    private Integer Id;

    @SerializedName("active")
    private Boolean Active;

    @SerializedName("token")
    private String Token;

    public User(String name, String email, String password) {
        Name = name;
        Email = email;
        Password = password;
    }

    public String getSuccess() {
        return Success;
    }

    public String getName() {
        return Name;
    }

    public Integer getId() {return Id;}


}

