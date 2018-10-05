package com.github.valv.directorium.view

import com.github.valv.directorium.app.Styles
import com.github.valv.directorium.control.Events.*
import javafx.geometry.Orientation
import javafx.geometry.Orientation.*
import javafx.scene.control.ComboBox
import javafx.scene.control.TableColumn
import javafx.scene.control.TextField
import javafx.scene.layout.Priority.*
import tornadofx.*
import java.time.Instant
import java.util.*

class DataViewControlFragment : Fragment("Fields") {
    private val orientation: Orientation = HORIZONTAL

    private enum class ColumnTypes {
        STRING, INTEGER, DOUBLE, BOOLEAN, DATE, LIST;

        override fun toString(): String {
            return this.name.toLowerCase().capitalize()
        }
    }

    val columnNames: List<TableColumn<*, *>>? by param()
    val creation: Boolean? by param()
    lateinit var fieldCombo: ComboBox<*>
    lateinit var fieldText: TextField
    override val root = form {
        fieldset(labelPosition = orientation) {
            field(if (creation == false) "Column Name" else "Column Type") {
                fieldCombo = if (creation == false) combobox<String> {
                    items = columnNames?.map { it.text }?.observable() ?: listOf<String>().observable()
                    value = items?.get(0)
                } else combobox<ColumnTypes> {
                    items = ColumnTypes.values().asList().observable()
                    value = items?.get(0)
                }
            }
            if (creation != false) field(if (creation == false) "Column Type" else "Column Name") {
                fieldText = textfield {
                    isEditable = creation != false
                    text = if (creation != false) "" else "" // TODO: selected column type
                }
            }
            field {
                if (creation != false) button("Create") {
                    action {
                        if (fieldCombo.value != null && fieldText.text.isNotBlank() &&
                                columnNames?.map { it.text }?.contains(fieldText.text) == false) {
                            fire(CommandTableCreateField(fieldText.text,
                                    when (fieldCombo.value) {
                                        ColumnTypes.INTEGER -> 0
                                        ColumnTypes.DOUBLE -> 0.0
                                        ColumnTypes.BOOLEAN -> false
                                        ColumnTypes.DATE -> Date.from(Instant.now())
                                        ColumnTypes.LIST -> listOf<String>().observable()
                                        else -> ""
                                    }
                            ))
                            close()
                        } else { // FIXME: status bar
                            fire(CommandStatusDisplay("Can't create unique column name"))
                        }
                    }
                }
                else button ("Delete") {
                    action {
                        fire(CommandTableDeleteField(columnNames?.get(fieldCombo.selectionModel.selectedIndex)))
                        close()
                    }
                }
                region { hgrow = ALWAYS; vgrow = ALWAYS }
                button("Cancel") {
                    action { close() }
                }
            }
        }
    }

    init {
        importStylesheet(Styles::class)
    }
}
