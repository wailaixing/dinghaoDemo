package com.shiyanqi.todo.activity

import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.marktony.fanfouhandpick.interfaze.OnRecyclerViewOnClickListener
import com.shiyanqi.todo.R
import com.shiyanqi.todo.adapter.TaskInfoAdapter
import com.shiyanqi.todo.base.BaseActivity
import com.shiyanqi.todo.db.Task
import com.shiyanqi.todo.extensions.database
import com.shiyanqi.todo.extensions.parseList
import com.shiyanqi.todo.utils.ToastUtils
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search_task.*
import org.jetbrains.anko.db.select
import java.util.ArrayList


class SearchTaskActivity: BaseActivity(), TextWatcher {

    private var adapter: TaskInfoAdapter? = null
    private var dbTasksList = ArrayList<Task>()

    override fun getLayoutId(): Int = R.layout.activity_search_task

    override fun setUpView() {
        edt_search_task.addTextChangedListener(this)
        rv_search_result.layoutManager = LinearLayoutManager(this)
        adapter = TaskInfoAdapter(this@SearchTaskActivity, dbTasksList)
        adapter!!.setItemClickListener(object: OnRecyclerViewOnClickListener{
            override fun onItemClick(v: View, position: Int) = Unit
        })
        rv_search_result.adapter = adapter
    }

    override fun setUpData() = Unit

    override fun afterTextChanged(s: Editable?) {
        val kw = s.toString()
        if("" != kw){
            Observable.create(ObservableOnSubscribe<List<Task>> { e ->
                this@SearchTaskActivity.database.use {
                    val list = select(com.shiyanqi.todo.db.TaskTable.TABLE_NAME).whereSimple("(task like ?)", "%$kw%")
                            .parseList { com.shiyanqi.todo.db.Task(java.util.HashMap(it)) }
                    e!!.onNext(list)
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread(), false, 100)
                    .subscribe(object : Observer<List<Task>> {
                        override fun onError(p0: Throwable?) = ToastUtils.showShortToast(R.string.text_search_task_error)

                        override fun onSubscribe(p0: Disposable?) = Unit

                        override fun onNext(list: List<Task>?) {
                            dbTasksList = list as ArrayList<Task>
                            adapter!!.setTasks(dbTasksList)
                        }

                        override fun onComplete() = Unit

                    })
        }else{
            dbTasksList.clear()
            adapter!!.notifyDataSetChanged()
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

}