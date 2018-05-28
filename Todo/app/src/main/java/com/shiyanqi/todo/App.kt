package com.shiyanqi.todo

import android.app.Application
import com.shiyanqi.todo.constants.ConstantValues
import kotlin.properties.Delegates

class App: Application() {

    //app被强杀
    @JvmField var mAppStatus: Int = ConstantValues.STATUS_FORCE_KILLED

    companion object {
        var instance: App by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }


}