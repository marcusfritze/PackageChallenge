package de.fritze.marcus.packer;

import de.fritze.marcus.exception.AppException;
import de.fritze.marcus.exception.GlobalErrorCodes;
import de.fritze.marcus.model.PackageItem;
import de.fritze.marcus.model.Packet;
import de.fritze.marcus.validation.Validator;
import org.paukov.combinatorics3.Generator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Packager {

    /**
     * Entry-point from the packager with the passed filename
     *
     * @param filePath
     * @throws AppException
     */
    public static void startPackaging(String filePath) throws AppException {

        // check if the passed argument is a valid file
        File inputFile = new File(filePath);

        if (!inputFile.exists()) {
            // the file doesn't exists
            throw new AppException(GlobalErrorCodes.NO_PATH_DEFINED, "The passed file path does not exists!");
        }
        if (!inputFile.isFile()) {
            // the filepath isn't a file, it's a directory for example
            throw new AppException(GlobalErrorCodes.PATH_IS_NOT_A_FILE, "The passed path is not a file!");
        }
        if (!inputFile.canRead()) {
            // we can not read the file
            throw new AppException(GlobalErrorCodes.UNABLE_TO_READ_FILE, "Unable to read file, please check permissions!");
        }

        // we read the file
        try (Stream<String> lines = Files.lines(Paths.get(filePath), Charset.defaultCharset())) {

            // we use this integer for counting the lines, for better output to the user if an exception occurs
            AtomicInteger lineCounter = new AtomicInteger();

            // for every line in the file...
            lines.forEachOrdered(
                    // ... we start packaging the line
                    // and we use the throwingConsumerWrapper around for throwing exceptions from this lambda if an exception occurs
                    AppException.throwingConsumerWrapper(item -> {
                        startPackagingLines(lineCounter.incrementAndGet(), item);
                    })
            );
        } catch (IOException ioException) {
            // something strange happens when we try to read the file
            throw new AppException(GlobalErrorCodes.ERROR_ON_READING_FILE, ioException.getMessage());
        }
    }

    /**
     * this function handles the line we read from the file and parses the string of the line - if it's valid - into PackageItem's
     *
     * @param lineNumber
     * @param line
     * @throws AppException
     */
    public static void startPackagingLines(int lineNumber, String line) throws AppException {

        if (line.isEmpty()) {
            // the line is empty, the output will be also empty
            System.out.println("");
        }
        else {
            // the line is not empty

            /*
                we check if the line matches the defined pattern for example:
                81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)
             */

            String linePattern = "([0-9]+([\\.][0-9]+)?)\\s:((\\s\\(\\d+,[0-9]+([\\.][0-9]+)?,€[0-9]+([\\.][0-9]+)?\\))+)";

            Matcher matcher = Pattern.compile(linePattern).matcher(line);

            if (matcher.find()) {
                // the line matches the defined pattern - work that line

                // save first group into maxweightPackage
                float maxWeightPackage = Float.parseFloat(matcher.group(1));

                // save items into String array
                String[] packagesString = matcher.group(3).trim().split("\\s+");

                // create PackageItem's from this String array
                List<PackageItem> packageItems = Arrays.stream(packagesString).map(PackageItem::createFromString).collect(Collectors.toList());

                // build the package for that line
                buildPackage(lineNumber, maxWeightPackage, packageItems);
            }
            else {
                // the line doesn't match the defines pattern for a line

                // we could analyze the error-position in that line...
                throw new AppException(GlobalErrorCodes.LINE_NOT_IN_CORRECT_FORMAT, "The line " + lineNumber + " in the file is not in correct format!");
            }
        }
    }

    /**
     * Builds the package from the given PackageItem's and the valid max weight for a package
     *
     * @param lineNumber
     * @param maxWeightPackage
     * @param packageItems
     * @throws AppException
     */
    public static void buildPackage(int lineNumber, float maxWeightPackage, List<PackageItem> packageItems) throws AppException {

        // remove null objects if one exists in packages list, this shouldn't happen because of the regex test
        packageItems.removeIf(Objects::isNull);

        // check constraints
        Validator.checkConstraints(lineNumber, maxWeightPackage, packageItems);

        // here are all possible packages stored for one line
        List<Packet> allPackedPacketsOfLine = new ArrayList<>();

        // we are getting all possible combinations (subset) from the PackageItems's ...
        // https://github.com/dpaukov/combinatoricslib3#6-subsets
        Generator.subset(packageItems).simple().stream().forEach(
                // ... and we loop through this subset ...
                packageItemSubSet -> {
                    // ... and we check if the sum of this particular subset is less or equal than the max weight of the package for that line
                    if (packageItemSubSet.size() > 0 && packageItemSubSet.stream().map(PackageItem::getWeight).mapToDouble(Double::doubleValue).sum() <= maxWeightPackage) {
                        // if it's valid we add this to our possible packages for that line
                        allPackedPacketsOfLine.add(new Packet(packageItemSubSet));
                    }
                }
        );

        // sort the valid packages of that line by price desc, weight asc, amount of items in package desc
        // we use amount of items in package desc if the price and weight is the same so we have the package with the most items if price and weight is equal
        allPackedPacketsOfLine.sort(Packet.PackageItemComparatorPriceDescAndWeightAscAndAmountOfItemsInPacketDesc);

        if (allPackedPacketsOfLine.size() > 0) {
            // the line has a valid package, print the first package
            allPackedPacketsOfLine.get(0).printPackageItemIdsOfPackage();
        }
        else {
            // the line doesn't have a valid package
            System.out.println("-");
        }
    }
}
