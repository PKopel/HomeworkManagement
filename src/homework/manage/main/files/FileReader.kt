package homework.manage.main.files

import java.io.File
import java.text.DateFormat
import java.util.*
import homework.manage.main.model.Task
import homework.manage.main.model.TaskLists.dateFormat
import java.text.SimpleDateFormat

fun readFile(file: File): Task {
    var assignmentDate = Date()
    var dueDate = Date()
    var subject = String()
    var toSend = false
    var contents = String()
    file.forEachLine {
        when {
            it.contains("data zadania:") -> {
                assignmentDate = dateFormat.parse(it.removePrefix("data zadania:"))
            }
            it.contains("data oddania:") -> {
                dueDate = dateFormat.parse(it.removePrefix("data oddania:"))
            }
            it.contains("przedmiot:") -> {
                subject = it.removePrefix("przedmiot:")
            }
            it.contains("do wysłania:") -> {
                toSend = it.contains("tak")
            }
            else -> {
                contents += it.removePrefix("treść:")
            }
        }
    }
    return Task(assignmentDate, dueDate, subject, toSend, contents)
}

fun writeFile(task: Task): Unit {
    val assignedString = dateFormat.format(task.assignmentDate)
    val dueString = dateFormat.format(task.dueDate)
    val fileName = "${assignedString}_${task.subject}_${dueString}.txt"
    val file = File("zadane/$fileName")
    file.writeText(
        "data zadania: $assignedString\ndata oddania: $dueString\ndo wysłania: ${task.toSend}\nprzedmiot: ${task.subject}\ntreść: ${task.contents}"
    )
}