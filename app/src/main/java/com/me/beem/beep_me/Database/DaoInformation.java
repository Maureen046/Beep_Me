package com.me.beem.beep_me.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;

import java.util.List;

@Dao
public interface DaoInformation {
    @Query("SELECT * FROM TeachersDatabase")
    LiveData<List<EntityInformation>> getAll();


    @Query("SELECT * FROM TeachersDatabase where first_name LIKE ' % '  || :firstNameSearch  ||' % ' OR middle_name LIKE " +
            "' % ' || :middleNameSearch || '%' OR last_name LIKE ' % ' || :lastNameSearch || ' % '")
    LiveData<List<EntityInformation>> getByName(String firstNameSearch, String middleNameSearch, String lastNameSearch);

    @Query("SELECT * FROM TeachersDatabase WHERE gender = 'Female'")
    LiveData<List<EntityInformation>> getAllFemale();

    @Query("SELECT * FROM TeachersDatabase WHERE gender = 'Male'")
    LiveData<List<EntityInformation>> getAllMale();

    @Query("SELECT email FROM TeachersDatabase")
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    LiveData<List<EntityInformation>> getAllEmail();

    @Insert
    void insertAll(EntityInformation... entityInformations);

//    @Query("SELECT * FROM TeachersDatabase where last_name ='lastNameString'")
//    LiveData<String> getLastName(String lastNameString);

//    @Query("SELECT * FROM TeachersDatabase where first_name LIKE ' % ' + :firstName +' % ' OR middle_name LIKE " +
//            "' % ' +:middleName + '%' OR last_name LIKE ' % ' +:lastName + ' % '")
//    List<EntityInformation> getByName();
}
