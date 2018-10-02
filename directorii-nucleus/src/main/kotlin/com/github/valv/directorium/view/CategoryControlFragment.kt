package com.github.valv.directorium.view

import com.github.valv.directorium.app.Styles
import javafx.geometry.Orientation
import javafx.geometry.Orientation.*
import javafx.scene.layout.Priority.*
import javafx.geometry.Pos.*
import tornadofx.*

class CategoryControlFragment (): Fragment("Categories") {
    private val orientation: Orientation = HORIZONTAL
    val creation: Boolean by param()
    override val root = form {
        fieldset(labelPosition = orientation) {
            field("Category") {
                combobox<String> {
                    if (creation) isEditable = creation
                    items = listOf("Books", "Music").observable()
                    value = items[0]
                }
            }
            field("Section") {
                combobox<String> {
                    if (creation) isEditable = creation
                    items = listOf("Sci-fi", "Other").observable()
                    value = items[0]
                }
            }
            field {
                if (creation) button("Create") {
                    action {  }
                }
                else button ("Delete") {
                    action {  }
                }
                region { hgrow = ALWAYS; vgrow = ALWAYS }
                button("Cancel") {
                    action { this@CategoryControlFragment.close() }
                }
            }
        }
    }

    init {
        importStylesheet(Styles::class)
    }
}