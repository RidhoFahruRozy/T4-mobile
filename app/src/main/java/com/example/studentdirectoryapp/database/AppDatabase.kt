package com.example.studentdirectoryapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.studentdirectoryapp.database.dao.StudentDao
import com.example.studentdirectoryapp.database.entity.StudentEntity

@Database(
    entities = [StudentEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun studentDao(): StudentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "student_directory_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
            }
        }
    }
}
