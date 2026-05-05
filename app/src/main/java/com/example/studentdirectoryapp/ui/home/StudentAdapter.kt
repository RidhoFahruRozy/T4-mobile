package com.example.studentdirectoryapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.studentdirectoryapp.R
import com.example.studentdirectoryapp.database.entity.StudentEntity
import com.example.studentdirectoryapp.databinding.ItemStudentBinding

class StudentAdapter(
    private var students: List<StudentEntity>,
    private val onDeleteClick: (StudentEntity) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(private val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(student: StudentEntity) {
            binding.tvName.text = student.name
            binding.tvNim.text = student.nim
            binding.tvProdi.text = student.prodi

            // Avatar: ambil 2 huruf pertama dari nama
            binding.tvAvatar.text = student.name
                .split(" ")
                .take(2)
                .joinToString("") { it.first().uppercaseChar().toString() }

            // Navigasi ke Detail
            binding.root.setOnClickListener {
                val bundle = Bundle().apply { putInt("studentId", student.id) }
                it.findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
            }

            // Tombol Edit
            binding.btnEdit.setOnClickListener {
                val bundle = Bundle().apply {
                    putInt("studentId", student.id)
                    putBoolean("isEdit", true)
                }
                it.findNavController().navigate(R.id.action_homeFragment_to_formFragment, bundle)
            }

            // Tombol Delete — callback ke fragment untuk konfirmasi
            binding.btnDelete.setOnClickListener { onDeleteClick(student) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) =
        holder.bind(students[position])

    override fun getItemCount() = students.size

    fun updateData(newList: List<StudentEntity>) {
        students = newList
        notifyDataSetChanged()
    }
}
