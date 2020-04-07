package homework.management.main.model

import homework.management.main.TaskComparator
import homework.management.main.files.*
import homework.management.main.files.moveTask
import homework.management.main.files.readSubjects
import homework.management.main.files.readTask
import homework.management.main.files.writeTask
import java.io.File
import java.text.SimpleDateFormat

object TaskLists {
    val tasksAssigned = mutableListOf<Task>()
    val tasksFinished = mutableListOf<Task>()
    val tasksToSend = mutableListOf<Task>()
    val subjects = mutableListOf<String>()
    val dateFormat = SimpleDateFormat("dd-MM-yyyy")

    fun fillLists(assigned: String, finished: String, toSend: String) {
        File(assigned).walk().forEach { if (it.isFile) tasksAssigned.add(readTask(it)) }
        File(finished).walk().forEach { if (it.isFile) tasksFinished.add(readTask(it)) }
        File(toSend).walk().forEach { if (it.isFile) tasksToSend.add(readTask(it)) }
    }

    fun fillSubjects(path: String) {
        subjects.addAll(readSubjects(File("$path/przedmioty")))
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
        tasksAssigned.sortWith(TaskComparator())
        writeTask(newTask)
    }

    fun removeTask(task: Task) {
        val taskDir = when {
            tasksAssigned.remove(task) -> "zadane"
            tasksFinished.remove(task) -> "gotowe"
            tasksToSend.remove(task) -> "do_wyslania"
            else -> throw IllegalStateException()
        }
        moveTask(task, taskDir, "./.usuniete")
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
        moveTask(task, "zadane", toDir)
        return task
    }

    fun sendTask(taskId: Int): Task {
        val task = tasksToSend.removeAt(taskId)
        tasksFinished.add(task)
        moveTask(task, "do_wyslania", "gotowe")
        return task
    }
}