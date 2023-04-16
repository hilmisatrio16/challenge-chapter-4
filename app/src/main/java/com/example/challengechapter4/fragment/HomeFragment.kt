package com.example.challengechapter4.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.challengechapter4.*
import com.example.challengechapter4.adapter.NoteAdapter
import com.example.challengechapter4.databinding.FragmentHomeBinding
import com.example.challengechapter4.room.Note


class HomeFragment : Fragment(), NoteAdapter.ItemClickListener {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var sharedPref : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showPopUpMenu()

        binding.btnAdd.setOnClickListener {
            val bundleData = Bundle().apply {
                putString("MENU", "insert")
            }
            findNavController().navigate(R.id.action_homeFragment_to_inputFragment, bundleData)

        }

        noteAdapter = NoteAdapter(ArrayList(),this)
        binding.rvNote.adapter = noteAdapter
        binding.rvNote.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //sharedpreferences
        sharedPref = requireContext().getSharedPreferences("dataprefs", Context.MODE_PRIVATE)


        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        //panggil fun setSortNote default (asc)
        var sort = sharedPref.getString("sorttype", "asc")


        Toast.makeText(context, "Data tersimpan secara $sort", Toast.LENGTH_SHORT).show()
        noteViewModel.setSortType(sort.toString())

        noteViewModel.sortType.observe(viewLifecycleOwner, Observer {
            setSortNotes(it as String)
//            Toast.makeText(context, it as String, Toast.LENGTH_SHORT).show()
        })




    }

    private fun setSortNotes(sort : String){
        if(sort == "asc"){
            noteViewModel.readAllNotesAsc.observe(viewLifecycleOwner, Observer {
                noteAdapter.setListNote(it as ArrayList<Note>)
                setImgData(it)
            })
        }else if(sort == "desc"){
            noteViewModel.readAllNotesDesc.observe(viewLifecycleOwner, Observer {
                noteAdapter.setListNote(it as ArrayList<Note>)
                setImgData(it)
            })
        }
    }

    private fun setImgData(list: List<Note>){

        if(list.isEmpty()){
            binding.imgDataEmpty.visibility = View.VISIBLE
        }else{
            binding.imgDataEmpty.visibility = View.GONE
        }

    }

    private fun showPopUpMenu(){
        binding.btnFilter.setOnClickListener {
            val popUp = PopupMenu(requireContext(), binding.btnFilter)
            popUp.menuInflater.inflate(R.menu.sort_menu, popUp.menu)

            popUp.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.asc -> {
                        val sortAsc = sharedPref.edit().apply{
                            putString("sorttype", "asc")
                        }
                        sortAsc.apply()
                        noteViewModel.setSortType("asc")
                        Toast.makeText(context, "Note diurutkan secara Ascending", Toast.LENGTH_SHORT).show()
                    }
                    R.id.decs -> {
                        val desc = sharedPref.edit().apply{
                            putString("sorttype", "desc")
                        }
                        desc.apply()
                        noteViewModel.setSortType("desc")
                        Toast.makeText(context, "Note diurutkan secara Descending", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
            popUp.show()
        }
    }

    override fun onItemRemove(note: Note) {

        val dialogKonfirmasiHapus = AlertDialog.Builder(context)

        dialogKonfirmasiHapus.apply {
            setTitle("Konfirmasi Hapus Note")
            setMessage("Yakin anda hapus ${note.judul}?")
            setNegativeButton("Batal"){ dialogInterface, i ->
                dialogInterface.dismiss()
            }

            setPositiveButton("Hapus"){ dialogInterface, i ->
                noteViewModel.deleteNote(note)
                Toast.makeText(context, "note ${note.judul} Berhasil dihapus", Toast.LENGTH_SHORT).show()
            }
        }
        dialogKonfirmasiHapus.show()
    }

    override fun onItemUpdate(note: Note) {
        val bundleData = Bundle().apply {
            putInt("ID", note.id)
            putString("MENU", "update")
        }
        findNavController().navigate(R.id.action_homeFragment_to_inputFragment, bundleData)
    }

    override fun onItemClick(note: Note) {
        val bundleData = Bundle().apply {
            putInt("ID", note.id)
            putString("MENU", "read")
        }

        findNavController().navigate(R.id.action_homeFragment_to_inputFragment, bundleData)
    }

}
