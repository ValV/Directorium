package com.github.valv.directorium.view

import com.github.valv.directorium.control.Data
import com.github.valv.directorium.app.Styles
import com.github.valv.directorium.control.Events.*
import javafx.application.Platform
import javafx.beans.property.*
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import javafx.geometry.Pos.*
import javafx.scene.control.*
import javafx.scene.layout.Priority.*
import javafx.util.converter.*
import tornadofx.*

class MainView : View("Directorium") {
    val dataState: Data by inject()
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
                subscribe<CommandTableCreateField<Any>> {
                    // TODO: move to Controller
                    val item = it.item
                    this@tableview.items.forEach { it.add(SimpleObjectProperty(item)) }
                    column(it.name, SimpleObjectProperty::class) {
                        setCellValueFactory {
                            dataState.records[items.indexOf(it.value)][tableView.columns.indexOf(this)]
                                    as ObservableValue<SimpleObjectProperty<*>>
                        }
                        when (item) {
                            is String -> (this as TableColumn<ObservableList<*>, String?>)
                                    .useTextField(DefaultStringConverter())
                        }
                        dataState.fields.add(item)
                    }
                }
                subscribe<CommandTableDeleteField> {
                    // TODO: move to Controller
                    val number = columns.indexOf(it.name)
                    columns.removeAt(number)
                    dataState.fields.removeAt(number)
                    dataState.records.forEach { it.removeAt(number) }
                }
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
                        button("◨✔") {
                            action { fire(CommandCreateField) }
                        } //▥▾↴⊕⊖▣◨✔✘
                        button("◨✘") { action { fire(CommandDeleteField) } }
                        region { hgrow = ALWAYS }
                        button("⬓✔") {
                            // TODO: move to Controller
                            action { dataState.addRecord() }
                        } //▤▸↳⊕⊖▣⬓✔✘
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
        subscribe<CommandTableCreateField<Any>> {
            println("Debug (CommandTableCreateField<Any>): ${it.name}, ${it.item}")
        }
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
