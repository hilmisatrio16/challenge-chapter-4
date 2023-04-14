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
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    val readAllNotesAsc: LiveData<List<Note>>
    val readAllNotesDesc: LiveData<List<Note>>

    private val repository: NoteRepository

    val noteById : LiveData<List<Note>>


    private val _sortType = MutableLiveData<String>()
    val sortType : LiveData<String>get() = _sortType


    init {
        val noteDao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
        readAllNotesAsc = repository.readAllNotesAsc
        readAllNotesDesc = repository.readAllNotesDesc
        noteById = repository.readNote
    }

//    masih salah
//    fun getNote(id : Int) = viewModelScope.launch(Dispatchers.IO){
//        repository.getNote(id)
//    }

    fun setSortType(sort : String){
        _sortType.value = sort
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(note)
        }
    }

    fun insertNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.insert(note)
        }
    }


}