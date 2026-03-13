import java.util.HashMap;
import java.util.Map;

// Class to manage centralized room inventory
class RoomInventory {
    // HashMap to store room type as key and available count as value
    private HashMap<String, Integer> roomAvailability;

    // Constructor to initialize inventory
    public RoomInventory() {
        roomAvailability = new HashMap<>();
    }

    // Method to register a new room type with initial count
    public void registerRoomType(String roomType, int count) {
        if (roomAvailability.containsKey(roomType)) {
            System.out.println("Room type already exists: " + roomType);
        } else {
            roomAvailability.put(roomType, count);
            System.out.println("Registered room type: " + roomType + " with count: " + count);
        }
    }

    // Method to retrieve availability for a specific room type
    public int getAvailability(String roomType) {
        return roomAvailability.getOrDefault(roomType, 0);
    }

    // Method to update room availability (increment or decrement)
    public void updateAvailability(String roomType, int change) {
        if (roomAvailability.containsKey(roomType)) {
            int newCount = roomAvailability.get(roomType) + change;
            if (newCount < 0) {
                System.out.println("Error: Cannot reduce below zero for room type: " + roomType);
            } else {
                roomAvailability.put(roomType, newCount);
                System.out.println("Updated " + roomType + " availability to: " + newCount);
            }
        } else {
            System.out.println("Room type not found: " + roomType);
        }
    }

    // Method to display current inventory state
    public void displayInventory() {
        System.out.println("Current Room Inventory:");
        for (Map.Entry<String, Integer> entry : roomAvailability.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() + " | Available: " + entry.getValue());
        }
    }
}

// Main class to run Use Case 3
public class HotelBookingApp {
    public static void main(String[] args) {
        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Register room types
        inventory.registerRoomType("Single", 10);
        inventory.registerRoomType("Double", 5);
        inventory.registerRoomType("Suite", 2);

        // Display initial inventory
        inventory.displayInventory();

        // Update availability
        inventory.updateAvailability("Single", -3);  // 3 rooms booked
        inventory.updateAvailability("Double", 2);   // 2 rooms added
        inventory.updateAvailability("Suite", -1);   // 1 room booked

        // Display updated inventory
        inventory.displayInventory();

        // Check availability of a specific room type
        System.out.println("Available Single rooms: " + inventory.getAvailability("Single"));
    }
}