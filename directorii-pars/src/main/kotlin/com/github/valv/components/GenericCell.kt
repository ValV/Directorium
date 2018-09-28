package com.github.valv.components

import javafx.beans.property.ObjectProperty
import javafx.scene.control.TableCell
import javafx.scene.control.TextField

// TODO: remove class after `StringConverter`s are implemented
class GenericCell<T>: TableCell<MutableList<ItemViewModel<T>>, T>() {
    private val editor = TextField()
    lateinit var oldText: String
    lateinit var list: ObjectProperty<*>

    override fun startEdit() {
        super.startEdit()
        //list = tableView.focusModel.focusedCellProperty().toProperty()
        editor.text = item.toString()
        graphic = editor
        //oldText = text
        text = null
    }

    override fun cancelEdit() {
        super.cancelEdit()
        //val newCost = editor.text.toFloat()
        //val newExpense = Expense(list.name, newCost, list.category)
        //observableList[observableList.indexOf(list)] = newExpense
        //list.value = editor.text as Any
        graphic = null
        text = editor.text
    }

    override fun updateItem(item: T?, empty: Boolean) {
        super.updateItem(item, empty)
        when {
            empty -> { text = null; graphic = null }
            isEditing -> { graphic = editor }
            else -> { text = getItem().toString(); graphic = null }
        }
    }
}