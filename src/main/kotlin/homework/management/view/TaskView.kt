package homework.management.view

import homework.management.files.Resources
import homework.management.model.Task
import homework.management.model.TaskLists
import homework.management.model.TaskLists.dateFormat
import homework.management.model.TaskLists.subjects
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
import javax.swing.text.JTextComponent


class TaskView(private var viewing: Boolean) : JFrame() {
    private val subject = JComboBox<String>()
    private var task: Task? = null

    private val header = JPanel(GridLayout(1, 4))
    private val assignedDate = JFormattedTextField(dateFormat)
    private val dueDate = JFormattedTextField(dateFormat)
    private val toSend = JTextField()

    private val contents = JTextArea()
    private val contentsWrapper = JScrollPane(contents)

    private val buttons = JPanel(GridLayout(1, 3))
    private val deleteButton = button(Resources.deleteLabel) {
        val confirmed = JOptionPane.showConfirmDialog(
                null,
                Resources.confirmDelete,
                Resources.deleteTitle,
                YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        )
        if (confirmed == 0) {
            TaskLists.removeTask(task!!)
            TasksApp.refresh()
            dispose()
        }
    }
    private val saveButton = button(
            if (viewing) Resources.editLabel else Resources.saveLabel,
            KeyStroke.getKeyStroke(VK_S, CTRL_DOWN_MASK)) {
        if (viewing) {
            viewing = false
            setEditable(true)
        } else {
            submit()
            dispose()
        }
    }
    private val returnButton = button(
            Resources.exitLabel,
            KeyStroke.getKeyStroke(VK_X, CTRL_DOWN_MASK)) {
        dispose()
    }

    constructor(task: Task) : this(true) {
        setEditable(false)
        subject.selectedItem = task.subject
        assignedDate.text = dateFormat.format(task.assignmentDate)
        dueDate.text = dateFormat.format(task.dueDate)
        toSend.text = if (task.toSend) "tak" else "nie"
        contents.text = task.contents
        this.task = task
    }

    private fun submit() {
        if (task != null) TaskLists.removeTask(task!!)
        TaskLists.addNewTask(
                assignedDate.text,
                dueDate.text,
                subject.selectedItem as String,
                toSend.text,
                contents.text
        )
        TasksApp.refresh()
    }

    private fun setEditable(editable: Boolean) {
        header.components.forEach {
            if (it is JTextComponent) it.isEditable = editable
        }
        contents.isEditable = editable
        saveButton.text = if (editable) Resources.saveLabel else Resources.editLabel
    }

    init {
        subject.model = DefaultComboBoxModel<String>().apply { subjects.forEach { this.addElement(it) } }

        (assignedDate.formatter as DateFormatter).allowsInvalid = false
        (assignedDate.formatter as DateFormatter).overwriteMode = true
        assignedDate.text = dateFormat.format(Date())
        assignedDate.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                assignedDate.text = DatePicker(this@TaskView).pickDate()
            }
        })

        (dueDate.formatter as DateFormatter).allowsInvalid = false
        (dueDate.formatter as DateFormatter).overwriteMode = true
        dueDate.text = dateFormat.format(Date())
        dueDate.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                dueDate.text = DatePicker(this@TaskView).pickDate()
            }
        })

        subject.border = TitledBorder(Resources.subjectTitle)
        assignedDate.border = TitledBorder(Resources.assignedDateTitle)
        dueDate.border = TitledBorder(Resources.dueDateTitle)
        toSend.border = TitledBorder(Resources.toSendTitle)
        contentsWrapper.border = TitledBorder(Resources.contentsTitle)

        header.add(subject)
        header.add(assignedDate)
        header.add(dueDate)
        header.add(toSend)

        this.add(BorderLayout.NORTH, header)

        contents.lineWrap = true
        contents.autoscrolls = true
        this.add(BorderLayout.CENTER, contentsWrapper)

        deleteButton.isEnabled = viewing
        buttons.add(returnButton)
        buttons.add(deleteButton)
        buttons.add(saveButton)

        this.add(BorderLayout.SOUTH, buttons)
    }
}