import java.util.*;

// Enum for Room Types
enum RoomType {
    SINGLE, DOUBLE, SUITE
}

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation
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

// Inventory Service
class InventoryService {
    private Map<RoomType, Integer> availability = new HashMap<>();

    public void setAvailability(RoomType type, int count) {
        availability.put(type, count);
    }

    public int getAvailability(RoomType type) {
        return availability.getOrDefault(type, 0);
    }

    public void decrement(RoomType type) throws InvalidBookingException {
        int current = getAvailability(type);

        // Guard against negative inventory
        if (current <= 0) {
            throw new InvalidBookingException("No rooms available for " + format(type));
        }

        availability.put(type, current - 1);
    }

    private String format(RoomType type) {
        return type.name().charAt(0) + type.name().substring(1).toLowerCase();
    }
}

// Validator (Fail-Fast)
class BookingValidator {

    public static void validate(Reservation reservation, InventoryService inventory)
            throws InvalidBookingException {

        // Validate guest name
        if (reservation.getGuestName() == null || reservation.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        // Validate room type
        if (reservation.getRoomType() == null) {
            throw new InvalidBookingException("Invalid room type selected");
        }

        // Validate availability
        if (inventory.getAvailability(reservation.getRoomType()) <= 0) {
            throw new InvalidBookingException(
                    "No availability for room type: " + reservation.getRoomType()
            );
        }
    }
}

// Booking Service
class BookingService {
    private InventoryService inventory;

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void book(Reservation reservation) {
        try {
            // Step 1: Validate input (Fail-Fast)
            BookingValidator.validate(reservation, inventory);

            // Step 2: Allocate (only if valid)
            inventory.decrement(reservation.getRoomType());

            System.out.println("Booking successful for Guest: " +
                    reservation.getGuestName() +
                    ", Room Type: " + format(reservation.getRoomType()));

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("Booking failed: " + e.getMessage());
        }
    }

    private String format(RoomType type) {
        return type.name().charAt(0) + type.name().substring(1).toLowerCase();
    }
}

// Main Class
public class HotelBookingApp {
    public static void main(String[] args) {

        // Setup inventory
        InventoryService inventory = new InventoryService();
        inventory.setAvailability(RoomType.SINGLE, 1);
        inventory.setAvailability(RoomType.DOUBLE, 0); // No availability

        // Booking service
        BookingService bookingService = new BookingService(inventory);

        // Test cases

        // ✅ Valid booking
        bookingService.book(new Reservation("Abhi", RoomType.SINGLE));

        // ❌ Invalid: No availability
        bookingService.book(new Reservation("Subha", RoomType.DOUBLE));

        // ❌ Invalid: Empty name
        bookingService.book(new Reservation("", RoomType.SINGLE));

        // ❌ Invalid: Null room type
        bookingService.book(new Reservation("Vanmathi", null));
    }
}