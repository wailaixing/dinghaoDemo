package com.shiyanqi.todo.db

/**
 * Created by shiyanqi on 18/5/22.
 */
data class Task(val map: MutableMap<String, Any?>) {
    var _id: Long by map
    var time: Long by map
    var task: String by map

    constructor(): this(HashMap()){}

    constructor(id:Long, time: Long, task:String) : this(HashMap()) {
        this._id = id
        this.time = time
        this.task = task
    }

}