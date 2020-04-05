package homework.manage.main.model

import homework.manage.main.files.moveFile
import homework.manage.main.files.readFile
import homework.manage.main.files.writeFile
import java.io.File
import java.text.SimpleDateFormat

object TaskLists {
    val tasksAssigned = mutableListOf<Task>()
    val tasksFinished = mutableListOf<Task>()
    val tasksToSend = mutableListOf<Task>()
    val dateFormat = SimpleDateFormat("dd-mm-yyyy")

    fun fillLists(assigned: String, finished: String, toSend: String) {
        File(assigned).walk().forEach { if (it.isFile) tasksAssigned.add(readFile(it)) }
        File(finished).walk().forEach { if (it.isFile) tasksFinished.add(readFile(it)) }
        File(toSend).walk().forEach { if (it.isFile) tasksToSend.add(readFile(it)) }
    }

    fun addNewTask(assigned: String, due: String, subject: String, toSend: String, contents: String) {
        val isToSend = toSend.contains("tak")
        val newTask = Task(
            dateFormat.parse(assigned),
            dateFormat.parse(due),
            subject,
            isToSend,
            contents
        )
        tasksAssigned.add(newTask)
        writeFile(newTask)
    }

    fun finishTask(taskId: Int): Task {
        val task = tasksAssigned.removeAt(taskId)
        val toDir = if (task.toSend) {
            tasksToSend.add(task)
            "do_wyslania"
        } else {
            tasksFinished.add(task)
            "gotowe"
        }
        moveFile(task, "zadane", toDir)
        return task
    }

    fun sendTask(taskId: Int): Task {
        val task = tasksToSend.removeAt(taskId)
        tasksFinished.add(task)
        moveFile(task, "do_wyslania", "gotowe")
        return task
    }
}