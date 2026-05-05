package com.example.studentdirectoryapp.ui.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.studentdirectoryapp.database.AppDatabase
import com.example.studentdirectoryapp.database.entity.StudentEntity
import com.example.studentdirectoryapp.databinding.FragmentFormBinding
import kotlinx.coroutines.launch

class FormFragment : Fragment() {

    private var _binding: FragmentFormBinding? = null
    private val binding get() = _binding!!
    private val dao by lazy { AppDatabase.getInstance(requireContext()).studentDao() }

    // Mode: true = edit, false = tambah baru
    private var isEdit = false
    private var studentId = 0

    private val prodiList = listOf(
        "T. Informatika", "Sistem Informasi", "T. Elektro",
        "T. Sipil", "Manajemen", "Akuntansi", "Hukum", "Kedokteran"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isEdit = arguments?.getBoolean("isEdit") ?: false
        studentId = arguments?.getInt("studentId") ?: 0

        // Setup Spinner Prodi
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, prodiList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerProdi.adapter = spinnerAdapter

        // Jika mode edit, load data mahasiswa yang akan diedit
        if (isEdit && studentId != 0) {
            binding.tvTitle.text = "Edit Mahasiswa"
            binding.btnSave.text = "Perbarui"
            lifecycleScope.launch {
                val student = dao.getStudentById(studentId)
                student?.let { populateForm(it) }
            }
        } else {
            binding.tvTitle.text = "Tambah Mahasiswa"
        }

        binding.btnSave.setOnClickListener { saveStudent() }
        binding.btnCancel.setOnClickListener { findNavController().popBackStack() }
    }

    private fun populateForm(student: StudentEntity) {
        binding.etName.setText(student.name)
        binding.etNim.setText(student.nim)
        binding.etEmail.setText(student.email)
        binding.etSemester.setText(student.semester.toString())
        val prodiIndex = prodiList.indexOf(student.prodi)
        if (prodiIndex >= 0) binding.spinnerProdi.setSelection(prodiIndex)
    }

    private fun saveStudent() {
        val name     = binding.etName.text.toString().trim()
        val nim      = binding.etNim.text.toString().trim()
        val email    = binding.etEmail.text.toString().trim()
        val semStr   = binding.etSemester.text.toString().trim()
        val prodi    = binding.spinnerProdi.selectedItem.toString()

        // Validasi semua input
        if (name.isEmpty())  { binding.etName.error = "Nama tidak boleh kosong"; return }
        if (nim.isEmpty())   { binding.etNim.error = "NIM tidak boleh kosong"; return }
        if (email.isEmpty()) { binding.etEmail.error = "Email tidak boleh kosong"; return }
        if (semStr.isEmpty()) { binding.etSemester.error = "Semester tidak boleh kosong"; return }

        val semester = semStr.toIntOrNull()
        if (semester == null || semester < 1 || semester > 14) {
            binding.etSemester.error = "Semester harus angka 1–14"
            return
        }

        lifecycleScope.launch {
            if (isEdit) {
                val updated = StudentEntity(id = studentId, name = name, nim = nim,
                    prodi = prodi, email = email, semester = semester)
                dao.update(updated)
                Toast.makeText(requireContext(), "Data berhasil diperbarui!", Toast.LENGTH_SHORT).show()
            } else {
                dao.insert(StudentEntity(name = name, nim = nim, prodi = prodi,
                    email = email, semester = semester))
                Toast.makeText(requireContext(), "Mahasiswa berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
            }
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
