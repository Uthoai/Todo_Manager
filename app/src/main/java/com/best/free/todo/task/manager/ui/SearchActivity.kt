package com.best.free.todo.task.manager.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.room.Room
import com.best.free.todo.task.manager.database.NoteDao
import com.best.free.todo.task.manager.database.NoteData
import com.best.free.todo.task.manager.database.NoteDatabase
import com.best.free.todo.task.manager.databinding.ActivitySearchBinding
import com.best.free.todo.task.manager.recycler_adapter.NoteAdapter
import java.util.Locale

class SearchActivity : AppCompatActivity(),NoteAdapter.HandleUserClick {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var noteDao: NoteDao
    private lateinit var searchList: MutableList<NoteData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            "noteDatabase"
        ).allowMainThreadQueries().build()
        noteDao = database.getDao()
        searchList = noteDao.getAllNote()

        searchNote()
        setNoteView()

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this@SearchActivity,MainActivity::class.java))
        }
    }

    private fun searchNote() {
        binding.searchView.clearFocus()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                return true
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                searchList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if (searchText.isNotEmpty()){
                    noteDao.getAllNote().forEach {
                        if (it.title.toLowerCase(Locale.getDefault()).contains(searchText)){
                            searchList.add(it)
                        }
                    }
                    binding.recyclerView.adapter!!.notifyDataSetChanged()
                } else{
                    searchList.clear()
                    searchList.addAll(noteDao.getAllNote())
                    binding.recyclerView.adapter!!.notifyDataSetChanged()
                }
                return false
            }
        })
    }

    private fun setNoteView() {
        noteAdapter = NoteAdapter(this@SearchActivity,searchList)
        binding.recyclerView.adapter = noteAdapter
    }

    override fun onEditClick(noteData: NoteData) {
        val editIntent = Intent(this@SearchActivity, AddNoteActivity::class.java)
        editIntent.putExtra(AddNoteActivity.editKey,noteData)
        startActivity(editIntent)
    }

    override fun onDeleteClick(noteData: NoteData) {
        /*Handler().postDelayed({
            noteDao.deleteNote(noteData)
            setNoteView()
        },1000)*/
    }
}
