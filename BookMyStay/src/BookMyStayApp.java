import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

class BookingHistory{

    private List<Reservation> confirmedReservations;

    public BookingHistory(){
        confirmedReservations = new ArrayList<>();
    }

    public void addReservation(Reservation reservation){
        confirmedReservations.add(reservation);
    }

    public List<Reservation> getConfirmedReservations(){
        return confirmedReservations;
    }
}

class BookingReportService{

    public void generateReport(BookingHistory history){
        List<Reservation> list = history.getConfirmedReservations();
        System.out.print("-- Booking History Report --\n");
        for( Reservation r : list){
            System.out.print("Guest : "+r.getGuestName()+", Room Type : "+r.getRoomType()+"\n");
        }
    }
}

public class BookMyStayApp {
    /*
     * version 8.0
     * author @Keerthi
     */
    public static void main (String[] args) {
        System.out.println("-- Booking History and Reporting --\n");

        BookingHistory history = new BookingHistory();

        history.addReservation(new Reservation("Abhi","Single"));
        history.addReservation(new Reservation("Subha","Double"));
        history.addReservation(new Reservation("Vanmathi","Suite"));

        BookingReportService service = new BookingReportService();

        service.generateReport(history);
    }
}
