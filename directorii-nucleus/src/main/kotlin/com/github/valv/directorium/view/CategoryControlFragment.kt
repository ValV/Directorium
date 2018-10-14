package com.github.valv.directorium.view

import com.github.valv.directorium.control.Events.*
import javafx.beans.InvalidationListener
import javafx.collections.ObservableList
import javafx.collections.ObservableMap
import javafx.geometry.Orientation.HORIZONTAL
import javafx.scene.control.ComboBox
import javafx.scene.layout.Priority.ALWAYS
import tornadofx.*

class CategoryControlFragment : Fragment("Sections") {
    private val sections = mutableListOf<String>().observable()

    val categories: ObservableMap<String, ObservableList<String>>? by param()
    val creation: Boolean? by param()

    lateinit var category: ComboBox<String>
    lateinit var section: ComboBox<String>

    override val root = form {
        fieldset(labelPosition = HORIZONTAL) {
            field("Category") {
                category = combobox {
                    items = categories?.keys?.toList()?.observable()
                            ?: listOf<String>().observable()
                    selectionModel.selectedItemProperty().addListener { _, _, v ->
                        sections.setAll(
                                if (categories?.contains(v) == true) categories?.get(v)
                                else listOf()
                        )
                    }
                    sections.setAll(categories?.get(items?.getOrNull(0) ?: "")
                            ?: listOf<String>().observable()
                    )
                    isEditable = creation != false
                    if (creation == false) {
                        value = items?.get(0)
                    }
                }
            }
            field("Section") {
                section = combobox {
                    items = sections
                    isEditable = creation != false
                    if (creation == false) {
                        items.addListener(InvalidationListener {
                            value = if (items == null || items.count() <= 0) ""
                            else items?.get(0) ?: ""
                        })
                        value = if (items == null || items.count() <= 0) ""
                        else items?.get(0) ?: ""
                    }
                }
            }
            field {
                if (creation != false) button("Create") {
                    action {
                        if (category.value == null || category.value.isBlank()) return@action
                        if (section.value == null || section.value.isBlank()) return@action
                        fire(CommandTreeCreateSection(category.value, section.value))
                        close()
                    }
                }
                else button("Delete") {
                    action {
                        fire(CommandTreeDeleteSection(
                                category.value, section.value ?: ""
                        ))
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
}
