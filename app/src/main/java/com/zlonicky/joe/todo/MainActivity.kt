package com.zlonicky.joe.todo

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var fadeDuration: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fadeDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
        TaskDatabase.internalDir = filesDir
        TaskDatabase.load()
        val container = findViewById<LinearLayout>(R.id.taskContainer)
        for (task in TaskDatabase.tasks) {
            val item = layoutInflater.inflate(R.layout.task_list_item,
                container, false) as LinearLayout
            (item.getChildAt(0) as TextView).text = task.name
            (item.getChildAt(1) as CheckBox).setOnCheckedChangeListener { _, _ ->
                Handler().postDelayed({
                    TaskDatabase.removeTask(task.id)
                    fadeItemOut(item)
                }, 400)

            }
            container.addView(item)
        }

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val intent = Intent(this, EditTaskActivity::class.java).apply {
                putExtra("Task", Task(""))
            }
            startActivity(intent)
            finish()
        }
    }

    private fun fadeItemOut(view: View) {
        view.apply {
            animate().alpha(0f).setDuration(fadeDuration.toLong()).setListener(object: AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    Handler().postDelayed({
                        view.visibility = View.GONE
                    }, 100)

                }
        })
        }
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
