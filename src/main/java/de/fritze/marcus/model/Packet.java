package de.fritze.marcus.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Packet {

    private List<PackageItem> packageItems = new ArrayList<>();

    private double totalWeight = 0;
    private double totalPrice = 0;

    public Packet() {

    }

    /**
     * This is a package Packet where the package items PackageItem are stored
     * it automatic calculates the total weight and total price for that package when we create that packet
     *
     * @param packageItems
     */
    public Packet(List<PackageItem> packageItems) {
        this.packageItems   = packageItems;
        this.totalWeight    = packageItems.stream().map(PackageItem::getWeight).mapToDouble(Double::doubleValue).sum();
        this.totalPrice     = packageItems.stream().map(PackageItem::getPrice).mapToDouble(Double::doubleValue).sum();
    }

    public List<PackageItem> getPackageItems() {
        return packageItems;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * here we can add a PackageItem to an initialized Packet
     * it automatic calculates the total weight and total price for that package when we add an item
     *
     * @param packageItem
     */
    public void addPackageItem(PackageItem packageItem) {
        packageItems.add(packageItem);
        totalWeight += packageItem.getWeight();
        totalPrice  += packageItem.getPrice();
    }

    /**
     * here we can remove a PackageItem from an initialized Packet
     * it automatic calculates the total weight and total price for that package when we remove an item
     *
     * @param packageItem
     */
    public void removePackage(PackageItem packageItem) {
        packageItems.remove(packageItem);
        totalWeight += packageItem.getWeight();
        totalPrice  += packageItem.getPrice();
    }

    /* Comparator for sorting the list by price descending, weight ascending, amount of items in packet descending */
    public static Comparator<Packet> PackageItemComparatorPriceDescAndWeightAscAndAmountOfItemsInPacketDesc = new Comparator<Packet>() {

        public int compare(Packet packet1, Packet packet2) {

            // sort descending order of total price of the packet
            int comparisonPrice         = Double.compare(packet2.getTotalPrice(),packet1.getTotalPrice());

            // if price is the same sort ascending order of total weight of the packet
            int comparisonWeight        = Double.compare(packet1.getTotalWeight(),packet2.getTotalWeight());

            // if price and weight is the same sort descending order of amount of items in packet
            int comparisonAmountItems   = Integer.compare(packet2.packageItems.size(), packet1.packageItems.size());

            if (comparisonPrice != 0) {
                return comparisonPrice;
            }
            else if (comparisonWeight != 0){
                return comparisonWeight;
            }
            else {
                return comparisonAmountItems;
            }
        }
    };

    /**
     * this functions prints the output of used PackageItem's in a Packet
     */
    public void printPackageItemIdsOfPackage() {
        System.out.println(this.getPackageItems().stream().map(packageItem -> String.valueOf(packageItem.getId())).collect(Collectors.joining(",")));
    }
}
