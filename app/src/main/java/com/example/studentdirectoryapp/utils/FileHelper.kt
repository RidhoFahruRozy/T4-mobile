package com.example.studentdirectoryapp.utils

import android.content.Context
import java.io.File

object FileHelper {

    private fun getFileName(nim: String) = "note_${nim}.txt"

    fun saveNote(context: Context, nim: String, content: String): Boolean {
        return try {
            val file = File(context.filesDir, getFileName(nim))
            file.writeText(content)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun loadNote(context: Context, nim: String): String {
        val file = File(context.filesDir, getFileName(nim))
        return if (file.exists()) file.readText() else ""
    }

    fun isNoteExists(context: Context, nim: String): Boolean =
        File(context.filesDir, getFileName(nim)).exists()

    fun deleteNote(context: Context, nim: String): Boolean {
        val file = File(context.filesDir, getFileName(nim))
        return if (file.exists()) file.delete() else false
    }

    fun getNoteSize(context: Context, nim: String): Long {
        val file = File(context.filesDir, getFileName(nim))
        return if (file.exists()) file.length() else 0L
    }
}
