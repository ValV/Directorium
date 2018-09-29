package com.github.valv.directorium.control

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import tornadofx.*

class Data: Controller() {
    data class Category(val category: String)
    data class Section(val section: String, val category: String)

    val fields = mutableListOf<Any>()
    val records = mutableListOf<ObservableList<ObservableValue<Any>>>().observable()
    val categories = listOf(Section("Sci-fi", "Books"),
            Section("Ambient", "Music"),
            Section("Industrial", "Music")).groupBy { Category(it.category) }

    fun addRecord() {
        val list = mutableListOf<ObservableValue<Any>>().observable()
        fields.forEach { list.add(SimpleObjectProperty(it)) }
        records.add(list)
    }
}