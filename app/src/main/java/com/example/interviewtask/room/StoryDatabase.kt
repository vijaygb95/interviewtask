package com.example.interviewtask.room

import android.content.Context
import androidx.room.*
import com.example.interviewtask.model.ListConverter
import com.example.interviewtask.model.StoryTableModel

@Database(entities = arrayOf(StoryTableModel::class), version = 1, exportSchema = false)
@TypeConverters(ListConverter::class)
abstract class StoryDatabase : RoomDatabase() {

    abstract fun storyDao() : DAOAccess

    companion object {

        @Volatile
        private var INSTANCE: StoryDatabase? = null

        fun getDataseClient(context: Context) : StoryDatabase {

            if (INSTANCE != null) return INSTANCE!!

            synchronized(this) {

                INSTANCE = Room
                    .databaseBuilder(context, StoryDatabase::class.java, "STORY_DATABASE")
                    .fallbackToDestructiveMigration()
                    .build()

                return INSTANCE!!

            }
        }

    }

}