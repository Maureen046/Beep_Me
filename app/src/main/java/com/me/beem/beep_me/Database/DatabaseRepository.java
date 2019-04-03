package com.me.beem.beep_me.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.util.List;




public class DatabaseRepository {
    private DaoInformation daoInformation;
    private LiveData<List<EntityInformation>> searchAll;
    private LiveData<List<EntityInformation>> searchEntities;
    private LiveData<List<EntityInformation>> searchEntitiesFemale;
    private LiveData<List<EntityInformation>> searchEntitiesMale;
    private LiveData<List<EntityInformation>> getAllEmail;

    private String firstNameSearch;
    private String middlenameSearch;
    private String lastNameSearch;

    public DatabaseRepository(Application application) {
        DatabaseInformation databaseInformation = DatabaseInformation.getInstance(application);
        daoInformation = databaseInformation.daoInformation();

        searchAll = daoInformation.getAll();
        searchEntities = daoInformation.getByName(firstNameSearch, middlenameSearch, lastNameSearch);
        searchEntitiesFemale = daoInformation.getAllFemale();
        searchEntitiesMale = daoInformation.getAllMale();
        getAllEmail = daoInformation.getAllEmail();
    }

    public LiveData<List<EntityInformation>> getSearchAll(){
        return searchAll;
    }

    public LiveData<List<EntityInformation>> getSearchEntities(){
        return searchEntities;
    }

    public LiveData<List<EntityInformation>> getSearchEntitiesFemale(){
        return searchEntitiesFemale;
    }

    public LiveData<List<EntityInformation>> getSearchEntitiesMale(){
        return searchEntitiesMale;
    }

    public LiveData<List<EntityInformation>> getGetAllEmail(){
        return getAllEmail;
    }

}
