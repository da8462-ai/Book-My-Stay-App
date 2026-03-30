import java.util.*;

// Add-On Service (Domain Object)
class AddOnService {
    private String name;
    private double price;

    public AddOnService(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // Map<ReservationID, List of Services>
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    // Add service to a reservation
    public void addService(String reservationId, AddOnService service) {
        serviceMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
    }

    // Get services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return serviceMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total cost of services
    public double calculateTotalCost(String reservationId) {
        double total = 0.0;

        for (AddOnService service : getServices(reservationId)) {
            total += service.getPrice();
        }

        return total;
    }

    // Display services
    public void displayServices(String reservationId) {
        System.out.println("Add-On Services for Reservation: " + reservationId);

        List<AddOnService> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        for (AddOnService service : services) {
            System.out.println(service.getName() + " - " + service.getPrice());
        }

        System.out.println("Total Add-On Cost: " + calculateTotalCost(reservationId));
    }
}

// Main Class
public class HotelBookingApp {
    public static void main(String[] args) {

        // Example reservation IDs (from Use Case 6)
        String res1 = "Single-1";
        String res2 = "Suite-1";

        // Create Add-On Services
        AddOnService breakfast = new AddOnService("Breakfast", 500.0);
        AddOnService wifi = new AddOnService("WiFi", 200.0);
        AddOnService spa = new AddOnService("Spa", 1500.0);

        // Service Manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Add services to reservations
        manager.addService(res1, breakfast);
        manager.addService(res1, wifi);

        manager.addService(res2, spa);

        // Display services
        manager.displayServices(res1);
        System.out.println();
        manager.displayServices(res2);
    }
}