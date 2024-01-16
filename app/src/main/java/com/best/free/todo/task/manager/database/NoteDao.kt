package com.best.free.todo.task.manager.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {

    @Insert
    fun addNote(noteData: NoteData)

    @Update
    fun editNote(noteData: NoteData)

    @Delete
    fun deleteNote(noteData: NoteData)

    @Query("SELECT * FROM notedata")
    fun getAllNote(): MutableList<NoteData>
}