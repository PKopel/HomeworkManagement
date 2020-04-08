package homework.management.model

import java.util.Comparator

class TaskComparator : Comparator<Task> {
    override fun compare(p0: Task?, p1: Task?): Int {
        return DateDaysComparator().compare(p0?.dueDate, p1?.dueDate)
    }
}