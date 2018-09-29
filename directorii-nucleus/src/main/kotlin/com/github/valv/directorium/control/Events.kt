package com.github.valv.directorium.control

import com.github.valv.directorium.control.Data.*
import tornadofx.*

class Events {
    // General GUI Events
    class CommandResize(val number: Double): FXEvent()
    class CommandStatusDisplay(val status: String): FXEvent()

    object CommandPrint: FXEvent()
    object CommandQuit: FXEvent()
    object CommandDebug: FXEvent() // TODO: remove after GUI is stable

    // TreeView events
    class CommandTreePopulate(val categories: Map<Category, List<Section>>): FXEvent()
    class CommandLoadSection(val section: Section): FXEvent()

    object CommandAddSection: FXEvent()
    object CommandDeleteSection: FXEvent()

    // TableView events
    class CommandTableAddField<T>(val name: String, var item: T): FXEvent()

    object CommandAddColumn: FXEvent()
    object CommandDeleteColumn: FXEvent()
}
