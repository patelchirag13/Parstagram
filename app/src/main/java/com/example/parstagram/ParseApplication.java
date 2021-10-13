package com.example.parstagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("4VyIZdewguzRrmP3SuyOf94Q0BXNE8c0BsrNMoBf")
                .clientKey("L8kxqPh4n9PapqbNW9JaiIrTToTGsHGoidLOKchr")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
