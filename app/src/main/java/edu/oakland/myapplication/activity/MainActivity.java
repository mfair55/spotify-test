package edu.oakland.myapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import edu.oakland.myapplication.R;
import edu.oakland.myapplication.controllers.SearchTrackController;
import edu.oakland.myapplication.screen_actions.SearchTrackURI;
import edu.oakland.myapplication.util.SearchResults;
import edu.oakland.myapplication.util.Settings;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.TracksPager;

import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends Activity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback
{

    private Button playButton, pauseButton, searchButton;
    public TextView status;
    private EditText trackSearch;
    private SearchActivity search;

    private String spotifyClientId = "";
    private String spotifyClientToken = "";


    private static final String FILE_NAME = "Settings.dat";
    private File file;






    private static final String CLIENT_ID = "6ebb0c251b6742dbbac3df964d636ea2";
    private static final String REDIRECT_URI = "myapp-spotifylogin://callback";

    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;

    private Player mPlayer;
    private SpotifyApi api = new SpotifyApi();
    private SpotifyService apiService = api.getService();
    private String userID,result;
    private String resultUri = "";
    private Settings s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        s = new Settings("default","value");

        file = new File(getFilesDir(), FILE_NAME);



            AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
            builder.setScopes(new String[]{"user-read-private", "streaming"});
            final AuthenticationRequest request = builder.build();


            AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        searchButton = (Button) findViewById(R.id.searchButton);
        EditText trackSearch = (EditText) findViewById(R.id.trackSearch);
        final String searchVariable = trackSearch.getText().toString();
        searchButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                status = (TextView) findViewById(R.id.status);
                status.setText("");
                try{
                    s = s.getSettings(file);
                } catch (Exception e){
                    e.printStackTrace();
                }
                SearchResults searchResults = new SearchResults();
                SearchTrackController stc = new SearchTrackController(s, searchResults);
                stc.SearchTrack("Home (with Machine Gun Kelly");
                resultUri = searchResults.getUri();
                //SearchTrackString("Home (with Machine Gun Kelly");
            }
        });

        playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(mPlayer.getPlaybackState().positionMs <= 0) {
                    mPlayer.playUri(null, resultUri, 0, 0);
                }
                else
                    mPlayer.resume(mOperationCallback);

            }
        });

        pauseButton = (Button) findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if(mPlayer.getPlaybackState().isPlaying)
                    mPlayer.pause(mOperationCallback);
            }
        });
    }


    private final Player.OperationCallback mOperationCallback = new Player.OperationCallback() {
        @Override
        public void onSuccess() {

        }

        @Override
        public void onError(Error error) {

        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {


                spotifyClientToken = response.getAccessToken();


                api.setAccessToken(spotifyClientToken);
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(MainActivity.this);
                        mPlayer.addNotificationCallback(MainActivity.this);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }

            SpotifyService spotifyService = api.getService();

            spotifyService.getMe(new Callback<UserPrivate>(){
                @Override
                public void success(UserPrivate userPrivate, Response response){
                    spotifyClientId = userPrivate.id;
                }
                @Override
                public void failure(RetrofitError error){
                    spotifyClientId = "Error";
                }
            });

            Settings s = new Settings(spotifyClientToken, spotifyClientId);
            s.saveSettings(s, file);

        }
    }


    public void SearchTrackString(String trackName){
        apiService.searchTracks(trackName, new Callback<TracksPager>(){
            @Override
            public void success(TracksPager tracksPager, Response response){
                resultUri = tracksPager.tracks.items.get(0).uri;
            }

            @Override
            public void failure(RetrofitError error){

            }
        });
    }



    @Override
    protected void onDestroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            default:                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("MainActivity", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");

        //mPlayer.playUri(null, resultUri, 0, 0);

    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error error) {

    }


    public void onLoginFailed(int i) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }
}