package com.example.android.notes;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM notes_table")    //to delete all notes from the table
    void deleteCompleteNotes();

    @Query("SELECT * FROM notes_table ORDER BY priority_column DESC") //* is used to select from all tables

    LiveData<List<Note>>  getAllNotes(); //this method is used to represent the the data in recycler view
                                         //livedata is used to observe the data like if there is any change in note_table,
                                          //then this list will be automatically updated and our activity will be notified
}
