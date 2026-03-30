import java.util.*;

// Enum for Room Types
enum RoomType {
    SINGLE, DOUBLE, SUITE
}

// Reservation (Represents booking request)
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

// Booking Request Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        this.queue = new LinkedList<>();
    }

    // Add request to queue
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
    }

    // Process requests in FIFO order
    public void processRequests() {
        System.out.println("Booking Request Queue");

        while (!queue.isEmpty()) {
            Reservation res = queue.poll();

            System.out.println(
                    "Processing booking for Guest: " +
                            res.getGuestName() +
                            ", Room Type: " +
                            formatRoomType(res.getRoomType())
            );
        }
    }

    private String formatRoomType(RoomType type) {
        return type.name().charAt(0) + type.name().substring(1).toLowerCase();
    }
}

// Main Class
public class HotelBookingApp {
    public static void main(String[] args) {

        // Step 1: Create Queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Step 2: Add booking requests (arrival order)
        bookingQueue.addRequest(new Reservation("Abhi", RoomType.SINGLE));
        bookingQueue.addRequest(new Reservation("Subha", RoomType.DOUBLE));
        bookingQueue.addRequest(new Reservation("Vanmathi", RoomType.SUITE));

        // Step 3: Process requests (FIFO)
        bookingQueue.processRequests();
    }
}