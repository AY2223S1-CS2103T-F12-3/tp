---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **2. Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **3. Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## *4. *Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/AY2223S1-CS2103T-F12-3/tp/tree/master/docs/diagrams/) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### 4.1 Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** has two classes called [`Main`](https://github.com/AY2223S1-CS2103T-F12-3/tp/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2223S1-CS2103T-F12-3/tp/tree/master/src/main/java/seedu/address/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.


**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### 4.2 UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2223S1-CS2103T-F12-3/tp/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `TaskListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder, except for `MainWindow`. The layout of the [`MainWindow`](https://github.com/AY2223S1-CS2103T-F12-3/tp/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`NotionUSMainWindow.fxml`](https://github.com/AY2223S1-CS2103T-F12-3/tp/tree/master/src/main/resources/view/NotionUSMainWindow.fxml).
An FXML variable declared in the Java Class must correspond to the fxml component with the same fx id.

The layout of `TaskCard` has been edited to fit the look of a task in Notion, especially the behaviour of task cards when resizing the main window. The CSS of the app is specified in [`NotionUS.css`](https://github.com/AY2223S1-CS2103T-F12-3/tp/tree/master/src/main/resources/view/NotionUS.css).

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified task data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Task` object residing in the `Model`.

### 4.3 Logic component

**API** : [`Logic.java`](https://github.com/AY2223S1-CS2103T-F12-3/tp/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

How the `Logic` component works:
1. When `Logic` is called upon to execute a command, it uses the `AddressBookParser` class to parse the user command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `AddCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to add a task).
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

The Sequence Diagram below illustrates the interactions within the `Logic` component for the `execute("delete 1")` API call.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### 4.4 Model component
**API** : [`Model.java`](https://github.com/AY2223S1-CS2103T-F12-3/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the task list data i.e., all `Task` objects (which are contained in a `UniqueTaskList` object).
* stores the currently 'selected' `Task` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Task>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* to support a default sorting of `Task` objects, `Task` implements the `Comparable<Task>` interface 
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)
* As `Deadline` is an optional field, their values are stored in an `Optional` object.

### 4.5 Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in json format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### 4.6 Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **5. Implementation**

This section describes some noteworthy details on how certain features are implemented.

### 5.1 Hide Command

In order to get a clearer view by archiving overdue and completed tasks whenever a user enters the command. Hidden (archived) tasks will still be stored and can be retrieved using `listAllCommand`.    

Command takes input
* `archive <index>` where `<index>` is the index of the tasks based on the displayed index shown in Main Window.
* `archive -d date` where `date` should be formatted as `YYYY-MM-DD` and all tasks before and on `date` will be hidden (archived).

Command result will tell us number of tasks remaining. 

Should `date` be improperly formatted or `<index>` entered is out of bound, a generic CommandResult and an error message will be given. Model will not be updated.

Below is the sequence diagram for an execution of `archive <index>`, assuming `<index>` is not out of bound. 

![Sequence diagram when command `archive 1` is executed](images/HideSequenceDiagram-0.png)

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added} 

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_

### 5.2 Mark/unmark feature

#### 5.2.1 Implementation

The mark/unmark feature marks a task in the task list as done/not done respectively. Its mechanism is facilitated by `MarkCommand` and `UnmarkCommand`. They both extend `Command`, overriding the `execute` method.
The implementation of `mark` and `unmark` is such that a task being marked or unmarked is effectively replaced by an identical task with a modified `isDone` field in the task list.

Below is the sequence diagram for an execution of `mark <index>`, assuming `<index>` is not out of bounds.

![Sequence diagram when command `mark 1` is executed](images/MarkSequenceDiagram.png)

### 5.3 Returning to a previous command 

#### 5.3.1 Implementation 

This feature allows the user to traverse through past commands via the up and down keys on the keyboard, similar to how it works in a terminal. 

This mechanism of returning to previous commands is facilitated by `CommandHistory`. It works with `CommandBox`, storing entered commands internally in `previousCommands` and controls the traversal with the aid of `pointer`. 
Additionally, it implements the following operations: 

* `CommandHistory#add(String)` – Saves the entered command into the history. 
* `CommandHistory#up()` – Traverses upwards through the history and restores the previously entered command.
* `CommandHistory#down()` – Traverses downwards through the history and restores the command. 

Given below is an example usage scenario and how the command history traversal mechanism behaves at each step. 

Step 1. The user launches the application for the first time. The `CommandBox` will be initialised, which in turns initialises the `CommandHistory` that is contained within. Since command history is empty, `previousCommands`will be empty. 

Step 2. The user enters a command `add n/Project m/CS2103T d/2022-10-18 t/lowPriority`. Upon entering a new command, `CommandBox#handleButtonPressed(KeyEvent)` is called, which in turn calls `CommandHistory#add(String)` to stores this command into `previousCommands`. 

Step 3. The user presses on the `up` key to return to the previously entered command. This action calls the `CommandHistory#up()` which will shift `pointer` once to the left, pointing it to the previous command in history, and feeds this String back to the command box. 

Step 4. The user presses on the `down` key to traverse down the history. This action calls the `CommandHistory#up()` which will shift `pointer` once to the right. Since the `pointer` is already pointing to the latest command in history, there are no more commands to be returned to. 
Hence, the command box will be cleared. 

