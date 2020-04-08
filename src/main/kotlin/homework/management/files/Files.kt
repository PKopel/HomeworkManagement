package homework.management.files

import homework.management.model.Task
import homework.management.model.TaskLists.dateFormat
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*


fun checkDir(path: String) {
    val dir = File(path)
    dir.mkdir()
}

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
            it.contains(Resources.assignedDatePrefix) -> {
                assignmentDate = dateFormat.parse(it.removePrefix(Resources.assignedDatePrefix).trim())
            }
            it.contains(Resources.dueDatePrefix) -> {
                dueDate = dateFormat.parse(it.removePrefix(Resources.dueDatePrefix).trim())
            }
            it.contains(Resources.subjectPrefix) -> {
                subject = it.removePrefix(Resources.subjectPrefix).trim()
            }
            it.contains(Resources.toSendPrefix) -> {
                toSend = it.contains("tak")
            }
            else -> {
                contents += it + "\n"
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
    val file = File("${Resources.assignedDir}/$fileName")
    file.writeText(
            """${Resources.assignedDatePrefix}${assignedString}\n
                    ${Resources.dueDatePrefix}${dueString}\n
                    ${Resources.toSendPrefix}${toSendString}\n 
                    ${Resources.subjectPrefix}${task.subject}\n
                    ${task.contents}"""
    )
}

fun moveTask(task: Task, fromDir: String, toDir: String) {
    val fileName = taskFileName(task)
    val sourcePath = Paths.get("$fromDir/$fileName")
    val targetPath = Paths.get("$toDir/$fileName")
    Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING)
}