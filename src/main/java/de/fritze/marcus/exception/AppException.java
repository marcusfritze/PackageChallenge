package de.fritze.marcus.exception;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AppException extends Exception {

    private int code = 0;

    /**
     * AppException has a message and a code for better understanding
     *
     * @param code
     * @param message
     */
    public AppException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    /**
     * This is needed for throwing exceptions from inside lambda functions
     *
     * @param throwingConsumer
     * @param <T>
     * @return
     */
    public static <T> Consumer<T> throwingConsumerWrapper(ThrowingConsumer<T, Exception> throwingConsumer) {
        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (AppException e) {
                System.out.println("An error occurred:\ncode: " + e.getCode() + "\n" + "error: " + e.getMessage());
                System.exit(e.getCode());
            } catch (Exception e) {
                String stackTraceString = Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n"));
                System.out.println("An error occurred:\ncode: " + GlobalErrorCodes.UNDEFINED_EXCEPTION + "\nerror: " + e.toString() + "\nstack: " + stackTraceString);
                System.exit(10);
            }
        };
    }
}
