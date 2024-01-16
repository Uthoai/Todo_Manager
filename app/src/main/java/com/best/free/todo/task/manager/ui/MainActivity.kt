 package com.best.free.todo.task.manager.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.room.Room
import com.best.free.todo.task.manager.R
import com.best.free.todo.task.manager.database.NoteDao
import com.best.free.todo.task.manager.database.NoteData
import com.best.free.todo.task.manager.database.NoteDatabase
import com.best.free.todo.task.manager.databinding.ActivityMainBinding
import com.best.free.todo.task.manager.recycler_adapter.NoteAdapter
import java.text.DateFormat
import java.util.Calendar
import java.util.Collections

 class MainActivity : AppCompatActivity(), NoteAdapter.HandleUserClick {
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var noteDao: NoteDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarLayout)      //toolbar Support
        currentDateTime()                               //current Date & Time

        val database = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            "noteDatabase"
        ).allowMainThreadQueries().build()
        noteDao = database.getDao()

        setNoteView()       //call RecyclerView Data function

        binding.btnAddNote.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddNoteActivity::class.java))
        }
    }

     //Sorting RecyclerView Data
     private fun sorting(){
         noteDao.getAllNote().let { noteList->
             noteList.sortBy {
                 it.title
             }
             noteAdapter = NoteAdapter(this@MainActivity,noteList)
             binding.recyclerView.adapter = noteAdapter
         }
     }

     //set note date on recycler view
     private fun setNoteView() {
         noteDao.getAllNote().let { noteList->
             noteAdapter = NoteAdapter(this@MainActivity,noteList)
             binding.recyclerView.adapter = noteAdapter
         }
     }

     override fun onEditClick(noteData: NoteData) {
         val editIntent = Intent(this@MainActivity, AddNoteActivity::class.java)
         editIntent.putExtra(AddNoteActivity.editKey,noteData)
         startActivity(editIntent)
     }

     override fun onDeleteClick(noteData: NoteData) {
         Handler().postDelayed({
                 noteDao.deleteNote(noteData)
                 setNoteView()
         },1000)
     }

     //menu connect to toolbar
     override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         menuInflater.inflate(R.menu.appbar_item_layout,menu)
         return super.onCreateOptionsMenu(menu)
     }

     //toolbar menu item select listener
     override fun onOptionsItemSelected(item: MenuItem): Boolean {
         return when(item.itemId){
             R.id.search -> {
                 startActivity(Intent(this@MainActivity,SearchActivity::class.java))
                 true
             }
             R.id.sort -> {
                 sorting()
                 true
             }
             R.id.setting -> {
                 Toast.makeText(applicationContext, "Click Setting", Toast.LENGTH_SHORT).show()
                 true
             }
             else -> super.onOptionsItemSelected(item)
         }
     }

     //Current Date & Time function
     private fun currentDateTime() {
         val calender = Calendar.getInstance().time
         val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT).format(calender)
         val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT).format(calender)
         binding.tvCurrentTime.text = "$dateFormat  $timeFormat"
     }
 }
