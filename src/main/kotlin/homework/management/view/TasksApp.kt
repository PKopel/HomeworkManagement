package homework.management.view

import homework.management.DateDaysComparator
import homework.management.TaskComparator
import homework.management.files.Resources
import homework.management.model.Task
import homework.management.model.TaskLists
import homework.management.model.TaskLists.subjects
import homework.management.model.TaskLists.tasksAssigned
import homework.management.model.TaskLists.tasksFinished
import homework.management.model.TaskLists.tasksToSend
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.*
import javax.swing.*
import javax.swing.border.TitledBorder

object TasksApp : JFrame() {
    private val modelAssigned = DefaultListModel<Task>()
    private val listAssigned = JList<Task>()
    private val modelFinished = DefaultListModel<Task>()
    private val listFinished = JList<Task>()
    private val modelToSend = DefaultListModel<Task>()
    private val listToSend = JList<Task>()
    private val addButton = button(Resources.addLabel, KeyStroke.getKeyStroke(VK_N, CTRL_DOWN_MASK)) {
        run(TaskView(false), Resources.taskTitle, DISPOSE_ON_CLOSE, 1000, 1000)
    }

    //aaa
    private val finishButton = button(Resources.finishLabel, KeyStroke.getKeyStroke(VK_Z, CTRL_DOWN_MASK)) {
        val taskId = listAssigned.selectedIndex
        val task = TaskLists.finishTask(taskId)
        modelAssigned.remove(taskId)
        if (task.toSend) {
            modelToSend.addElement(task)
        } else {
            modelFinished.addElement(task)
        }
    }

    private val sendButton = button(Resources.sendLabel, KeyStroke.getKeyStroke(VK_W, CTRL_DOWN_MASK)) {
        val taskId = listToSend.selectedIndex
        val task = TaskLists.sendTask(taskId)
        modelToSend.remove(taskId)
        modelFinished.addElement(task)
    }

    private val clearFilterButton = button(Resources.clearLabel) {
        refresh()
    }

    private val modelFilter = DefaultComboBoxModel<String>()
    private val filter = JComboBox<String>()

    private val filterListener = { it: ActionEvent ->
        @Suppress("UNCHECKED_CAST") val filter = it.source as JComboBox<String>
        modelAssigned.clear()
        tasksAssigned.filter { it.subject == filter.selectedItem }.forEach { modelAssigned.addElement(it) }
        modelToSend.clear()
        tasksToSend.filter { it.subject == filter.selectedItem }.forEach { modelToSend.addElement(it) }
        modelFinished.clear()
        tasksFinished.filter { it.subject == filter.selectedItem }.forEach { modelFinished.addElement(it) }
    }

    private val listListener = object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent?) {
            @Suppress("UNCHECKED_CAST") val list = e?.source as JList<Task>
            if (e.clickCount == 2) {
                run(TaskView(list.selectedValue), Resources.taskTitle, DISPOSE_ON_CLOSE, 1000, 1000)
            }
        }
    }

    init {
        tasksAssigned.sortWith(TaskComparator())
        tasksAssigned.forEach { modelAssigned.addElement(it) }
        listAssigned.model = modelAssigned
        listAssigned.border = TitledBorder(Resources.assignedLabel)
        listAssigned.addMouseListener(listListener)
        listAssigned.cellRenderer = object : DefaultListCellRenderer() {
            override fun getListCellRendererComponent(
                    list: JList<*>,
                    value: Any,
                    index: Int,
                    isSelected: Boolean,
                    cellHasFocus: Boolean
            ): Component {
                val component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
                foreground = when {
                    DateDaysComparator().compare((value as Task).dueDate, Date()) < 0 -> Color.RED
                    DateDaysComparator().compare(value.dueDate, Date()) == 0 -> Color.YELLOW.darker()
                    else -> Color.GREEN.darker()
                }
                if (isSelected) {
                    background = background.darker()
                }
                return component
            }
        }

        tasksToSend.forEach { modelToSend.addElement(it) }
        listToSend.model = modelToSend
        listToSend.border = TitledBorder(Resources.toSendLabel)
        listToSend.addMouseListener(listListener)

        tasksFinished.forEach { modelFinished.addElement(it) }
        listFinished.model = modelFinished
        listFinished.border = TitledBorder(Resources.finishedLabel)
        listFinished.addMouseListener(listListener)

        val filters = JPanel()
        filters.layout = FlowLayout()
        filter.addActionListener(filterListener)
        filter.maximumRowCount = subjects.size
        subjects.forEach { modelFilter.addElement(it) }
        filter.model = modelFilter
        filters.add(filter)
        filters.add(clearFilterButton)
        this.add(BorderLayout.NORTH, filters)

        val lists = JPanel(GridLayout(1, 3))
        lists.add(listAssigned)
        lists.add(listToSend)
        lists.add(listFinished)
        this.add(BorderLayout.CENTER, lists)

        val buttons = JPanel(GridLayout(1, 3))
        buttons.add(addButton)
        buttons.add(finishButton)
        buttons.add(sendButton)
        this.add(BorderLayout.SOUTH, buttons)
    }

    fun refresh() {
        modelAssigned.clear()
        tasksAssigned.forEach { modelAssigned.addElement(it) }
        modelToSend.clear()
        tasksToSend.forEach { modelToSend.addElement(it) }
        modelFinished.clear()
        tasksFinished.forEach { modelFinished.addElement(it) }
    }
}