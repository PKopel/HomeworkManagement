package homework.manage.main.view

import java.awt.Color
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.KeyStroke

fun button(
    text: String = "",
    keyShortcut: KeyStroke? = null,
    background: Color = Color.LIGHT_GRAY,
    action: (ActionEvent) -> Unit
): JButton {
    val button = JButton()
    button.background = background
    button.action = object : AbstractAction() {
        override fun actionPerformed(p0: ActionEvent) = action(p0)
    }
    if (keyShortcut != null) {
        button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyShortcut, "key shortcut")
        button.actionMap.put("key shortcut", button.action)
    }
    button.text = text
    return button
}