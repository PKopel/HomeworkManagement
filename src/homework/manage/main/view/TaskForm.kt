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
import javax.swing.border.TitledBorder
import javax.swing.text.DateFormatter


class TaskForm() : JFrame() {
    private val subject = JComboBox<String>()

    private val assignedDate = JFormattedTextField(dateFormat)
    private val dueDate = JFormattedTextField(dateFormat)
    private val toSend = JTextField()
    private val contents = JTextArea()
    private val saveButton = button("Zapisz", KeyStroke.getKeyStroke(VK_S, CTRL_DOWN_MASK)) {
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
    private val returnButton = button("Wróć", KeyStroke.getKeyStroke(VK_X, CTRL_DOWN_MASK)) {
        dispose()
    }

    constructor(task: Task) : this() {
        subject.selectedItem = task.subject
        assignedDate.text = dateFormat.format(task.assignmentDate)
        dueDate.text = dateFormat.format(task.dueDate)
        toSend.text = if (task.toSend) "tak" else "nie"
        contents.text = task.contents

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

        val buttons = JPanel(GridLayout(1, 2))
        buttons.add(returnButton)
        buttons.add(saveButton)

        this.add(BorderLayout.SOUTH, buttons)
    }
}