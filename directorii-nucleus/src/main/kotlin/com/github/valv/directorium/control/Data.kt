package com.github.valv.directorium.control

import com.github.valv.components.useDateField
import com.github.valv.components.useDoubleField
import com.github.valv.components.useIntegerField
import com.github.valv.directorium.control.Events.*
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import javafx.scene.control.TableColumn
import tornadofx.*
import java.util.*

class Data : Controller() {
    private val fieldValues = mutableListOf<Any>()
    val records = mutableListOf<ObservableList<ObservableValue<Any>>>().observable()
    val categories = mutableMapOf<String, ObservableList<String>>().observable()

    private fun createSection(category: String, section: String) {
        val v = categories.contains(category)
        categories.getOrPut(category) { mutableListOf<String>().observable() }.add(section)
        if (!v) fire(Events.CommandTreePopulate(categories))
    }

    private fun deleteSection(category: String, section: String) {
        if (categories[category]?.size == 0) {
            categories.remove(category)
            fire(Events.CommandTreePopulate(categories))
        } else {
            categories[category]?.remove(section)
        }
    }

    private fun <T : Any> createField(name: String, item: T) {
        fire(CommandTableUpdate {
            this.items.forEach { it.add(SimpleObjectProperty(item)) }
            column(name, SimpleObjectProperty::class) {
                setCellValueFactory {
                    records[items.indexOf(it.value)][tableView.columns.indexOf(this)]
                            as ObservableValue<SimpleObjectProperty<*>>
                }
                when (item) {
                    is String -> (this as TableColumn<ObservableList<*>, String?>)
                            .useTextField()
                    is Int -> (this as TableColumn<ObservableList<*>, Int?>)
                            .useIntegerField()
                    is Double -> (this as TableColumn<ObservableList<*>, Double?>)
                            .useDoubleField()
                    is Boolean -> (this as TableColumn<ObservableList<*>, Boolean?>)
                            .useCheckbox()
                    is Date -> (this as TableColumn<ObservableList<*>, Date?>)
                            .useDateField()
                    is List<*> -> (this as TableColumn<ObservableList<*>, String?>)
                            .useComboBox(listOf("One", "Two").observable())
                }
                fieldValues.add(item)
            }
        })
    }

    private fun deleteField(column: TableColumn<*, *>?) {
        fire(CommandTableUpdate {
            val number = columns.indexOf(column)
            if (number > -1) {
                columns.removeAt(number)
                fieldValues.removeAt(number)
                println("Debug (deleteField): columns = ${columns.count()}")
                if (columns.count() > 0) records.forEach { it.removeAt(number) }
                else records.clear()
            }
        })
    }

    private fun createRecord() {
        fire(CommandTableUpdate {
            if (columns.count() > 0) {
                val list = mutableListOf<ObservableValue<Any>>().observable()
                val current = selectionModel.selectedIndex
                fieldValues.forEach { list.add(SimpleObjectProperty(it)) }
                records.add(current + 1, list)
            }
        })
    }

    private fun deleteRecord() {
        fire(CommandTableUpdate {
            var current = selectionModel.selectedIndex
            if (current < 0) current = records.lastIndex
            if (current > -1) records.removeAt(current)
        })
    }

    init {
        subscribe<Events.CommandDataViewLoadSection<Map<String, String>>> {
            println("Loaded: ${it.section}") // FIXME: remove (debug)
        }
        subscribe<CommandTreeCreateSection> { createSection(it.category, it.section) }
        subscribe<CommandTreeDeleteSection> { deleteSection(it.category, it.section) }
        subscribe<CommandTableCreateField<Any>> { createField(it.name, it.item) }
        subscribe<CommandTableDeleteField> { deleteField(it.name) }
        subscribe<CommandCreateRecord> { createRecord() }
        subscribe<CommandDeleteRecord> { deleteRecord() }

        OnErrorAction.SKIP
    }
}
