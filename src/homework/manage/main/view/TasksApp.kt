package homework.manage.main.view

import java.awt.FlowLayout
import javax.swing.DefaultListModel
import javax.swing.JFrame
import javax.swing.JList
import homework.manage.main.model.Task
import homework.manage.main.model.TaskLists.tasksAssigned
import homework.manage.main.model.TaskLists.tasksFinished
import homework.manage.main.model.TaskLists.tasksToSend
import java.awt.GridLayout

class TasksApp : JFrame() {
    private val listAssigned = JList<Task>()
    private val listFinished = JList<Task>()
    private val listToSend = JList<Task>()
    private val addButton = button("Dodaj zadanie") {
        homework.manage.main.run(TaskForm(), 1000, 1000, "Nowe zadanie")
        dispose()
    }
    private val finishButton = button("Oznacz już zrobione") {
        val taskId = listAssigned.selectedIndex
        val task = tasksAssigned.removeAt(taskId)
        if (task.toSend) tasksToSend.add(task)
        else tasksFinished.add(task)
    }
    private val sendButton = button("Oznacz już wysłane") {
        val taskId = listToSend.selectedIndex
        val task = tasksToSend.removeAt(taskId)
        tasksFinished.add(task)
    }

    init {
        val modelAssigned = DefaultListModel<Task>()
        modelAssigned.addAll(tasksAssigned)
        listAssigned.model = modelAssigned
        val modelFinished = DefaultListModel<Task>()
        modelFinished.addAll(tasksFinished)
        listFinished.model = modelFinished
        val modelToSend = DefaultListModel<Task>()
        modelToSend.addAll(tasksToSend)
        listToSend.model = modelToSend

        this.layout = GridLayout(2,3)
        this.add(listAssigned)
        this.add(listFinished)
        this.add(listToSend)
        this.add(addButton)
        this.add(finishButton)
        this.add(sendButton)
    }
}