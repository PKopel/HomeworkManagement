package homework.management.view

import homework.management.model.DateDaysComparator
import homework.management.files.Resources
import homework.management.model.Task
import homework.management.model.TaskLists
import homework.management.model.TaskLists.subjects
import homework.management.model.TaskLists.tasksAssigned
import homework.management.model.TaskLists.tasksFinished
import homework.management.model.TaskLists.tasksToSend
import swingKt.button
import swingKt.comboBox
import swingKt.list
import swingKt.show
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.*
import javax.swing.*

object TasksApp : JFrame() {

    private val listAssigned = list(tasksAssigned, background = Color.WHITE, title = Resources.assignedLabel,
            foregroundColorFunction = {
                when {
                    DateDaysComparator().compare(it.dueDate, Date()) < 0 -> Color.RED
                    DateDaysComparator().compare(it.dueDate, Date()) == 0 -> Color.YELLOW.darker()
                    else -> Color.GREEN.darker()
                }
            })

    private val listToSend = list(tasksToSend, background = Color.YELLOW, title = Resources.toSendLabel)

    private val listFinished = list(tasksFinished, background = Color.GREEN, title = Resources.finishLabel)

    private val addButton = button(Resources.addLabel, KeyStroke.getKeyStroke(VK_N, CTRL_DOWN_MASK)) {
        show(TaskView(true), Resources.taskTitle, DISPOSE_ON_CLOSE, 1000, 1000)
    }

    private val finishButton = button(Resources.finishLabel, KeyStroke.getKeyStroke(VK_Z, CTRL_DOWN_MASK)) {
        val taskId = listAssigned.first.selectedIndex
        val task = TaskLists.finishTask(taskId)
        listAssigned.second.remove(taskId)
        if (task.toSend) {
            listToSend.second.addElement(task)
        } else {
            listFinished.second.addElement(task)
        }
    }

    private val sendButton = button(Resources.sendLabel, KeyStroke.getKeyStroke(VK_W, CTRL_DOWN_MASK)) {
        val taskId = listToSend.first.selectedIndex
        val task = TaskLists.sendTask(taskId)
        listToSend.second.remove(taskId)
        listFinished.second.addElement(task)
    }

    private val clearFilterButton = button(Resources.clearLabel) {
        refresh()
    }

    private val subjectFilter = comboBox(subjects) { e: ActionEvent ->
        @Suppress("UNCHECKED_CAST") val filter = e.source as JComboBox<String>
        listAssigned.second.clear()
        tasksAssigned.filter { it.subject == filter.selectedItem }.forEach { listAssigned.second.addElement(it) }
        listToSend.second.clear()
        tasksToSend.filter { it.subject == filter.selectedItem }.forEach { listToSend.second.addElement(it) }
        listFinished.second.clear()
        tasksFinished.filter { it.subject == filter.selectedItem }.forEach { listFinished.second.addElement(it) }
    }

    private val listListener = object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
            @Suppress("UNCHECKED_CAST") val list = e.source as JList<Task>
            if (e.clickCount == 2) {
                show(TaskView(false, list.selectedValue), Resources.taskTitle, DISPOSE_ON_CLOSE, 1000, 1000)
            }
        }
    }

    init {
        listAssigned.first.addMouseListener(listListener)

        listToSend.first.addMouseListener(listListener)

        listFinished.first.addMouseListener(listListener)

        val filters = JPanel()
        filters.layout = FlowLayout()
        filters.add(subjectFilter.first)
        filters.add(clearFilterButton)
        this.add(BorderLayout.NORTH, filters)

        val lists = JPanel(GridLayout(1, 3))
        lists.add(listAssigned.first)
        lists.add(listToSend.first)
        lists.add(listFinished.first)
        this.add(BorderLayout.CENTER, lists)

        val buttons = JPanel(GridLayout(1, 3))
        buttons.add(addButton)
        buttons.add(finishButton)
        buttons.add(sendButton)
        this.add(BorderLayout.SOUTH, buttons)
    }

    fun refresh() {
        listAssigned.second.clear()
        tasksAssigned.forEach { listAssigned.second.addElement(it) }
        listToSend.second.clear()
        tasksToSend.forEach { listToSend.second.addElement(it) }
        listFinished.second.clear()
        tasksFinished.forEach { listFinished.second.addElement(it) }
    }
}