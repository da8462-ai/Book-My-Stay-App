abstract class Room {
    private String type;
    private int beds;
    private int size; // in square feet
    private double price; // per night

    public Room(String type, int beds, int size, double price) {
        this.type = type;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public String getType() {
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

    public abstract void displayInfo();
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 250, 1500.0);
    }

    @Override
    public void displayInfo() {
        System.out.println("Single Room:");
        System.out.println("Beds: " + getBeds());
        System.out.println("Size: " + getSize() + " sqft");
        System.out.println("Price per night: " + getPrice());
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 400, 2500.0);
    }

    @Override
    public void displayInfo() {
        System.out.println("Double Room:");
        System.out.println("Beds: " + getBeds());
        System.out.println("Size: " + getSize() + " sqft");
        System.out.println("Price per night: " + getPrice());
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 750, 5000.0);
    }

    @Override
    public void displayInfo() {
        System.out.println("Suite Room:");
        System.out.println("Beds: " + getBeds());
        System.out.println("Size: " + getSize() + " sqft");
        System.out.println("Price per night: " + getPrice());
    }
}

public class HotelBookingApp {
    public static void main(String[] args) {
        int singleRoomAvailable = 5;
        int doubleRoomAvailable = 3;
        int suiteRoomAvailable = 2;

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        System.out.println("Hotel Room Initialization\n");

        single.displayInfo();
        System.out.println("Available: " + singleRoomAvailable + "\n");

        doubleRoom.displayInfo();
        System.out.println("Available: " + doubleRoomAvailable + "\n");

        suite.displayInfo();
        System.out.println("Available: " + suiteRoomAvailable + "\n");
    }
}