package com.shiyanqi.todo.activity

import android.os.Bundle
import com.shiyanqi.todo.R
import com.shiyanqi.todo.base.BaseActivity
import android.content.Intent
import android.os.Handler
import android.os.Message
import com.shiyanqi.todo.App
import com.shiyanqi.todo.constants.ConstantValues

class IndexActivity : BaseActivity() {

    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            startActivity(Intent(this@IndexActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_index
    }

    override fun setUpView() {}

    override fun setUpData() {
        handler.sendEmptyMessageDelayed(0, 1000)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        App.instance.mAppStatus = ConstantValues.STATUS_ONLINE
        super.onCreate(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

}