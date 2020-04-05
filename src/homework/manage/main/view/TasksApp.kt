package homework.manage.main.view

import homework.manage.main.model.Task
import homework.manage.main.model.TaskLists
import homework.manage.main.model.TaskLists.tasksAssigned
import homework.manage.main.model.TaskLists.tasksFinished
import homework.manage.main.model.TaskLists.tasksToSend
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.GridLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.DefaultListModel
import javax.swing.JFrame
import javax.swing.JList
import javax.swing.JPanel

class TasksApp : JFrame() {
    private val modelAssigned = DefaultListModel<Task>()
    private val listAssigned = JList<Task>()
    private val modelFinished = DefaultListModel<Task>()
    private val listFinished = JList<Task>()
    private val modelToSend = DefaultListModel<Task>()
    private val listToSend = JList<Task>()
    private val addButton = button("Dodaj zadanie") {
        homework.manage.main.run(TaskForm(true), 1000, 1000, "Nowe zadanie")
        dispose()
    }

    private val finishButton = button("Oznacz już zrobione") {
        val taskId = listAssigned.selectedIndex
        val task = TaskLists.finishTask(taskId)
        modelAssigned.remove(taskId)
        if (task.toSend) {
            modelToSend.addElement(task)
        } else {
            modelFinished.addElement(task)
        }
    }
    private val sendButton = button("Oznacz już wysłane") {
        val taskId = listToSend.selectedIndex
        val task = TaskLists.sendTask(taskId)
        modelToSend.remove(taskId)
        modelFinished.addElement(task)
    }

    private val listListener = object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent?) {
            @Suppress("UNCHECKED_CAST") val list = e?.source as JList<Task>
            if (e.clickCount == 2) {
                homework.manage.main.run(TaskForm(list.selectedValue), 1000, 1000, "Zadanie")
                dispose()
            }
        }
    }

    init {

        tasksAssigned.sortBy { task -> task.dueDate }
        modelAssigned.addAll(tasksAssigned)
        listAssigned.model = modelAssigned
        listAssigned.addMouseListener(listListener)

        modelFinished.addAll(tasksFinished)
        listFinished.model = modelFinished
        listFinished.addMouseListener(listListener)

        modelToSend.addAll(tasksToSend)
        listToSend.model = modelToSend
        listToSend.addMouseListener(listListener)

        val lists = JPanel()
        lists.layout = GridLayout(1, 3)
        lists.add(listAssigned)
        lists.add(listFinished)
        lists.add(listToSend)
        this.add(BorderLayout.CENTER, lists)

        val buttons = JPanel()
        buttons.layout = FlowLayout()
        buttons.add(addButton)
        buttons.add(finishButton)
        buttons.add(sendButton)
        this.add(BorderLayout.SOUTH, buttons)
    }
}