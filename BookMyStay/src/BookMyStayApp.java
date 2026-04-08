import java.util.*;
import java.io.*;

class FilePersistenceService{

    public void saveInventory(RoomInventory inventory,String filepath){
        try(PrintWriter writer = new PrintWriter(new FileWriter(filepath))){
            Map<String, Integer> availability = inventory.getRoomAvailability();

            for(Map.Entry<String, Integer> entry : availability.entrySet()){
                writer.println(entry.getKey()+" : "+entry.getValue() );
            }
            System.out.println("Inventory saved successfully.");
        }catch (IOException e){
            System.out.println("Error saving inventory.");
        }
    }

    public void loadInventory(RoomInventory inventory,String filepath){
        File file = new File(filepath);
        System.out.println("System Recovery");

        if(!file.exists()){
            System.out.println("No valid inventory data found. Starting fresh.");
            return;
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = reader.readLine())!=null){
                String[] part = line.split(":");
                if (part.length==2){
                    inventory.updateAvailability(part[0].toLowerCase(),Integer.parseInt(part[1]));
                }
            }
        }catch (IOException | NumberFormatException e){
            System.out.println("Error loading data. Staring fresh.");
        }
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
        System.out.println("updated "+roomType+" Room availability: "+roomAvailability.get(roomType));
    }
    public void unbookRoom(String roomType){
        int available = roomAvailability.getOrDefault(roomType,0);
        roomAvailability.put(roomType,available+1);
        System.out.println("updated "+roomType+" Room availability: "+roomAvailability.get(roomType));

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
            System.out.println("Processing booking for Guest : "+reservation.getGuestName()+", Room type : "+type+", Room ID : "+roomId+"\nBooking confirmed.");
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

class ConcurrentBookingProcessor implements Runnable{

    private BookingRequestQueue bookingQueue;
    private RoomInventory inventory;
    private RoomAllocationService allocationService;

    public ConcurrentBookingProcessor(BookingRequestQueue bookingQueue,RoomInventory inventory,RoomAllocationService allocationService){
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.allocationService = allocationService;
    }

    @Override
    public void run(){
        while (true){
            Reservation reservation;

            synchronized (bookingQueue){
                if(bookingQueue.hasPendingRequests()){
                    reservation = bookingQueue.getNextRequest();
                }else {
                    break;
                }

            }

            synchronized (inventory){
                allocationService.allocateRoom(reservation,inventory);
            }

            try{
                Thread.sleep(100);
            }catch (InterruptedException e){
                break;
            }
        }
    }
}

public class BookMyStayApp {
    /*
     * version 12.0
     * author @Keerthi
     */
    public static void main (String[] args) {
        System.out.println("-- Concurrent Booking Simulation --\n");
        String filepath = "inventory.txt";
        RoomInventory inventory = new RoomInventory();
        FilePersistenceService fps = new FilePersistenceService();

        fps.loadInventory(inventory,filepath);

        inventory.bookRoom("single");
        inventory.bookRoom("double");

        System.out.println("\nCurrent inventory :");
        Map<String,Integer> currentMap= inventory.getRoomAvailability();
        String[] types = {"single","double","suite"};
        for(String type : types){
            String capitalized = type.substring(0, 1).toUpperCase() + type.substring(1);
            System.out.println(capitalized + ": " + currentMap.get(type));
        }

        fps.saveInventory(inventory, filepath);

    }
}
