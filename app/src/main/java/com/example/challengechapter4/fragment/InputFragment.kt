package com.example.challengechapter4.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.challengechapter4.NoteViewModel
import com.example.challengechapter4.room.Note
import com.example.challengechapter4.databinding.FragmentInputBinding
import com.example.challengechapter4.room.NoteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class InputFragment : DialogFragment() {

    private lateinit var binding: FragmentInputBinding
    private lateinit var noteViewModel: NoteViewModel
    lateinit var database: NoteDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInputBinding.inflate(layoutInflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)


        database = NoteDatabase.getDatabase(requireContext())
        val idNote = arguments?.getInt("ID")
        val menu = arguments?.getString("MENU")
//        readDataNote(idNote!!.toInt())
        setMenu(idNote!!.toInt(), menu.toString())
        setFullScreen()
//        inputDataNote()
    }

    private fun DialogFragment.setFullScreen() {
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }


    private fun setMenu(id: Int = 0, menu : String){
        when(menu){
            "read" -> {
                binding.btnInputData.visibility = View.GONE
                binding.btnEditData.visibility = View.GONE
                readDataNote(id)
            }
            "insert" ->{
                binding.btnInputData.visibility = View.VISIBLE
                binding.btnEditData.visibility = View.GONE
                inputDataNote()
            }
            "update" ->{
                binding.btnInputData.visibility = View.GONE
                binding.btnEditData.visibility = View.VISIBLE
                readDataNote(id)
                updateDataNote(id)
            }
        }
    }

    private fun inputDataNote(){
        binding.btnInputData.setOnClickListener {
            var inputJudul = binding.inputJudul.text.toString()
            var inputCatatan = binding.inputCatatan.text.toString()
            if(inputJudul.isNotEmpty() && inputCatatan.isNotEmpty()){
                val note = Note(0, inputJudul, inputCatatan)
                noteViewModel.insertNote(note)
                Toast.makeText(context, "Data berhasil dimasukkan", Toast.LENGTH_SHORT).show()
                dismiss()
            }else{
                Toast.makeText(context, "Data tidak boleh kosong!!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun readDataNote(id : Int){
//        noteViewModel.getNote(id)

//        noteViewModel.noteById.observe(viewLifecycleOwner, Observer {
//            Toast.makeText(context, "Data $it", Toast.LENGTH_SHORT).show()
//
//        })

        CoroutineScope(Dispatchers.IO).launch {
            val note = database.noteDao().getNote(id)
            binding.note = note
        }
    }

    fun updateDataNote(id : Int){
        binding.btnEditData.setOnClickListener {
            var inputJudul = binding.inputJudul.text.toString()
            var inputCatatan = binding.inputCatatan.text.toString()
            if(inputJudul.isNotEmpty() && inputCatatan.isNotEmpty()){
                val noteUpdate = Note(id,inputJudul, inputCatatan)
                noteViewModel.updateNote(noteUpdate)
                Toast.makeText(context, "Data berhasil diubah", Toast.LENGTH_SHORT).show()
                dismiss()
            }else{
                Toast.makeText(context, "Data tidak boleh kosong!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}