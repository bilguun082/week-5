import java.util.Locale;
import java.util.Objects;

public class Vehicle implements VehicleInterface, Comparable<Vehicle> {
    protected String brand;
    protected String model;
    protected double basePrice;
    protected int age;
    protected double km;
    protected FuelType fuelType;

    public Vehicle() {
        this("", "", 0.0, 0, 0.0, FuelType.PETROL);
    }

    public Vehicle(String brand, String model, double basePrice, int age, double km, FuelType fuelType) {
        this.brand = brand != null ? brand : "";
        this.model = model != null ? model : "";
        this.basePrice = basePrice;
        this.age = age;
        this.km = km;
        this.fuelType = fuelType;
    }

    public Vehicle(String brandAndModel, double basePrice, int age, double km, FuelType fuelType) {
        this.basePrice = basePrice;
        this.age = age;
        this.km = km;
        this.fuelType = fuelType;
        if (brandAndModel != null && brandAndModel.contains(" ")) {
            int firstSpace = brandAndModel.indexOf(' ');
            this.brand = brandAndModel.substring(0, firstSpace).trim();
            this.model = brandAndModel.substring(firstSpace + 1).trim();
        } else {
            this.brand = brandAndModel != null ? brandAndModel : "";
            this.model = "";
        }
    }

    // Getters and Setters
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getKm() {
        return km;
    }

    public void setKm(double km) {
        this.km = km;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    @Override
    public void applyDiscount(int percentage) {
        this.basePrice = this.basePrice * (1.0 - percentage / 100.0);
    }

    @Override
    public int getFinalPrice() {
        double calculatedPrice = basePrice - (age * 150000.0);
        if (calculatedPrice < 200000.0) {
            calculatedPrice = 200000.0;
        }

        if (fuelType == FuelType.HYBRID) {
            calculatedPrice *= 1.15;
        } else if (fuelType == FuelType.DIESEL) {
            calculatedPrice *= 1.05;
        }
        // PETROL: price remains unchanged

        return (int) Math.round(calculatedPrice);
    }

    @Override
    public double averageKmPerYear() {
        if (age == 0) {
            return km;
        }
        return km / age;
    }

    @Override
    public int compareTo(Vehicle other) {
        if (this == other) {
            return 0;
        }
        if (other == null) {
            return 1;
        }
        // Primary ordering by final price (ascending)
        int priceComparison = Integer.compare(this.getFinalPrice(), other.getFinalPrice());
        if (priceComparison != 0) {
            return priceComparison;
        }
        // Secondary tie-breaker so TreeSet does not discard distinct vehicles with identical final price
        int brandComparison = (this.brand != null ? this.brand : "").compareTo(other.brand != null ? other.brand : "");
        if (brandComparison != 0) {
            return brandComparison;
        }
        int modelComparison = (this.model != null ? this.model : "").compareTo(other.model != null ? other.model : "");
        if (modelComparison != 0) {
            return modelComparison;
        }
        int kmComparison = Double.compare(this.km, other.km);
        if (kmComparison != 0) {
            return kmComparison;
        }
        return Integer.compare(this.age, other.age);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;
        Vehicle vehicle = (Vehicle) o;
        return Double.compare(vehicle.basePrice, basePrice) == 0 &&
                age == vehicle.age &&
                Double.compare(vehicle.km, km) == 0 &&
                Objects.equals(brand, vehicle.brand) &&
                Objects.equals(model, vehicle.model) &&
                fuelType == vehicle.fuelType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, model, basePrice, age, km, fuelType);
    }

    @Override
    public String toString() {
        String fullName = (model == null || model.isEmpty()) ? brand : (brand + " " + model);
        return String.format(Locale.US,
                "Vehicle: %s | Base Price: %,.0f | Age: %d yrs | Mileage: %,.0f km | Fuel: %s | Final Price: %,d | Avg Annual Mileage: %,.2f km/yr",
                fullName, basePrice, age, km, fuelType, getFinalPrice(), averageKmPerYear());
    }
}
