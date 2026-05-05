package com.example.studentdirectoryapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.studentdirectoryapp.database.AppDatabase
import com.example.studentdirectoryapp.database.entity.StudentEntity
import com.example.studentdirectoryapp.databinding.FragmentDetailBinding
import com.example.studentdirectoryapp.utils.FileHelper
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val dao by lazy { AppDatabase.getInstance(requireContext()).studentDao() }
    private var currentStudent: StudentEntity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val studentId = arguments?.getInt("studentId") ?: return

        lifecycleScope.launch {
            val student = dao.getStudentById(studentId)
            student?.let {
                currentStudent = it
                displayStudent(it)
                loadNote(it.nim) // Otomatis muat catatan saat halaman dibuka
            }
        }

        // Tombol Simpan Catatan
        binding.btnSaveNote.setOnClickListener {
            currentStudent?.let { student ->
                val content = binding.etNote.text.toString()
                val success = FileHelper.saveNote(requireContext(), student.nim, content)
                if (success) {
                    val size = FileHelper.getNoteSize(requireContext(), student.nim)
                    binding.tvNoteStatus.text = "Tersimpan ($size bytes)"
                    Toast.makeText(requireContext(), "Catatan berhasil disimpan!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Gagal menyimpan catatan", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Tombol Muat Catatan
        binding.btnLoadNote.setOnClickListener {
            currentStudent?.let { loadNote(it.nim) }
        }
    }

    private fun displayStudent(student: StudentEntity) {
        binding.tvAvatar.text = student.name
            .split(" ").take(2)
            .joinToString("") { it.first().uppercaseChar().toString() }
        binding.tvName.text = student.name
        binding.tvNimProdi.text = "${student.nim} · ${student.prodi}"
        binding.tvEmail.text = student.email
        binding.tvSemester.text = "Semester ${student.semester}"
    }

    private fun loadNote(nim: String) {
        val content = FileHelper.loadNote(requireContext(), nim)
        binding.etNote.setText(content)
        val size = FileHelper.getNoteSize(requireContext(), nim)
        binding.tvNoteStatus.text = if (content.isNotEmpty()) {
            "Catatan dimuat ($size bytes)"
        } else {
            "Belum ada catatan"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
