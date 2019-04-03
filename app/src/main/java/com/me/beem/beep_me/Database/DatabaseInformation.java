package com.me.beem.beep_me.Database;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.Executors;

@Database(entities = {EntityInformation.class}, version = 1, exportSchema = false)
public abstract class DatabaseInformation extends RoomDatabase {

    private static DatabaseInformation INSTANCE;

    public abstract DaoInformation daoInformation();

    private Context context;


    public DatabaseInformation() {
        super();
    }

    public synchronized static DatabaseInformation getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    private static DatabaseInformation buildDatabase(final Context context) {
        return Room.databaseBuilder(context,
                DatabaseInformation.class,
                "TeachersDatabase")
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                getInstance(context).daoInformation().insertAll(EntityInformation.populateData());
                            }
                        });
                    }
                })
                .build();
    }

}
