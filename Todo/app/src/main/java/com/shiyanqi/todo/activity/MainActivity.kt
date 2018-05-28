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
import android.content.Intent
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import com.shiyanqi.todo.constants.ConstantValues
import com.shiyanqi.todo.utils.ToastUtils
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class MainActivity : BaseActivity(), View.OnClickListener, TextWatcher, OnRecyclerViewOnClickListener {

    private var adapter: TaskInfoAdapter? = null
    private var dbTasksList = ArrayList<Task>()

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun setUpView() {
        btn_add_task.setOnClickListener(this)
        btn_search_task.setOnClickListener(this)
        edt_add_task.addTextChangedListener(this)
        rv_main.layoutManager = LinearLayoutManager(this)
        adapter = TaskInfoAdapter(this@MainActivity, dbTasksList)
        adapter!!.setItemClickListener(this)
        rv_main.adapter = adapter
    }

    override fun setUpData() = loadData()

    /**
     * recycleView加载数据
     */
    private fun loadData() = Observable.create(ObservableOnSubscribe<List<Task>> { e ->
        this@MainActivity.database.use {
            val list = select(com.shiyanqi.todo.db.TaskTable.TABLE_NAME)
                    .parseList { com.shiyanqi.todo.db.Task(java.util.HashMap(it)) }
            e!!.onNext(list)
        }
    }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread(), false, 100)
            .subscribe(object : Observer<List<Task>> {
                override fun onError(p0: Throwable?) = ToastUtils.showShortToast(R.string.text_load_task_error)

                override fun onSubscribe(p0: Disposable?) = Unit

                override fun onNext(list: List<Task>?) {
                    dbTasksList = list as ArrayList<Task>
                    adapter!!.setTasks(dbTasksList)
                }

                override fun onComplete() = Unit

            })


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_add_task -> addNewTask()
            R.id.btn_search_task -> searchTask()
        }
    }

    override fun onItemClick(v: View, position: Int) =
            Observable.create(ObservableOnSubscribe<Int> { e ->
                this@MainActivity.database.use {
                    val result = delete(TaskTable.TABLE_NAME, "_id={_id}", Pair("_id", dbTasksList[position]._id))
                    e.onNext(result)
                    e.onComplete()
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread(), false, 100)
                    .subscribe(object : Observer<Int> {
                        override fun onError(p0: Throwable?) = ToastUtils.showShortToast(R.string.text_delete_task_error)

                        override fun onSubscribe(p0: Disposable?) = Unit

                        override fun onNext(taskId: Int?) = adapter!!.removeTask(position)

                        override fun onComplete() = ToastUtils.showShortToast(R.string.text_delete_task_complete)

                    })

    private fun searchTask() = startActivity(Intent(this, SearchTaskActivity::class.java))

    /**
     * 添加代办事项
     */
    private fun addNewTask() = if ("" != edt_add_task.text.toString() && edt_add_task.text.toString().length > 2) {
        val task = Task()
        task.task = edt_add_task.text.toString()
        task.time = Date().time

        Observable.create(ObservableOnSubscribe<Long> { e ->
            this@MainActivity.database.use {
                val taskId = insert(TaskTable.TABLE_NAME, *task.map.toVarargArray())
                e!!.onNext(taskId)
                e.onComplete()
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(), false, 100)
                .subscribe(object : Observer<Long> {
                    override fun onError(p0: Throwable?) = ToastUtils.showShortToast(R.string.text_add_task_error)

                    override fun onSubscribe(p0: Disposable?) = Unit

                    override fun onNext(taskId: Long?) {
                        adapter!!.addTask(Task(taskId!!, task.time, task.task))
                        edt_add_task.text.clear()
                    }

                    override fun onComplete() = ToastUtils.showShortToast(R.string.text_add_task_complete)

                })
    } else {
        ToastUtils.showShortToast(R.string.text_input_empty_hint)
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

    override fun afterTextChanged(s: Editable?) = Unit

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
            if (s!!.length > 2) {
                btn_add_task.setTextColor(Color.rgb(91, 154, 250))
            } else {
                btn_add_task.setTextColor(Color.rgb(95, 95, 95))
            }


}
