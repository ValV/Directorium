# Directorium
*Directorium* is a *Kotlin/TornadoFX* application.

![Directorium GUI](resources/directorium-gui.png)

It was created as an academic project which has the following sections.

## Specification

1. Name: Directorium.
2. Purpose: store user data.
3. Functionality: create, modify, store arbitrary user data schema; input, display, and print user data.
4. Structure and GUI: main menu, category/section area, data view area, tool bar, category/section create/delete dialog, table column create/delete dialog, print dialog.

## Development

1. Language: _**Kotlin**_.
2. Framework: _**TornadoFX**_.
3. Platform: _**JavaFX**_.
4. IDE: _**IntelliJ IDEA**_.
5. Build system: _**Maven**_.
6. Libraries: _jdk8-openjdk_, _jre8-openjdk_, _java-openjfx_, _tornadofx_, _kotlin-stdlib_, _kotlin-test_, _kotlin-reflect_, _kotlinx-serialization-runtime_.
7. Host system: _**Archlinux**_.

## Program structure

Modules are represented by packages. Diagram below shows project-specific packages.

![Directorium packages](resources/directorium-packages.png)

Packages `directorium` and `components` are separated with build system into `directorii-nucleus` and `directorii-pars` artifacts respectively.

Contents of `directorii-nucleus`:


```
com
└── github
    └── valv
        └── directorium
            ├── app
            │   ├── Directorium.kt
            │   └── Styles.kt
            ├── control
            │   ├── Data.kt
            │   └── Events.kt
            └── view
                ├── FragmentCategoryControl.kt
                ├── FragmentCategoryTree.kt
                ├── FragmentDataViewControl.kt
                ├── FragmentDataViewPrint.kt
                ├── FragmentMainMenu.kt
                ├── FragmentToolPanel.kt
                └── ViewMain.kt
```

Contents of `directorii-pars`:

```
com
└── github
    └── valv
        └── components
            └── controls
                └── GenericCell.kt
```

Each file inside a package represents a separate class. Some classes have nested classes (marked as composition on the diagram).

![Directorium class diagram](resources/directorium-classes.png)

Class purpose:

* _**Directorium**_ is an *App* subclass, application instance, uses *ViewMain* and *Styles*;
* _**Styles**_ is a *Stylesheet* subclass that us used by *Directorium*, provides visual effects to GUI components;
* _**GenericCell**_ is a *TableCell* subclass with extension functions that is used by *Data* class to build custom cell factories and text formatters for *TableView* in *ViewMain* class;
* _**Data**_ is a *Controller* subclass that contains main application logic, it has *Field* nested class for table view columns' metadata, provides serialization and file i/o for the application, depends on *Events* and *GenericCell*;
* _**Events**_ is a custom class (*Any* subclass by default) that contains nested classes to represent events to be sent to *MessageBus*, each of them is a *FXEvent* subclass;
* _**ViewMain**_ is a *View* subclass and is the main window class that comprises another parts of GUI as *Fragment* classes (*FragmentCategoryControl* and *FragmentDataViewControl* are created dynamically with parameters, the rest fragments are created statically), depends on *Data* and *Events*, fires events to *MessageBus*;
* _**FragmentMainMenu**_ is a *Fragment* subclass that contains main menu and its submenus, depends on *Events*, fires events to *MessageBus*;
* _**FragmentCategoryTree**_ is a *Fragment* subclass that contains pre-configured *TreeView* instance with custom cell formatter and listeners, depends on *Events*, fires events to *MessageBus*;
* _**FragmentToolPanel**_ is a *Fragment* subclass that contains tool bars with buttons analogous to main menu items for quick access, depends on *Events*, fires events to *MessageBus*;
* _**FragmentDataViewPrint**_ is a *Fragment* subclass that contains print preview dialog with another *TableView* and printer controls, depends on *Data*;
* _**FragmentCategoryControl**_ is a *Fragment* subclass that contains category/section creation/deletion dialog, depends on *Events*, fires events to *MessageBus*, takes parameters on creation;
* _**FragmentDataViewControl**_ is a *Fragment* subclass that contains field creation/deletion dialog with nested *ColumnTypes* enum class (for fixed set of available column types), depends on *Events*, fires events to *MessageBus*, takes parameters on creation.

> Classes that depend on *Events* class have *subscribers* (listeners) for *MessageBus*. All classes depend on *tornadofx* package.

> All fragments are able to run separately from the main view (that ability was used during development).

## Implementation

Interaction between components of the applicaion is shown on the sequence diagram.

![Directorium object interaction](resources/directorium-sequence.png)

