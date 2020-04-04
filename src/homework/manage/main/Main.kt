package homework.manage.main

import java.io.File
import java.nio.file.Path
import javax.swing.JFrame
import javax.swing.SwingUtilities
import homework.manage.main.files.readFile
import homework.manage.main.model.Task
import homework.manage.main.model.TaskLists
import homework.manage.main.view.TasksApp

fun main() {
    val wd = Path.of(".").toString()
    val pathToAssigned = "$wd/zadane"
    val pathToFinished = "$wd/gotowe"
    val pathToSend = "$wd/do_wyslania"
    TaskLists.fillLists(pathToAssigned, pathToFinished, pathToSend)
    run(
        TasksApp(), 600, 600, "Zadania domowe"
    )
}

fun run(f: JFrame, width: Int, height: Int, name: String) {
    SwingUtilities.invokeLater {
        f.title = name
        f.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        f.setSize(width, height)
        f.isVisible = true
    }
}