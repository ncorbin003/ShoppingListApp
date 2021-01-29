package com.shoppinglistapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ListItem.class}, version = 1, exportSchema = false)
public abstract class ItemRoomDatabase extends RoomDatabase {

        public abstract ItemDao wordDao();

        private static volatile ItemRoomDatabase INSTANCE;
        private static final int NUMBER_OF_THREADS = 4;
        static final ExecutorService databaseWriteExecutor =
                Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        static ItemRoomDatabase getDatabase(final Context context) {
            if (INSTANCE == null) {
                //make sure when we are writing or reading to the database that it is protected
                //to reduce conflicts when multiple people are accessing the database
                synchronized (ItemRoomDatabase.class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                ItemRoomDatabase.class, "item_database")
                                .build();
                    }
                }
            }
            return INSTANCE;
        }
    }


