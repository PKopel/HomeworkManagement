package homework.manage.main.files

import homework.manage.main.model.Task
import homework.manage.main.model.TaskLists.dateFormat
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*

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

fun writeFile(task: Task) {
    val assignedString = dateFormat.format(task.assignmentDate)
    val dueString = dateFormat.format(task.dueDate)
    val fileName = "${assignedString}_${task.subject.trim()}_${dueString}.txt"
    val file = File("zadane/$fileName")
    file.writeText(
        "data zadania: $assignedString\ndata oddania: $dueString\ndo wysłania: ${task.toSend}\nprzedmiot: ${task.subject}\ntreść: ${task.contents}"
    )
}

fun moveFile(task: Task, fromDir: String, toDir: String) {
    val assignedString = dateFormat.format(task.assignmentDate)
    val dueString = dateFormat.format(task.dueDate)
    val fileName = "${assignedString}_${task.subject.trim()}_${dueString}.txt"
    val sourcePath = Paths.get("$fromDir/$fileName")
    val targetPath = Paths.get("$toDir/$fileName")
    Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING)
}