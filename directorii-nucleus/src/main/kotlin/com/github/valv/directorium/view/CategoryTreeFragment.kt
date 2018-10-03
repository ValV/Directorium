package com.github.valv.directorium.view

import com.github.valv.directorium.control.Events.*
import javafx.scene.control.TreeItem
import javafx.scene.layout.Priority.*
import tornadofx.*

class CategoryTreeFragment: Fragment() {
    override val root = treeview<Any> {
        root = TreeItem()
        isShowRoot = false
        vgrow = ALWAYS
        cellFormat {
            text = when (it) {
                is String -> it
                else -> kotlin.error("Invalid type value")
            }
        }
        subscribe<CommandResize> { prefWidth = it.number / 4 }
        subscribe<CommandTreePopulate<String, String>> {
            val categories = it.categories
            populate { parent ->
                val v = parent.value
                when {
                    parent == root -> categories.keys
                    v is String -> categories[v]
                    else -> null
                }
            }
        }
        selectionModel.selectedItemProperty().addListener { _, _, item ->
            if (item.isLeaf) fire(CommandLoadSection(mapOf(item.parent.value to item.value)))
        }
    }
}