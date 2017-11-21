package edu.oakland.myapplication.util;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.security.auth.callback.Callback;

import edu.oakland.myapplication.activity.MainActivity;

/**
 * Created by Matthew Fair on 11/20/2017.
 */

public class Settings implements Serializable {

    private String accessToken, userID;

    public Settings(String accessToken, String userID){
        setAccessToken(accessToken);
        setUserID(userID);
    }

    public Settings(){
        accessToken = null;
        userID = null;
    }

    public void setAccessToken(String newAccessToken){
        accessToken = newAccessToken;
    }

    public void setUserID(String newUserID){
        userID = newUserID;
    }

    public String getAccessToken(){
        return accessToken;
    }

    public String getUserID(){
        return userID;
    }

    public void saveSettings(Settings newSettings, File newFile){
        try{
            FileOutputStream outStream = new FileOutputStream(newFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
            objectOutputStream.writeObject(newSettings);
            objectOutputStream.close();
        } catch (Exception E){
                E.printStackTrace();
        }
    }

    public Settings getSettings(File newFile){
        Settings s = null;
        try{
            FileInputStream inStream = new FileInputStream(newFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(inStream);
            s = (Settings) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception E){
            E.printStackTrace();
        }

        return s;
    }

}
