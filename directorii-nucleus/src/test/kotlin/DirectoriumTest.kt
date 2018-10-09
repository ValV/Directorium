import com.github.valv.directorium.app.Styles
import com.github.valv.directorium.control.Data
import com.github.valv.directorium.control.Events.*
import com.github.valv.directorium.view.MainView
import javafx.stage.Stage
import tornadofx.*

class DirectoriumTest : App(MainView::class, Styles::class) {
    private val dataState: Data by inject()

    override fun start(stage: Stage) {
        super.start(stage)
        runLater {
            dataState.categories.putAll(mutableMapOf(
                    "Books" to mutableListOf("Sci-fi").observable(),
                    "Music" to mutableListOf("Ambient", "Industrial").observable()
            ).observable())

            // Populate TreeView with predefined data
            fire(CommandTreePopulate(dataState.categories))

            // Add categories and sections separately
            fire(CommandTreeCreateSection("Books", "Academic"))
            fire(CommandTreeCreateSection("Films", "Sci-fi"))

            // Add columns
            fire(CommandTableCreateField("String", "EMPTY"))
            fire(CommandTableCreateField("Integer", 0))
            fire(CommandTableCreateField("Double", 0.0))
            fire(CommandTableCreateField("Boolean", true))

            // Add rows
            fire(CommandCreateRecord)
        }
    }
}
