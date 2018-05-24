package com.shiyanqi.todo.base

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.shiyanqi.todo.App
import com.shiyanqi.todo.activity.MainActivity
import com.shiyanqi.todo.constants.ConstantValues

abstract class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(App.instance.mAppStatus == -1){
            protectApp()
        }else{
            val layoutId: Int = getLayoutId()
            if (layoutId > 0) {
                setContentView(layoutId)
            }
            setUpView()
            setUpData()
        }
    }

    protected abstract fun  getLayoutId(): Int

    protected abstract fun setUpView()

    protected abstract fun setUpData()

    protected open fun protectApp() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(ConstantValues.KEY_HOME_ACTION, ConstantValues.ACTION_RESTART_APP)
        startActivity(intent)
    }

}