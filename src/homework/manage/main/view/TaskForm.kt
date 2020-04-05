package homework.manage.main.view

import homework.manage.main.model.Task
import homework.manage.main.model.TaskLists
import homework.manage.main.model.TaskLists.dateFormat
import homework.manage.main.model.TaskLists.subjects
import java.awt.BorderLayout
import java.awt.GridLayout
import java.util.*
import javax.swing.*
import javax.swing.border.TitledBorder
import javax.swing.text.DateFormatter


class TaskForm(editable: Boolean) : JFrame() {
    private val subject = JComboBox<String>()

    private val assignedDate = JFormattedTextField(dateFormat)
    private val dueDate = JFormattedTextField(dateFormat)
    private val toSend = JTextField()
    private val contents = JTextArea()
    private val addButton = button("Dodaj") {
        TaskLists.addNewTask(
            assignedDate.text,
            dueDate.text,
            subject.selectedItem as String,
            toSend.text,
            contents.text
        )
        TasksApp.refresh()
        dispose()
    }
    private val returnButton = button("Wróć") {
        dispose()
    }

    constructor(task: Task) : this(false) {
        subject.selectedItem = task.subject
        assignedDate.text = dateFormat.format(task.assignmentDate)
        dueDate.text = dateFormat.format(task.dueDate)
        toSend.text = if (task.toSend) "tak" else "nie"
        contents.text = task.contents
    }

    init {
        subject.model = DefaultComboBoxModel<String>().apply { addAll(subjects) }

        assignedDate.isEditable = editable
        (assignedDate.formatter as DateFormatter).allowsInvalid = false
        (assignedDate.formatter as DateFormatter).overwriteMode = true
        assignedDate.text = dateFormat.format(Date())

        dueDate.isEditable = editable
        (dueDate.formatter as DateFormatter).allowsInvalid = false
        (dueDate.formatter as DateFormatter).overwriteMode = true
        dueDate.text = dateFormat.format(Date())

        toSend.isEditable = editable
        contents.isEditable = editable
        subject.border = TitledBorder("Przedmoit")
        assignedDate.border = TitledBorder("Data zadania")
        dueDate.border = TitledBorder("Data oddania")
        toSend.border = TitledBorder("Do wysłania")
        contents.border = TitledBorder("Treść")

        val header = JPanel()
        header.layout = GridLayout(1, 4)
        header.add(subject)
        header.add(assignedDate)
        header.add(dueDate)
        header.add(toSend)

        this.add(BorderLayout.NORTH, header)

        this.add(BorderLayout.CENTER, contents)

        val buttons = JPanel()
        buttons.layout = GridLayout(1, 2)
        buttons.add(returnButton)
        if (editable) buttons.add(addButton)

        this.add(BorderLayout.SOUTH, buttons)
    }
}