# Directorium
*Directorium* is a *Kotlin/TornadoFX* application. It was created as academic project which has the following sections.

## Specification

1. Name: Directorium.
2. Purpose: store user data.
3. Functions: create, modify, store arbitrary user data schema; input, display, and print user data.
4. Structure and GUI: main menu, category/section area, data view area, tool bar, category/section create/delete dialog, table column create/delete dialog, print dialog.

## Development

1. Language: **Kotlin**.
2. Framework: **TornadoFX**.
3. Platform: **JavaFX**.
4. IDE: **IntelliJ IDEA**.
5. Build system: **Maven**.
6. Libraries: jdk8-openjdk, jre8-openjdk, java-openjfx, tornadofx, kotlin-stdlib, kotlin-test, kotlin-reflect, kotlinx-serialization-runtime.
7. Host system: **Archlinux**.

## Program structure

*TODO:* class diagram, module diagram, class purpose.

## Implementation

*TODO:* functions description (sequence diagrams).

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

#### Add a section into a category

Adding sections is done in the same way as it is created, but choosing existing category.

#### Remove a section from a category

You can delete a section from a category via dialog as well.

> When the last section is removed from a category, the category remains empty. This is done for convenience (not to create the same category again), but is you need to delete it as well, call the dialog again and remove the category with empty section.

#### Add a column into the table view

Column creation dialog is used to create column, where you can specify column name and it's data type.

> Column name must be unique. Each column represents data field.

#### Remove a column from the table view

Column removal is done via the dialog as well. You must specify column name to delete.

#### Add a row into the table view

Select menu item _Create Record_ or use a button with the same pictogram on the tool bar.

> Every new row is created next to selected one, or at the end of the table, if none is selected. Each row represents data record.

#### Remove a row from the table view

Row removal is done in the same way as its creation, but with _Delete Record_ menu item (or corresponding button on the tool bar).

#### Print data view

Select _Print_ menu item in the main menu, the print view will be opened. Initial size of the table view inside the print view will be adjusted to *A4 portrait* layout with maximum printable area for the default printer.

Press button with the printer pictogram on the left to send the view to the selected printer.

> You can select another printer device in the dropdown list in the middle (initialy it is system default printer)

> Button between printer selection list and close button allow you to configure selected printer.

#### Saving data

Data is saved automatically. Category/section data is saved on category/section creation/deletion or upon program exit. Data view is saved on section change or upon exit.

> If *Directorium* is terminated abnormally, some data may not be saved.

## TODO

*Directorium* is an academic project, and on the current milestone it meets all requirements. But further improvements may be done:

- [ ] Add versioning into Maven

- [ ] Add KDoc to the source

- [ ] Add multi-page print support

- [ ] Add data export / import

- [ ] Add more field data types

- [ ] Add more column factories

- [ ] Add / specify *TODO*s
