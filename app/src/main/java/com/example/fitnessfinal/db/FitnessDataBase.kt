package com.example.fitnessfinal.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, Meal::class], version = 1, exportSchema = false)
abstract class FitnessDataBase : RoomDatabase() {
    abstract fun userDao():FitnessDao

    companion object{
        @Volatile
        private var INSTANCE : FitnessDataBase? = null
        fun getInstance(context: Context): FitnessDataBase {
            synchronized(this){
                var instance = INSTANCE
                if(instance==null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FitnessDataBase::class.java,
                        "fitness_db"
                    ).build()
                }
                return instance
            }
        }
    }
}