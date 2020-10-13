package de.fritze.marcus.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PackageItem {

    private int id          = 0;
    private double weight   = 0;
    private double price    = 0;

    /**
     * This is a PackageItem (each item in a line)
     *
     * @param id
     * @param weight
     * @param price
     */
    public PackageItem(int id, double weight, double price) {
        this.id     = id;
        this.weight = weight;
        this.price  = price;
    }

    public int getId() {
        return id;
    }

    public double getWeight() {
        return weight;
    }

    public double getPrice() {
        return price;
    }

    /**
     * here we create a PackageItem from a string by the defined pattern
     *
     * @param string
     * @return
     */
    public static PackageItem createFromString(String string) {

        String itemPattern = "\\((\\d+),([0-9]+([\\.][0-9]+)?),€([0-9]+([\\.][0-9]+)?)\\)";

        Matcher matcher = Pattern.compile(itemPattern).matcher(string);

        if (matcher.find()) {
            return new PackageItem(
                    Integer.parseInt(matcher.group(1)),
                    Double.parseDouble(matcher.group(2)),
                    Double.parseDouble(matcher.group(4))
            );
        }
        else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "PackageItem{" +
                "id=" + id +
                ", weight=" + weight +
                ", price=€" + price +
                '}';
    }
}


