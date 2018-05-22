package com.shiyanqi.todo.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.marktony.fanfouhandpick.interfaze.OnRecyclerViewOnClickListener
import com.shiyanqi.todo.R
import com.shiyanqi.todo.adapter.TaskInfoAdapter
import com.shiyanqi.todo.db.Task
import com.shiyanqi.todo.db.TaskTable
import com.shiyanqi.todo.extensions.database
import com.shiyanqi.todo.extensions.parseList
import com.shiyanqi.todo.extensions.toVarargArray
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header_base.*
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.delete
import java.util.*

class MainActivity: AppCompatActivity(), View.OnClickListener {

    private var adapter: TaskInfoAdapter? = null
    private var dbTasksList = ArrayList<Task>()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        this.database.use {
            val list = select(com.shiyanqi.todo.db.TaskTable.TABLE_NAME)
                    .parseList { com.shiyanqi.todo.db.Task(java.util.HashMap(it)) }
            async{
                if(null != list && list.size > 0){
                    dbTasksList = list as ArrayList<Task>
                    initTaskList()
                }
            }
        }
    }

    private fun initTaskList() {
        adapter = TaskInfoAdapter(this@MainActivity, dbTasksList)
        adapter!!.setItemClickListener(object: OnRecyclerViewOnClickListener{
            override fun OnItemClick(v: View, position: Int) {

                dbTasksList.removeAt(position)
                adapter!!.notifyDataSetChanged()
            }
        })
        rv_main.adapter = adapter
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_add_task -> addNewTask()
        }
    }

    private fun addNewTask() {
        if(null != edt_add_task.text.toString() && "" != edt_add_task.text.toString()){
//            tasksList.add(TaskBean(DateUtils.fromatShortTime(Date().time), edt_add_task.text.toString()))
            this.database.use {
                val task = Task()
                task.task = edt_add_task.text.toString()
                task.time = Date().time
                var taskId = insert(TaskTable.TABLE_NAME, *task.map.toVarargArray())
                dbTasksList.add(Task(taskId, task.time, task.task))
            }
            adapter!!.notifyDataSetChanged()
            edt_add_task.text.clear()
        }

    }

}
