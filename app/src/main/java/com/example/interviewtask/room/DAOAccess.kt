package com.example.interviewtask.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.interviewtask.model.StoryTableModel

@Dao
interface DAOAccess {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(loginTableModel: StoryTableModel)

    @Query("SELECT * FROM StoryTable WHERE StoryCategory =:storycategory")
    fun getDatas(storycategory: Int) : LiveData<StoryTableModel>

}