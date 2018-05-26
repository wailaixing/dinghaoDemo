package com.shiyanqi.todo.base

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import java.util.ArrayList

/**
 * Created by shiyanqi on 18/5/25.
 */
abstract class AbstractBaseAdapter<T>: BaseAdapter() {
    private lateinit var mInflater: LayoutInflater
    protected abstract fun getItemLayoutId(position: Int): Int?
    private var items: MutableList<T>? = null
    private lateinit var context: Context

    fun AbstractBaseAdapter(context: Context, items: List<T>?) {
        this.context = context
        this.mInflater = LayoutInflater.from(context)
        this.items = ArrayList()
        if (items != null) {
            this.items!!.addAll(items)
        }
    }

    fun addItem(t: T) {
        items!!.add(t)
        notifyDataSetChanged()
    }

    fun addItems(items: List<T>) {
        this.items!!.addAll(items)
        notifyDataSetChanged()
    }

    fun addItems(location: Int, items: List<T>) {
        this.items!!.addAll(location, items)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        items!!.removeAt(position)
        notifyDataSetChanged()
    }

    fun remove(t: T) {
        items!!.remove(t)
        notifyDataSetChanged()
    }

    fun resetItem(position: Int, t: T) {
        items!![position] = t
        notifyDataSetChanged()
    }

    fun reset(position: Int, items: List<T>?) {
        val size = this.items!!.size
        if (position > size) {
            return
        }
        if (position == 0) {
            reset(items)
            return
        }

        if (items != null) {
            this.items!!.removeAll(this.items!!.subList(position, size))
            this.items!!.addAll(items)
        }
        notifyDataSetChanged()
    }

    fun reset(items: List<T>?) {
        this.items!!.clear()
        if (items != null) {
            this.items!!.addAll(items)
        }
        notifyDataSetChanged()
    }

    fun getItems(): MutableList<T>? = items

    override fun getCount(): Int = items!!.size

    override fun getItem(position: Int): T = items!![position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val viewHolder: ViewHolder
        if (convertView == null) {
            convertView = mInflater.inflate(getItemLayoutId(position)!!, null)
            viewHolder = ViewHolder(convertView)
            bindView(position, convertView, viewHolder)
        } else {
            viewHolder = convertView.tag as AbstractBaseAdapter<T>.ViewHolder
        }

        initView(position, convertView!!, parent, viewHolder)

        val backgroundResource = getBackgroundResource(position)
        if (backgroundResource > 0) {
            convertView!!.setBackgroundResource(backgroundResource)
        }
        return convertView
    }

    protected fun getBackgroundResource(position: Int): Int = 0 // R.drawable.selector_list

    protected fun bindView(position: Int, convertView: View, viewHolder: ViewHolder) {}

    protected abstract fun initView(position: Int, convertView: View, parent: ViewGroup, viewHolder: ViewHolder)

    inner class ViewHolder(private val convertView: View) {

        private var data: Any? = null
        private val viewMap = SparseArray<View>()

        init {
            convertView.tag = this
        }

        fun <T> getData(): T? = data as T?

        fun setData(data: Any) {
            this.data = data
        }

        fun <V : View> findViewById(viewId: Int): V? {

            var view: View? = viewMap.get(viewId)
            if (view == null) {
                view = convertView.findViewById(viewId)
                viewMap.put(viewId, view)
            }
            return view as V?
        }
    }

}