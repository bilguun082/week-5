import java.util.Locale;
import java.util.Objects;

public class ElectricVehicle extends Vehicle implements ElectricInterface {
    protected int batteryCapacity;
    protected boolean hasHeatPump;

    public ElectricVehicle() {
        super("", "", 0.0, 0, 0.0, FuelType.HYBRID);
        this.batteryCapacity = 0;
        this.hasHeatPump = false;
    }

    public ElectricVehicle(String brand, String model, double basePrice, int age, double km, FuelType fuelType, int batteryCapacity, boolean hasHeatPump) {
        super(brand, model, basePrice, age, km, fuelType != null ? fuelType : FuelType.HYBRID);
        this.batteryCapacity = batteryCapacity;
        this.hasHeatPump = hasHeatPump;
    }

    public ElectricVehicle(String brand, String model, double basePrice, int age, double km, int batteryCapacity, boolean hasHeatPump) {
        super(brand, model, basePrice, age, km, FuelType.HYBRID);
        this.batteryCapacity = batteryCapacity;
        this.hasHeatPump = hasHeatPump;
    }

    public ElectricVehicle(String brandAndModel, double basePrice, int age, double km, FuelType fuelType, int batteryCapacity, boolean hasHeatPump) {
        super(brandAndModel, basePrice, age, km, fuelType != null ? fuelType : FuelType.HYBRID);
        this.batteryCapacity = batteryCapacity;
        this.hasHeatPump = hasHeatPump;
    }

    public ElectricVehicle(String brandAndModel, double basePrice, int age, double km, int batteryCapacity, boolean hasHeatPump) {
        super(brandAndModel, basePrice, age, km, FuelType.HYBRID);
        this.batteryCapacity = batteryCapacity;
        this.hasHeatPump = hasHeatPump;
    }

    // Getters and Setters
    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public boolean isHasHeatPump() {
        return hasHeatPump;
    }

    public boolean hasHeatPump() {
        return hasHeatPump;
    }

    public void setHasHeatPump(boolean hasHeatPump) {
        this.hasHeatPump = hasHeatPump;
    }

    @Override
    public int getFinalPrice() {
        double price = super.getFinalPrice();
        if (batteryCapacity >= 60) {
            price *= 1.10;
        }
        if (hasHeatPump) {
            price *= 1.05;
        }
        return (int) Math.round(price);
    }

    @Override
    public boolean hasSameRangeCategory(Vehicle other) {
        if (other instanceof ElectricVehicle) {
            ElectricVehicle ev = (ElectricVehicle) other;
            return Math.abs(this.batteryCapacity - ev.batteryCapacity) <= 5;
        }
        return false;
    }

    @Override
    public int batteryCostShare() {
        return this.batteryCapacity * 25000;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElectricVehicle)) return false;
        if (!super.equals(o)) return false;
        ElectricVehicle that = (ElectricVehicle) o;
        return batteryCapacity == that.batteryCapacity && hasHeatPump == that.hasHeatPump;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), batteryCapacity, hasHeatPump);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(Locale.US,
                " | Battery: %d kWh | Heat Pump: %s",
                batteryCapacity, hasHeatPump ? "Yes" : "No");
    }
}
