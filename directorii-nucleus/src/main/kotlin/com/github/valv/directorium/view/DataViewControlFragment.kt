package com.github.valv.directorium.view

import com.github.valv.directorium.app.Styles
import com.github.valv.directorium.control.Events.*
import javafx.geometry.Orientation.*
import javafx.scene.control.ComboBox
import javafx.scene.control.TableColumn
import javafx.scene.control.TextField
import javafx.scene.layout.Priority.*
import tornadofx.*
import java.time.Instant
import java.util.*

class DataViewControlFragment : Fragment("Fields") {
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
        fieldset(labelPosition = HORIZONTAL) {
            field(if (creation == false) "Column Name" else "Column Type") {
                fieldCombo = if (creation == false) combobox<String> {
                    items = columnNames?.map { it.text }?.observable()
                            ?: listOf<String>().observable()
                    value = items?.getOrNull(0)
                } else combobox<ColumnTypes> {
                    items = ColumnTypes.values().asList().observable()
                    value = items?.getOrNull(0)
                }
            }
            if (creation != false) field("Column Name") {
                fieldText = textfield {
                    isEditable = true
                    text = ""
                    action { create() }
                }
            }
            field {
                if (creation != false) button("Create") {
                    action { create() }
                }
                else button("Delete") {
                    action { delete() }
                }
                region { hgrow = ALWAYS; vgrow = ALWAYS }
                button("Cancel") {
                    action { close() }
                }
            }
        }
    }

    private fun create() {
        if (fieldCombo.value != null && fieldText.text.isNotBlank() &&
                columnNames?.asSequence()?.map { it.text }?.contains(fieldText.text) == false) {
            fire(CommandTableCreateField(fieldText.text,
                    when (fieldCombo.value) {
                        ColumnTypes.INTEGER -> 0
                        ColumnTypes.DOUBLE -> 0.0
                        ColumnTypes.BOOLEAN -> false
                        ColumnTypes.DATE -> Date.from(Instant.now())
                        ColumnTypes.LIST -> listOf<String>()
                        else -> ""
                    }
            ))
            close()
        } else {
            fire(CommandStatusDisplay("Can't create unique column name"))
        }
    }

    private fun delete() {
        fire(CommandTableDeleteField(
                columnNames?.getOrNull(fieldCombo.selectionModel.selectedIndex)
        ))
        close()
    }
}
