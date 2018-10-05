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
            menuitem("◪✔ Create Section") { fire(CommandCreateSection) }
            menuitem("◪✘ Delete Section") { fire(CommandDeleteSection) }
        }
        menu {
            text = "▣ Data view" //▦
            menuitem("◨✔ Create Field") { fire(CommandCreateField) } // TODO: remove ⊞↴
            menuitem("◨✘ Delete Field") { fire(CommandDeleteField) } // TODO: remove ⊟↴
            separator()
            menuitem("⬓✔ Create Record") { fire(CommandCreateRecord) } // TODO: remove ⊞↳
            menuitem("⬓✘ Delete Record") { fire(CommandDeleteRecord) } // TODO: remove ⊟↳
        }
    }
}
