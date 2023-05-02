package com.example.simplelogin

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [User::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserDao() : UserDAO

//    fun getDatabase(context: Context) : AppDatabase{
//
//        val db: AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "database").build();
//
//        return db
//
//    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            return INSTANCE
                ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    )
                        .build()
                    INSTANCE = instance
                    return instance
                }
        }
    }

}