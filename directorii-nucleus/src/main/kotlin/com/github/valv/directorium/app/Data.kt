package com.github.valv.directorium.app

import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import tornadofx.*

class Data: Controller() {
    data class Category(val category: String)
    data class Section(val section: String, val category: String)

    val fields = mutableListOf<Any>()
    val records = mutableListOf<ObservableList<ObservableValue<Any>>>().observable()
    val categories = listOf(Data.Section("Sci-fi", "Books"),
            Data.Section("Ambient", "Music"),
            Data.Section("Industrial", "Music")).groupBy { Data.Category(it.category) }

    fun makeField() {
    }
}