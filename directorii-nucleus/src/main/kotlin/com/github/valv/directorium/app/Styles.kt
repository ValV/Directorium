package com.github.valv.directorium.app

import javafx.geometry.Pos
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
        val basis by cssclass()
        val circle by cssclass()
        val bigCircle by cssclass()
    }

    init {
        basis {
            minWidth = 640.px
            minHeight = 360.px
            prefWidth = 640.px
            prefHeight = 360.px
            padding = box(1.px)
        }
        treeView {
            borderInsets += box(1.px)
        }
        tableView {
            borderInsets += box(1.px)
        }
        button {
            padding = box(5.px, 20.px)
        }
        button and circle {
            padding = box(0.px)
            minHeight = 16.px
            maxHeight = 16.px
            minWidth = 16.px
            maxWidth = 16.px
            backgroundRadius += box(8.px)
            fontSize = 12.px
            //fontWeight = FontWeight.BOLD
            //borderInsets += box(0.px)
            backgroundInsets += box(0.px)
        }
        fieldset {
            //alignment = Pos.CENTER
            borderColor += box(c("blue"))
        }
        s(field) {
            //minWidth = 240.px
            //maxWidth = 400.px
            borderColor += box(c("green"))
            alignment = Pos.CENTER_RIGHT
            comboBox {
                //maxWidth = Double.MAX_VALUE.px
                minWidth = 120.px
                maxWidth = 120.px
            }
            textField {
                //maxWidth = Double.MAX_VALUE.px
                minWidth = 120.px
                maxWidth = 120.px
            }
            inputContainer {
                minWidth = 200.px
                borderColor += box(c("red"))
            }
        }
        label and heading {
            minHeight = 24.px
            maxHeight = 24.px
            padding = box(0.px, 8.px)
            fontSize = 16.px
            fontWeight = FontWeight.BOLD
        }
    }
}