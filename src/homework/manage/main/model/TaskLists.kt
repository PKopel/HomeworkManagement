package homework.manage.main.model

import homework.manage.main.files.readFile
import homework.manage.main.files.writeFile
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object TaskLists {
    val tasksAssigned = mutableListOf<Task>()
    val tasksFinished = mutableListOf<Task>()
    val tasksToSend = mutableListOf<Task>()
    val dateFormat = SimpleDateFormat("dd-mm-yyyy")

    public fun fillLists(assigned: String, finished: String, toSend: String) {
        File(assigned).walk().forEach { if (it.isFile) tasksAssigned.add(readFile(it)) }
        File(finished).walk().forEach { if (it.isFile) tasksFinished.add(readFile(it)) }
        File(toSend).walk().forEach { if (it.isFile) tasksToSend.add(readFile(it)) }
    }

    public fun addNewTask(assigned: String, due: String, subject: String, toSend: Boolean, contents: String) {
        val newTask = Task(
            dateFormat.parse(assigned),
            dateFormat.parse(due),
            subject,
            toSend,
            contents
        )
        tasksAssigned.add(newTask)
        writeFile(newTask)
    }
}