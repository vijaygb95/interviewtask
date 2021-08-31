package com.example.interviewtask.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.interviewtask.utility.Constants
import com.example.interviewtask.utility.network.Resource
import com.kotlintest.app.network.ApiInterface
import kotlinx.coroutines.Dispatchers

class StoryViewModel(var apiInterface: ApiInterface) : ViewModel() {

    var page = 1
    var textSearch = ""


    fun getData(type: Int?) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            when (type) {

                Constants.NewStories -> {
                    emit(Resource.success(data = apiInterface.getNewstories()))
                }
                Constants.TopStories -> {
                    emit(Resource.success(data = apiInterface.getTopstories()))
                }
                Constants.BestStories -> {
                    emit(Resource.success(data = apiInterface.getBeststories()))
                }
                Constants.AskStories -> {
                    emit(Resource.success(data = apiInterface.getAskstories()))
                }
                Constants.ShowStories -> {
                    emit(Resource.success(data = apiInterface.getShowstories()))
                }
                Constants.JobStories -> {
                    emit(Resource.success(data = apiInterface.getJobstories()))
                }

            }

        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

fun getStoreDetails(type: Int?,id:String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            when (type) {
                Constants.StoryDetails -> {
                    emit(Resource.success(data = apiInterface.getItem(id)))
                }

            }

        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


}