package com.github.valv.directorium.view

import com.github.valv.components.StringCell
import com.github.valv.directorium.app.Data
import com.github.valv.directorium.app.Styles
import com.github.valv.directorium.app.Events.*
import com.github.valv.directorium.app.Data.*
import javafx.application.Platform
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.Priority
import tornadofx.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class MainView : View("Directorium") {
    val table = Table()
    override val root = borderpane {
        addClass(Styles.basis)
        top {
            menubar {
                menu {
                    text = "⌨ File"
                    menuitem("⎙ Print", "Ctrl+P") { fire(CommandPrint) }
                    separator()
                    menuitem("⏻ Quit", "Ctrl+Q") { fire(CommandQuit)}
                }
                menu {
                    text = "◩ Sections"
                    menuitem("Add to Category") { fire(CommandAddSection) }
                    menuitem("Delete selected") { fire(CommandDeleteSection) }
                }
                menu {
                    text = "▦ Data view"
                    menuitem("⊞↴ Add Field") { fire(CommandAddColumn) }
                    menuitem("⊟↴ Delete Field") { fire(CommandDeleteColumn) }
                    menuitem("⊞↳ Add Record") { fire(CommandAddColumn) }
                    menuitem("⊟↳ Delete Record") { fire(CommandDeleteColumn) }
                }
            }
        }
        left {
            treeview<Any> {
                // Items
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
                // Events
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
            tableview<ObservableList<ObjectProperty<*>>> {
                columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
                isEditable = true
                //multiSelect()
                // Rows & Columns
                items = null // set data
                columns.setAll() // set columns
                subscribe<CommandTableItemsSet> {
                    this@tableview.items = it.table.contents
                    this@tableview.columns.setAll(it.table.header)
                }
                focusModel.focusedCellProperty().addListener { _, _, y ->
                    fire(CommandStatusDisplay("${y.row}:${y.column}"))
                }
                //selectionModel.selectedItemProperty().addListener { _ -> println(focusModel.focusedCell) }
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
                            addField(table, "Added", 0.0)
                            fire(CommandTableItemsSet(table))
                        }
                    }
                    button("⊕▤") { //▸↳⊖
                        action {
                            addRecord()
                            fire(CommandTableItemsSet(table))
                        }
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
        runLater {
            fire(CommandTreePopulate(Data.categories))
            addField(table, "First Column", "First Data")
            addRecord()
            addRecord()
            fire(CommandTableItemsSet(table))
        }
    }

    private fun <T: Any> addField(table: Table, name: String, data: T) {
        val columnProperty: ObjectProperty<T> = SimpleObjectProperty<T>(data)
        val column = TableColumn<ObservableList<ObjectProperty<*>>, T>(name)
        /*column.graphic = button("✗") {
            addClass("circle")
            action { println("${width}x${height}") }
        }*/
        column.setCellValueFactory { columnProperty as ObservableValue<T> }
        column.setCellFactory { StringCell() as TableCell<ObservableList<ObjectProperty<*>>, T> }
        table.header.add(column)
        table.contents.forEach { it.add(SimpleObjectProperty<T>(data)) }
    }
/*
    @PublishedApi()
    internal open class TypeBase<T>
    inline fun <reified T> typeTokenOf(): Type {
        val base = object: TypeBase<T>() {}
        val superType = base::class.java.genericSuperclass!!
        return (superType as ParameterizedType).actualTypeArguments.first()!!
    }
*/
    private fun addRecord() {
        val record = FXCollections.observableArrayList<ObjectProperty<*>>()
        table.header.forEach {
            fun <S, T> translate(c: TableColumn<S, T>): ObjectProperty<T> =
                    SimpleObjectProperty<T>(c.getCellData(null))
            record.add(translate(it))
        }
        table.contents.add(record)
    }
}