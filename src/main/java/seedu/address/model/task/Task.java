package seedu.address.model.task;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a Task in the task tracker.
 * Guarantees: All details are present and not null, field values are validated and immutable
 */
public class Task {

    private final Name name;
    private final Module module;
    private final Deadline deadline;
    private final Set<Tag> tags = new HashSet<>();
    private final boolean isDone;

    /**
     * A convenience constructor for the {@code Task} class.
     * Every field must be present and not null.
     */
    public Task(Name name, Module module, Deadline deadline, Set<Tag> tags) {
        this(name, module, deadline, tags, false);
    }

    /**
     * Constructor for the {@code Task} class.
     * Every field must be present and not null.
     */
    public Task(Name name, Module module, Deadline deadline, Set<Tag> tags, boolean isDone) {
        requireAllNonNull(name, module, deadline, tags);
        this.name = name;
        this.module = module;
        this.deadline = deadline;
        this.tags.addAll(tags);
        this.isDone = isDone;
    }

    public Name getName() {
        return name;
    }

    public Module getModule() {
        return module;
    }

    public Deadline getDeadline() {
        return deadline;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public Boolean isDone() {
        return isDone;
    }

    /**
     * Returns true if both tasks have the same name and module.
     * This defines a weaker notion of equality between two tasks.
     */
    public boolean isSamePerson(Task otherTask) {
        if (this == otherTask) {
            return true;
        }

        return otherTask != null
                && otherTask.getName().equals(this.getName())
                && otherTask.getModule().equals(this.getModule());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Task)) {
            return false;
        }

        Task otherTask = (Task) other;
        return otherTask.getName().equals(getName())
                && otherTask.getModule().equals(getModule())
                && otherTask.getDeadline().equals(getDeadline())
                && otherTask.getTags().equals(getTags())
                && otherTask.isDone == this.isDone;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, module, deadline, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append("; Module: ")
                .append(getModule())
                .append("; Deadline: ")
                .append(getDeadline());

        Set<Tag> tags = getTags();
        if (!tags.isEmpty()) {
            builder.append("; Tags: ");
            tags.forEach(builder::append);
        }

        builder.append("; Done: ").append(isDone);
        return builder.toString();
    }
}
