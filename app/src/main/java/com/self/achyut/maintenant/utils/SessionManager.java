package com.self.achyut.maintenant.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.self.achyut.maintenant.activity.Login;
import com.self.achyut.maintenant.domain.Landlord;

public class SessionManager {

    private static SessionManager sessionManager;
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "LoginPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";


    public static final String KEY_PASSWORD = "password";

    public static final String KEY_EMAIL = "email";

    public static final String KEY_ID = "participantID";


    public static SessionManager getInstance(Context mContext)
    {
        if(sessionManager == null )
        {
            sessionManager = new SessionManager(mContext);
        }
        return  sessionManager;
    }



    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(Landlord landlord){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        Gson gson = new Gson();
        String json = gson.toJson(landlord);
        editor.putString(Constants.KEY_LANDLORD,json);

        editor.commit();
    }

    public boolean checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){

            return false;

        }else
            return true;


    }

    public  Landlord getLandlordFromPref(){
        Gson gson = new Gson();
        String json = pref.getString(Constants.KEY_LANDLORD,"");
        return gson.fromJson(json,Landlord.class);
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }



}
