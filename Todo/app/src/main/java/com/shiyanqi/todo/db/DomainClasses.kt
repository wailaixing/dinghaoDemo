package com.shiyanqi.todo.db

import java.util.*

/**
 * Created by vslimit on 17/1/26.
 */

data class Task(val map: MutableMap<String, Any?>) {
    var _id: Long by map
    var time: Long by map
    var task: String by map

    constructor() : this(HashMap()) {}

    constructor(id: Long, time: Long, task: String) : this(HashMap()) {
        this._id = id
        this.time = time
        this.task = task
    }

}

