package ru.dipech.listeneng.service;

public interface FetchService {

    /*
     * Recursively fetch all the fields of all nested data
     * (to avoid LazyInitializationException and "No session" error)
     */
    void fetch(Object object);

    /*
     * Fetch some field
     */
    void fetch(Object object, String path);

    /*
     * Fetch some fields
     */
    void fetch(Object object, String... paths);

}
