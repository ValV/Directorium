package com.github.valv.directorium.control

import tornadofx.*

class Events {
    // General GUI Events
    class CommandResize(val number: Double): FXEvent()
    class CommandStatusDisplay(val status: String): FXEvent()

    object CommandPrint: FXEvent()
    object CommandQuit: FXEvent()
    object CommandDebug: FXEvent() // TODO: remove after GUI is stable

    // TreeView events
    class CommandTreePopulate<S, T>(val categories: Map<S, List<T>>): FXEvent()
    class CommandLoadSection<T>(val section: T): FXEvent()

    object CommandAddSection: FXEvent()
    object CommandDeleteSection: FXEvent()

    // TableView events
    class CommandTableAddField<T>(val name: String, var item: T): FXEvent()

    object CommandAddColumn: FXEvent()
    object CommandDeleteColumn: FXEvent()
}
