package edu.oakland.myapplication.controllers;

import android.content.Context;

import edu.oakland.myapplication.util.SearchResults;
import edu.oakland.myapplication.util.Settings;
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

    private static String resultUri;
    private SpotifyService apiService;
    private Settings savedSettings;
    private SearchResults searchResults;


    public SearchTrackController(Settings s, SearchResults newResults){
        savedSettings = s;
        searchResults = newResults;
        SpotifyApi api  = new SpotifyApi();
        api.setAccessToken(savedSettings.getAccessToken());
        apiService = api.getService();
    }

    public void SearchTrack(String trackName) {

        apiService.searchTracks(trackName, new Callback<TracksPager>(){
            @Override
            public void success(TracksPager tracksPager, Response response){
                searchResults.SetUri(tracksPager.tracks.items.get(0).uri);
            }

            @Override
            public void failure(RetrofitError error){

            }
        });
    }

    public String getResultUri(){
        return resultUri;
    }





}
