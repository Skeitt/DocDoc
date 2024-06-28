package com.example.docdoc.view.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.docdoc.databinding.FragmentInserisciEventoBinding
import com.example.docdoc.view.activity.EventoActivity
import com.example.docdoc.view.adapter.FileListAdapter
import com.example.docdoc.viewmodel.EventoViewModel
import java.io.File
import java.util.Calendar

class FragmentInserisciEvento : Fragment() {

    private lateinit var binding: FragmentInserisciEventoBinding
    private val viewModel: EventoViewModel by viewModels({ requireActivity() })

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    // RecyclerView di eventi
    private lateinit var recyclerView: RecyclerView
    private lateinit var fileList: ArrayList<String>
    private lateinit var fileListAdapter: FileListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInserisciEventoBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        // recyclerView
        recyclerView = binding.listaFile
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        // creo il dataset di eventi
        fileList = arrayListOf<String>()

        // imposto l'adapter e lo lego al dataset
        fileListAdapter = FileListAdapter(fileList, true)
        recyclerView.adapter = fileListAdapter

        viewModel.event.observe(viewLifecycleOwner) { evento ->
            evento.listaFile?.let {
                fileList.clear()
                fileList.addAll(evento.listaFile!!)
            }
            fileListAdapter.notifyDataSetChanged()
        }

        setRvOnClick()

        binding.buttonEditData.setOnClickListener {
            showDatePickerDialog()
        }

        binding.buttonAggFile.setOnClickListener {
            openFileDialog()
        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                 // Ottieni l'Uri del file selezionato
                val uri: Uri? = result.data?.data

                if(uri != null){
                    getFileNameFromUri(requireActivity(), uri)?.let { viewModel.addFile(it,uri)}
                }
            }
        }

        return binding.root
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = (selectedYear.toString() + "-" + String.format("%02d", (selectedMonth + 1)) + "-" + selectedDay.toString())
            binding.editData.text = selectedDate
        }, year, month, day).show()
    }

    private fun openFileDialog(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        try {
            resultLauncher.launch(intent)
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(requireContext(), "Nessuna applicazione trovata per aprire i file", Toast.LENGTH_SHORT).show()
        }
    }

    fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var fileName: String? = null

        if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        fileName = it.getString(nameIndex)
                    }
                }
            }
        } else if (uri.scheme == ContentResolver.SCHEME_FILE) {
            fileName = uri.lastPathSegment
        }

        return fileName
    }
    private fun setRvOnClick(){
        fileListAdapter.onTrashClick = {filename ->
            viewModel.deleteStorageFile(filename)
        }
    }

}