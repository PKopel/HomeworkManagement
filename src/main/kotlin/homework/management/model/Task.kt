package homework.management.main.model

import java.util.*

data class Task(val assignmentDate: Date, val dueDate: Date, val subject: String, val toSend: Boolean, val contents: String){
    override fun toString(): String {
        return "$subject na ${TaskLists.dateFormat.format(dueDate)}"
    }
}
