package homework.management

import homework.management.main.checkDir
import homework.management.main.run
import homework.management.main.model.TaskLists
import homework.management.main.setUIFont
import homework.management.main.view.TasksApp
import java.awt.Font
import java.io.File
import javax.swing.plaf.FontUIResource

fun main(args: Array<String>) {
    setUIFont(FontUIResource(Font("MS Mincho", Font.PLAIN, 20)))
    val wd = File(".").absolutePath
    val pathToAssigned = "$wd/zadane"
    val pathToFinished = "$wd/gotowe"
    val pathToSend = "$wd/do_wyslania"
    val pathToDeleted = "$wd/.usuniete"
    checkDir(pathToAssigned)
    checkDir(pathToFinished)
    checkDir(pathToSend)
    checkDir(pathToDeleted)
    TaskLists.fillSubjects(wd)
    TaskLists.fillLists(pathToAssigned, pathToFinished, pathToSend)
    run(TasksApp, "Zadania domowe")
}


