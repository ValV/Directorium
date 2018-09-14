package com.github.valv.directorium.app

import com.github.valv.directorium.view.MainView
import javafx.application.Application
import tornadofx.*

class Directorium: App(MainView::class, Styles::class) {
    companion object {
        fun main(args: Array<String>) {
            Application.launch(Directorium::class.java, *args)
        }
    }
}
