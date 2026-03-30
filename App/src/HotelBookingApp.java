import java.io.*;
import java.util.*;

// Enum for Room Types
enum RoomType implements Serializable {
    SINGLE, DOUBLE, SUITE
}

// Reservation class
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String reservationId;
    private String guestName;
    private RoomType roomType;

    public Reservation(String reservationId, String guestName, RoomType roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public RoomType getRoomType() { return roomType; }

    @Override
    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType;
    }
}

// Inventory Service
class InventoryService implements Serializable {
    private static final long serialVersionUID = 1L;
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

    public void increment(RoomType type) {
        availability.put(type, getAvailability(type) + 1);
    }

    public Map<RoomType, Integer> getAll() {
        return availability;
    }
}

// Booking History
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Reservation> activeBookings = new HashMap<>();

    public void add(Reservation res) {
        activeBookings.put(res.getReservationId(), res);
    }

    public Reservation get(String reservationId) {
        return activeBookings.get(reservationId);
    }

    public boolean exists(String reservationId) {
        return activeBookings.containsKey(reservationId);
    }

    public void remove(String reservationId) {
        activeBookings.remove(reservationId);
    }

    public Collection<Reservation> getAll() {
        return activeBookings.values();
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILENAME = "hotel_state.dat";

    public static void saveState(InventoryService inventory, BookingHistory history) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(inventory);
            oos.writeObject(history);
            System.out.println("State saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving state: " + e.getMessage());
        }
    }

    public static Object[] loadState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME))) {
            InventoryService inventory = (InventoryService) ois.readObject();
            BookingHistory history = (BookingHistory) ois.readObject();
            System.out.println("State loaded successfully.");
            return new Object[]{inventory, history};
        } catch (FileNotFoundException e) {
            System.out.println("Persistence file not found. Starting with empty state.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading state: " + e.getMessage());
        }
        return null;
    }
}

// Main Class
public class HotelBookingApp {
    public static void main(String[] args) {

        // Step 1: Attempt to load previous state
        Object[] loadedState = PersistenceService.loadState();
        InventoryService inventory;
        BookingHistory history;

        if (loadedState != null) {
            inventory = (InventoryService) loadedState[0];
            history = (BookingHistory) loadedState[1];
        } else {
            // Initialize new state
            inventory = new InventoryService();
            history = new BookingHistory();

            inventory.setAvailability(RoomType.SINGLE, 2);
            inventory.setAvailability(RoomType.DOUBLE, 1);
            inventory.setAvailability(RoomType.SUITE, 1);

            // Sample reservations
            history.add(new Reservation("Res-1", "Alice", RoomType.SINGLE));
            inventory.decrement(RoomType.SINGLE);

            history.add(new Reservation("Res-2", "Bob", RoomType.DOUBLE));
            inventory.decrement(RoomType.DOUBLE);
        }

        // Display current state
        System.out.println("\n--- Current Inventory ---");
        for (RoomType type : RoomType.values()) {
            System.out.println(type + ": " + inventory.getAvailability(type));
        }

        System.out.println("\n--- Booking History ---");
        for (Reservation res : history.getAll()) {
            System.out.println(res);
        }

        // Step 2: Save state before shutdown
        PersistenceService.saveState(inventory, history);
    }
}