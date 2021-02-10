package com.example.remotepaging.db.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.remotepaging.data.db.converter.StringListConverter
import com.example.remotepaging.model.PageKey
import com.example.remotepaging.model.User

@Database(entities = [User::class, PageKey::class], version = 1, exportSchema = false)
@TypeConverters(StringListConverter::class)
abstract class AppDb : RoomDatabase() {
    abstract fun userDao(): UserDAO
    abstract fun pageKeyDao(): PageKeyDao

    companion object {
        @Volatile private var instance: AppDb? = null

        fun getDatabase(context: Context): AppDb =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDb::class.java, "usersdb")
                .fallbackToDestructiveMigration()
                .build()
    }
}