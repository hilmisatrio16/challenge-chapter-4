package com.example.challengechapter4.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.challengechapter4.room.Note
import com.example.challengechapter4.databinding.ItemNoteBinding

class NoteAdapter(private var listNote : ArrayList<Note>, private val
    listener : ItemClickListener) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    class ViewHolder(var binding : ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindNote(itemData : Note, listener: ItemClickListener){
            binding.note = itemData
            binding.listener = listener
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.ViewHolder {
        val view = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteAdapter.ViewHolder, position: Int) {
        holder.bindNote(listNote[position], listener)
    }

    override fun getItemCount(): Int {
        return listNote.size
    }

    fun setListNote(list : ArrayList<Note>){
        this.listNote = list
        notifyDataSetChanged()
    }

    interface ItemClickListener{
        fun onItemRemove(note: Note)
        fun onItemUpdate(note: Note)
        fun onItemClick(note: Note)
    }
}