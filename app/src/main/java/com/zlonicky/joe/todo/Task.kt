package com.zlonicky.joe.todo

import java.io.Serializable

class Task (var name: String): Serializable {
    val id = TaskDatabase.createID()
}