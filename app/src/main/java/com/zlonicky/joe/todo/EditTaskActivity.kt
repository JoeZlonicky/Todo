package com.zlonicky.joe.todo

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

class EditTaskActivity : AppCompatActivity() {

    private lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)
        task = intent.getSerializableExtra("Task") as Task
        findViewById<EditText>(R.id.editTaskName).setText(task.name)
    }

    override fun onBackPressed() {
        task.name = findViewById<EditText>(R.id.editTaskName).text.toString()
        TaskDatabase.addTask(task)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Release focus on touch outside the view
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (view is EditText) {
                val rect = Rect()
                view.getGlobalVisibleRect(rect)
                if (!rect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    view.clearFocus()
                    val inputManger = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManger.hideSoftInputFromWindow(view.windowToken, 0)
                    return true
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}
