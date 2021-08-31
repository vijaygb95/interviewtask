package com.example.interviewtask.utility.di

import com.example.interviewtask.viewmodel.StoryViewModel
import com.kotlintest.app.network.ApiInterface
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val viewModelModule = module {
    single { StoryViewModel(get()) }
}

val apiModule = module {

    fun provideUserApi(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

    single { provideUserApi(get()) }
}

val netModule = module {

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://hacker-news.firebaseio.com/v0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build() //Doesn't require the adapter
    }
    single { getRetrofit() }
}
