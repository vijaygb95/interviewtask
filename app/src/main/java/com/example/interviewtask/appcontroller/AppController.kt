package com.example.interviewtask.appcontroller

import android.app.Application
import com.example.interviewtask.utility.di.apiModule
import com.example.interviewtask.utility.di.netModule
import com.example.interviewtask.utility.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level



class AppController : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AppController)
            androidLogger(Level.DEBUG)
            modules(listOf(netModule,apiModule, viewModelModule))
        }
    }

}