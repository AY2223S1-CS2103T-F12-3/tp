package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyTaskList;

/**
 * Represents a storage for {@link seedu.address.model.ArchivedTaskBook}.
 */
public interface ArchivedTaskBookStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getArchivedTaskBookFilePath();

    /**
     * Returns TaskBook data as a {@link ReadOnlyTaskList}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyTaskList> readArchivedTaskBook() throws DataConversionException, IOException;

    /**
     * @see #getArchivedTaskBookFilePath()
     */
    Optional<ReadOnlyTaskList> readArchivedTaskBook(Path filePath) throws DataConversionException, IOException;

    void saveArchivedTaskBook(ReadOnlyTaskList addressBook) throws IOException;

    void saveArchivedTaskBook(ReadOnlyTaskList addressBook, Path filePath) throws IOException;

}

