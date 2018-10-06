package com.github.valv.directorium.view

import com.github.valv.directorium.control.Data
import com.github.valv.directorium.app.Styles
import com.github.valv.directorium.control.Events.*
import javafx.application.Platform
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import javafx.geometry.Pos.*
import javafx.scene.control.*
import javafx.scene.layout.Priority.*
import tornadofx.*

class MainView : View("Directorium") {
    private val dataState: Data by inject()
    lateinit var dataView: TableView<ObservableList<ObservableValue<Any>>>

    override val root = borderpane {
        addClass(Styles.basis)
        widthProperty().addListener { _, _, new -> fire(CommandResize(new.toDouble())) }
        top {
            add(MainMenuFragment::class)
        }
        left {
            vbox {
                label(title) { addClass(Styles.heading) }
                add(CategoryTreeFragment::class)
            }
        }
        center {
            dataView = tableview(dataState.records) {
                columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
                isEditable = true
                subscribe<CommandTableUpdate> { it.update(this@tableview) }
                focusModel.focusedCellProperty().addListener { _, _, y ->
                    fire(CommandStatusDisplay("${y.row}:${y.column}"))
                }
            }
        }
        bottom {
            vbox {
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
                        button("◨✔") { action { fire(CommandCreateField) } } //▥▾↴⊕⊖▣◨✔✘
                        button("◨✘") { action { fire(CommandDeleteField) } }
                        region { hgrow = ALWAYS }
                        button("⬓✔") { action { fire(CommandCreateRecord) } } //▤▸↳⊕⊖▣⬓✔✘
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
    }

    init {
        subscribe<CommandQuit> { Platform.exit() }
        subscribe<CommandCreateSection> {
            find<CategoryControlFragment>(mapOf(
                    CategoryControlFragment::categories to dataState.categories,
                    CategoryControlFragment::creation to true
            )).openModal()
        }
        subscribe<CommandDeleteSection> {
            find<CategoryControlFragment>(mapOf(
                    CategoryControlFragment::categories to dataState.categories,
                    CategoryControlFragment::creation to false
            )).openModal()
        }
        subscribe<CommandCreateField> {
            find<DataViewControlFragment>(mapOf(
                    DataViewControlFragment::columnNames to dataView.columns,
                    DataViewControlFragment::creation to true
            )).openModal()
        }
        subscribe<CommandDeleteField> {
            find<DataViewControlFragment>(mapOf(
                    DataViewControlFragment::columnNames to dataView.columns,
                    DataViewControlFragment::creation to false
            )).openModal()
        }
        // FIXME: BEGIN: remove DEBUG events handling section
        subscribe<CommandDebug> { println("Debug Message!") }
        // FIXME: END: remove DEBUG events handling section
        runLater {
            // TODO: move to Test unit
            dataState.categories.putAll(mutableMapOf(
                    "Books" to mutableListOf("Sci-fi").observable(),
                    "Music" to mutableListOf("Ambient", "Industrial").observable()
            ).observable())
            fire(CommandTreePopulate(dataState.categories))
            fire(CommandTreeCreateSection("Books", "Academic"))
            fire(CommandTreeCreateSection("Hooks", "Fishing"))
        }
    }
}
