package com.example.studentdirectoryapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentdirectoryapp.R
import com.example.studentdirectoryapp.database.AppDatabase
import com.example.studentdirectoryapp.database.entity.StudentEntity
import com.example.studentdirectoryapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: StudentAdapter
    private val dao by lazy { AppDatabase.getInstance(requireContext()).studentDao() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = StudentAdapter(emptyList()) { student -> showDeleteDialog(student) }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // FAB untuk tambah mahasiswa baru
        binding.fabAdd.setOnClickListener {
            val bundle = Bundle().apply { putBoolean("isEdit", false) }
            findNavController().navigate(R.id.action_homeFragment_to_formFragment, bundle)
        }

        loadStudents()
    }

    override fun onResume() {
        super.onResume()
        loadStudents() // Refresh saat kembali dari form
    }

    private fun loadStudents() {
        lifecycleScope.launch {
            // Jika database kosong, insert sample data
            if (dao.getStudentCount() == 0) insertSampleData()

            val students = dao.getAllStudents()
            adapter.updateData(students)

            // Tampilkan empty state jika tidak ada data
            binding.tvEmpty.visibility = if (students.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private suspend fun insertSampleData() {
        dao.insertAll(
            listOf(
                StudentEntity(name = "Ilham Brando", nim = "2024001", prodi = "T. Elektro", email = "ilham@gmail.com", semester = 3),
                StudentEntity(name = "Lionel Messi", nim = "2024002", prodi = "Sistem Informasi", email = "Goat@email.com", semester = 8),
                StudentEntity(name = "Mihammad Ridho Fahru Rozy", nim = "2024003", prodi = "T. Informatika", email = "ridho@email.com", semester = 12)
            )
        )
    }

    private fun showDeleteDialog(student: StudentEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Data?")
            .setMessage("Hapus \"${student.name}\"? Tindakan ini tidak dapat dibatalkan.")
            .setPositiveButton("Hapus") { _, _ ->
                lifecycleScope.launch {
                    dao.deleteById(student.id)
                    loadStudents()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
