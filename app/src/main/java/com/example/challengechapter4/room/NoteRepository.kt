package com.example.challengechapter4.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NoteRepository(private val noteDatabaseDao: NoteDatabaseDao) {

    val readAllNotesAsc : LiveData<List<Note>> = noteDatabaseDao.getNotesAsc()
    val readAllNotesDesc : LiveData<List<Note>> = noteDatabaseDao.getNotesDesc()

    val readNote : MutableLiveData<List<Note>> = MutableLiveData()


    suspend fun insert(note : Note){
        noteDatabaseDao.insertNote(note)
    }

    suspend fun delete(note: Note){
        noteDatabaseDao.deleteNote(note)
    }

    suspend fun update(note: Note){
        noteDatabaseDao.updateNote(note)
    }

}