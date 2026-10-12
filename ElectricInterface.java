public interface ElectricInterface {
    /**
     * Checks if another vehicle has the same range category.
     * Returns true if other is an instance of ElectricVehicle and their
     * battery capacities differ by at most 5 kWh.
     * @param other The vehicle to compare with
     * @return true if within 5 kWh battery capacity difference, false otherwise
     */
    boolean hasSameRangeCategory(Vehicle other);

    /**
     * Calculates the estimated monetary share of the battery package
     * (25,000 currency units per kWh).
     * @return The battery package cost share
     */
    int batteryCostShare();
}
