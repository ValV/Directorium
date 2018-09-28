package com.github.valv.directorium.app

import javafx.beans.InvalidationListener
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.TableColumn
import tornadofx.*

// TODO: remove Data class after switch to Controller
class Data {
    data class Category(val category: String)
    data class Section(val section: String, val category: String)

    class Table {
        var header: ObservableList<TableColumn<MutableList<ItemViewModel<Any>>, Any>> =
                FXCollections.observableArrayList()
        var contents: ObservableList<MutableList<ItemViewModel<Any>>> = FXCollections.observableArrayList()

        init {
            header.addListener(InvalidationListener { println("Columns number changed: $it") })
            contents.addListener(InvalidationListener { println("Rows number changed: $it") })
        }
    }

    companion object {
        val categories = listOf(Data.Section("Sci-fi", "Books"),
                Data.Section("Ambient", "Music"),
                Data.Section("Industrial", "Music")).groupBy { Data.Category(it.category) }
    }
}