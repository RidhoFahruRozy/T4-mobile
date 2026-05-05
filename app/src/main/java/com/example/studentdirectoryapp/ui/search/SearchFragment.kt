package com.example.studentdirectoryapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentdirectoryapp.database.AppDatabase
import com.example.studentdirectoryapp.databinding.FragmentSearchBinding
import com.example.studentdirectoryapp.ui.home.StudentAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: StudentAdapter
    private val dao by lazy { AppDatabase.getInstance(requireContext()).studentDao() }
    private var searchJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adapter tanpa fitur delete di SearchFragment
        adapter = StudentAdapter(emptyList()) {}
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // Search dengan debounce 300ms agar tidak terlalu sering hit database
        binding.etSearch.addTextChangedListener { editable ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(300)
                val keyword = editable.toString().trim()
                val results = if (keyword.isEmpty()) {
                    dao.getAllStudents()
                } else {
                    dao.searchStudents(keyword)
                }
                adapter.updateData(results)
                binding.tvEmpty.visibility = if (results.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        // Load semua data saat pertama kali
        lifecycleScope.launch {
            val all = dao.getAllStudents()
            adapter.updateData(all)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
