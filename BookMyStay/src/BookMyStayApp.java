import java.util.Map;
import java.util.HashMap;

class RoomInventory{
    private Map<String,Integer> roomAvailability;

    public RoomInventory(){
        initializeInventory();
    }

    public void initializeInventory(){
        roomAvailability= new HashMap<>();
        roomAvailability.put("single",6);
        roomAvailability.put("double",4);
        roomAvailability.put("suite",2);
    }

    public Map<String,Integer> getRoomAvailability(){
        return roomAvailability;
    }

    public void updateAvailability(String roomType,int count){
        roomAvailability.put(roomType,count);
    }

    public void bookRoom(String roomType){
        int available = roomAvailability.getOrDefault(roomType,0);
        roomAvailability.put(roomType,available-1);
    }
    public void unbookRoom(String roomType){
        int available = roomAvailability.getOrDefault(roomType,0);
        roomAvailability.put(roomType,available+1);
    }
}

abstract class Room{
    protected int noOfBeds;
    protected int squareFeet;
    protected double pricePerNight;
    RoomInventory inventory = new RoomInventory();

    public Room(int noOfBeds,int squareFeet,double pricePerNight){
        this.noOfBeds=noOfBeds;
        this.squareFeet=squareFeet;
        this.pricePerNight=pricePerNight;
    }
    public void displayRoomDetails(){
        System.out.println("----Room details----");
        System.out.println("Number of bed in room : "+noOfBeds+"\ntotal room size : "+squareFeet+" sq ft\ncost per night : Rs."+pricePerNight);
    }
}

class SingleRoom extends Room{
    public static int availability = 5;
    public SingleRoom(){
        super(1,25,1500.0);
    }

    @Override
    public void displayRoomDetails() {
        System.out.println("\n- Single Room -");
        super.displayRoomDetails();
        System.out.println("Availability : "+inventory.getRoomAvailability().get("single"));
    }
}

class DoubleRoom extends Room{
    public static int availability = 3;
    public DoubleRoom(){
        super(2,400,2500.0);
    }
    @Override
    public void displayRoomDetails() {
        System.out.println("\n- Double Room -");
        super.displayRoomDetails();
        System.out.println("Availability : "+inventory.getRoomAvailability().get("double"));
    }
}

class SuiteRoom extends Room{
    public static int availability = 2;
    public SuiteRoom(){
        super(3,750,5000.0);
    }
    @Override
    public void displayRoomDetails() {
        System.out.println("\n- Suite Room -");
        super.displayRoomDetails();
        System.out.println("Availability : "+inventory.getRoomAvailability().get("suite"));
    }
}

public class BookMyStayApp {
    /*
     * version 3.0
     * author @Keerthi
     */
    public static void main (String[] args) {
        SingleRoom singleRoom = new SingleRoom();
        DoubleRoom doubleRoom = new DoubleRoom();
        SuiteRoom suiteRoom = new SuiteRoom();
        singleRoom.displayRoomDetails();
        doubleRoom.displayRoomDetails();
        suiteRoom.displayRoomDetails();
    }
}
