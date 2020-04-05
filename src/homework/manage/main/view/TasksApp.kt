package homework.manage.main.view

import homework.manage.main.model.Task
import homework.manage.main.model.TaskLists
import homework.manage.main.model.TaskLists.subjects
import homework.manage.main.model.TaskLists.tasksAssigned
import homework.manage.main.model.TaskLists.tasksFinished
import homework.manage.main.model.TaskLists.tasksToSend
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.border.TitledBorder

object TasksApp : JFrame() {
    private val modelAssigned = DefaultListModel<Task>()
    private val listAssigned = JList<Task>()
    private val modelFinished = DefaultListModel<Task>()
    private val listFinished = JList<Task>()
    private val modelToSend = DefaultListModel<Task>()
    private val listToSend = JList<Task>()
    private val addButton = button("Dodaj zadanie") {
        homework.manage.main.run(TaskForm(true), 1000, 1000, "Nowe zadanie")
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

    private val clearFilterButton = button("Wyczyść") {
        refresh()
    }

    private val modelFilter = DefaultComboBoxModel<String>()
    private val filter = JComboBox<String>()

    private val filterListener = { it: ActionEvent ->
        val filter = it.source as JComboBox<String>
        modelAssigned.clear()
        modelAssigned.addAll(tasksAssigned.filter { it.subject == filter.selectedItem })
        modelToSend.clear()
        modelToSend.addAll(tasksToSend.filter { it.subject == filter.selectedItem })
        modelFinished.clear()
        modelFinished.addAll(tasksFinished.filter { it.subject == filter.selectedItem })
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
            }
        }
    }

    init {
        tasksAssigned.sortBy { task -> task.dueDate }
        modelAssigned.addAll(tasksAssigned)
        listAssigned.model = modelAssigned
        listAssigned.border = TitledBorder("Zadane")
        listAssigned.addMouseListener(listListener)

        modelToSend.addAll(tasksToSend)
        listToSend.model = modelToSend
        listToSend.border = TitledBorder("Do wysłania")
        listToSend.addMouseListener(listListener)

        modelFinished.addAll(tasksFinished)
        listFinished.model = modelFinished
        listFinished.border = TitledBorder("Gotowe")
        listFinished.addMouseListener(listListener)

        val filters = JPanel()
        filters.layout = FlowLayout()
        filter.addActionListener(filterListener)
        modelFilter.addAll(subjects)
        filter.model = modelFilter
        filters.add(filter)
        filters.add(clearFilterButton)
        this.add(BorderLayout.NORTH, filters)

        val lists = JPanel()
        lists.layout = GridLayout(1, 3)
        lists.add(listAssigned)
        lists.add(listToSend)
        lists.add(listFinished)
        this.add(BorderLayout.CENTER, lists)

        val buttons = JPanel()
        buttons.layout = GridLayout(1, 3)
        buttons.add(addButton)
        buttons.add(finishButton)
        buttons.add(sendButton)
        this.add(BorderLayout.SOUTH, buttons)
    }

    fun refresh() {
        modelAssigned.clear()
        modelAssigned.addAll(tasksAssigned)
        modelToSend.clear()
        modelToSend.addAll(tasksToSend)
        modelFinished.clear()
        modelFinished.addAll(tasksFinished)
    }
}