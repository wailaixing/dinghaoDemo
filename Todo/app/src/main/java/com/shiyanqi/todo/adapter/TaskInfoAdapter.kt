package com.shiyanqi.todo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marktony.fanfouhandpick.interfaze.OnRecyclerViewOnClickListener
import com.shiyanqi.todo.R
import com.shiyanqi.todo.db.Task
import com.shiyanqi.todo.utils.DateUtils
import kotlinx.android.synthetic.main.item_task_info.view.*

class TaskInfoAdapter(context: Context, list: MutableList<Task>) : RecyclerView.Adapter<TaskInfoAdapter.FanfouPostsViewHolder>() {

    private var tasks: MutableList<Task> = list

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var mListener: OnRecyclerViewOnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FanfouPostsViewHolder? {
        val view: View = inflater.inflate(R.layout.item_task_info, parent, false)
        return FanfouPostsViewHolder(view, mListener!!)
    }

    override fun onBindViewHolder(holder: FanfouPostsViewHolder, position: Int) {

        val item = tasks[position]

        holder.itemView.tv_task_content.text = item.task
        holder.itemView.tv_task_time.text = DateUtils.fromatTime(item.time)
    }

    override fun getItemCount(): Int = tasks.size

    fun setItemClickListener(listener: OnRecyclerViewOnClickListener) {
        this.mListener = listener
    }

    inner class FanfouPostsViewHolder(itemView: View, private var listener: OnRecyclerViewOnClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) = listener.onItemClick(p0!!, layoutPosition)

    }

    fun setTasks(tasks: MutableList<Task>){
        this.tasks = tasks
        notifyDataSetChanged()
    }

    fun addTask(task: Task) {
        this.tasks.add(task)
        notifyDataSetChanged()
    }

    fun addTasks(tasks: MutableList<Task>) {
        this.tasks.addAll(tasks)
        notifyDataSetChanged()
    }

    fun removeTask(position: Int) {
        tasks!!.removeAt(position)
        notifyDataSetChanged()
    }

}
