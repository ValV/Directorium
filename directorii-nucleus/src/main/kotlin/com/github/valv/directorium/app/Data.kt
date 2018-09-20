package com.github.valv.directorium.app

import javafx.beans.property.ObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.TableColumn

class Data {
    data class Category(val category: String)
    data class Section(val section: String, val category: String)

    class Table {
        var header: ObservableList<TableColumn<ObservableList<ObjectProperty<*>>, *>> =
                FXCollections.observableArrayList()
        var contents: ObservableList<ObservableList<ObjectProperty<*>>> = FXCollections.observableArrayList()
    }

    companion object {
        val categories = listOf(Data.Section("Sci-fi", "Books"),
                Data.Section("Ambient", "Music"),
                Data.Section("Darkwave", "Music")).groupBy { Data.Category(it.category) }
    }
}