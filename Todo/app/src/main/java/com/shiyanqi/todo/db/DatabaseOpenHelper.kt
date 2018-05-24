package com.shiyanqi.todo.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.shiyanqi.todo.App
import org.jetbrains.anko.db.*

class DatabaseOpenHelper(ctx: Context = App.instance) : ManagedSQLiteOpenHelper(ctx, DB_NAME, null, DB_VERSION) {

    companion object {
        val DB_NAME = "task"
        val DB_VERSION = 1
        val instance by lazy { DatabaseOpenHelper() }
    }

    override fun onCreate(db: SQLiteDatabase) = db.createTable(TaskTable.TABLE_NAME, true,
            TaskTable.ID to INTEGER + PRIMARY_KEY + UNIQUE,
            TaskTable.TIME to INTEGER,
            TaskTable.TASK to TEXT)

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(TaskTable.TABLE_NAME, true)
        onCreate(db)
    }
}