The following activity diagram summarizes what happens when a user clicks on the `up`/`down` keys. 

![UpDownKeyActivityDiagram](images/UpDownKeyActivityDiagram.png)

#### 5.3.2 Design considerations: 

**Aspect: How command history is stored:** 

* **Alternative 1 (current choice):** Stores entered commands in an arrayList, renewed everytime the app launches. 
  * Pros: Easy to implement. 
  * Cons: Will not be able to restore commands from previous launches. 
* **Alternative 2:** Stores entered commands into a local `json` file. 
  * Pros: Will be able to restore commands from previous launches. 
  * Cons: Difficult to implement as storage architecture will have to be renewed.  

--------------------------------------------------------------------------------------------------------------------

## **6. Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **7. Appendix: Requirements**

### 7.1 Product scope

**7.1.1 Target user profile**:

* tech-savvy
* university student
* has a need to manage tasks from school
* prefer desktop apps over other types
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**7.1.2 Value proposition**: manage module tasks faster than a typical mouse/GUI driven app and organise them by certain parameters


### 7.2 User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

*Core Functionalities*

| Priority | As a …​        | I want to …​                                                              | So that I can…​                             |
|--------|----------------|---------------------------------------------------------------------------|---------------------------------------------|
| `* * *` | beginner user  | add tasks                                                                 | keep track of the tasks on hand             |
| `* * *` | beginner user  | delete tasks                                                              | remove tasks that are no longer relevant    |
| `* * *` | beginner user  | tag my tasks to a specific module or commitment                           | organise them better                        |
| `* * *` | beginner user  | keep track of deadlines related to added tasks                            | complete tasks on time                      |
| `* * *` | beginner user  | see all the tasks I have that have yet to be completed                    | easily identify tasks to work on            |
| `* *`  | potential user | see example tasks that show how the app displays tasks and their statuses | have a better idea of how the app functions |
| `* *`  | beginner user  | edit the task names                                                       | rectify mistakes                            |
| `* *`  | familiar user  | sort my tasks by due date                                                 | easily see which are the most urgent        |



*{More to be added}*


### 7.3 Use cases

Unless specified otherwise, the **System** is the `NotionUS` application and the **Actor** is the `user`.

**Use Case: UC1 - Add a task**

**MSS:**
1. User requests to add a task into the task list
2. NotioNUS adds task into task list and displays it
   
   Use case ends.

**Extensions:**

* 1a. User does not provide the required information for the task
  * 1a1. NotioNUS shows an error, requesting the user re-enter their task
  
     Use case ends.

**Use Case: UC2 - Edit a task**

**MSS:**

1. User finds the id associated with the task
2. User requests to edit the task
3. NotioNUS edits the task and displays it

   Use case ends.

**Extensions:**

* 2a. User provides an invalid ID
  * 2a1. NotioNUS shows an error, requesting the user check the task id
     
    Use case starts from 1.


* 2b. User does not provide any changes
  * 2b1. NotioNUS provides a note that nothing was changed

    Use case ends

**Use Case: UC3 - Delete a task**

**MSS:**

1. User finds the id associated with the task
2. User requests to delete the task
3. NotioNUS deletes the task and updates the view
   
   Use case ends.

**Extensions:**

* 2a. User provides an invalid ID
  * 2a1. NotioNUS shows an error, requesting the user check the task id

    Use case starts from 1.

**Use Case: UC4 - Tag a task**

**MSS:**

1. User creates a task (UC1)
2. With the task id, user requests to tag the task
3. NotioNUS tags the task and displays it
   Use case ends.

**Extensions:**
* 2a. User provides an invalid ID 
  * 2a1. NotioNUS shows an error, requesting the user check the task id
  
    Use case starts from 1.


### 7.4 Non-Functional Requirements

1. Should work on any mainstream OS as long as it has Java 11 or above installed. (compatibility) 
2. Should be able to hold up to 1000 users without a noticeable lag in performance for typical usage. (performance)
3. Should have a response time less than or equal to 5 seconds given any command. (Performance)
4. Should take less than 2GB of memory while in operation. (Performance)
5. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse. (Usability) 
6. The error rate of the commands entered by the user should not exceed 5%. (Usability)
7. Should take an user less than 1 day to familiarise with all the main commands used for the application. (Usability - learnability)
8. An user who returned to the interface after stopping for some time should be able to use the application efficiently right away. (Usability - memorability)
9. Should provide a pleasant user experience and a high user satisfactory level. (Usability - satisfaction) 
10. Should not face any error or system failure 95% of the time. (Reliability)
11. Should support the use of the UK English language. (Localisation)
12. Should be available to users 99% of the time. (Availability)
13. Maintainence should not take up more than 20 minutes when an error is encountered. (Maintainability)
14. Should be easily moved from one computing environment to another without any change in its behaviour or performance. ie. should not require an installer.  (Portability)
15. Should take less than 2GB of storage space. 
16. Data should be stored locally in the user's operating device. 


*{More to be added}*

### 7.5 Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X
* **Tag**: A user created tag that is linked to a task

--------------------------------------------------------------------------------------------------------------------

## **8 Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### 8.1 Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### 8.2 Deleting a task

1. Deleting a task while all tasks are being shown

   1. Prerequisites: List all tasks using the `ls -a` command. Multiple tasks in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### 8.3 Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
