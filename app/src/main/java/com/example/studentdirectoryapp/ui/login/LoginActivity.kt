package com.example.studentdirectoryapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.studentdirectoryapp.MainActivity
import com.example.studentdirectoryapp.databinding.ActivityLoginBinding
import com.example.studentdirectoryapp.utils.PrefManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefManager = PrefManager(this)

        // Cek apakah user sudah login dan remember me aktif — langsung ke MainActivity
        if (prefManager.isLoggedIn() && prefManager.isRememberMe()) {
            navigateToMain()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Jika sebelumnya remember me aktif, isi username otomatis
        if (prefManager.isRememberMe()) {
            binding.etUsername.setText(prefManager.getUsername())
            binding.cbRememberMe.isChecked = true
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan password harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi login: username = Ridho, password = 12345
            if (username == "Ridho" && password == "12345") {
                prefManager.saveLoginSession(username, binding.cbRememberMe.isChecked)
                Toast.makeText(this, "Login berhasil! Selamat datang, $username", Toast.LENGTH_SHORT).show()
                navigateToMain()
            } else {
                Toast.makeText(this, "Username atau password salah", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish() // Tutup LoginActivity agar tidak bisa back
    }
}
