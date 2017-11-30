package edu.oakland.myapplication.util;

import android.arch.persistence.room.*;
import android.content.Context;

/**
 * Created by Matthew Fair on 11/29/2017.
 */

@Database(entities = {SettingsTable.class}, version = 1)
public abstract class SettingsDatabase extends RoomDatabase {

    private static SettingsDatabase INSTANCE;

    public abstract SettingsDao settingsDao();

    public static SettingsDatabase getSettingsDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), SettingsDatabase.class, "settings-database") .allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance(){
        INSTANCE = null;
    }

}
