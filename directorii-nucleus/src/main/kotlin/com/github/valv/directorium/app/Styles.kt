package com.github.valv.directorium.app

import javafx.geometry.Pos
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    val highlight = false

    companion object {
        val heading by cssclass()
        val grounding by cssclass()
    }

    init {
        if (highlight) {
            s(label, fieldset) {
                borderColor += box(c("blue"))
            }
            s(menuBar, toolBar, inputContainer) {
                borderColor += box(c("red"))
            }
            s(tableView, treeView, field) {
                borderColor += box(c("green"))
            }
            s(tableColumn, treeCell) {
                borderColor += box(c("lightgray"))
            }
        }
        grounding {
            minWidth = 640.px
            minHeight = 360.px
            prefWidth = 640.px
            prefHeight = 360.px
            padding = box(1.px)
        }
        label and heading {
            minHeight = 24.px
            maxHeight = 24.px
            padding = box(0.px, 8.px)
            fontSize = 16.px
            fontWeight = FontWeight.BOLD
        }
        s(tableView, treeView) {
            borderInsets += box(1.px)
        }
        button {
            padding = box(5.px, 20.px)
        }
        field {
            alignment = Pos.CENTER_RIGHT
            comboBox {
                minWidth = 120.px
                maxWidth = 120.px
            }
            textField {
                minWidth = 120.px
                maxWidth = 120.px
            }
            inputContainer {
                minWidth = 200.px
            }
        }
    }
}
