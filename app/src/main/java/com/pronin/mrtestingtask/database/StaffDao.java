package com.pronin.mrtestingtask.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface StaffDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertStaff(Staff staff);

    @Insert
    void insertStaffList(List<Staff> staffList);

    @Update
    Completable updateStaff(Staff staff);

    @Query("SELECT * FROM Staff WHERE (Staff.city LIKE :search || '%' " +
            "OR Staff.first_name LIKE :search || '%' OR Staff.last_name LIKE :search || '%' OR Staff.office LIKE :search || '%' " +
            "OR Staff.position LIKE :search || '%') ORDER BY CASE WHEN :sort = 'Staff.first_name' THEN Staff.first_name " +
            "WHEN :sort = 'Staff.last_name' THEN Staff.last_name WHEN :sort = 'Staff.city' THEN Staff.city " +
            "WHEN :sort = 'Staff.office' THEN Staff.office WHEN :sort = 'Staff.position' THEN Staff.position END")
    Single<List<Staff>> getStaffList(String search, String sort);

}
