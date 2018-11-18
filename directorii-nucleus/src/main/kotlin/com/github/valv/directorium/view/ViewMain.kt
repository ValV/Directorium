package com.github.valv.directorium.view

import com.github.valv.directorium.app.Styles
import com.github.valv.directorium.control.Data
import com.github.valv.directorium.control.Events.*
import com.sun.javafx.scene.control.skin.TableViewSkinBase
import javafx.application.Platform
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.TableView
import kotlinx.serialization.ImplicitReflectionSerializer
import tornadofx.*

@ImplicitReflectionSerializer
class ViewMain : View("Directorium") {
    private val dataState: Data by inject()
    private val categoryView = find(FragmentCategoryTree::class)
    private lateinit var dataView: TableView<MutableList<SimpleObjectProperty<Any>>>

    override val root = borderpane {
        addClass(Styles.grounding)
        widthProperty().addListener { _, _, new -> fire(CommandResize(new.toDouble())) }
        top {
            add(FragmentMainMenu::class)
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
            add(FragmentToolPanel::class)
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
            find<FragmentCategoryControl>(mapOf(
                    FragmentCategoryControl::categories to dataState.categories,
                    FragmentCategoryControl::creation to true
            )).openModal()
        }
        subscribe<CommandDeleteSection> {
            find<FragmentCategoryControl>(mapOf(
                    FragmentCategoryControl::categories to dataState.categories,
                    FragmentCategoryControl::creation to false
            )).openModal()
        }
        subscribe<CommandCreateField> {
            find<FragmentDataViewControl>(mapOf(
                    FragmentDataViewControl::columnNames to dataView.columns,
                    FragmentDataViewControl::creation to true
            )).openModal()
        }
        subscribe<CommandDeleteField> {
            find<FragmentDataViewControl>(mapOf(
                    FragmentDataViewControl::columnNames to dataView.columns,
                    FragmentDataViewControl::creation to false
            )).openModal()
        }
        subscribe<CommandPrint> {
            find<FragmentDataViewPrint>(mapOf(
                    FragmentDataViewPrint::source to dataView
            )).openModal()
        }
        runLater {
            dataState.loadIndex()
            root.requestFocus()
        }
    }
}
