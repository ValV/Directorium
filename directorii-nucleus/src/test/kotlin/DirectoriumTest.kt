package com.github.valv.directorium.app

import com.github.valv.directorium.app.Styles
import com.github.valv.directorium.control.Data
import com.github.valv.directorium.view.MainView
import javafx.stage.Stage
import tornadofx.*

class DirectoriumTest : App(MainView::class, Styles::class) {
    private val mainView: MainView by inject()
    private val dataState: Data by inject()

    override fun start(stage: Stage) {
        super.start(stage)
        runLater { /* TODO: debug code here */ }
    }
}
