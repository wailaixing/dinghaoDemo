package com.shiyanqi.todo

import android.app.Application
import kotlin.properties.Delegates

/**
 * Created by shiyanqi on 18/5/22.
 */
class App: Application() {
    companion object {
        var instance: App by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}