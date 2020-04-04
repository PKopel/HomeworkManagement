package homework.manage.main.view

import java.awt.Color
import java.awt.event.ActionEvent
import javax.swing.JButton

fun button(text: String = "", background: Color = Color.LIGHT_GRAY, listener: (ActionEvent) -> Unit): JButton {
    val button = JButton(text)
    button.background = background
    button.addActionListener(listener)
    return button
}