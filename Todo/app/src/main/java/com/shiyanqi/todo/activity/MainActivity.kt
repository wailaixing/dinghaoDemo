package com.shiyanqi.todo.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.marktony.fanfouhandpick.interfaze.OnRecyclerViewOnClickListener
import com.shiyanqi.todo.R
import com.shiyanqi.todo.adapter.TaskInfoAdapter
import com.shiyanqi.todo.bean.TaskBean
import com.shiyanqi.todo.utils.DateUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header_base.*
import java.util.*

class MainActivity: AppCompatActivity(), View.OnClickListener {

    private var adapter: TaskInfoAdapter? = null
    private var tasksList = ArrayList<TaskBean>()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        initView()
        loadData()
    }

    /**
     * 初始化UI、点击事件
     */
    fun initView(){
        btn_add_task.setOnClickListener(this)
        rv_main.layoutManager = LinearLayoutManager(this)
    }

    /**
     * recycleView加载数据
     */
    private fun loadData() {
        //测试数据
        for(i in 1..5){
            var task = TaskBean(DateUtils.fromatShortTime(Date().time), "测试数据" + i.toString())
            tasksList.add(task)
        }

        adapter = TaskInfoAdapter(this@MainActivity, tasksList)
        rv_main.adapter = adapter
        adapter!!.setItemClickListener(object: OnRecyclerViewOnClickListener{
            override fun OnItemClick(v: View, position: Int) {
                tasksList.removeAt(position)
                adapter!!.notifyDataSetChanged()
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_add_task -> addNewTask()
        }
    }

    private fun addNewTask() {
        if(null != edt_add_task.text.toString() && "" != edt_add_task.text.toString()){
            tasksList.add(TaskBean(DateUtils.fromatShortTime(Date().time), edt_add_task.text.toString()))
            adapter!!.notifyDataSetChanged()
            edt_add_task.text.clear()
        }
//        edt_add_task.text.toString()?.let {
//            tasksList.add(TaskBean(DateUtils.fromatShortTime(Date().time), edt_add_task.text.toString()))
//            adapter!!.notifyDataSetChanged()
//        }
    }

}
