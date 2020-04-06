package homework.manage.main.view

import homework.manage.main.model.Task
import homework.manage.main.model.TaskLists
import homework.manage.main.model.TaskLists.dateFormat
import homework.manage.main.model.TaskLists.subjects
import java.awt.BorderLayout
import java.awt.GridLayout
import java.awt.event.InputEvent.CTRL_DOWN_MASK
import java.awt.event.KeyEvent.VK_S
import java.awt.event.KeyEvent.VK_X
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.*
import javax.swing.*
import javax.swing.JOptionPane.YES_NO_OPTION
import javax.swing.border.TitledBorder
import javax.swing.text.DateFormatter


class TaskForm(private val editing: Boolean) : JFrame() {
    private val subject = JComboBox<String>()
    private var task: Task? = null

    private val assignedDate = JFormattedTextField(dateFormat)
    private val dueDate = JFormattedTextField(dateFormat)
    private val toSend = JTextField()
    private val contents = JTextArea()
    private val removeButton = button("Usuń") {
        val confirmed = JOptionPane.showConfirmDialog(
            null,
            "Czy na pewno chcesz usunąć to zadanie?",
            "Usuwanie zadania",
            YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        )
        if (confirmed == 0) {
            TaskLists.removeTask(task!!)
            TasksApp.refresh()
            dispose()
        }
    }
    private val saveButton = button("Zapisz", KeyStroke.getKeyStroke(VK_S, CTRL_DOWN_MASK)) {
        submit()
        dispose()
    }
    private val returnButton = button("Wróć", KeyStroke.getKeyStroke(VK_X, CTRL_DOWN_MASK)) {
        dispose()
    }

    constructor(task: Task) : this(true) {
        subject.selectedItem = task.subject
        assignedDate.text = dateFormat.format(task.assignmentDate)
        dueDate.text = dateFormat.format(task.dueDate)
        toSend.text = if (task.toSend) "tak" else "nie"
        contents.text = task.contents
        this.task = task
    }

    private fun submit() {
        if (editing && task != null) TaskLists.removeTask(task!!)
        TaskLists.addNewTask(
            assignedDate.text,
            dueDate.text,
            subject.selectedItem as String,
            toSend.text,
            contents.text
        )
        TasksApp.refresh()
    }

    init {
        subject.model = DefaultComboBoxModel<String>().apply { subjects.forEach { this.addElement(it) } }

        (assignedDate.formatter as DateFormatter).allowsInvalid = false
        (assignedDate.formatter as DateFormatter).overwriteMode = true
        assignedDate.text = dateFormat.format(Date())
        assignedDate.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                assignedDate.text = DatePicker(this@TaskForm).pickDate()
            }
        })

        (dueDate.formatter as DateFormatter).allowsInvalid = false
        (dueDate.formatter as DateFormatter).overwriteMode = true
        dueDate.text = dateFormat.format(Date())
        dueDate.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                dueDate.text = DatePicker(this@TaskForm).pickDate()
            }
        })

        subject.border = TitledBorder("Przedmoit")
        assignedDate.border = TitledBorder("Data zadania")
        dueDate.border = TitledBorder("Data oddania")
        toSend.border = TitledBorder("Do wysłania")
        contents.border = TitledBorder("Treść")

        val header = JPanel(GridLayout(1, 4))
        header.add(subject)
        header.add(assignedDate)
        header.add(dueDate)
        header.add(toSend)

        this.add(BorderLayout.NORTH, header)

        this.add(BorderLayout.CENTER, contents)

        val buttons = JPanel(GridLayout(1, 3))
        removeButton.isEnabled = editing
        buttons.add(returnButton)
        buttons.add(removeButton)
        buttons.add(saveButton)

        this.add(BorderLayout.SOUTH, buttons)
    }
}