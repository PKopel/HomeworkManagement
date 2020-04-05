package homework.manage.main

import homework.manage.main.model.TaskLists
import homework.manage.main.view.TasksApp
import java.awt.Font
import java.io.File
import java.nio.file.Path
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.UIManager
import javax.swing.plaf.FontUIResource

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        setUIFont(FontUIResource(Font("MS Mincho", Font.PLAIN, 20)))
        val wd = Path.of(".").toString()
        val pathToAssigned = "$wd/zadane"
        val pathToFinished = "$wd/gotowe"
        val pathToSend = "$wd/do_wyslania"
        checkDir(pathToAssigned)
        checkDir(pathToFinished)
        checkDir(pathToSend)
        TaskLists.fillSubjects(wd)
        TaskLists.fillLists(pathToAssigned, pathToFinished, pathToSend)
        run(TasksApp, name = "Zadania domowe")
    }
}

fun run(f: JFrame, width: Int = 0, height: Int = 0, name: String) {
    SwingUtilities.invokeLater {
        f.title = name
        f.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        if (width == 0 || height == 0)
            f.extendedState = JFrame.MAXIMIZED_BOTH
        else
            f.setSize(width, height)
        f.isVisible = true
    }
}

fun checkDir(path: String) {
    val dir = File(path)
    dir.mkdir()
}

fun setUIFont(f: FontUIResource) {
    val keys = UIManager.getDefaults().keys()
    while (keys.hasMoreElements()) {
        val key = keys.nextElement()
        val value = UIManager.get(key)
        if (value is FontUIResource) {
            UIManager.put(key, f)
        }
    }
}