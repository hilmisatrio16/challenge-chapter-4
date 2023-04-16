package com.example.challengechapter4.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.challengechapter4.NoteViewModel
import com.example.challengechapter4.R
import com.example.challengechapter4.room.Note
import com.example.challengechapter4.databinding.FragmentInputBinding
import com.example.challengechapter4.room.NoteDatabase


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
                binding.titleDialog = "Read Data"
                binding.btnInputData.visibility = View.GONE
                binding.btnEditData.visibility = View.GONE
                readDataNote(id)
                editTextEnable()
            }
            "insert" ->{
                binding.titleDialog = "Input Data"
                binding.btnInputData.visibility = View.VISIBLE
                binding.btnEditData.visibility = View.GONE
                inputDataNote()
            }
            "update" ->{
                binding.titleDialog = "Edit Data"
                binding.btnInputData.visibility = View.GONE
                binding.btnEditData.visibility = View.VISIBLE
                readDataNote(id)
                updateDataNote(id)
            }
        }
    }

    private fun editTextEnable(){
        binding.inputJudul.isEnabled = false
        binding.inputCatatan.isEnabled = false
        binding.inputJudul.setTextColor(resources.getColor(android.R.color.black))
        binding.inputCatatan.setTextColor(resources.getColor(android.R.color.black))
    }

    private fun inputDataNote(){
        binding.btnInputData.setOnClickListener {
            val inputJudul = binding.inputJudul.text.toString()
            val inputCatatan = binding.inputCatatan.text.toString()
            if(inputJudul.isNotEmpty() && inputCatatan.isNotEmpty()){
                val note = Note(0, inputJudul, inputCatatan)
                noteViewModel.insertNote(note)
                Toast.makeText(context, "Data berhasil dimasukkan", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_inputFragment_to_homeFragment)
            }else{
                Toast.makeText(context, "Data tidak boleh kosong!!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun readDataNote(id : Int){

        noteViewModel.getNoteById(id)
        noteViewModel.getNoteByIdObservers().observe(viewLifecycleOwner, Observer {
            binding.note = it as Note
        })
    }

    private fun updateDataNote(id : Int){
        binding.btnEditData.setOnClickListener {
            val inputJudul = binding.inputJudul.text.toString()
            val inputCatatan = binding.inputCatatan.text.toString()
            if(inputJudul.isNotEmpty() && inputCatatan.isNotEmpty()){
                val noteUpdate = Note(id,inputJudul, inputCatatan)
                noteViewModel.updateNote(noteUpdate)
                Toast.makeText(context, "Data berhasil diubah", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_inputFragment_to_homeFragment)
            }else{
                Toast.makeText(context, "Data tidak boleh kosong!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}