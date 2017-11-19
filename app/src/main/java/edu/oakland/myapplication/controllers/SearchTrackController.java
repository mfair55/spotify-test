package edu.oakland.myapplication.controllers;

import android.content.Context;

import edu.oakland.myapplication.screen_actions.SearchTrackURI;
import edu.oakland.myapplication.util.SharedPreferenceHelper;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Matthew Fair on 11/18/2017.
 */

public class SearchTrackController {

    private static final String TAG = SearchTrackController.class.getSimpleName();
    private final SharedPreferenceHelper mSharedPreferenceHelper;
    private final SearchTrackURI mSearchTrackURI;
    private final SpotifyService mSpotifyService;

    private String returnTrackURI = "";

    public SearchTrackController(SearchTrackURI searchTrackActivity){
        mSearchTrackURI = searchTrackActivity;
        mSharedPreferenceHelper = new SharedPreferenceHelper((Context) searchTrackActivity);

        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(mSharedPreferenceHelper.getCurrentSpotifyToken());
        mSpotifyService = api.getService();
    }

    public void searchURI(String trackName){
        mSpotifyService.searchTracks(trackName, new Callback<TracksPager>(){
            String trackURI;
            @Override
            public void success(TracksPager tracksPager, Response response){
               setTrackURI(tracksPager.tracks.items.get(0).uri);

                //mSearchTrackURI.setTrackURI(tracksPager.tracks.items.get(0).uri);
            }
            public void failure(RetrofitError error) {

            }

        });

    }

    public void setTrackURI(String newURI){
        returnTrackURI = newURI;
    }

    public String getTrackURI(){
        if(returnTrackURI != "")
            return returnTrackURI;
        else
            return "";
    }



}
