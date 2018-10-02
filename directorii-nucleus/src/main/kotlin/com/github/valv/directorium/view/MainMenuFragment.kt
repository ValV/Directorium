package com.github.valv.directorium.view

import com.github.valv.directorium.control.Events.*
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
            menuitem("⊕◩ Create Section") { fire(CommandAddSection) }
            menuitem("⊖◩ Delete Section") { fire(CommandDeleteSection) }
        }
        menu {
            text = "▦ Data view"
            menuitem("⊞↴ Create Field") { fire(CommandAddColumn) }
            menuitem("⊟↴ Delete Field") { fire(CommandDeleteColumn) }
            separator()
            menuitem("⊞↳ Create Record") { fire(CommandAddColumn) }
            menuitem("⊟↳ Delete Record") { fire(CommandDeleteColumn) }
        }
    }
}