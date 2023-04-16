package com.example.challengechapter4

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.challengechapter4.room.Note
import com.example.challengechapter4.room.NoteDatabase
import com.example.challengechapter4.room.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    val readAllNotesAsc: LiveData<List<Note>>
    val readAllNotesDesc: LiveData<List<Note>>

    private val repository: NoteRepository

    private val noteById : MutableLiveData<Note>

    private val _sortType = MutableLiveData<String>()
    val sortType : LiveData<String>get() = _sortType


    init {
        val noteDao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
        readAllNotesAsc = repository.readAllNotesAsc
        readAllNotesDesc = repository.readAllNotesDesc
        noteById = MutableLiveData()
    }

    fun getNoteByIdObservers() : MutableLiveData<Note>{
        return noteById
    }

    fun getNoteById(id : Int){
        GlobalScope.launch {
            val noteDao = NoteDatabase.getDatabase(getApplication())!!.noteDao()
            val listNote = noteDao.getNote(id)
            noteById.postValue(listNote)
        }
    }

    fun setSortType(sort : String){
        _sortType.value = sort
    }

    fun deleteNote(note: Note) {
        viewModelScope.async(Dispatchers.IO) {
            repository.delete(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.async(Dispatchers.IO) {
            repository.update(note)
        }
    }

    fun insertNote(note: Note) {
        viewModelScope.async(Dispatchers.IO)
        {
            repository.insert(note)
        }
    }


}