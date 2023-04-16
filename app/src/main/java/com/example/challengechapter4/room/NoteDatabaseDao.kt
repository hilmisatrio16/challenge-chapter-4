package com.example.challengechapter4.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNote(note : Note)

    @Update
    fun updateNote(note: Note)

    @Query("SELECT * FROM note_table ORDER BY UPPER(judul) ASC")
    fun getNotesAsc() : LiveData<List<Note>>

    @Query("SELECT * FROM note_table ORDER BY UPPER(judul) DESC")
    fun getNotesDesc() : LiveData<List<Note>>

    @Query("SELECT * FROM note_table WHERE id = :noteId")
    fun getNote(noteId : Int) : Note

    @Delete
    fun deleteNote(note : Note)
}