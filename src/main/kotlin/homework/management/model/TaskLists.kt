package homework.management.model

import homework.management.TaskComparator
import homework.management.files.*
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
        subjects.addAll(readSubjects(File("$path/${Resources.subjectsFile}")))
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
            tasksAssigned.remove(task) -> Resources.assignedDir
            tasksFinished.remove(task) -> Resources.finishedDir
            tasksToSend.remove(task) -> Resources.toSendDir
            else -> throw IllegalStateException()
        }
        moveTask(task, taskDir, Resources.deletedDir)
    }

    fun finishTask(taskId: Int): Task {
        val task = tasksAssigned.removeAt(taskId)
        val toDir = if (task.toSend) {
            tasksToSend.add(task)
            Resources.toSendDir
        } else {
            tasksFinished.add(task)
            Resources.finishedDir
        }
        moveTask(task, Resources.assignedDir, toDir)
        return task
    }

    fun sendTask(taskId: Int): Task {
        val task = tasksToSend.removeAt(taskId)
        tasksFinished.add(task)
        moveTask(task, Resources.toSendDir, Resources.finishedDir)
        return task
    }
}