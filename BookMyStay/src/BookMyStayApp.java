abstract class Room{
    protected int noOfBeds;
    protected int squareFeet;
    protected double pricePerNight;

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
        System.out.println("- Single Room -");
        super.displayRoomDetails();
        System.out.println("Availablity : "+availability);
    }
}

class DoubleRoom extends Room{
    public static int availability = 3;
    public DoubleRoom(){
        super(2,400,2500.0);
    }
    @Override
    public void displayRoomDetails() {
        System.out.println("- Double Room -");
        super.displayRoomDetails();
        System.out.println("Availablity : "+availability);
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
        System.out.println("Availablity : "+availability);
    }
}

public class BookMyStayApp {
    /*
     * version 2.0
     * author @Keerthi
     */
    public static void main (String[] args) {
        SingleRoom sr1 = new SingleRoom();
        DoubleRoom dr1 = new DoubleRoom();
        SuiteRoom su1 = new SuiteRoom();

        sr1.displayRoomDetails();
        dr1.displayRoomDetails();
        su1.displayRoomDetails();
    }
}
