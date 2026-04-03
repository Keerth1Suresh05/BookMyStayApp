import java.util.*;

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

class Reservation{
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType){
        this.guestName=guestName;
        this.roomType=roomType;
    }

    public String getGuestName(){
        return guestName;
    }

    public String getRoomType(){
        return roomType;
    }
}

class RoomAllocationService{
    private Set<String> allocatedRoomId;

    private Map<String, Set<String>> assignedRoomsByType;

    public RoomAllocationService(){
        this.allocatedRoomId = new HashSet<>();
        this.assignedRoomsByType = new HashMap<>();
    }

    public void allocateRoom(Reservation reservation,RoomInventory inventory){

        String type = reservation.getRoomType().toLowerCase();

        int available = inventory.getRoomAvailability().getOrDefault(type,0);

        if(available>0){
            String roomId = generateRoomId(type);
            allocatedRoomId.add(roomId);
            assignedRoomsByType.computeIfAbsent(type,k->new HashSet<>()).add(roomId);
            inventory.bookRoom(type);
            System.out.println("Processing booking for Guest : "+reservation.getGuestName()+", Room type : "+type+", Room ID : "+roomId);
        }else{
            System.out.println("Processing booking for Guest : "+reservation.getGuestName()+", No "+type+" Room available.");
        }
    }

    private String generateRoomId(String roomType){
        String roomId;
        do{
            roomId = roomType.toUpperCase()+(int)(Math.random()*100);
        }while(allocatedRoomId.contains(roomId));
        return roomId;
    }
}


class BookingRequestQueue{
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue(){
        requestQueue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation){
        requestQueue.offer(reservation);
    }

    public Reservation getNextRequest(){
        return requestQueue.poll();
    }

    public boolean hasPendingRequests(){
        return !requestQueue.isEmpty();
    }
}

public class BookMyStayApp {
    /*
     * version 5.0
     * author @Keerthi
     */
    public static void main (String[] args) {
        System.out.println("-- Booking Request Queue --");
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomAllocationService service = new RoomAllocationService();
        RoomInventory inventory = new RoomInventory();

        Reservation r1 = new Reservation("Abhi","Single");
        Reservation r2 = new Reservation("Subha","Double");
        Reservation r3 = new Reservation("Vanmathi","Suite");

        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);


        while(bookingQueue.hasPendingRequests()){
            Reservation next = bookingQueue.getNextRequest();
            service.allocateRoom(next,inventory);
        }
    }
}
