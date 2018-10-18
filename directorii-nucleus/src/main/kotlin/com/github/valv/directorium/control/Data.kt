package com.github.valv.directorium.control

import com.github.valv.components.controls.useDateField
import com.github.valv.components.controls.useDoubleField
import com.github.valv.components.controls.useIntegerField
import com.github.valv.directorium.control.Events.*
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import javafx.scene.control.TableColumn
import kotlinx.serialization.*
import kotlinx.serialization.internal.StringSerializer
import kotlinx.serialization.json.JSON
import tornadofx.*
import java.io.File
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.*

class Data : Controller() {
    @Serializer(forClass = Date::class)
    object DateSerializer : KSerializer<Date> {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS")

        override fun save(output: KOutput, obj: Date) {
            output.writeStringValue(dateFormat.format(obj))
        }

        override fun load(input: KInput): Date {
            return dateFormat.parse(input.readStringValue())
        }
    }

    @Serializable
    data class Field(val name: String, val value: Any)

    val fields = mutableListOf<Field>()
    val records = mutableListOf<MutableList<SimpleObjectProperty<Any>>>().observable()
    val categories = mutableMapOf<String, ObservableList<String>>()

    private val dateFormat = DateSerializer.dateFormat
    private val serialFields = Field.serializer().list
    private val serialRecords = StringSerializer.list.list
    private val serialCategories = (StringSerializer to StringSerializer.list).map
    private val serialContext = SerialContext().apply {
        registerSerializer(Date::class, DateSerializer)
    }

    fun saveIndex() {
        val index = categories.mapValues { it.value.toList() }
        val sIndex = JSON.stringify(serialCategories, index)
        try {
            File("data").mkdirs()
            File("data/index.json").writeText(sIndex)
        } catch (e: FileNotFoundException) {
        } catch (e: RuntimeException) {
            println("Index save exception:\n${e.message}\n")
        }
    }

    fun loadIndex() {
        try {
            val sIndex = File("data/index.json").readText()
            val index = JSON.parse(serialCategories, sIndex)
            categories.putAll(index.mapValues { it.value.observable() }.observable())
            fire(CommandTreePopulate(categories))
        } catch (e: FileNotFoundException) {
        } catch (e: RuntimeException) {
            println("Index load exception:\n${e.message}\n")
        }
    }

    fun saveData(path: String = "") {
        if (path.isEmpty()) return
        if (fields.isEmpty()) {
            try {
                File("data/$path").deleteRecursively()
                return
            } catch (e: FileNotFoundException) {
            } catch (e: RuntimeException) {
                println("Data save exception:\n${e.message}\n")
            }
            return
        }
        try {
            File("data/$path").mkdirs()
            val list = records.map { row ->
                row.asSequence().map { cell ->
                    val value = cell.value
                    when (value) {
                        is String -> value
                        is Int -> value.toString()
                        is Double -> value.toString()
                        is Boolean -> value.toString()
                        is Date -> dateFormat.format(value)
                        else -> ""
                    }
                }.toList()
            }
            val sFields = JSON(context = serialContext).stringify(serialFields, fields)
            val sRecords = JSON.stringify(serialRecords, list)
            File("data/$path/fields.json").writeText(sFields)
            File("data/$path/records.json").writeText(sRecords)
        } catch (e: FileNotFoundException) {
        } catch (e: RuntimeException) {
            println("Data save exception:\n${e.message}\n")
        }
    }

    fun loadData(path: String = "") {
        fire(CommandTableUpdate { columns.clear() })
        fields.clear()
        records.clear()
        if (path.isEmpty()) return
        try {
            val sFields = File("data/$path/fields.json").readText()
            (JSON(context = serialContext).parse(serialFields, sFields)).forEach { createField(it.name, it.value) }
        } catch (e: FileNotFoundException) {
            return
        } catch (e: RuntimeException) {
            println("Data load exception:\n${e.message}\n")
            return
        }
        try {
            val sRecords = File("data/$path/records.json").readText()
            val list = JSON.parse(serialRecords, sRecords)
            val data =
                    list.map {
                        it.mapIndexedTo(mutableListOf()) { index, value ->
                            when (fields[index].value) {
                                is Int -> SimpleObjectProperty(value.toInt()) as SimpleObjectProperty<Any>
                                is Double -> SimpleObjectProperty(value.toDouble()) as SimpleObjectProperty<Any>
                                is Boolean -> SimpleObjectProperty(value.toBoolean()) as SimpleObjectProperty<Any>
                                is Date -> SimpleObjectProperty(dateFormat.parse(value)) as SimpleObjectProperty<Any>
                                else -> SimpleObjectProperty(value) as SimpleObjectProperty<Any>
                            }
                        }
                    }.observable()
            records.addAll(data)
            fire(CommandTableResize)
        } catch (e: FileNotFoundException) {
        } catch (e: RuntimeException) {
            println("Data load exception:\n${e.message}\n")
            records.clear()
        }
    }

