import java.util.*;

// Enum for Room Types
enum RoomType {
    SINGLE, DOUBLE, SUITE
}

// Reservation (Confirmed Booking)
class Reservation {
    private String reservationId;
    private String guestName;
    private RoomType roomType;

    public Reservation(String reservationId, String guestName, RoomType roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public RoomType getRoomType() {
        return roomType;
    }
}

// Inventory Service
class InventoryService {
    private Map<RoomType, Integer> availability = new HashMap<>();

    public void setAvailability(RoomType type, int count) {
        availability.put(type, count);
    }

    public int getAvailability(RoomType type) {
        return availability.getOrDefault(type, 0);
    }

    public void increment(RoomType type) {
        availability.put(type, getAvailability(type) + 1);
    }
}

// Booking History (Tracks Active Bookings)
class BookingHistory {
    private Map<String, Reservation> activeBookings = new HashMap<>();

    public void add(Reservation res) {
        activeBookings.put(res.getReservationId(), res);
    }

    public Reservation get(String reservationId) {
        return activeBookings.get(reservationId);
    }

    public void remove(String reservationId) {
        activeBookings.remove(reservationId);
    }

    public boolean exists(String reservationId) {
        return activeBookings.containsKey(reservationId);
    }
}

// Cancellation Service (Rollback Logic)
class CancellationService {
    private BookingHistory history;
    private InventoryService inventory;

    // Stack for rollback tracking (LIFO)
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(BookingHistory history, InventoryService inventory) {
        this.history = history;
        this.inventory = inventory;
    }

    public void cancel(String reservationId) {

        // Validation: Check existence
        if (!history.exists(reservationId)) {
            System.out.println("Cancellation failed: Reservation not found -> " + reservationId);
            return;
        }

        // Fetch reservation
        Reservation res = history.get(reservationId);

        // Step 1: Push to rollback stack
        rollbackStack.push(reservationId);

        // Step 2: Restore inventory
        inventory.increment(res.getRoomType());

        // Step 3: Remove booking from history
        history.remove(reservationId);

        // Confirmation
        System.out.println("Cancellation successful for Reservation ID: " + reservationId);
    }

    // Optional: View rollback history
    public void showRollbackStack() {
        System.out.println("\nRollback Stack (Most Recent First): " + rollbackStack);
    }
}

// Main Class
public class HotelBookingApp {
    public static void main(String[] args) {

        // Step 1: Setup inventory
        InventoryService inventory = new InventoryService();
        inventory.setAvailability(RoomType.SINGLE, 0);
        inventory.setAvailability(RoomType.DOUBLE, 1);

        // Step 2: Setup booking history (from previous use case)
        BookingHistory history = new BookingHistory();
        history.add(new Reservation("Single-1", "Abhi", RoomType.SINGLE));
        history.add(new Reservation("Double-1", "Subha", RoomType.DOUBLE));

        // Step 3: Cancellation service
        CancellationService cancellationService = new CancellationService(history, inventory);

        // Step 4: Perform cancellations

        // ✅ Valid cancellation
        cancellationService.cancel("Single-1");

        // ❌ Invalid cancellation (already removed)
        cancellationService.cancel("Single-1");

        // ❌ Invalid cancellation (non-existent)
        cancellationService.cancel("Suite-1");

        // View rollback stack
        cancellationService.showRollbackStack();
    }
}