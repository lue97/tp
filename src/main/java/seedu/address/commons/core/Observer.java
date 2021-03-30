package seedu.address.commons.core;

public interface Observer<T> {
    public void update(T arg);
}
