package com.mars.rool.criminalintent.model;

public class User {
    private static final String PASSWORD_ACCESSOR = "js2o3$4d-i46=fh@hks№ad[an@ao_d45u:ig#qw25kdhj32vas";
    private final String mEmail;
    private final String mPassword;

    public User(String email, String password) {
        mEmail = email;
        mPassword = password;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPassword(String key) {
        if (key == PASSWORD_ACCESSOR)
            return mPassword;
        else
            return "";
    }
}
