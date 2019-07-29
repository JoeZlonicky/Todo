package com.zlonicky.joe.todo

import java.io.*

object TaskDatabase {
    var tasks = mutableListOf<Task>()
    lateinit var internalDir: File

    // Saves recipes to internal storage
    fun save() {
        FileOutputStream(File(internalDir, "database.ser")).use { fileStream ->
            ByteArrayOutputStream().use { byteStream ->
                ObjectOutputStream(byteStream).use { objStream ->
                    objStream.writeObject(tasks)
                    objStream.flush()
                    fileStream.write(byteStream.toByteArray())
                }
            }
        }
    }

    // Retrieves any saved recipes from internal storage
    fun load() {
        val file = File(internalDir, "database.ser")
        if (!file.exists()) {
            return
        }
        val bytes = file.readBytes()
        ByteArrayInputStream(bytes).use { byteStream ->
            ObjectInputStream(byteStream).use { objStream ->
                val loadedTasks = objStream.readObject() as MutableList<*>
                tasks = loadedTasks.filterIsInstance<Task>().toMutableList()
            }
        }
    }

    fun addTask(task: Task) {
        tasks.add(task)
        save()
    }

    fun createID(): Int {
        var id = 0
        while (tasks.any {it.id == id}) {
            ++id
        }
        return id
    }

    fun removeTask(id: Int) {
        tasks.removeAll { it.id == id }
        save()
    }
}