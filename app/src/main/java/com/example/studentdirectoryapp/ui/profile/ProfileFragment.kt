package com.example.studentdirectoryapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.studentdirectoryapp.databinding.FragmentProfileBinding
import com.example.studentdirectoryapp.ui.login.LoginActivity
import com.example.studentdirectoryapp.utils.PrefManager
import com.example.studentdirectoryapp.utils.SettingsManager

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefManager: PrefManager
    private lateinit var settingsManager: SettingsManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefManager = PrefManager(requireContext())
        settingsManager = SettingsManager(requireContext())

        // Tampilkan nama user yang sedang login (dari SharedPreferences)
        binding.tvUsername.text = "Halo, ${prefManager.getUsername()}!"

        // Set state switch sesuai nilai di SharedPreferences
        binding.switchDarkMode.isChecked = settingsManager.isDarkMode
        binding.switchNotification.isChecked = settingsManager.isNotificationEnabled

        // Simpan perubahan ke SharedPreferences saat switch diubah
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            settingsManager.isDarkMode = isChecked
        }

        binding.switchNotification.setOnCheckedChangeListener { _, isChecked ->
            settingsManager.isNotificationEnabled = isChecked
        }

        // Logout — hapus session lalu kembali ke LoginActivity
        binding.btnLogout.setOnClickListener {
            prefManager.logout()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
