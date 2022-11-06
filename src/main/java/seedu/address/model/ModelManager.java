package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.task.Task;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final TaskList taskList;
    private final ArchivedTaskBook archivedTaskBook;
    private final UserPrefs userPrefs;
    private FilteredList<Task> filteredTasks;
    private String filterStatus = "";
    private final FilteredList<Task> filteredArchivedTasks;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyTaskList taskList,
                        ReadOnlyTaskList archivedTaskBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(taskList, archivedTaskBook, userPrefs);

        logger.fine("Initializing with address book: " + taskList
                + "Archived Task Book: " + archivedTaskBook + " and user prefs " + userPrefs);

        this.taskList = new TaskList(taskList);
        this.userPrefs = new UserPrefs(userPrefs);
        this.archivedTaskBook = new ArchivedTaskBook(archivedTaskBook);
        filteredTasks = new FilteredList<>(this.taskList.getTaskList());
        filteredArchivedTasks = new FilteredList<>(this.archivedTaskBook.getTaskList());
    }

    public ModelManager() {
        this(new TaskList(), new ArchivedTaskBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public Path getArchivedTaskBookFilePath() {
        return userPrefs.getArchivedTaskBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    @Override
    public void setArchivedTaskBookFilePath(Path archivedTaskBookFilePath) {
        requireNonNull(archivedTaskBookFilePath);
        userPrefs.setArchivedTaskBookFilePath(archivedTaskBookFilePath);
    }

    //=========== TaskList ================================================================================

    @Override
    public void setTaskList(ReadOnlyTaskList taskList) {
        this.taskList.resetData(taskList);
    }

    @Override
    public ReadOnlyTaskList getTaskList() {
        return taskList;
    }

    @Override
    public boolean hasPerson(Task task) {
        requireNonNull(task);
        return taskList.hasPerson(task);
    }

    @Override
    public void deletePerson(Task target) {
        taskList.removePerson(target);
    }

    @Override
    public void addPerson(Task task) {
        taskList.addPerson(task);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setTask(Task target, Task editedTask) {
        requireAllNonNull(target, editedTask);

        taskList.setPerson(target, editedTask);
    }


    //=========== ArchivedTaskBook ================================================================================

    @Override
    public ReadOnlyTaskList getArchivedAddressBook() {
        return archivedTaskBook;
    }

    @Override
    public void archivedTask(Task task) {
        taskList.removePerson(task);
        archivedTaskBook.addTask(task);
    }

    @Override
    public boolean hasTaskInArchives(Task task) {
        requireAllNonNull(task);
        return archivedTaskBook.hasTask(task);
    }

    @Override
    public ObservableList<Task> getArchivedTaskList() {
        return archivedTaskBook.getTaskList();
    }

    @Override
    public void setArchivedTaskBook(ReadOnlyTaskList addressBook) {
        this.archivedTaskBook.resetData(addressBook);
    }

    @Override
    public String getArchivedTasks() {
        return archivedTaskBook.toString();
    }

    //=========== Filtered Task List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Task} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Task> getFilteredPersonList() {
        return filteredTasks;
    }

    @Override
    public ObservableList<Task> getFilteredArchivedTaskList() {
        return filteredArchivedTasks;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Task> predicate) {
        requireNonNull(predicate);
        filteredTasks.setPredicate(predicate);
    }
    @Override
    public void updateFilterStatus(String filter) {
        requireNonNull(filter);
        if (this.filterStatus.equalsIgnoreCase("Showing all tasks") || this.filterStatus.equals("")) {
            this.filterStatus = filter;
        } else {
            this.filterStatus += ", " + filter;
        }
    }

    @Override
    public void updateFilterStatus(String filter, boolean isNewFilterSet) {
        requireNonNull(filter);
        if (isNewFilterSet) {
            this.filterStatus = filter;
        } else {
            this.filterStatus += ", " + filter;
        }
    }

    @Override
    public String getFilterStatus() {
        return this.filterStatus;
    }

    @Override
    public void updateFilteredArchivedTaskList(Predicate<Task> predicate) {
        requireNonNull(predicate);
        filteredArchivedTasks.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return taskList.equals(other.taskList)
                && userPrefs.equals(other.userPrefs)
                && archivedTaskBook.equals(other.archivedTaskBook)
                && filteredTasks.equals(other.filteredTasks)
                && filteredArchivedTasks.equals(other.filteredArchivedTasks);
    }

}
