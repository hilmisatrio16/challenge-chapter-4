package com.example.challengechapter4.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true) var id : Int = 0,
    @ColumnInfo(name = "judul") var judul : String,
    @ColumnInfo(name = "catatan") var catatan : String
)
