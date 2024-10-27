package com.example.simpletodolist

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private lateinit var taskAdapter: ArrayAdapter<String>
    private lateinit var sharedPreferences: SharedPreferences
    private val taskList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etNewTask = findViewById<EditText>(R.id.et_new_task)
        val btnAddTask = findViewById<Button>(R.id.btn_add_task)
        val lvTasks = findViewById<ListView>(R.id.lv_tasks)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("tasks_prefs", Context.MODE_PRIVATE)

        // Load saved tasks
        loadTasks()

        // Initialize the ArrayAdapter to display tasks in the ListView
        taskAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, taskList)
        lvTasks.adapter = taskAdapter

        // Add a new task
        btnAddTask.setOnClickListener {
            val task = etNewTask.text.toString()
            if (task.isNotEmpty()) {
                taskList.add(task)
                taskAdapter.notifyDataSetChanged()
                etNewTask.text.clear()
                saveTasks()
            }
        }

        // Long press to delete task
        lvTasks.setOnItemLongClickListener { _, _, position, _ ->
            taskList.removeAt(position)
            taskAdapter.notifyDataSetChanged()
            saveTasks()
            true
        }

        // Short click to mark task as completed (cross it out)
        lvTasks.setOnItemClickListener { _, view, position, _ ->
            val task = taskList[position]
            taskList[position] = "✔️ $task" // Mark as completed
            taskAdapter.notifyDataSetChanged()
            saveTasks()
        }
    }

    // Save tasks to SharedPreferences
    private fun saveTasks() {
        sharedPreferences.edit {
            putStringSet("tasks", taskList.toSet())
        }
    }

    // Load tasks from SharedPreferences
    private fun loadTasks() {
        val savedTasks = sharedPreferences.getStringSet("tasks", setOf())
        taskList.clear()
        taskList.addAll(savedTasks!!)
    }
}
