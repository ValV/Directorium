package com.github.valv.directorium.view

import com.github.valv.directorium.app.Data
import com.github.valv.directorium.app.Styles
import com.github.valv.directorium.app.Events.*
import com.github.valv.directorium.app.Data.*
import javafx.application.Platform
import javafx.beans.InvalidationListener
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.Priority
import tornadofx.*

class MainView : View("Directorium") {
    val table = Table()
    override val root = borderpane {
        addClass(Styles.basis)
        top {
            menubar {
                menu {
                    text = "File"
                    menuitem("Print", "Ctrl+P") { fire(CommandPrint) }
                    separator()
                    menuitem("Quit", "Ctrl+Q") { fire(CommandQuit)}
                }
                menu {
                    text = "Sections"
                    menuitem("Add to Category") { fire(CommandAddSection) }
                    menuitem("Delete selected") { fire(CommandDeleteSection) }
                }
                menu {
                    text = "Data view"
                    menuitem("Add column") { fire(CommandAddColumn) }
                    menuitem("Remove column") { fire(CommandDeleteColumn) }
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
                        //println(parent)
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
                // Rows & Columns
                items = null // set data
                columns.setAll() // set columns
                //
                subscribe<CommandTableItemsSet> {
                    //println(it.table.contents)
                    //println(it.table.header)
                    this@tableview.items = it.table.contents
                    this@tableview.columns.setAll(it.table.header)
                    for (column in it.table.header) {
                        println(column.cellValueFactory)
                    }
                }
            }
        }
        bottom {
            hbox {
                padding = insets(4)
                alignment = Pos.BASELINE_RIGHT
                label(title) {
                    addClass(Styles.heading)
                }
                button {
                    text = "Add"
                    action {
                        table.header.add(TableColumn<ObservableList<ObjectProperty<*>>, Float>("Added"))
                        table.contents.forEach { it.add(SimpleObjectProperty(0.6)) }
                        fire(CommandTableItemsSet(table))
                        println(table.header)
                    }
                }
                region {
                    hgrow = Priority.ALWAYS
                }
                button {
                    text = " Close "
                    action {
                        fire(CommandQuit)
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
            val data = FXCollections.observableArrayList<ObjectProperty<*>>()
            val property = SimpleObjectProperty("First data")
            data.add(property)
            val column = TableColumn<ObservableList<ObjectProperty<*>>, String>("First column")
            column.graphic = button("✗"/*"✖"*/) {
                addClass("circle")
                action { println("${width}x${height}") }
            }
            column.setCellValueFactory { property }
            table.header.add(column)
            table.contents.add(data)
            fire(CommandTableItemsSet(table))
        }
    }
}