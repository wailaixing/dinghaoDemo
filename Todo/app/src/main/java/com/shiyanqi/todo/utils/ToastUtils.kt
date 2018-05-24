package com.shiyanqi.todo.utils

import android.graphics.Color
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.shiyanqi.todo.App
import com.shiyanqi.todo.R


object ToastUtils {
    private var toast: Toast? = null

    fun showShortToast(msg: Int) = showToast(App.instance.getString(msg), Toast.LENGTH_SHORT)

    fun showShortToast(msg: String) = showToast(msg, Toast.LENGTH_SHORT)

    fun showLongToast(msg: Int) = showToast(App.instance.getString(msg), Toast.LENGTH_LONG)

    fun showLongToast(msg: String) = showToast(msg, Toast.LENGTH_LONG)

    /**
     * 显示提示窗
     *
     * @param msg      显示内容
     * @param duration 显示时间
     */
    private fun showToast(msg: String, duration: Int) {
        if (toast == null) {
            toast = Toast.makeText(App.instance, msg, duration)
            val layout = toast!!.view as LinearLayout
            layout.setBackgroundResource(R.drawable.bg_toast)
            layout.setPadding(10, 4, 10, 4)
            val v = toast!!.view.findViewById(android.R.id.message) as TextView
            v.setTextColor(Color.WHITE)
            v.textSize = 14F
        } else {
            toast!!.duration = duration
            toast!!.setText(msg)
        }
        toast!!.show()
    }
}
