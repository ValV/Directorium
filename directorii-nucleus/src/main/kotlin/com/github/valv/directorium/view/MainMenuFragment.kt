package com.github.valv.directorium.view

import com.github.valv.directorium.app.Events.*
import tornadofx.*

class MainMenuFragment: Fragment() {
    override val root = menubar {
        menu {
            text = "⌨ File"
            menuitem("⎙ Print", "Ctrl+P") { fire(CommandPrint) }
            separator()
            menuitem("⏻ Quit", "Ctrl+Q") { fire(CommandQuit)}
        }
        menu {
            text = "◩ Sections"
            menuitem("Add to Category") { fire(CommandAddSection) }
            menuitem("Delete selected") { fire(CommandDeleteSection) }
        }
        menu {
            text = "▦ Data view"
            menuitem("⊞↴ Add Field") { fire(CommandAddColumn) }
            menuitem("⊟↴ Delete Field") { fire(CommandDeleteColumn) }
            menuitem("⊞↳ Add Record") { fire(CommandAddColumn) }
            menuitem("⊟↳ Delete Record") { fire(CommandDeleteColumn) }
        }
    }
}