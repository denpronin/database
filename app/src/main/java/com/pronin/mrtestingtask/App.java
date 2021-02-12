package com.pronin.mrtestingtask;

import android.app.Application;
import androidx.room.Room;
import com.pronin.mrtestingtask.dagger.AppComponent;
import com.pronin.mrtestingtask.dagger.DaggerAppComponent;
import com.pronin.mrtestingtask.database.StaffDataBase;

public class App extends Application {

    private static App instance;
    private static AppComponent component;
    private StaffDataBase database;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.create();
        instance = this;
        database = Room.databaseBuilder(this, StaffDataBase.class, StaffDataBase.DB_NAME).build();
    }

    public static App getInstance() {
        return instance;
    }

    public static AppComponent getComponent() {
        return component;
    }

    public StaffDataBase getDatabase() {
        return database;
    }
}
