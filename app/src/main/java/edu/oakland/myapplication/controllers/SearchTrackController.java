package edu.oakland.myapplication.controllers;

import java.io.File;

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
    private File file;



    public SearchTrackController(Settings s, File newFile){
        savedSettings = s;
        SpotifyApi api  = new SpotifyApi();
        api.setAccessToken(savedSettings.getAccessToken());
        apiService = api.getService();
        file = newFile;
    }

    public void SearchTrack(String trackName) {

        apiService.searchTracks(trackName, new Callback<TracksPager>(){
            @Override
            public void success(TracksPager tracksPager, Response response){
                savedSettings.setTrackName(tracksPager.tracks.items.get(0).name);
                savedSettings.setTrackArtist(tracksPager.tracks.items.get(0).artists.get(0).name);
                savedSettings.setUriResult(tracksPager.tracks.items.get(0).uri);
                savedSettings.saveSettings(savedSettings, file);
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
