package com.example.interviewtask.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.interviewtask.model.StoryTableModel
import com.example.interviewtask.room.StoryDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class StoryRepository {

    companion object {

        var storyDatabase: StoryDatabase? = null


        fun initializeDB(context: Context) : StoryDatabase {
            return StoryDatabase.getDataseClient(context)
        }

        fun insertData(context: Context, type: Int,dataList: List<String>) {

            storyDatabase = initializeDB(context)

            CoroutineScope(IO).launch {
                val loginDetails = StoryTableModel(type, dataList)
                storyDatabase!!.storyDao().insertData(loginDetails)
            }

        }

        fun getDatas(context: Context, type: Int) : LiveData<StoryTableModel>? {

            storyDatabase = initializeDB(context)

            var storyTableModel: LiveData<StoryTableModel>?  = storyDatabase!!.storyDao().getDatas(type)

            return storyTableModel
        }

    }
}