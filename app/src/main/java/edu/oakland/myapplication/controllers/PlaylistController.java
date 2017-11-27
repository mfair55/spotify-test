package edu.oakland.myapplication.controllers;

/**
 * Created by Matthew Fair on 11/22/2017.
 */

import java.io.File;
import java.util.HashMap;

import edu.oakland.myapplication.util.Settings;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PlaylistController {


    private SpotifyService apiService;
    private Settings s;
    private File file;
    private final String PLAYLIST_NAME = "Wabam";

    public PlaylistController(Settings settings, File f){
        s = settings;
        file = f;
        SpotifyApi api  = new SpotifyApi();
        api.setAccessToken(s.getAccessToken());
        apiService = api.getService();
    }

    public void getPlaylistID(){
        apiService.getMyPlaylists(new Callback<Pager<PlaylistSimple>>(){
            @Override
            public void success(Pager<PlaylistSimple> playlistSimplePager, Response response){
                for(int i = 0; i < playlistSimplePager.items.size(); i++) {
                    if (playlistSimplePager.items.get(i).name.equals(PLAYLIST_NAME)) {
                        s.setPlaylistID(playlistSimplePager.items.get(i).id);
                        System.out.println("GET: " + playlistSimplePager.items.get(i).id);
                        s.saveSettings(s , file);
                    }
                }
            }
            @Override
            public void failure(RetrofitError error){

            }
        });

    }

    public void createPlaylist(){
        if(s.getPlaylistID() == null) {
            HashMap<String, Object> playlistParams = new HashMap<String, Object>();
            playlistParams.put("name", PLAYLIST_NAME);
            playlistParams.put("public", false);

            apiService.createPlaylist(s.getUserID(), playlistParams, new Callback<Playlist>() {
                @Override
                public void success(Playlist playlist, Response response) {
                    System.out.println("CREATE: " + playlist.id);
                    s.setPlaylistID(playlist.id);
                    s.saveSettings(s, file);
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
            System.out.println("PRINT: " + s.getPlaylistID());
    }

    public void addToPlaylist(String trackUri){

            getPlaylistID();
            HashMap parametersMap = new HashMap();
            parametersMap.put("uris", trackUri);
            apiService.addTracksToPlaylist(s.getUserID(), s.getPlaylistID(), parametersMap, new HashMap<String, Object>(), new Callback<Pager<PlaylistTrack>>() {
                @Override
                public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {

                }

                @Override
                public void failure(RetrofitError error) {

                }
            });


            //System.out.println("Playlist does not exist");

    }



}
