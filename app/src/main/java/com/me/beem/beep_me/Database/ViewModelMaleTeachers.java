package com.me.beem.beep_me.Database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class ViewModelMaleTeachers extends AndroidViewModel {
    private DatabaseRepository databaseRepository;
    private LiveData<List<EntityInformation>> maleTeachers;

    public ViewModelMaleTeachers (@NonNull Application application) {
        super(application);
        databaseRepository = new DatabaseRepository(application);
        maleTeachers = databaseRepository.getSearchEntitiesMale();
    }

    public LiveData<List<EntityInformation>> getMaleTeachers(){
        return maleTeachers;
    }
}
