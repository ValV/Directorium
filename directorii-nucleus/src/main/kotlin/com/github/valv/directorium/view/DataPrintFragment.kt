package com.github.valv.directorium.view

import com.github.valv.directorium.control.Data
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import javafx.print.PrinterJob
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import tornadofx.*

class DataPrintFragment : Fragment("Print preview") {
    val dataState: Data by inject()
    var dataView: TableView<*> by singleAssign()
    val source: TableView<ObservableList<ObservableValue<Any>>>? by param()
    override val root = scrollpane {
        prefWidth = 640.0
        prefHeight = 360.0
        isCenterShape = true
        dataView = tableview<ObservableList<ObservableValue<Any>>> {
            addClass("a4")
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
                            (this as TableColumn<ObservableList<*>, Boolean?>)
                                    .useCheckbox()
                            contentWidth(padding = 2.0)
                        }
                        else -> contentWidth(padding = 2.0)
                    }
                }
            }
            if (source != null) items.addAll(source!!.items)
            smartResize()
        }
    }

    override fun onUndock() {
        println("Undocking Print Window...") // FIXME: remove (debug)
        println("Print is beginning") // FIXME: remove (debug)
        val printerJob = PrinterJob.createPrinterJob()
        println(printerJob) // FIXME: remove (debug)
        if (printerJob != null) {
            val ok = printerJob.showPageSetupDialog(modalStage)
            println("Print setup ok=$ok") // FIXME: remove (debug)
            if (ok) {
                val done = printerJob.printPage(dataView)
                println("Print job done=$done") // FIXME: remove (debug)
                if (done) {
                    printerJob.endJob()
                }
            }
        }
        super.onUndock()
    }
}
