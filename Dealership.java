import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

public class Dealership {
    private TreeSet<Vehicle> vehicles;

    public Dealership() {
        this.vehicles = new TreeSet<>();
    }

    public TreeSet<Vehicle> getVehicles() {
        return vehicles;
    }

    public void addVehicle(Vehicle vehicle) {
        if (vehicle != null) {
            vehicles.add(vehicle);
        }
    }

    /**
     * Reads and parses vehicle records from a file into the TreeSet.
     * Falls back to predefined hardcoded instances if the file cannot be read.
     *
     * @param filename Path to the file (e.g., "vehicles.txt")
     * @return true if loaded successfully from file, false if fallback was used
     */
    public boolean loadVehiclesFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists() || !file.canRead()) {
            System.err.println("Warning: Unable to read file '" + filename + "'. Falling back to predefined instances.");
            loadPredefinedInstances();
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("//")) {
                    continue;
                }

                String[] parts = line.split("#");
                if (parts.length < 6) {
                    continue;
                }

                String type = parts[0].trim().toUpperCase();
                String brandAndModel = parts[1].trim();
                double basePrice = Double.parseDouble(parts[2].trim());
                int age = Integer.parseInt(parts[3].trim());
                double km = Double.parseDouble(parts[4].trim());
                FuelType fuelType = FuelType.valueOf(parts[5].trim().toUpperCase());

                if (type.equals("ELECTRIC") && parts.length >= 8) {
                    int batteryCapacity = Integer.parseInt(parts[6].trim());
                    String heatPumpStr = parts[7].trim().toLowerCase();
                    boolean hasHeatPump = heatPumpStr.equals("yes") || heatPumpStr.equals("true");

                    ElectricVehicle ev = new ElectricVehicle(brandAndModel, basePrice, age, km, fuelType, batteryCapacity, hasHeatPump);
                    vehicles.add(ev);
                } else if (type.equals("VEHICLE")) {
                    Vehicle v = new Vehicle(brandAndModel, basePrice, age, km, fuelType);
                    vehicles.add(v);
                }
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error reading '" + filename + "': " + e.getMessage() + ". Falling back to predefined instances.");
            vehicles.clear();
            loadPredefinedInstances();
            return false;
        }
    }

    /**
     * Predefined hardcoded fallback instances as requested in Part 4.
     */
    public void loadPredefinedInstances() {
        vehicles.clear();
        vehicles.add(new Vehicle("Toyota Corolla", 5500000, 4, 75000, FuelType.HYBRID));
        vehicles.add(new Vehicle("Volkswagen Golf", 4200000, 6, 120000, FuelType.DIESEL));
        vehicles.add(new Vehicle("Ford Focus", 3100000, 8, 145000, FuelType.PETROL));
        vehicles.add(new Vehicle("BMW 320d", 7800000, 3, 60000, FuelType.DIESEL));
        vehicles.add(new Vehicle("Opel Astra", 2200000, 11, 190000, FuelType.PETROL));
        vehicles.add(new ElectricVehicle("Nissan Leaf", 4800000, 5, 65000, FuelType.HYBRID, 40, false));
        vehicles.add(new ElectricVehicle("Tesla Model 3", 11500000, 2, 38000, FuelType.HYBRID, 75, true));
        vehicles.add(new ElectricVehicle("Renault Zoe", 3900000, 6, 82000, FuelType.HYBRID, 52, true));
        vehicles.add(new ElectricVehicle("Kia EV6", 13200000, 1, 15000, FuelType.HYBRID, 77, true));
        vehicles.add(new ElectricVehicle("VW ID.3", 7100000, 4, 54000, FuelType.HYBRID, 58, false));
    }

    public static void main(String[] args) {
        Dealership dealership = new Dealership();
        String inputFilename = "vehicles.txt";
        dealership.loadVehiclesFromFile(inputFilename);

        List<String> outputLines = new ArrayList<>();

        outputLines.add("================================================================================");
        outputLines.add("                  CAR DEALERSHIP MANAGEMENT SYSTEM - REPORT                     ");
        outputLines.add("================================================================================");
        outputLines.add("");

        // 1. Fleet Average Mileage
        double totalKm = 0.0;
        for (Vehicle v : dealership.getVehicles()) {
            totalKm += v.getKm();
        }
        double avgKm = dealership.getVehicles().isEmpty() ? 0.0 : totalKm / dealership.getVehicles().size();
        outputLines.add(String.format(Locale.US, "1. Fleet Average Mileage: %,.2f km", avgKm));
        outputLines.add("");

        // 2. Cheapest Vehicle
        Vehicle cheapest = dealership.getVehicles().isEmpty() ? null : dealership.getVehicles().first();
        outputLines.add("2. Cheapest Vehicle:");
        if (cheapest != null) {
            outputLines.add("   " + cheapest.toString());
        } else {
            outputLines.add("   No vehicles available.");
        }
        outputLines.add("");

        // 3. Top Electric Vehicle
        ElectricVehicle topElectric = null;
        for (Vehicle v : dealership.getVehicles()) {
            if (v instanceof ElectricVehicle) {
                ElectricVehicle ev = (ElectricVehicle) v;
                if (topElectric == null || ev.getFinalPrice() > topElectric.getFinalPrice()) {
                    topElectric = ev;
                }
            }
        }
        outputLines.add("3. Top Electric Vehicle:");
        if (topElectric != null) {
            String name = (topElectric.getModel() == null || topElectric.getModel().isEmpty())
                    ? topElectric.getBrand()
                    : topElectric.getBrand() + " " + topElectric.getModel();
            outputLines.add(String.format(Locale.US,
                    "   Vehicle: %s | Final Price: %,d | Battery Capacity: %d kWh | Average Annual Mileage: %,.2f km/yr",
                    name, topElectric.getFinalPrice(), topElectric.getBatteryCapacity(), topElectric.averageKmPerYear()));
        } else {
            outputLines.add("   No electric vehicles found.");
        }
        outputLines.add("");

        // 4. Total Fleet Inventory Value
        long totalInventoryValue = 0L;
        for (Vehicle v : dealership.getVehicles()) {
            totalInventoryValue += v.getFinalPrice();
        }
        outputLines.add(String.format(Locale.US, "4. Total Fleet Inventory Value: %,d", totalInventoryValue));
        outputLines.add("");

        // 5. Filtered Hybrid Inventory
        double averagePrice = dealership.getVehicles().isEmpty() ? 0.0 : (double) totalInventoryValue / dealership.getVehicles().size();
        outputLines.add(String.format(Locale.US,
                "5. Filtered Hybrid Inventory (FuelType == HYBRID and Final Price <= Average Price of %,.2f):",
                averagePrice));
        boolean foundHybrid = false;
        for (Vehicle v : dealership.getVehicles()) {
            if (v.getFuelType() == FuelType.HYBRID && v.getFinalPrice() <= averagePrice) {
                outputLines.add("   - " + v.toString());
                foundHybrid = true;
            }
        }
        if (!foundHybrid) {
            outputLines.add("   (None)");
        }
        outputLines.add("");

        // 6. Young Inventory Count
        int youngCount = 0;
        for (Vehicle v : dealership.getVehicles()) {
            if (v.getAge() < 5) {
                youngCount++;
            }
        }
        outputLines.add("6. Young Inventory Count (age < 5): " + youngCount);
        outputLines.add("");

        // 7. Discount Simulation
        outputLines.add("7. Discount Simulation:");
        if (!dealership.getVehicles().isEmpty()) {
            // Select a vehicle to demonstrate the discount
            Vehicle targetVehicle = dealership.getVehicles().first();
            for (Vehicle v : dealership.getVehicles()) {
                if (v.getBrand().contains("Toyota") || v.getModel().contains("Corolla")) {
                    targetVehicle = v;
                    break;
                }
            }

            String targetName = (targetVehicle.getModel() == null || targetVehicle.getModel().isEmpty())
                    ? targetVehicle.getBrand()
                    : targetVehicle.getBrand() + " " + targetVehicle.getModel();

            int priceBeforeDiscount = targetVehicle.getFinalPrice();
            double basePriceBefore = targetVehicle.getBasePrice();

            outputLines.add("   Selected Vehicle: " + targetName);
            outputLines.add(String.format(Locale.US, "   Original Base Price: %,.0f | Original Final Price: %,d",
                    basePriceBefore, priceBeforeDiscount));

            targetVehicle.applyDiscount(10);

            int priceAfterDiscount = targetVehicle.getFinalPrice();
            double basePriceAfter = targetVehicle.getBasePrice();

            outputLines.add(String.format(Locale.US,
                    "   After 10%% Discount -> Updated Base Price: %,.0f | Updated Final Price: %,d",
                    basePriceAfter, priceAfterDiscount));
        } else {
            outputLines.add("   No vehicle available for discount simulation.");
        }
        outputLines.add("");
        outputLines.add("================================================================================");

        // Display to console and write to outputDealership.txt
        String outputFilename = "outputDealership.txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilename))) {
            for (String line : outputLines) {
                System.out.println(line);
                writer.println(line);
            }
            System.out.println("\n[Output successfully written to " + outputFilename + "]");
        } catch (IOException e) {
            System.err.println("Error writing to " + outputFilename + ": " + e.getMessage());
        }
    }
}
