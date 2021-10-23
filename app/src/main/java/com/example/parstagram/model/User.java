package com.example.parstagram.model;

import com.parse.ParseFile;
import com.parse.ParseUser;

public class User extends ParseUser {
    public static final String KEY_PROFILE_IMAGE = "profilePic";


    public ParseFile getProfileImage(){
        return getParseFile(KEY_PROFILE_IMAGE);
    }
}
