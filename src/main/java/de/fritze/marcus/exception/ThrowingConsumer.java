package de.fritze.marcus.exception;

/**
 * This is used for throwing an exception from inside a lambda function
 *
 * @param <T>
 * @param <E>
 */
@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception> {

    void accept(T t) throws E;

}