package com.pronin.mrtestingtask.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = { Staff.class },version = StaffDataBase.VERSION)
public abstract class StaffDataBase extends RoomDatabase {

    public static final int VERSION = 1;
    public static final String DB_NAME = "staffDB";

    public abstract StaffDao getStaffDao();
}
