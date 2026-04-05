import java.util.Stack;
import java.util.HashMap;
import java.util.Map;


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

class CancellationService{

    private Stack<String> releasedRoomIds;
    private Map<String,String> reservationRoomTypeMap;

    public CancellationService(){
        releasedRoomIds = new Stack<>();
        reservationRoomTypeMap = new HashMap<>();
    }

    public void registerBooking(String reservationId, String roomType){
        reservationRoomTypeMap.put(reservationId,roomType);
    }

    public void cancelBooking(String reservationId,RoomInventory inventory){
        if(reservationRoomTypeMap.containsKey(reservationId)){
            String roomType = reservationRoomTypeMap.get(reservationId);
            inventory.unbookRoom(roomType);
            releasedRoomIds.push(reservationId);
            reservationRoomTypeMap.remove(reservationId);
            System.out.println("Booking cancelled successfully. Inventory restored for room type : "+roomType+"\n");
        }
    }

    public void showRollbackHistory(){
        System.out.println("\nRollback History (Most Recent First) : ");
        for(int i=releasedRoomIds.size()-1;i>=0;i--){
            System.out.println("Released reservation ID : "+releasedRoomIds.get(i));
        }
    }
}

public class BookMyStayApp {
    /*
     * version 9.0
     * author @Keerthi
     */
    public static void main (String[] args) {
        System.out.println("-- Booking Cancellation --\n");
        CancellationService service = new CancellationService();
        RoomInventory inventory = new RoomInventory();
        service.registerBooking("single-1","single");
        service.registerBooking("single-2","single");
        service.registerBooking("double-1","double");
        service.cancelBooking("single-1",inventory);
        service.cancelBooking("double-1",inventory);
        service.showRollbackHistory();
    }
}
