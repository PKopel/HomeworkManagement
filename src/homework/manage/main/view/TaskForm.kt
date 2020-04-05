package homework.manage.main.view

import homework.manage.main.model.Task
import homework.manage.main.model.TaskLists
import homework.manage.main.model.TaskLists.dateFormat
import homework.manage.main.run
import java.awt.FlowLayout
import java.awt.GridLayout
import javax.swing.*
import javax.swing.border.TitledBorder

class TaskForm(editable: Boolean) : JFrame() {
    private val subject = JTextField()
    private val assignedDate = JTextField()
    private val dueDate = JTextField()
    private val toSend = JTextField()
    private val contents = JTextArea()
    private val addButton = button("Dodaj") {
        TaskLists.addNewTask(assignedDate.text, dueDate.text, subject.text, toSend.text, contents.text)
        run(TasksApp(), 600, 600, "Zadania domowe")
        dispose()
    }
    private val returnButton = button ("Wróć"){
        run(TasksApp(), 600, 600, "Zadania domowe")
        dispose()
    }

    constructor(task: Task) : this(false) {
        subject.text = task.subject
        assignedDate.text = dateFormat.format(task.assignmentDate)
        dueDate.text = dateFormat.format(task.dueDate)
        toSend.text = if (task.toSend) "tak" else "nie"
        contents.text = task.contents
    }

    init {
        subject.isEditable = editable
        assignedDate.isEditable = editable
        dueDate.isEditable = editable
        toSend.isEditable = editable
        contents.isEditable = editable
        subject.border = TitledBorder("Przedmoit")
        assignedDate.border = TitledBorder("Data zadania")
        dueDate.border = TitledBorder("Data oddania")
        toSend.border = TitledBorder("Do wysłania")
        contents.border = TitledBorder("Treść")
        this.layout = GridLayout(6, 1)
        this.add(subject)
        this.add(assignedDate)
        this.add(dueDate)
        this.add(toSend)
        this.add(contents)

        val buttons = JPanel()
        buttons.layout = FlowLayout()
        buttons.add(returnButton)
        if (editable) buttons.add(addButton)

        this.add(buttons)
    }
}