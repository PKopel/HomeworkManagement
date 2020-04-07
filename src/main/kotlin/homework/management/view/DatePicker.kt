package homework.management.main.view

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.GridLayout
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.*

class DatePicker(parent: JFrame?) {
    private val daysOfWeek = arrayOf("Pon", "Wt", "Śr", "Czw", "Pt", "Sob", "Nd")

    private var selectedYear = Calendar.getInstance()[Calendar.YEAR]
    private var selectedMonth = Calendar.getInstance()[Calendar.MONTH]
        set(value) {
            field = value
            displayMonth()
        }
    private var selectedDay = ""

    private val dateDialog: JDialog = JDialog()
    private val dayHeaders = Array(7) {
        JLabel(daysOfWeek[it], JLabel.CENTER)
    }
    private val current = JLabel("", JLabel.CENTER)
    private val dayButtons = Array(42) {
        JButton().apply {
            background = Color.LIGHT_GRAY
            addActionListener {
                selectedDay = this.actionCommand
                dateDialog.dispose()
            }
        }
    }

    private fun displayMonth() {
        for (x in 7 until dayButtons.size) dayButtons[x].text = ""
        val cal = Calendar.getInstance()
        cal[selectedYear, selectedMonth] = 1
        var x = (5 + cal[Calendar.DAY_OF_WEEK]) % 7
        for (day in 1..cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            dayButtons[x++].text = day.toString()
        }
        current.text = SimpleDateFormat("MM-yyyy").format(cal.time)
        dateDialog.title = "Wybierz datę"
    }

    fun pickDate(): String {
        if (selectedDay == "") return selectedDay
        val sdf = SimpleDateFormat(
            "dd-MM-yyyy"
        )
        val cal = Calendar.getInstance()
        cal[selectedYear, selectedMonth] = selectedDay.toInt()
        return sdf.format(cal.time)
    }

    init {
        dateDialog.isModal = true

        val header = JPanel(GridLayout(1, 7))
        dayHeaders.forEach { header.add(it) }

        val days = JPanel(GridLayout(6, 7))
        dayButtons.forEach { days.add(it) }
        days.preferredSize = Dimension(500, 140)

        val months = JPanel(GridLayout(1, 3))
        val previous = button("Poprzedni") { selectedMonth-- }
        val next = button("Następny") { selectedMonth++ }
        months.add(previous)
        months.add(current)
        months.add(next)

        dateDialog.add(header, BorderLayout.NORTH)
        dateDialog.add(days, BorderLayout.CENTER)
        dateDialog.add(months, BorderLayout.SOUTH)
        dateDialog.pack()
        dateDialog.setLocationRelativeTo(parent)
        displayMonth()
        dateDialog.isVisible = true
    }
}