package homework.management

import homework.management.files.Resources
import homework.management.files.checkDir
import homework.management.model.TaskLists
import homework.management.view.TasksApp
import swingKt.show
import swingKt.setUIFont
import java.awt.Font
import java.io.File
import javax.swing.plaf.FontUIResource


fun update() {
    File("./zadane").apply { if (isDirectory) renameTo(File("./${Resources.assignedDir}")) }
    File("./gotowe").apply { if (isDirectory) renameTo(File("./${Resources.finishedDir}")) }
    File("./do_wyslania").apply { if (isDirectory) renameTo(File("./${Resources.toSendDir}")) }
    File("./.usuniete").apply { if (isDirectory) renameTo(File("./${Resources.deletedDir}")) }
    File("./zadane").delete()
    File("./gotowe").delete()
    File("./do_wyslania").delete()
    File("./.usuniete").delete()
}

fun main() {
    setUIFont(FontUIResource(Font("MS Mincho", Font.PLAIN, 20)))
    update()
    val wd = File(".").absolutePath
    val pathToAssigned = "$wd/${Resources.assignedDir}"
    val pathToFinished = "$wd/${Resources.finishedDir}"
    val pathToSend = "$wd/${Resources.toSendDir}"
    val pathToDeleted = "$wd/${Resources.deletedDir}"
    checkDir(pathToAssigned)
    checkDir(pathToFinished)
    checkDir(pathToSend)
    checkDir(pathToDeleted)
    TaskLists.fillSubjects(wd)
    TaskLists.fillLists(pathToAssigned, pathToFinished, pathToSend)
    show(TasksApp, Resources.appTitle)
}