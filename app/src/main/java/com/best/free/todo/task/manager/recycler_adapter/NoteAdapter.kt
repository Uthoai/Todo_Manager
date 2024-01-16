package com.best.free.todo.task.manager.recycler_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.best.free.todo.task.manager.database.NoteData
import com.best.free.todo.task.manager.databinding.NoteItemBinding
import java.util.logging.Handler

class NoteAdapter(private var handleUserClick: HandleUserClick ,private var noteList: MutableList<NoteData>):
    RecyclerView.Adapter<NoteViewHolder>() {

    interface HandleUserClick{
        fun onEditClick(noteData: NoteData)
        fun onDeleteClick(noteData: NoteData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        noteList[position].let { note->
            holder.binding.apply {
                tvTitle.text = note.title
                tvDetail.text = note.detail
                tvSelectTime.text = note.selectedDate

                root.setOnClickListener {
                    handleUserClick.onEditClick(note)
                }
                btnCheckCompleteTask.setOnClickListener {
                    handleUserClick.onDeleteClick(note)
                }
            }
        }
    }
}