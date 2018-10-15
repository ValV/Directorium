package com.github.valv.directorium.view

import com.github.valv.directorium.control.Events.*
import javafx.geometry.Pos.BASELINE_RIGHT
import javafx.scene.layout.Priority.ALWAYS
import tornadofx.*

class FragmentToolPanel : Fragment() {
    override val root = vbox {
        hbox {
            toolbar {
                subscribe<CommandResize> { prefWidth = it.number / 4 }
                padding = insets(4)
                button("◪✔") {
                    action { fire(CommandCreateSection) }
                }
                button("◪✘") {
                    action { fire(CommandDeleteSection) }
                }
            }
            toolbar {
                padding = insets(4)
                alignment = BASELINE_RIGHT
                hgrow = ALWAYS
                button("◨✔") { action { fire(CommandCreateField) } }
                button("◨✘") { action { fire(CommandDeleteField) } }
                region { hgrow = ALWAYS }
                button("⬓✔") { action { fire(CommandCreateRecord) } }
                button("⬓✘") { action { fire(CommandDeleteRecord) } }
                region { hgrow = ALWAYS }
                button("⏻") { action { fire(CommandQuit) } }
            }
        }
        hbox {
            style { alignment = BASELINE_RIGHT }
            label {
                subscribe<CommandStatusDisplay> { text = it.status }
            }
        }
    }
}
