package homework.manage.main.files

import homework.manage.main.model.Task
import homework.manage.main.model.TaskLists.dateFormat
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*

fun taskFileName(task: Task): String {
    val assignedString = dateFormat.format(task.assignmentDate)
    val dueString = dateFormat.format(task.dueDate)
    return "${assignedString}_${task.subject.trim()}_${dueString}.txt"
}

fun readSubjects(file: File): List<String> {
    val subjects = mutableListOf<String>()
    file.forEachLine { subjects.add(it.trim()) }
    return subjects
}

fun readTask(file: File): Task {
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
                subject = it.removePrefix("przedmiot:").trim()
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

fun writeTask(task: Task) {
    val fileName = taskFileName(task)
    val assignedString = dateFormat.format(task.assignmentDate)
    val dueString = dateFormat.format(task.dueDate)
    val toSendString = if (task.toSend) "tak" else "nie"
    val file = File("zadane/$fileName")
    file.writeText(
        "data zadania: $assignedString\ndata oddania: $dueString\ndo wysłania: ${toSendString}\nprzedmiot: ${task.subject}\ntreść: ${task.contents}"
    )
}

fun moveTask(task: Task, fromDir: String, toDir: String) {
    val fileName = taskFileName(task)
    val sourcePath = Paths.get("$fromDir/$fileName")
    val targetPath = Paths.get("$toDir/$fileName")
    Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING)
}