package com.github.valv.directorium.control

import javafx.collections.ObservableList
import javafx.collections.ObservableMap
import tornadofx.*

class Events {
    // General GUI Events
    class CommandResize(val number: Double): FXEvent()
    class CommandStatusDisplay(val status: String): FXEvent()

    object CommandPrint: FXEvent()
    object CommandQuit: FXEvent()
    object CommandDebug: FXEvent() // TODO: remove after GUI is stable

    // TreeView events
    class CommandTreePopulate<S, T>(val categories: ObservableMap<S, ObservableList<T>>): FXEvent()
    class CommandTreeCreateSection(val category: String, val section: String): FXEvent()
    class CommandTreeDeleteSection(val category: String, val section: String): FXEvent()
    class CommandLoadSection<T>(val section: T): FXEvent()

    object CommandAddSection: FXEvent()
    object CommandDeleteSection: FXEvent()

    // TableView events
    class CommandTableAddField<T>(val name: String, var item: T): FXEvent()

    object CommandAddColumn: FXEvent()
    object CommandDeleteColumn: FXEvent()
}
