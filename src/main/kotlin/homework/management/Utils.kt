package homework.management

import homework.management.model.Task
import java.util.*

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