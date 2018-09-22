package com.github.valv.components

import javafx.beans.property.ObjectProperty
import javafx.collections.ObservableList
import javafx.scene.control.TableCell
import javafx.scene.control.TextField

class StringCell: TableCell<ObservableList<ObjectProperty<*>>, Any>() {
    private val editor = TextField()
    lateinit var oldText: String
    lateinit var list: ObjectProperty<*>

    override fun startEdit() {
        super.startEdit()
        //list = tableView.focusModel.focusedCellProperty().toProperty()
        editor.text = text
        graphic = editor
        oldText = text
        text = null
    }

    override fun cancelEdit() {
        super.cancelEdit()
        //val newCost = editor.text.toFloat()
        //val newExpense = Expense(list.name, newCost, list.category)
        //observableList[observableList.indexOf(list)] = newExpense
        //list.value = editor.text as Any
        graphic = null
        text = oldText
    }

    override fun updateItem(item: Any?, empty: Boolean) {
        super.updateItem(item, empty)
        when {
            empty -> { text = null; graphic = null }
            isEditing -> { graphic = editor }
            else -> { text = getItem().toString(); graphic = null }
        }
    }
}