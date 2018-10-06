package com.github.valv.directorium.control

import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import javafx.collections.ObservableMap
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import tornadofx.*

class Events {
    // General GUI Events
    class CommandResize(val number: Double) : FXEvent()

    class CommandStatusDisplay(val status: String) : FXEvent()

    object CommandPrint : FXEvent()
    object CommandQuit : FXEvent()
    object CommandDebug : FXEvent() // TODO: remove after GUI is stable

    // TreeView events
    class CommandTreePopulate<S, T>(val categories: ObservableMap<S, ObservableList<T>>) : FXEvent()

    class CommandTreeCreateSection(val category: String, val section: String) : FXEvent()
    class CommandTreeDeleteSection(val category: String, val section: String) : FXEvent()

    class CommandDataViewLoadSection<T>(val section: T) : FXEvent()

    object CommandCreateSection : FXEvent()
    object CommandDeleteSection : FXEvent()

    // TableView events
    class CommandTableCreateField<T>(val name: String, var item: T) : FXEvent()

    class CommandTableDeleteField(val name: TableColumn<*, *>?) : FXEvent()

    class CommandTableUpdate(
            val update: TableView<ObservableList<ObservableValue<Any>>>.() -> Unit
    ) : FXEvent()

    object CommandCreateField : FXEvent()
    object CommandDeleteField : FXEvent()
    object CommandCreateRecord : FXEvent()
    object CommandDeleteRecord : FXEvent()
}
