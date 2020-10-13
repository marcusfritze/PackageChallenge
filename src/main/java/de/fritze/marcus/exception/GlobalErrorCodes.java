package de.fritze.marcus.exception;

public class GlobalErrorCodes {

    /**
     * constants for AppExceptions, so every Exception has his own error code
     */
    public static final int UNDEFINED_EXCEPTION                 = 10;
    public static final int ARGUMENT_NOT_ONE                    = 11;

    public static final int NO_PATH_DEFINED                     = 20;
    public static final int PATH_IS_NOT_A_FILE                  = 21;
    public static final int UNABLE_TO_READ_FILE                 = 22;
    public static final int ERROR_ON_READING_FILE               = 23;

    public static final int LINE_NOT_IN_CORRECT_FORMAT          = 30;

    public static final int PACKAGE_MAX_WEIGHT_EXCEEDED         = 40;
    public static final int PACKAGE_ITEMS_AMOUNT_EXCEEDED       = 41;
    public static final int PACKAGE_ITEM_MAX_WEIGHT_EXCEEDED    = 42;
    public static final int PACKAGE_ITEM_MAX_PRICE_EXCEEDED     = 43;
    public static final int PACKAGE_ITEMS_ITEM_NUMBER_1_MISSING = 44;
    public static final int PACKAGE_ITEMS_ITEM_NUMBER_MISSING   = 45;
}
