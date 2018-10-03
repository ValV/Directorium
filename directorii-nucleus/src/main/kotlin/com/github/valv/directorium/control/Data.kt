package com.github.valv.directorium.control

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import tornadofx.*

class Data: Controller() {
    val fields = mutableListOf<Any>()
    val records = mutableListOf<ObservableList<ObservableValue<Any>>>().observable()
    val categories = mutableMapOf<String, ObservableList<String>>().observable()

    fun createSection(category: String, section: String) {
        val v = categories.contains(category)
        categories.getOrPut(category) { mutableListOf<String>().observable() }.add(section)
        if (!v) fire(Events.CommandTreePopulate(categories))
    }

    fun deleteSection(category: String, section: String) {
        if (categories[category]?.size == 0) {
            categories.remove(category)
            fire(Events.CommandTreePopulate(categories))
        } else {
            categories[category]?.remove(section)
        }
    }

    fun addRecord() {
        val list = mutableListOf<ObservableValue<Any>>().observable()
        fields.forEach { list.add(SimpleObjectProperty(it)) }
        records.add(list)
    }

    init {
        subscribe<Events.CommandLoadSection<Map<String, String>>> { println("Loaded: ${it.section}") }
        subscribe<Events.CommandTreeCreateSection> { createSection(it.category, it.section) }
        subscribe<Events.CommandTreeDeleteSection> { deleteSection(it.category, it.section) }
    }
}