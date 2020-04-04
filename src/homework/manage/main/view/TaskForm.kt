package homework.manage.main.view

import homework.manage.main.model.TaskLists
import homework.manage.main.run
import java.awt.GridLayout
import javax.swing.JCheckBox
import javax.swing.JFrame
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.border.TitledBorder

class TaskForm : JFrame() {
    private val subject = JTextField()
    private val assignedDate = JTextField()
    private val dueDate = JTextField()
    private val toSend = JCheckBox()
    private val contents = JTextArea()
    private val addButton = button("Dodaj") {
        TaskLists.addNewTask(assignedDate.text, dueDate.text, subject.text, toSend.isSelected, contents.text)
        run(TasksApp(), 600, 600, "Zadania domowe")
        dispose()
    }

    init {
        subject.border = TitledBorder("Przedmoit")
        assignedDate.border = TitledBorder("Data zadania")
        dueDate.border = TitledBorder("Data oddania")
        toSend.border = TitledBorder("Do wysłania")
        contents.border = TitledBorder("Treść")
        this.layout = GridLayout(6, 1)
        this.add(subject)
        this.add(assignedDate)
        this.add(dueDate)
        this.add(toSend)
        this.add(contents)
        this.add(addButton)
    }
}