    private fun createSection(category: String, section: String) {
        val categoryExists = categories.contains(category)
        categories.getOrPut(category) { mutableListOf<String>().observable() }.add(section)
        if (!categoryExists) fire(Events.CommandTreePopulate(categories))
        saveIndex()
    }

    private fun deleteSection(category: String, section: String) {
        if (categories[category]?.size == 0) {
            categories.remove(category)
            fire(Events.CommandTreePopulate(categories))
            try {
                File("data/$category").deleteRecursively()
            } catch (e: FileNotFoundException) {
            } catch (e: RuntimeException) {
                println("Category delete exception:\n${e.message}\n")
            }
            saveIndex()
        } else {
            categories[category]?.remove(section)
            try {
                File("data/$category/$section").deleteRecursively()
            } catch (e: FileNotFoundException) {
            } catch (e: RuntimeException) {
                println("Category delete exception:\n${e.message}\n")
            }
            saveIndex()
        }
    }

    private fun <T : Any> createField(name: String, item: T) {
        fire(CommandTableUpdate {
            items.forEach { it.add(SimpleObjectProperty(item)) }
            column(name, SimpleObjectProperty::class) {
                value { records[items.indexOf(it.value)][tableView.columns.indexOf(this)] }
                when (item) {
                    is String -> {
                        (this as TableColumn<*, String?>).useTextField { requestResize() }
                        remainingWidth()
                    }
                    is Int -> {
                        (this as TableColumn<*, Int?>).useIntegerField { requestResize() }
                        contentWidth(padding = 2.0)
                    }
                    is Double -> {
                        (this as TableColumn<*, Double?>).useDoubleField { requestResize() }
                        contentWidth(padding = 2.0)
                    }
                    is Boolean -> {
                        (this as TableColumn<*, Boolean?>).useCheckbox()
                        contentWidth(padding = 2.0)
                    }
                    is Date -> {
                        (this as TableColumn<*, Date?>).useDateField()
                        contentWidth(padding = 2.0)
                    }
                }
                fields.add(Field(name, item))
            }
        })
    }

    private fun deleteField(column: TableColumn<*, *>?) {
        fire(CommandTableUpdate {
            val number = columns.indexOf(column)
            if (number > -1) {
                columns.removeAt(number)
                fields.removeAt(number)
                if (columns.count() > 0) records.forEach { it.removeAt(number) }
                else records.clear()
            }
        })
    }

    private fun createRecord() {
        fire(CommandTableUpdate {
            if (columns.count() > 0) {
                val list = mutableListOf<SimpleObjectProperty<Any>>()
                val current = selectionModel.selectedIndex
                fields.forEach { list.add(SimpleObjectProperty(it.value)) }
                records.add(current + 1, list)
            }
        })
    }

    private fun deleteRecord() {
        fire(CommandTableUpdate {
            var current = selectionModel.selectedIndex
            if (current < 0) current = records.lastIndex
            if (current > -1) records.removeAt(current)
        })
    }

    init {
        subscribe<Events.CommandTreeLoadSection> {
            saveData(it.save)
            loadData(it.load)
        }
        subscribe<CommandTreeCreateSection> { createSection(it.category, it.section) }
        subscribe<CommandTreeDeleteSection> { deleteSection(it.category, it.section) }
        subscribe<CommandTableCreateField<Any>> { createField(it.name, it.item) }
        subscribe<CommandTableDeleteField> { deleteField(it.name) }
        subscribe<CommandCreateRecord> { createRecord(); fire(CommandTableResize) }
        subscribe<CommandDeleteRecord> { deleteRecord(); fire(CommandTableResize) }
    }
}
