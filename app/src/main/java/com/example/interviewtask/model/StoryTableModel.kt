package com.example.interviewtask.model

import androidx.databinding.adapters.Converters
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "StoryTable")
data class StoryTableModel(

    @ColumnInfo(name = "StoryCategory")
    var storycategory: Int,

    @TypeConverters(ListConverter::class)
    @ColumnInfo(name = "StoryList")
    var storylist: List<String>

) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var Id: Int? = null

}