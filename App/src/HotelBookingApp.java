import java.util.*;

// Reservation (Confirmed Booking)
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
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

    public String getRoomType() {
        return roomType;
    }
}

// Booking History (State Holder)
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    // Read-only access
    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(history);
    }
}

// Reporting Service
class BookingReportService {
    private BookingHistory bookingHistory;

    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    // Display all bookings
    public void displayAllBookings() {
        System.out.println("Booking History:\n");

        for (Reservation res : bookingHistory.getAllReservations()) {
            System.out.println(
                    "Reservation ID: " + res.getReservationId() +
                            ", Guest: " + res.getGuestName() +
                            ", Room Type: " + res.getRoomType()
            );
        }
    }

    // Generate summary report
    public void generateSummaryReport() {
        Map<String, Integer> countByRoomType = new HashMap<>();

        for (Reservation res : bookingHistory.getAllReservations()) {
            countByRoomType.put(
                    res.getRoomType(),
                    countByRoomType.getOrDefault(res.getRoomType(), 0) + 1
            );
        }

        System.out.println("\nBooking Summary Report:");

        for (String type : countByRoomType.keySet()) {
            System.out.println(type + " Rooms Booked: " + countByRoomType.get(type));
        }
    }
}

// Main Class
public class HotelBookingApp {
    public static void main(String[] args) {

        // Step 1: Create booking history
        BookingHistory history = new BookingHistory();

        // Step 2: Add confirmed reservations (from Use Case 6)
        history.addReservation(new Reservation("Single-1", "Abhi", "Single"));
        history.addReservation(new Reservation("Single-2", "Subha", "Single"));
        history.addReservation(new Reservation("Suite-1", "Vanmathi", "Suite"));

        // Step 3: Reporting
        BookingReportService reportService = new BookingReportService(history);

        reportService.displayAllBookings();
        reportService.generateSummaryReport();
    }
}