package edu.oakland.myapplication.activity;

import android.app.Activity;
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

import edu.oakland.myapplication.R;
import edu.oakland.myapplication.controllers.SearchTrackController;
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
    private static final String CLIENT_ID = "6ebb0c251b6742dbbac3df964d636ea2";
    private static final String REDIRECT_URI = "myapp-spotifylogin://callback";
    private static final int REQUEST_CODE = 1337;
    private static final String FILE_NAME = "Settings.dat";
    private static final String PLAYLIST_NAME = "Wabam";



    private Button playButton, pauseButton, searchButton, resumeButton;
    public TextView title, artist;
    private EditText trackSearch, artistSearch;

    private String spotifyClientToken;
    private Player mPlayer;
    private SpotifyApi api = new SpotifyApi();
    private SpotifyService apiService = api.getService();

    private Settings s;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        file = new File(getFilesDir(), FILE_NAME);

            AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
            builder.setScopes(new String[]{"user-read-private", "streaming"});
            final AuthenticationRequest request = builder.build();
            AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        searchButton = (Button) findViewById(R.id.searchButton);
        trackSearch = (EditText) findViewById(R.id.trackSearch);
        artistSearch = (EditText) findViewById(R.id.editText2);
        searchButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                s = s.getSettings(file);
                SearchTrackController stc = new SearchTrackController(s, file);
                stc.SearchTrack(trackSearch.getText().toString(), artistSearch.getText().toString());
            }
        });

        playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                s = s.getSettings(file); //Reload file in case search changed.

                title = (TextView) findViewById(R.id.trackTitle);
                title.setText(s.getTrackName());

                artist = (TextView) findViewById(R.id.artistName);
                artist.setText(s.getTrackArtist());

                mPlayer.playUri(null, s.getUriResult(), 0, 0);
            }
        });

        resumeButton = (Button) findViewById(R.id.resumeButton);
        resumeButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if(!mPlayer.getPlaybackState().isPlaying) {
                    mPlayer.resume(mOperationCallback);
                }
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
                s = new Settings(spotifyClientToken);

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


            apiService.getMe(new Callback<UserPrivate>(){
                @Override
                public void success(UserPrivate userPrivate, Response response){
                    s.setUserID(userPrivate.id);
                    s.saveSettings(s, file);
                }
                @Override
                public void failure(RetrofitError error){

                }
            });

            s.saveSettings(s, file);
        }
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