package com.example.android.notes;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "notes_table")  //by default table name will be Note(i.e. class name)
public class Note {

    //room will automatically generate columns for title,description ,etc.
    @PrimaryKey(autoGenerate = true)  //ids will be automatically generated
    private int id;      //uniquely identify each entry

    private String title;

    private String description;

    @ColumnInfo(name = "priority_column")
    private int priority;    //by default column name will be priority

    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
