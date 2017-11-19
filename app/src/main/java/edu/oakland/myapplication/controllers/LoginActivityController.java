package edu.oakland.myapplication.controllers;

import android.app.Activity;
import android.content.Context;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import edu.oakland.myapplication.screen_actions.LoginScreen;
import edu.oakland.myapplication.util.SharedPreferenceHelper;
import edu.oakland.myapplication.util.SpotifyScope;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.UserPrivate;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Matthew Fair on 11/18/2017.
 */

public class LoginActivityController {

    private static final String TAG = LoginActivityController.class.getSimpleName();

    private static final String CLIENT_ID = "6ebb0c251b6742dbbac3df964d636ea2";
    private static final String REDIRECT_URI = "myapp-spotifylogin://callback";
    public static final int REQUEST_CODE = 1337;

    private final SharedPreferenceHelper mSharedPreferenceHelper;
    private Context mContext;

    public LoginActivityController(Context context){
        mContext = context;
        mSharedPreferenceHelper = new SharedPreferenceHelper(context);
    }

    public void onLoginUserToSpotify(){
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{SpotifyScope.USER_READ_PRIVATE, SpotifyScope.STREAMING});

        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity((Activity) mContext, REQUEST_CODE, request);
    }

    public void onUserLoggedInSuccessfully(String accessToken){
        mSharedPreferenceHelper.saveSpotifyToken(accessToken);

        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(accessToken);
        SpotifyService spotifyService = api.getService();

        spotifyService.getMe(new Callback<UserPrivate>(){
            @Override
            public void success(UserPrivate userPrivate, Response response){
                mSharedPreferenceHelper.saveCurrentUserId(userPrivate.id);
            }
            @Override
            public void failure(RetrofitError error){

            }
        });

        ((LoginScreen) mContext).showMainScreen();
    }

    public void onCheckIfUserIsLoggedIn(){
        if(mSharedPreferenceHelper.getCurrentSpotifyToken().length() != 0){
            ((LoginScreen) mContext).showMainScreen();
        }
    }


}


