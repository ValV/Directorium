package com.github.valv.directorium.app

import com.github.valv.directorium.app.Styles
import com.github.valv.directorium.control.Data
import com.github.valv.directorium.control.Events.*
import com.github.valv.directorium.view.MainView
import com.sun.javafx.collections.ObservableListWrapper
import javafx.beans.property.SimpleListProperty
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import javafx.scene.control.TableView
import javafx.stage.Stage
import javafx.stage.Window
import tornadofx.*
import kotlinx.serialization.*
import kotlinx.serialization.json.JSON
import java.io.StringWriter
import java.util.*
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import kotlin.collections.ArrayList

class DirectoriumTest : App(MainView::class, Styles::class) {
    private val mainView: MainView by inject()
    private val dataState: Data by inject()

    override fun start(stage: Stage) {
        super.start(stage)
        runLater {
            //dataState.categories.putAll(mutableMapOf(
            //        "Books" to mutableListOf("Sci-fi").observable(),
            //        "Music" to mutableListOf("Ambient", "Industrial").observable()
            //).observable())

            // Populate TreeView with predefined data
            //fire(CommandTreePopulate(dataState.categories))

            // Add categories and sections separately
            //fire(CommandTreeCreateSection("Books", "Academic"))
            //fire(CommandTreeCreateSection("Films", "Sci-fi"))

            // Add columns
            //fire(CommandTableCreateField("String", "EMPTY"))
            //fire(CommandTableCreateField("Integer", 0))
            //fire(CommandTableCreateField("Double", 0.0))
            //fire(CommandTableCreateField("Boolean", true))

            // Add rows
            //fire(CommandCreateRecord)
            //fire(CommandCreateRecord)

            //dataState.saveIndex()
            //dataState.loadData("Books/Academic")
        }
    }
}
