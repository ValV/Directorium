package com.github.valv.directorium.app

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
        label and heading {
            padding = box(8.px)
            fontSize = 16.px
            fontWeight = FontWeight.BOLD
        }
    }
}