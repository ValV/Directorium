package com.github.valv.components.controls

import java.lang.NumberFormatException
import java.lang.RuntimeException
import javafx.beans.property.Property
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.scene.control.cell.TextFieldTableCell
import javafx.scene.input.KeyCode
import javafx.util.converter.*
import javafx.util.Callback
import javafx.util.StringConverter
import tornadofx.*

class GenericCell<S, T>(converter: StringConverter<T>?, formatter: TextFormatter<T>?) :
        TableCell<S, T?>() {
    private val converterProperty = SimpleObjectProperty<StringConverter<T>>(converter)
    private var converter by converterProperty
    private var editor = TextField(getItemText()).apply {
        textFormatter = formatter
        setOnAction { event ->
            try {
                commitEdit(this@GenericCell.converter.fromString(text))
            } catch (e: RuntimeException) {
                println(e.message) // FIXME: fromString conversion stub
                commitEdit(this@GenericCell.item)
            }
            event.consume()
        }
        setOnKeyReleased { t ->
            if (t.code == KeyCode.ESCAPE) {
                cancelEdit()
                t.consume()
            }
        }
    }

    private fun getItemText(): String {
        return if (this.converter == null) {
            if (this.item == null) "" else this.item.toString()
        } else this.converter.toString(this.item)
    }

    override fun startEdit() {
        if (this.isEditable && this.tableView.isEditable && this.tableColumn.isEditable) {
            super.startEdit()
            if (this.isEditing) {
                editor.text = getItemText()

                text = null
                graphic = this.editor

                editor.selectAll()
                editor.requestFocus()
            }

        }
    }

    override fun cancelEdit() {
        super.cancelEdit()
        text = getItemText()
        graphic = null
    }

    override fun updateItem(item: T?, empty: Boolean) {
        super.updateItem(item, empty)
        when {
            isEmpty -> {
                text = null
                graphic = null
            }
            isEditing -> {
                editor.text = getItemText()
                text = null
                graphic = editor
            }
            else -> {
                text = getItemText()
                graphic = null
            }
        }
    }
}

@Suppress("UNUSED", "UNCHECKED_CAST")
inline fun <S, reified T> forTableColumn(converter: StringConverter<T>?) =
        Callback<TableColumn<S, T?>, TableCell<S, T?>> { GenericCell(converter, setFormatter()) }

@Suppress("UNUSED", "UNCHECKED_CAST")
inline fun <reified T> setFormatter() = when (T::class) {
    Int::class -> TextFormatter<T> { s ->
        if (s.controlNewText.isEmpty()) s else try {
            s.controlNewText.toInt(); s
        } catch (e: NumberFormatException) {
            null
        }
    }
    Double::class -> TextFormatter<T> { s ->
        if (s.controlNewText.isEmpty()) s else try {
            s.controlNewText.toDouble(); s
        } catch (e: NumberFormatException) {
            null
        }
    }
    else -> null
}

@Suppress("UNUSED", "UNCHECKED_CAST")
inline fun <S, reified Int> TableColumn<S, Int?>.useIntegerField(
        noinline afterCommit: (TableColumn.CellEditEvent<S, Int?>) -> Unit = {}
) = useTextField(IntegerStringConverter() as StringConverter<Int>, afterCommit)

@Suppress("UNUSED", "UNCHECKED_CAST")
inline fun <S, reified Double> TableColumn<S, Double?>.useDoubleField(
        noinline afterCommit: (TableColumn.CellEditEvent<S, Double?>) -> Unit = {}
) = useTextField(DoubleStringConverter() as StringConverter<Double>, afterCommit)

@Suppress("UNUSED", "UNCHECKED_CAST")
inline fun <S, reified Date> TableColumn<S, Date?>.useDateField(
        noinline afterCommit: (TableColumn.CellEditEvent<S, Date?>) -> Unit = {}
) = useTextField(DateStringConverter() as StringConverter<Date>, afterCommit)

@Suppress("UNUSED")
inline fun <S, reified T> TableColumn<S, T?>.useTextField(
        converter: StringConverter<T>? = null,
        noinline afterCommit: (TableColumn.CellEditEvent<S, T?>) -> Unit = {}
) = apply {
    when (T::class) {
        String::class -> {
            @Suppress("UNCHECKED_CAST")
            val stringColumn = this as TableColumn<S, String?>
            stringColumn.cellFactory = TextFieldTableCell.forTableColumn()
        }
        else -> {
            requireNotNull(converter) { "You must supply a converter for non String columns" }
            cellFactory = forTableColumn(converter)
        }
    }

    setOnEditCommit {
        val property = it.tableColumn.getCellObservableValue(it.rowValue) as Property<T?>
        property.value = it.newValue
        afterCommit(it)
    }
}
