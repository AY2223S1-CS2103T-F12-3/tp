package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.ArchivedTaskBook;
import seedu.address.model.ReadOnlyTaskList;
import seedu.address.model.task.Task;

/**
 * An Immutable TaskList that is serializable to JSON format.
 */
@JsonRootName(value = "archivedTaskBook")
public class JsonSerializableArchivedTaskBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate task(s).";

    private final List<JsonAdaptedPerson> archivedTasks = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableArchivedTaskBook(@JsonProperty("archivedTasks") List<JsonAdaptedPerson> persons) {
        this.archivedTasks.addAll(persons);
    }

    /**
     * Converts a given {@code ReadOnlyTaskList} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableArchivedTaskBook(ReadOnlyTaskList source) {
        archivedTasks.addAll(source.getTaskList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code TaskList} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public ArchivedTaskBook toModelType() throws IllegalValueException {
        ArchivedTaskBook archivedTaskBook = new ArchivedTaskBook();
        for (JsonAdaptedPerson jsonAdaptedPerson : archivedTasks) {
            Task task = jsonAdaptedPerson.toModelType();
            if (archivedTaskBook.hasTask(task)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            archivedTaskBook.addTask(task);
        }
        return archivedTaskBook;
    }
}
