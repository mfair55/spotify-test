package edu.oakland.myapplication.util;

import android.arch.persistence.room.*;

/**
 * Created by Matthew Fair on 11/29/2017.
 */
@Dao
public interface SettingsDao {

    @Query("SELECT * FROM settings  where loggedIn LIKE :status")
    SettingsTable findByLoggedIn(Boolean status);

    @Insert
    void insertAll(SettingsTable... settingsTables);

    @Delete
    void delete(SettingsTable settingsTable);
}
