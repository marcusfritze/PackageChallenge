package de.fritze.marcus;


import de.fritze.marcus.exception.AppException;
import de.fritze.marcus.exception.GlobalErrorCodes;
import de.fritze.marcus.packer.Packager;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        try {
            // check if app has only one argument
            if (args.length != 1) {
                throw new AppException(GlobalErrorCodes.ARGUMENT_NOT_ONE, "Please pass the file name as one argument!");
            } else {
                // starts packaging
                Packager.startPackaging(args[0]);
            }
        } catch (AppException e) {
            // here is the normal AppException shown to the user
            System.out.println("An error occurred:\ncode: " + e.getCode() + "\n" + "error: " + e.getMessage());
            System.exit(e.getCode());
        } catch (Exception e) {
            // this shouldn't happen, but better safe than sorry
            String stackTraceString = Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n"));
            System.out.println("An error occurred:\ncode: " + GlobalErrorCodes.UNDEFINED_EXCEPTION + "\nerror: " + e.toString() + "\nstack: " + stackTraceString);
            System.exit(10);
        }
    }
}
