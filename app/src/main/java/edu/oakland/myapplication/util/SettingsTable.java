package edu.oakland.myapplication.util;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Matthew Fair on 11/29/2017.
 */
@Entity(tableName = "settings")
public class SettingsTable {
    @PrimaryKey(autoGenerate = false)
    private int uid;

    @ColumnInfo(name = "accessToken")
    private String accessToken;

    @ColumnInfo(name = "clientID")
    private String clientID;

    @ColumnInfo(name = "playlistID")
    private String playlistID;

    @ColumnInfo(name = "uriResult")
    private String uriResult;

    @ColumnInfo(name = "loggedIn")
    private Boolean loggedIn;

    public int getUid(){
        return uid;
    }

    public String getAccessToken(){
        return accessToken;
    }

    public String getClientID(){
        return clientID;
    }

    public String getPlaylistID(){
        return playlistID;
    }

    public String getUriResult(){
        return uriResult;
    }

    public Boolean getLoggedIn(){
        return loggedIn;
    }

    public void setUid(int newUid){
        uid = newUid;
    }

    public void setAccessToken(String newAccessToken){
        accessToken = newAccessToken;
    }

    public void setClientID(String newClientId){
        clientID = newClientId;
    }

    public void setPlaylistID(String newPlaylistId){
        playlistID = newPlaylistId;
    }

    public void setUriResult(String newUriResult){
        uriResult = newUriResult;
    }

    public void setLoggedIn(Boolean newStatus){
        loggedIn = newStatus;
    }
}
