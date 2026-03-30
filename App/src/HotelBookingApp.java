import java.util.*;
import java.util.concurrent.*;

// Enum for Room Types
enum RoomType {
    SINGLE, DOUBLE, SUITE
}

// Reservation Request
class ReservationRequest {
    private String guestName;
    private RoomType roomType;

    public ReservationRequest(String guestName, RoomType roomType) {
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

// Inventory Service (Thread-Safe)
class InventoryService {
    private final Map<RoomType, Integer> availability = new HashMap<>();

    public synchronized void setAvailability(RoomType type, int count) {
        availability.put(type, count);
    }

    public synchronized int getAvailability(RoomType type) {
        return availability.getOrDefault(type, 0);
    }

    public synchronized boolean allocate(RoomType type) {
        int available = getAvailability(type);
        if (available > 0) {
            availability.put(type, available - 1);
            return true;
        }
        return false;
    }
}

// Booking Service (Processes Requests)
class BookingService implements Runnable {
    private final ReservationRequest request;
    private final InventoryService inventory;

    public BookingService(ReservationRequest request, InventoryService inventory) {
        this.request = request;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        boolean allocated = false;

        // Critical Section: synchronize on inventory
        synchronized (inventory) {
            if (inventory.allocate(request.getRoomType())) {
                allocated = true;
            }
        }

        if (allocated) {
            System.out.println("Booking SUCCESS for Guest: " + request.getGuestName()
                    + ", Room: " + request.getRoomType());
        } else {
            System.out.println("Booking FAILED (No availability) for Guest: "
                    + request.getGuestName() + ", Room: " + request.getRoomType());
        }
    }
}

// Main Class
public class HotelBookingApp {
    public static void main(String[] args) throws InterruptedException {

        // Step 1: Setup inventory
        InventoryService inventory = new InventoryService();
        inventory.setAvailability(RoomType.SINGLE, 2);
        inventory.setAvailability(RoomType.DOUBLE, 1);
        inventory.setAvailability(RoomType.SUITE, 1);

        // Step 2: Create multiple booking requests (simulate multiple guests)
        List<ReservationRequest> requests = Arrays.asList(
                new ReservationRequest("Guest1", RoomType.SINGLE),
                new ReservationRequest("Guest2", RoomType.SINGLE),
                new ReservationRequest("Guest3", RoomType.SINGLE), // Should fail
                new ReservationRequest("Guest4", RoomType.DOUBLE),
                new ReservationRequest("Guest5", RoomType.SUITE),
                new ReservationRequest("Guest6", RoomType.SUITE)  // Should fail
        );

        // Step 3: Use ExecutorService for concurrent processing
        ExecutorService executor = Executors.newFixedThreadPool(4);

        for (ReservationRequest req : requests) {
            executor.submit(new BookingService(req, inventory));
        }

        // Step 4: Shutdown executor gracefully
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("\nFinal Inventory:");
        for (RoomType type : RoomType.values()) {
            System.out.println(type + ": " + inventory.getAvailability(type));
        }
    }
}