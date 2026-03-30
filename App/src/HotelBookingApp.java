import java.util.*;

// Enum for Room Types
enum RoomType {
    SINGLE, DOUBLE, SUITE
}

// Room Domain Model (Read-only usage)
class Room {
    private RoomType type;
    private int beds;
    private int size;
    private double price;

    public Room(RoomType type, int beds, int size, double price) {
        this.type = type;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public RoomType getType() {
        return type;
    }

    public int getBeds() {
        return beds;
    }

    public int getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }
}

// Inventory (State Holder - Read Only in Search)
class Inventory {
    private Map<RoomType, Integer> availability = new HashMap<>();

    public void setAvailability(RoomType type, int count) {
        availability.put(type, count);
    }

    public int getAvailability(RoomType type) {
        return availability.getOrDefault(type, 0);
    }

    public Map<RoomType, Integer> getAllAvailability() {
        return Collections.unmodifiableMap(availability); // defensive (read-only)
    }
}

// Search Service (Read-only logic)
class SearchService {
    private Inventory inventory;
    private Map<RoomType, Room> roomCatalog;

    public SearchService(Inventory inventory, Map<RoomType, Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    public void displayAvailableRooms() {
        System.out.println("Room Search\n");

        for (RoomType type : roomCatalog.keySet()) {
            int available = inventory.getAvailability(type);

            // Validation: Only show available rooms
            if (available > 0) {
                Room room = roomCatalog.get(type);

                System.out.println(formatRoomType(type) + " Room:");
                System.out.println("Beds: " + room.getBeds());
                System.out.println("Size: " + room.getSize() + " sqft");
                System.out.println("Price per night: " + room.getPrice());
                System.out.println("Available: " + available);
                System.out.println();
            }
        }
    }

    private String formatRoomType(RoomType type) {
        return type.name().charAt(0) + type.name().substring(1).toLowerCase();
    }
}

// Main Class
public class HotelBookingApp {
    public static void main(String[] args) {

        // Step 1: Setup Room Catalog (Domain Data)
        Map<RoomType, Room> roomCatalog = new HashMap<>();
        roomCatalog.put(RoomType.SINGLE, new Room(RoomType.SINGLE, 1, 250, 1500.0));
        roomCatalog.put(RoomType.DOUBLE, new Room(RoomType.DOUBLE, 2, 400, 2500.0));
        roomCatalog.put(RoomType.SUITE, new Room(RoomType.SUITE, 3, 750, 5000.0));

        // Step 2: Setup Inventory (State Holder)
        Inventory inventory = new Inventory();
        inventory.setAvailability(RoomType.SINGLE, 5);
        inventory.setAvailability(RoomType.DOUBLE, 3);
        inventory.setAvailability(RoomType.SUITE, 2);

        // Step 3: Perform Search (Read-only operation)
        SearchService searchService = new SearchService(inventory, roomCatalog);
        searchService.displayAvailableRooms();
    }
}