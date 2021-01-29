package com.shoppinglistapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "item_table")
public class ListItem {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ID")
    public String iD;
    @ColumnInfo(name = "item")
    public String checkString;
    @ColumnInfo(name = "isChecked")
    public boolean isChecked;



    public ListItem (@NonNull String checkString, boolean isChecked) {
        this.checkString = checkString;
        this.isChecked = isChecked;
        this.iD = UUID.randomUUID().toString().replace("-", "");
    }

    public String getCheckString() {
        return checkString;
    }

    public boolean isChecked() {
        return isChecked;
    }
}
