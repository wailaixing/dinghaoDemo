package com.shiyanqi.todo.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.marktony.fanfouhandpick.interfaze.OnRecyclerViewOnClickListener
import com.shiyanqi.todo.R
import com.shiyanqi.todo.adapter.TaskInfoAdapter
import com.shiyanqi.todo.base.BaseActivity
import com.shiyanqi.todo.db.Task
import com.shiyanqi.todo.db.TaskTable
import com.shiyanqi.todo.extensions.database
import com.shiyanqi.todo.extensions.parseList
import com.shiyanqi.todo.extensions.toVarargArray
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header_base.*
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.delete
import java.util.*
import android.content.Intent
import com.shiyanqi.todo.constants.ConstantValues
import org.jetbrains.anko.doAsync


class MainActivity : BaseActivity(), View.OnClickListener {

    private var adapter: TaskInfoAdapter? = null
    private var dbTasksList = ArrayList<Task>()

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun setUpView() {
        btn_add_task.setOnClickListener(this)
        btn_search_task.setOnClickListener(this)
        rv_main.layoutManager = LinearLayoutManager(this)
    }

    override fun setUpData() {
        loadData()
    }


    /**
     * recycleView加载数据
     */
    private fun loadData() {
        this.database.use {
            val list = select(com.shiyanqi.todo.db.TaskTable.TABLE_NAME)
                    .parseList { com.shiyanqi.todo.db.Task(java.util.HashMap(it)) }
            doAsync {
                if (list.isNotEmpty()) {
                    dbTasksList = list as ArrayList<Task>
                    initTaskList()
                }
            }
        }
    }

    private fun initTaskList() {
        adapter = TaskInfoAdapter(this@MainActivity, dbTasksList)
        adapter!!.setItemClickListener(object : OnRecyclerViewOnClickListener {
            override fun OnItemClick(v: View, position: Int) {
                this@MainActivity.database.use {
                    delete(TaskTable.TABLE_NAME, "_id={_id}", Pair("_id", dbTasksList[position]._id))
                }
                dbTasksList.removeAt(position)
                adapter!!.notifyDataSetChanged()
            }
        })
        rv_main.adapter = adapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_add_task -> addNewTask()
            R.id.btn_search_task -> searchTask()
        }
    }

    private fun searchTask() {
        startActivity(Intent(this, SearchTaskActivity::class.java))
    }

    private fun addNewTask() {
        if ("" != edt_add_task.text.toString()) {
            this.database.use {
                val task = Task()
                task.task = edt_add_task.text.toString()
                task.time = Date().time
                val taskId = insert(TaskTable.TABLE_NAME, *task.map.toVarargArray())
                dbTasksList.add(Task(taskId, task.time, task.task))
            }
            adapter!!.notifyDataSetChanged()
            edt_add_task.text.clear()
        }
    }

    override fun protectApp() {
        startActivity(Intent(this, IndexActivity::class.java))
        finish()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val action = intent!!.getIntExtra(ConstantValues.KEY_HOME_ACTION, ConstantValues.ACTION_BACK_TO_HOME)
        when (action) {
            ConstantValues.ACTION_RESTART_APP -> protectApp()
        }
    }

}