Implementation details for every class comprise function description. All classes inside `view` package have functional type safe builders, provided by *TornadoFX* framework. Type safe GUI builders are nested.

### ViewMain

Main view component has functional builders for `root` node and subscribers in its `init` block. Main view subscribes for events that require explicit interaction with application (something that has not been implemented via *JavaFX* reactive mechanisms). Those events are:

* *CommandQuit* - request application termination;
* *CommandPrint* - create and open print dialog;
* *CommandCreateSection* - create and open category/section dialog with create option;
* *CommandDeleteSection* - create and open category/section dialog with delete option;
* *CommandCreateField* - create and open field dialog with create option;
* *CommandDeleteField* - create and open filed dialog with delete option.

### Data

Controller for main application logic. It does not contain any builders, but implements functions and sets subscribers for events that do not require explicit GUI interaction. Those are:

* *CommandTreeLoadSection* - request serialize and save current data view, then load and deserialize new data view, triggered on tree view item selected, calls *saveData* and *loadData* functions;
* *CommandTreeCreateSection* - request creation of specific category/section association, calls *createSection* function;
* *CommandTreeDeleteSection* - request deletion of specific category/section association, calls *deleteSection* function;
* *CommandTableCreateField* - request creation of specific column inside main table view, calls *createField* function;
* *CommandTableDeleteField* - request deletion of specific column inside main table view, calls *deleteField* function;
* *CommandCreateRecord* - request creation of a row inside main table view, calls *createRecord* function;
* *CommandDeleteRecord* - request deletion of a row inside main table view, calls *deleteRecord* function.

All data manipulation inside the data view is provided by these functions:

* *saveIndex* - serializes and writes to disk tree view (category/section) data;
* *loadIndex* - loads from disk and deserializes tree view (category/section) data;
* *saveData* - serializes and writes to disk main table view (data view) contents;
* *loadData* - loads from disk and deserializes main table view (data view) contents;
* *createSection* - creates a new category/section association the tree view (or adds a new section into a category);
* *deleteSection* - removes a section from a category, or a category, if it has no sections;
* *createField* - creates a new field in data view (a column in the main table view) with specified name and data type;
* *deleteField* - removes specified field from data view (removes a column by name from the main table view, thus column names are unique);
* *createRecord* - creates a new record in data view (a row inside the main table view) below selected, or at the end if none selected;
* *deleteRecord* - removes selected record from data view (selected row in tha main table view), of the last if none selected.

### FragmentCategoryControl

Category control fragment provides a dialog window to specify category/section parameters. It comprises combo boxes and buttons inside *TornadoFX* form. All functionality is done inside `root` builders. Events are triggered `onAction`:

* *CommandTreeCreateSection*;
* *CommandTreeDeleteSection*.

### FragmentDataViewControl

Data view control fragment provides a dialog window to specify data view field parameters. It comprises conditionally created combo boxes and buttons inside a form (like in category control fragment). Functions that are separated from builders:

* *create* - checks column name uniqueness, sets default value for selected type, and fires *CommandTreeCreateField* event;
* *delete* - just fires *CommandTreeDeleteField* with selected field name as a parameter.

### FragmentDataViewPrint

Data view print fragment provides a dialog window with another one table view, which is resized an aligned to fit default printer's page layout. The dialog provides printer configuration controls as well: print button, printer selection combo, and printer configuration button.

> In order to be able to configure a printer, only one instance of *PrinterJob* is created per fragment.
 
Printing-specific functions added to the fragment:

* *print* - calls *printPage* for the current printer job instance;
* *printConfig* - calls *showPageSetupDialog* for the current printer job;
* *printBoundsSet* - changes printing table view size according to the new page layout for the current printer job, invoked by *pageLayoutProperty* listener.

Item selection in printer combo just changes current printer job's *printer* parameter.

### FragmentCategoryTree

Tree view for category/section is separated to minimize program code inside main view. It has customized *populate* function, on *selectedItemProperty* changes fires *CommandTreeLoadSection*.

### FragmentMainMenu

Comprises menu bar with menus, fires events:

* *CommandPrint*;
* *CommandQuit*;
* *CommandCreateSection*;
* *CommandDeleteSection*;
* *CommandCreateField*;
* *CommandDeleteField*;
* *CommandCreateRecord*;
* *CommandDeleteRecord*.

### FragmentToolPanel

Comprises vertical box with status label and horizontal box. Horizontal box contains two tool bars with buttons that fire events:

* *CommandQuit*;
* *CommandCreateSection*;
* *CommandDeleteSection*;
* *CommandCreateField*;
* *CommandDeleteField*;
* *CommandCreateRecord*;
* *CommandDeleteRecord*.

