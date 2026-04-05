import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;

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
    }
    public void unbookRoom(String roomType){
        int available = roomAvailability.getOrDefault(roomType,0);
        roomAvailability.put(roomType,available+1);
    }
}

class InvalidBookingException extends Exception{

    public InvalidBookingException(String message){
        super(message);
    }
}

class ReservationValidator{

    public void validate(String guestName,String roomType,RoomInventory inventory) throws InvalidBookingException{

        if(guestName==null || guestName.trim().isEmpty()){
            throw new InvalidBookingException("Guest name cannot be empty.");
        }
        if(roomType==null || roomType.trim().isEmpty()){
            throw new InvalidBookingException("Room type cannot be empty");
        }
        Map<String, Integer> availability = inventory.getRoomAvailability();

        if(!availability.containsKey(roomType)){
            throw new InvalidBookingException("Invalid room type : "+roomType);
        }

        int count = availability.get(roomType);
        if (count<=0){
            throw new InvalidBookingException("No "+roomType+" rooms available.");
        }
    }
}

public class BookMyStayApp {
    /*
     * version 9.0
     * author @Keerthi
     */
    public static void main (String[] args) {
        System.out.println("-- Booking Validation --\n");
        Scanner scn = new Scanner(System.in);

        RoomInventory inventory = new RoomInventory();
        ReservationValidator validator = new ReservationValidator();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        try{
            System.out.print("Enter guest name : ");
            String guestName = scn.nextLine();
            System.out.print("Enter room type (Single/Double/Suite) : ");
            String roomType = scn.next();

            validator.validate(guestName,roomType,inventory);

            Reservation reservation = new Reservation(guestName,roomType);
            bookingQueue.addRequest(reservation);
            System.out.println("Successfully reserved "+roomType+" Room, for "+guestName);
        }catch (InvalidBookingException e) {
            System.out.println("Booking failed: "+e.getMessage());
        }finally {
            scn.close();
        }
    }
}
