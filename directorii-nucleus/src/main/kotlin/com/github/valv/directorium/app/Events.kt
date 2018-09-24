package com.github.valv.directorium.app

import com.github.valv.directorium.app.Data.*
import javafx.collections.ObservableList
import javafx.scene.control.TableView
import tornadofx.*

class Events {
    class CommandResize(val number: Double): FXEvent()
    class CommandTreePopulate(val categories: Map<Category, List<Section>>): FXEvent()
    class CommandTableItemsSet(val table: Table): FXEvent()
    class CommandTableAddField(val addField: TableView<Any>.() -> Unit): FXEvent()
    class CommandLoadSection(val section: Section): FXEvent()
    class CommandStatusDisplay(val status: String): FXEvent()

    object CommandAddColumn: FXEvent()
    object CommandDeleteColumn: FXEvent()
    object CommandAddSection: FXEvent()
    object CommandDeleteSection: FXEvent()
    object CommandPrint: FXEvent()
    object CommandQuit: FXEvent()
    object CommandDebug: FXEvent()
}
