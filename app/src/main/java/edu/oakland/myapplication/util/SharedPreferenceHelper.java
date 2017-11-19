package edu.oakland.myapplication.util;

/**
 * Created by Matthew Fair on 11/12/2017.
 */


import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferenceHelper {

    public static final String PREFERENCES = "SpotifyPreferences";

    public static final String CURRENT_SPOTIFY_TOKEN_KEY = "CurrentSpotifyToken";
    public static final String CURRENT_USER_SPOTIFY_ID_KEY = "CurrentSpotifyID";

    private Context mContext;
    private SharedPreferences mSharedPreferences;

    public SharedPreferenceHelper(Context context){
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(PREFERENCES, 0);
    }

    public void saveSpotifyToken(String token){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(CURRENT_SPOTIFY_TOKEN_KEY, token);
        editor.apply();
    }

    public void saveCurrentUserId(String id) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(CURRENT_USER_SPOTIFY_ID_KEY, id);
        editor.apply();
    }

    public String getCurrentSpotifyToken() {
        return mSharedPreferences.getString(CURRENT_SPOTIFY_TOKEN_KEY, "");
    }

    public String getCurrentUserId() {
        return mSharedPreferences.getString(CURRENT_USER_SPOTIFY_ID_KEY, "");
    }


}
