import java.util.*;

// Enum for Room Types
enum RoomType {
    SINGLE, DOUBLE, SUITE
}

// Reservation (Booking Request)
class Reservation {
    private String guestName;
    private RoomType roomType;

    public Reservation(String guestName, RoomType roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public RoomType getRoomType() {
        return roomType;
    }
}

// Inventory Service (State Holder)
class InventoryService {
    private Map<RoomType, Integer> availability = new HashMap<>();

    public void setAvailability(RoomType type, int count) {
        availability.put(type, count);
    }

    public int getAvailability(RoomType type) {
        return availability.getOrDefault(type, 0);
    }

    public void decrement(RoomType type) {
        availability.put(type, getAvailability(type) - 1);
    }
}

// Booking Service (Allocation Logic)
class BookingService {
    private Queue<Reservation> queue;
    private InventoryService inventory;

    // Track allocated room IDs
    private Map<RoomType, Set<String>> allocatedRooms = new HashMap<>();

    // Counters for room ID generation
    private Map<RoomType, Integer> counters = new HashMap<>();

    public BookingService(Queue<Reservation> queue, InventoryService inventory) {
        this.queue = queue;
        this.inventory = inventory;

        // Initialize maps
        for (RoomType type : RoomType.values()) {
            allocatedRooms.put(type, new HashSet<>());
            counters.put(type, 1);
        }
    }

    public void processBookings() {
        System.out.println("Room Allocation Processing");

        while (!queue.isEmpty()) {
            Reservation res = queue.poll();
            RoomType type = res.getRoomType();

            // Check availability
            if (inventory.getAvailability(type) > 0) {

                // Generate unique Room ID
                String roomId = generateRoomId(type);

                // Ensure uniqueness using Set
                allocatedRooms.get(type).add(roomId);

                // Update inventory immediately
                inventory.decrement(type);

                // Confirm booking
                System.out.println(
                        "Booking confirmed for Guest: " +
                                res.getGuestName() +
                                ", Room ID: " + roomId
                );

            } else {
                System.out.println(
                        "Booking failed for Guest: " +
                                res.getGuestName() +
                                " (No rooms available)"
                );
            }
        }
    }

    private String generateRoomId(RoomType type) {
        int count = counters.get(type);
        counters.put(type, count + 1);

        return formatRoomType(type) + "-" + count;
    }

    private String formatRoomType(RoomType type) {
        return type.name().charAt(0) + type.name().substring(1).toLowerCase();
    }
}

// Main Class
public class HotelBookingApp {
    public static void main(String[] args) {

        // Step 1: Create booking queue (FIFO)
        Queue<Reservation> queue = new LinkedList<>();
        queue.offer(new Reservation("Abhi", RoomType.SINGLE));
        queue.offer(new Reservation("Subha", RoomType.SINGLE));
        queue.offer(new Reservation("Vanmathi", RoomType.SUITE));

        // Step 2: Setup inventory
        InventoryService inventory = new InventoryService();
        inventory.setAvailability(RoomType.SINGLE, 2);
        inventory.setAvailability(RoomType.DOUBLE, 1);
        inventory.setAvailability(RoomType.SUITE, 1);

        // Step 3: Process bookings
        BookingService bookingService = new BookingService(queue, inventory);
        bookingService.processBookings();
    }
}