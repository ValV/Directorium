package com.github.valv.directorium.view

import com.github.valv.directorium.control.Data
import com.github.valv.directorium.app.Styles
import com.github.valv.directorium.control.Events.*
import com.github.valv.directorium.control.Data.*
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
            tableview(dataState.records) {
                columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
                isEditable = true
                subscribe<CommandTableAddField<Any>> {
                    // TODO: move to Controller
                    val item = it.item
                    this@tableview.items.forEach { it.add(SimpleObjectProperty(item)) }
                    column(it.name, SimpleObjectProperty::class) {
                        setCellValueFactory {
                            dataState.records[items.indexOf(it.value)][tableView.columns.indexOf(this)]
                            as ObservableValue<SimpleObjectProperty<*>>
                        }
                        (this as TableColumn<ObservableList<*>, String?>).
                                useTextField(DefaultStringConverter())
                        dataState.fields.add(item)
                    }
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
                        button("⊕◩") {
                            action { fire(CommandAddSection) }
                        }
                        button("⊖◩") {
                            action { fire(CommandDeleteSection) }
                        }
                    }
                    toolbar {
                        padding = insets(4)
                        alignment = BASELINE_RIGHT
                        hgrow = ALWAYS
                        button("⊕▥") {
                            action {
                                fire(CommandTableAddField("Field", "value"))
                            }
                        } //▾↴⊖
                        button("⊕▤") {
                            action { dataState.addRecord() }
                        } //▸↳⊖
                        region { hgrow = ALWAYS }
                        button("Close") { action { fire(CommandQuit) } }
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
        subscribe<CommandAddSection> {
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
        subscribe<CommandDebug> { println("Debug Message!") } // TODO: remove after tests are implemented
        runLater {

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