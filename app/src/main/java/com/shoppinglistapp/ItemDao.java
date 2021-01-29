package com.shoppinglistapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ListItem item);

    @Query("DELETE FROM item_table")
    void deleteAll();

    @Query("SELECT * FROM item_table")
    LiveData<List<ListItem>> getItems();

    @Query("SELECT * FROM item_table")
    List<ListItem> getList();
}
