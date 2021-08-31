package com.example.interviewtask.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StoreDetailsModel(
    var url: String? = null,

    var type: String? = null,

    var title: String? = null,

    var time: String? = null,

    var score:String? = null,

    var id:String? = null,

    var descendants:String? = null,

    var by: String? = null
)