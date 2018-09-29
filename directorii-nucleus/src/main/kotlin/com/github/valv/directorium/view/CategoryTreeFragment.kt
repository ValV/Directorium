package com.github.valv.directorium.view

import com.github.valv.directorium.control.Data.*
import com.github.valv.directorium.control.Events.*
import javafx.scene.control.TreeItem
import tornadofx.*

class CategoryTreeFragment: Fragment() {
    override val root = treeview<Any> {
        /* Items */
        root = TreeItem()
        isShowRoot = false
        cellFormat {
            text = when (it) {
                is String -> it
                is Category -> it.category
                is Section -> it.section
                else -> kotlin.error("Invalid type value")
            }
        }
        /* Events */
        subscribe<CommandResize> { prefWidth = it.number / 4 }
        subscribe<CommandTreePopulate<Category, Section>> {
            val categories = it.categories
            populate { parent ->
                val v = parent.value
                when {
                    parent == root -> categories.keys
                    v is Category -> categories[v]
                    else -> null
                }
            }
        }
        selectionModel.selectedItemProperty().addListener {
            _, _, item -> if (item.value is Section) fire(CommandLoadSection(item.value as Section))
        }
    }
}