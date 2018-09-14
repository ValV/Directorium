package com.github.valv.directorium.view

import com.github.valv.directorium.app.Styles
import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.control.TableColumn
import javafx.scene.control.TreeItem
import javafx.scene.layout.Priority
import tornadofx.*

class CommandResize(val number: Double): FXEvent()
object CommandQuit: FXEvent()

class MainView : View("Directorium") {
    override val root = borderpane {
        addClass(Styles.basis)
        top {
            menubar {
                menu {
                    text = "File"
                    menuitem("Print", "Ctrl+P")
                    separator()
                    menuitem("Quit", "Ctrl+Q") { fire(CommandQuit)}
                }
                menu {
                    text = "Tree"
                    menuitem("Add")
                    menuitem("Delete")
                }
                menu {
                    text = "Table"
                    menuitem("Add column")
                    menuitem("Remove column")
                }
            }
        }
        left {
            treeview<Any> {
                subscribe<CommandResize> { prefWidth = it.number / 4 }
                // Items
                root = TreeItem("Directorium")
            }
        }
        center {
            tableview<TableColumn<*, *>> {
                // Rows & Columns
            }
        }
        bottom {
            hbox {
                padding = insets(4)
                alignment = Pos.BASELINE_RIGHT
                label(title) {
                    addClass(Styles.heading)
                }
                region {
                    hgrow = Priority.ALWAYS
                }
                button {
                    text = " Close "
                }
            }
        }
        widthProperty().addListener { _, _, new -> fire(CommandResize(new.toDouble())) }
    }

    init {
        subscribe<CommandQuit> { Platform.exit() }
    }
}