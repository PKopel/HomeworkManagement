package homework.manage.main

import homework.manage.main.model.Task
import java.io.File
import java.util.*
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.UIManager
import javax.swing.plaf.FontUIResource

class TaskComparator : Comparator<Task> {
    override fun compare(p0: Task?, p1: Task?): Int {
        return DateDaysComparator().compare(p0?.dueDate, p1?.dueDate)
    }
}

class DateDaysComparator : Comparator<Date> {
    override fun compare(d1: Date?, d2: Date?): Int {
        val c1 = Calendar.getInstance().apply { time = d1 }
        val c2 = Calendar.getInstance().apply { time = d2 }
        return when {
            c1[Calendar.YEAR] != c2[Calendar.YEAR] -> c1[Calendar.YEAR] - c2[Calendar.YEAR]
            c1[Calendar.MONTH] != c2[Calendar.MONTH] -> c1[Calendar.MONTH] - c2[Calendar.MONTH]
            else -> c1[Calendar.DAY_OF_MONTH] - c2[Calendar.DAY_OF_MONTH]
        }
    }
}

fun run(f: JFrame, name: String, exitOperation: Int = JFrame.EXIT_ON_CLOSE, width: Int = 0, height: Int = 0) {
    SwingUtilities.invokeLater {
        f.title = name
        f.defaultCloseOperation = exitOperation
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