Subscribes for *CommandStatusDisplay* to set status label's text to various messages.

### Events

Events class has only event definitions. All events are objects from *FXEvent* (and its subclasses), their purpose is to transmit data inside properties (declared as classes), or transmit a message by their instance (declared as objects).

### Styles

Type safe builders for *JavaFX CSS*, provided by *TornadoFX*. This class is used for shaping controls.

## Users guide

### Prerequisites

Java 8 (JRE for running, JDK for building) with JavaFX platform is required to be installed in the system. Maven 3 is required for building.

### Building

Get the project:
```
$ git clone https://github.com/ValV/Directorium.git
```
Change directory:
```
$ cd Directorium
```
Call Maven to build:
```
$ mvn package
```
Target jar-archive is located in `directorii-nucleus/target/directorii-nucleus-VERSION-jar-with-dependencies.jar`. This is self-contained archive that is ready to run.

Copy target archive to any directory and call Maven to clean (if necessary):
```
$ mvn clean
```

### Running

Assuming that you have target archive `directorii-nucleus-VERSION-jar-with-dependencies.jar`, you can rename (if necessary, e.g. to `directorium.jar`) and run it:
```
$ mv directorii-nucleus-VERSION-jar-with-dependencies.jar directorium.jar
$ java -jar directorium.jar
```

### Working with Directorium

Working with *Directorium* implies creating custom data scheme. It is done by creating custom data view with arbitrary number of fields with unique name and one of the supported data types within a section.

> Data view is represented by the table view GUI component.

#### Create a section in a category

*Directorium* was developed to be simple, thus only two-level hierarchy (category/section) is supported. Any section must exist in a category, e.g. `Books/Sci-fi`. You can use main menu or tool bar buttons to create a section within a category.

![Directorium section create](resources/directorium-section-create.png)

In section create dialog one can specify names for both category and a section.

![Directorium section create dialog](resources/directorium-section-create-dialog.png)

#### Add a section into a category

Adding sections is done in the same way as it is created, but choosing existing category.

#### Remove a section from a category

You can delete a section from a category via tool bar or main menu.

![Directorium section delete](resources/directorium-section-delete.png)

Removal dialog will appear as in case of creation.

![Directorium section delete dialog](resources/directorium-section-delete-dialog.png)

> When the last section is removed from a category, the category remains empty. This is done for convenience (not to create the same category again), but is you need to delete it as well, call the dialog again and remove the category with empty section.

![Directorium section delete empty](resources/directorium-section-delete-dialog-empty.png)

#### Add a column into the table view

Column creation also has menu and tool bar controls.

![Directorium field creation](resources/directorium-field-create.png)

Column creation dialog is used to create column, where you can specify column name and it's data type.

![Directorium field create dialog](resources/directorium-field-create-dialog.png)

> Column name must be unique. Each column represents data field.

#### Remove a column from the table view

![Directorium field delete](resources/directorium-field-delete.png)

Column removal is done via the dialog as well. You must specify column name to delete.

![Directorium field delete dialog](resources/directorium-field-delete-dialog.png)

#### Add a row into the table view

Select menu item _Create Record_ or use a button with the same pictogram on the tool bar.

![Directorium record create](resources/directorium-record-create.png)

> Every new row is created next to selected one, or at the end of the table, if none is selected. Each row represents data record.

#### Remove a row from the table view

Row removal is done in the same way as its creation, but with _Delete Record_ menu item (or corresponding button on the tool bar).

![Directorium record create](resources/directorium-record-delete.png)

#### Print data view

Select _Print_ menu item in the main menu, the print view will be opened. Initial size of the table view inside the print view will be adjusted to *A4 portrait* layout with maximum printable area for the default printer.

![Directorium print](resources/directorium-print.png)

Press button with the printer pictogram on the left to send the view to the selected printer.

![Directorium print view](resources/directorium-print-view.png)

> You can select another printer device in the dropdown list in the middle (initialy it is system default printer).

> Button between printer selection list and close button allow you to configure selected printer.

Printing into PDF example [can be found in resources](resources/directorium-print.pdf).

#### Saving data

Data is saved automatically. Category/section data is saved on category/section creation/deletion or upon program exit. Data view is saved on section change or upon exit.

> If *Directorium* is terminated abnormally, some data may not be saved.

## TODO

*Directorium* is an academic project, and on the current milestone it meets all requirements. But further improvements may be done:

- [ ] Add versioning into Maven.

- [ ] Add KDoc to the source.

- [ ] Add multi-page print support.

- [ ] Add data export / import.

- [ ] Add more field data types.

- [ ] Add more column factories.

- [x] Add / specify *TODO*s.

- [ ] Add i18n.
