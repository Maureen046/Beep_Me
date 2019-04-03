package com.me.beem.beep_me.Database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class ViewModelSearch extends AndroidViewModel {
    private DatabaseRepository databaseRepository;
    private LiveData<List<EntityInformation>> searchResults;

    public ViewModelSearch(@NonNull Application application) {
        super(application);
        databaseRepository = new DatabaseRepository(application);
        searchResults = databaseRepository.getSearchEntities();
    }

    public LiveData<List<EntityInformation>> getSearchResults() {
        return searchResults;
    }
}

