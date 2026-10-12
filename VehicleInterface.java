public interface VehicleInterface {
    /**
     * Reduces basePrice by the specified integer percentage.
     * @param percentage The discount percentage (e.g., 10 for 10%)
     */
    void applyDiscount(int percentage);

    /**
     * Calculates and returns the selling price as an integer.
     * @return The final selling price
     */
    int getFinalPrice();

    /**
     * Calculates the average annual distance driven in km (mileage / age).
     * If age is 0, returns the total mileage.
     * @return The average km per year
     */
    double averageKmPerYear();

    /**
     * Returns a string representation of the vehicle's state including its
     * final price and annual average mileage.
     * @return Formatted string representation
     */
    @Override
    String toString();
}
