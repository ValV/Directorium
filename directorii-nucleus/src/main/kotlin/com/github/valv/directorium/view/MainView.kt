package com.github.valv.directorium.view

import com.github.valv.directorium.control.Data
import com.github.valv.directorium.app.Styles
import com.github.valv.directorium.control.Events.*
import com.github.valv.directorium.control.Data.*
import javafx.application.Platform
import javafx.beans.property.*
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.Priority
import javafx.util.converter.*
import tornadofx.*

class MainView : View("Directorium") {
    val dataState: Data by inject()

    override val root = borderpane {
        addClass(Styles.basis)
        top {
            add(MainMenuFragment::class)
        }
        left {
            treeview<Any> {
                /* Items */
                root = TreeItem()
                isShowRoot = false
                cellFormat {
                    text = when (it) {
                        is String -> it
                        is Category -> it.category
                        is Section -> it.section
                        else -> kotlin.error("Invalid type value")
                    }
                }
                /* Events */
                subscribe<CommandResize> { prefWidth = it.number / 4 }
                subscribe<CommandTreePopulate> {
                    val categories = it.categories
                    populate { parent ->
                        val v = parent.value
                        when {
                            parent == root -> categories.keys
                            v is Category -> categories[v]
                            else -> null
                        }
                    }
                }
                selectionModel.selectedItemProperty().addListener {
                    _, _, item -> if (item.value is Section) fire(CommandLoadSection(item.value as Section))
                }
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
                    padding = insets(4)
                    alignment = Pos.BASELINE_RIGHT
                    label(title) {
                        addClass(Styles.heading)
                    }
                    button("⊕▥") { //▾↴⊖
                        action {
                            fire(CommandTableAddField("Field", "value"))
                        }
                    }
                    button("⊕▤") { //▸↳⊖
                        action { dataState.addRecord() }
                    }
                    region {
                        hgrow = Priority.ALWAYS
                    }
                    button {
                        text = "Close"
                        action {
                            fire(CommandQuit)
                        }
                    }
                }
                hbox {
                    label {
                        subscribe<CommandStatusDisplay> { text = it.status }
                    }
                }
            }
        }
        widthProperty().addListener { _, _, new -> fire(CommandResize(new.toDouble())) }
    }

    init {
        subscribe<CommandQuit> { Platform.exit() }
        subscribe<CommandDebug> { println("Debug Message!") } // TODO: remove after tests are implemented
        runLater {
            fire(CommandTreePopulate(dataState.categories))
        }
    }
}