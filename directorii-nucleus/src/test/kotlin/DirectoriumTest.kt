package com.github.valv.directorium.app

import com.github.valv.directorium.control.Data
import com.github.valv.directorium.view.ViewMain
import javafx.stage.Stage
import kotlinx.serialization.ImplicitReflectionSerializer
import tornadofx.*

@ImplicitReflectionSerializer
class DirectoriumTest : App(ViewMain::class, Styles::class) {
    private val viewMain: ViewMain by inject()
    private val dataState: Data by inject()

    override fun start(stage: Stage) {
        super.start(stage)
        runLater { /* TODO: debug code here */ }
    }
}
