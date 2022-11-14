package se.deepcloud.cloudkingdoms.utilities.sorting;

public abstract class Singleton<E> {

    private E instance;

    public E get() {
        return instance == null ? (instance = create()) : instance;
    }

    protected abstract E create();

}