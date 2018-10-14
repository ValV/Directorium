package com.github.valv.directorium.view

import com.github.valv.directorium.control.Data
import com.github.valv.directorium.app.Styles
import com.github.valv.directorium.control.Events.*
import com.sun.javafx.scene.control.skin.TableViewSkinBase
import javafx.application.Platform
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import javafx.scene.control.*
import tornadofx.*

class MainView : View("Directorium") {
    private val dataState: Data by inject()
    private val categoryView = find(CategoryTreeFragment::class)
    lateinit var dataView: TableView<ObservableList<ObservableValue<Any>>>

    override val root = borderpane {
        addClass(Styles.grounding)
        widthProperty().addListener { _, _, new -> fire(CommandResize(new.toDouble())) }
        top {
            add(MainMenuFragment::class)
        }
        left {
            vbox {
                label(title) { addClass(Styles.heading) }
                add(categoryView)
            }
        }
        center {
            dataView = tableview(dataState.records) {
                skinProperty().addListener { _, _, skin ->
                    val s =
                            (skin as TableViewSkinBase<*, *, *, *, *, *>).getTableHeaderRow()
                    s.reorderingProperty().addListener { _ -> s.isReordering = false }
                }
                isEditable = true
                subscribe<CommandTableUpdate> { it.update(this@tableview) }
                focusModel.focusedCellProperty().addListener { _, _, y ->
                    fire(CommandStatusDisplay("${y.row}:${y.column}"))
                }
                smartResize()
                subscribe<CommandTableResize> { this@tableview.requestResize() }
            }
        }
        bottom {
            add(ToolPanelFragment::class)
        }
    }

    init {
        subscribe<CommandQuit> {
            val selected = categoryView.root.selectionModel.selectedItem
            if (selected?.parent?.value != null) {
                val path = "${selected.parent.value}/${selected.value}"
                dataState.saveData(path)
            }
            Platform.exit()
        }
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
        subscribe<CommandPrint> {
            find<DataPrintFragment>(mapOf(
                    DataPrintFragment::source to dataView
            )).openModal()
            println("Opened modal!") // FIXME: remove (debug)
        }
        // FIXME: remove DEBUG events handling
        subscribe<CommandDebug> { println("Debug Message!") }
        runLater {
            dataState.loadIndex()
        }
    }
}
