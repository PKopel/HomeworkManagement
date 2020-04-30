package homework.management.view

import homework.management.files.Resources
import homework.management.model.Task
import homework.management.model.TaskLists
import homework.management.model.TaskLists.dateFormat
import homework.management.model.TaskLists.subjects
import swingKt.*
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


class TaskView(private var editable: Boolean, private val task: Task? = null) : JFrame() {

    private val header = JPanel(GridLayout(1, 4))
    private val subject = comboBox(subjects, title = Resources.subjectTitle).first.apply {
        if (task != null) selectedItem = task.subject
    }
    private val assignedDate = formattedTextField(
            text = when {
                task != null -> dateFormat.format(task.assignmentDate)
                else -> ""
            },
            formatter = dateFormat,
            editable = editable,
            title = Resources.assignedDateTitle)
    private val dueDate = formattedTextField(
            text = when {
                task != null -> dateFormat.format(task.dueDate)
                else -> ""
            },
            formatter = dateFormat,
            editable = editable,
            title = Resources.assignedDateTitle)
    private val toSend = textField(
            text = when {
                task != null -> if (task.toSend) "tak" else "nie"
                else -> ""
            },
            title = Resources.toSendTitle,
            editable = editable)

    private val contentsWrapper = JScrollPane()
    private val contents = JTextArea().apply {
        if (task != null) text = task.contents
    }

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
            if (editable) Resources.editLabel else Resources.saveLabel,
            KeyStroke.getKeyStroke(VK_S, CTRL_DOWN_MASK)) {
        if (editable) {
            submit()
            dispose()
        } else {
            editable = true
            setEditable()
        }
    }
    private val returnButton = button(
            Resources.exitLabel,
            KeyStroke.getKeyStroke(VK_X, CTRL_DOWN_MASK)) {
        dispose()
    }

    private fun submit() {
        if (task != null) TaskLists.removeTask(task)
        TaskLists.addNewTask(
                assignedDate.text,
                dueDate.text,
                subject.selectedItem as String,
                toSend.text,
                contents.text
        )
        TasksApp.refresh()
    }

    private fun setEditable() {
        header.components.forEach {
            if (it is JTextComponent) it.isEditable = editable
        }
        contents.isEditable = editable
        deleteButton.isEnabled = editable
        saveButton.text = if (editable) Resources.saveLabel else Resources.editLabel
    }

    init {
        (assignedDate.formatter as DateFormatter).allowsInvalid = false
        (assignedDate.formatter as DateFormatter).overwriteMode = true
        if (editable) assignedDate.text = dateFormat.format(Date())
        assignedDate.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (editable) assignedDate.text = DatePicker(this@TaskView).pickDate()
            }
        })

        (dueDate.formatter as DateFormatter).allowsInvalid = false
        (dueDate.formatter as DateFormatter).overwriteMode = true
        if (editable) dueDate.text = dateFormat.format(Date())
        dueDate.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (editable) dueDate.text = DatePicker(this@TaskView).pickDate()
            }
        })

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
        contentsWrapper.viewport.view = contents
        this.add(BorderLayout.CENTER, contentsWrapper)

        deleteButton.isEnabled = editable
        buttons.add(returnButton)
        buttons.add(deleteButton)
        buttons.add(saveButton)

        this.add(BorderLayout.SOUTH, buttons)
        setEditable()
    }
}