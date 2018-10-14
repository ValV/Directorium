package com.github.valv.directorium.view

import com.github.valv.directorium.control.Data
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import javafx.geometry.Pos.TOP_CENTER
import javafx.print.PageOrientation
import javafx.print.Paper
import javafx.print.Printer
import javafx.print.PrinterJob
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.layout.Priority.*
import tornadofx.*

class DataPrintFragment : Fragment("Print preview") {
    val source: TableView<ObservableList<ObservableValue<Any>>>? by param()

    private val dataState: Data by inject()
    private var dataView: TableView<*> by singleAssign()

    private val printers = Printer.getAllPrinters()
    private var printerJob = PrinterJob.createPrinterJob()?.apply {
        jobSettings.pageLayout = printer.createPageLayout(
                Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM
        )
    }

    override val root = vbox {
        prefWidth = primaryStage.scene.width
        prefHeight = primaryStage.scene.height
        alignment = TOP_CENTER
        toolbar {
            button("⎙") {
                action {
                    this@toolbar.isDisable = true
                    print()
                    close()
                }
            }
            region { hgrow = ALWAYS }
            combobox<Printer> {
                items = printers.toList().observable()
                value = Printer.getDefaultPrinter()
                selectionModel.selectedItemProperty().addListener { _, old, new ->
                    if (new != old) printerJob?.printer = new
                }
            }
            button("⚒") { action { printConfig() } }
            region { hgrow = ALWAYS }
            button("⏻") { action { close() } }

        }
        hbox {
            hgrow = ALWAYS
            alignment = TOP_CENTER
            scrollpane {
                hgrow = NEVER
                vgrow = ALWAYS
                dataView = tableview(source?.items) {
                    isEditable = false
                    dataState.fields.forEach { field ->
                        column(field.name, SimpleObjectProperty::class) {
                            value {
                                val row = items.indexOf(it.value)
                                val column = this@tableview.columns.indexOf(this)
                                tableView.items[row][column]
                            }
                            when (field.value) {
                                is String -> remainingWidth()
                                is Boolean -> {
                                    (this as TableColumn<*, Boolean?>).useCheckbox()
                                    contentWidth(padding = 2.0)
                                }
                                else -> contentWidth(padding = 2.0)
                            }
                        }
                    }
                    focusedProperty().addListener { _ -> this@vbox.requestFocus() }
                    printBoundsSet(this)
                    smartResize()
                }
            }
        }
    }

    private fun print() {
        if (printerJob?.printPage(dataView) == true) printerJob!!.endJob()
    }

    private fun printConfig() {
        if (printerJob != null) printerJob!!.showPageSetupDialog(modalStage)
    }

    private fun printBoundsSet(dataView: TableView<*>) {
        val layout = printerJob?.jobSettings?.pageLayout
        if (layout != null) {
            dataView.prefWidth = layout.printableWidth
            dataView.prefHeight = layout.printableHeight
        }
    }

    init {
        printerJob?.jobSettings?.pageLayoutProperty()?.addListener { _, old, new ->
            if (new != old) printBoundsSet(dataView)
        }
        runLater { printBoundsSet(dataView) }
    }
}
