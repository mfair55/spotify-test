package edu.oakland.myapplication.activity;

/**
 * Created by Matthew Fair on 11/12/2017.
 */


import edu.oakland.myapplication.util.SharedPreferenceHelper;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SearchActivity {
    private final SpotifyService mSpotifyService;
    private SharedPreferenceHelper mSharedPreferences;
    private String result;
    private String resultUri;

    public SearchActivity(){
        SpotifyApi mSpotifyApi = new SpotifyApi();
        mSpotifyApi.setAccessToken(mSharedPreferences.getCurrentSpotifyToken());
        mSpotifyService = mSpotifyApi.getService();
    }

    public String getResult(){
        return result;
    }
    public String getResultUri(){
        return resultUri;
    }

    public void SearchTrackString(String trackName){
           mSpotifyService.searchTracks(trackName, new Callback<TracksPager>(){
           @Override
            public void success(TracksPager tracksPager, Response response){
               result = tracksPager.tracks.items.get(0).toString();
               resultUri = tracksPager.tracks.items.get(0).uri;
           }

           @Override
           public void failure(RetrofitError error){

           }
        });
    }

}
