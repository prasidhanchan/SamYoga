package com.sam.yoga

import android.app.Application
import android.content.Context

class SamYogaApplication: Application() {

    companion object {
        private var instance: SamYogaApplication? = null

        fun getAppContext(): Context = instance!!.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}