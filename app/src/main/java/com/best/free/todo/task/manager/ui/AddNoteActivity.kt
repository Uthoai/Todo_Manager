package com.best.free.todo.task.manager.ui

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.room.Room
import com.best.free.todo.task.manager.database.NoteDao
import com.best.free.todo.task.manager.database.NoteData
import com.best.free.todo.task.manager.database.NoteDatabase
import com.best.free.todo.task.manager.databinding.ActivityAddNoteBinding
import java.util.Calendar

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var noteDao: NoteDao
    private var noteID = 0
    private var selectedDate = ""

    companion object{
        const val editKey = "edit"
        const val Update = "Update"
        const val save = "Save"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarLayout)

        val database = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            "noteDatabase"
        ).allowMainThreadQueries().build()
        noteDao = database.getDao()

        userEditMethod()        //check save or update

        binding.btnSaveNote.setOnClickListener {
            if (binding.editTextTitle.text.isNotEmpty()){
                saveNote()
            }else {
                Toast.makeText(this@AddNoteActivity, "Please fill up title", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnCalender.setOnClickListener {
            clickDatePicker()
        }
    }

    private fun userEditMethod() {
        if (intent.hasExtra(editKey)){
            val note = intent.getParcelableExtra<NoteData>(editKey)
            binding.apply {
                btnSaveNote.text = Update
                editTextTitle.setText(note?.title)
                editTextDetail.setText(note?.detail)
                tvPickUPDate.setText(note?.selectedDate)
                noteID = note?.id ?:0
            }
        } else{
            binding.btnSaveNote.text = save
        }
    }

    private fun saveNote() {
        val title = binding.editTextTitle.text.toString()
        val detail = binding.editTextDetail.text.toString()
        val sDate = binding.tvPickUPDate.text.toString()

        if (binding.btnSaveNote.text.toString() == save){
            addNote(title,detail,sDate)
        } else{
            upadteNote(title,detail,sDate)
        }
        val intent = Intent(this@AddNoteActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun clickDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(
            this,
            { view, selectedYear, selectedMonth, selectedDayOfMonth ->
                selectedDate = "${selectedDayOfMonth}/${selectedMonth + 1}/${selectedYear}"
                binding.tvPickUPDate.text = selectedDate
            },
            year,
            month,
            day
        ).show()
    }

    private fun upadteNote(title: String, detail: String,selectedDate: String) {
        val edit = NoteData(noteID,title,detail,selectedDate)
        noteDao.editNote(edit)
    }

    private fun addNote(title: String, detail: String,selectedDate: String) {
        val note = NoteData(0,title,detail,selectedDate)
        noteDao.addNote(note)
    }
}
