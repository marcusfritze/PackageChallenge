package de.fritze.marcus.validation;

import de.fritze.marcus.model.PackageItem;
import de.fritze.marcus.exception.AppException;
import de.fritze.marcus.exception.GlobalErrorCodes;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Validator {

    private static final int MAX_WEIGHT_PACKAGE = 100;
    private static final int MAX_ITEMS_IN_LINE  = 15;
    private static final int MAX_WEIGHT_ITEM    = 100;
    private static final int MAX_PRICE_ITEM     = 100;

    /**
     * This function checks the constraints
     * 1. The maximum weight that a package can hold must be <= 100.
     * 2. There may be up to 15 items you can to choose from.
     * 3. The maximum weight of an item should be <= 100.
     * 4. The maximum cost of an item should be <= €100.
     * 5. check duplicate item numbers and missing item numbers and first item number should be 1
     *
     * @param lineNumber
     * @param maxWeightPackage
     * @param packageItems
     * @throws AppException
     */
    public static void checkConstraints(int lineNumber, float maxWeightPackage, List<PackageItem> packageItems) throws AppException {

        // 1. The maximum weight that a package can hold must be <= 100.
        if (maxWeightPackage > MAX_WEIGHT_PACKAGE) {
            throw new AppException(GlobalErrorCodes.PACKAGE_MAX_WEIGHT_EXCEEDED, "Package max weight exceeded in line " + lineNumber + ". The maximum weight that a package can hold must be <= 100.");
        }

        // 2. There may be up to 15 items you can to choose from.
        if (packageItems.size() > MAX_ITEMS_IN_LINE) {
            throw new AppException(GlobalErrorCodes.PACKAGE_ITEMS_AMOUNT_EXCEEDED, "Too many items for the package in line " + lineNumber + ". There may be up to 15 items you can to choose from.");
        }

        // 3. The maximum weight of an item should be <= 100.
        if (packageItems.stream().anyMatch(packageItem -> packageItem.getWeight() > MAX_WEIGHT_ITEM)) {
            String tooHeavyPackages = packageItems.stream().filter(packageItem -> packageItem.getWeight() > MAX_WEIGHT_ITEM).map(Object::toString).collect(Collectors.joining(","));

            throw new AppException(GlobalErrorCodes.PACKAGE_ITEM_MAX_WEIGHT_EXCEEDED, "The maximum weight of an item should be <= 100. The following items are too heavy in line " + lineNumber + ". " + tooHeavyPackages);
        }

        // 4. The maximum cost of an item should be <= €100.
        if (packageItems.stream().anyMatch(packageItem -> packageItem.getPrice() > MAX_PRICE_ITEM)) {
            String tooExpensivePackages = packageItems.stream().filter(packageItem -> packageItem.getPrice() > MAX_PRICE_ITEM).map(Object::toString).collect(Collectors.joining(","));

            throw new AppException(GlobalErrorCodes.PACKAGE_ITEM_MAX_PRICE_EXCEEDED, "The maximum cost of an item should be <= €100. The following items are too expensive in line " + lineNumber + ". " + tooExpensivePackages);
        }

        // 5. check duplicate item numbers and missing item numbers and first item number should be 1
        int amountOfItemsInLine         = (int) packageItems.stream().map(PackageItem::getId).mapToInt(Integer::intValue).count();
        List<Integer> actualDistinctIds = packageItems.stream().map(PackageItem::getId).distinct().sorted().collect(Collectors.toList());
        List<Integer> validIdRange      = IntStream.rangeClosed(1, amountOfItemsInLine).boxed().collect(Collectors.toList());

        // check if item number 1 exists in line
        if (actualDistinctIds.get(0) != 1) {
            throw new AppException(GlobalErrorCodes.PACKAGE_ITEMS_ITEM_NUMBER_1_MISSING, "The item number 1 is missing in the item list of line " + lineNumber);
        }

        // remove the actual existing distinct item numbers from the generated valid id rage, so we have the missing item numbers
        validIdRange.removeIf(actualDistinctIds::contains);

        if (validIdRange.size() > 0) {
            throw new AppException(GlobalErrorCodes.PACKAGE_ITEMS_ITEM_NUMBER_MISSING, "The following item numbers are missing in the item list of line " + lineNumber + ": " + validIdRange.toString());
        }
    }
}
