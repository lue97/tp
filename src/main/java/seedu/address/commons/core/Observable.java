package seedu.address.commons.core;

public interface Observable<T> {
    public void addObserver(Observer<T> observer);
    public void updateAll();
}
