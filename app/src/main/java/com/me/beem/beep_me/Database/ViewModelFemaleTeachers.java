package com.me.beem.beep_me.Database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class ViewModelFemaleTeachers extends AndroidViewModel {
    private DatabaseRepository databaseRepository;
    private LiveData<List<EntityInformation>> femaleTeachers;
    private LiveData<List<EntityInformation>> femaleEmail;

    public ViewModelFemaleTeachers (@NonNull Application application) {
        super(application);
        databaseRepository = new DatabaseRepository(application);
        femaleTeachers = databaseRepository.getSearchEntitiesFemale();
        femaleEmail = databaseRepository.getGetAllEmail();
    }

    public LiveData<List<EntityInformation>> getFemaleTeachers(){
        return femaleTeachers;
    }

    public LiveData<List<EntityInformation>> getFemaleEmail(){
        return femaleEmail;
    }
}
