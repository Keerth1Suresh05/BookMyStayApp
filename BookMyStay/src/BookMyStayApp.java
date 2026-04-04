import java.util.*;

class AddOnService{

    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost){
        this.serviceName=serviceName;
        this.cost=cost;
    }

    public String getServiceName(){
        return serviceName;
    }

    public double getCost(){
        return cost;
    }
}

class AddOnServiceManager{

    private Map<String,List<AddOnService>> serviceByReservation;


    public AddOnServiceManager(){
        serviceByReservation = new HashMap<>();
    }

    public void addService(String reservationId, AddOnService service){
        serviceByReservation.computeIfAbsent(reservationId,k->new ArrayList<>()).add(service);
    }

    public double calculateTotalService(String reservationId){
        List<AddOnService> services = serviceByReservation.get(reservationId);
        System.out.println(reservationId);

        if(services == null){return 0.0;}

        for(AddOnService s : services){
            System.out.println(s.getServiceName()+"|"+s.getCost());
        }

        System.out.print("Total cost : ");
        return services.stream().mapToDouble(AddOnService::getCost).sum();
    }
}

public class BookMyStayApp {
    /*
     * version 7.0
     * author @Keerthi
     */
    public static void main (String[] args) {
        System.out.println("-- Add On Service selection --");
        AddOnServiceManager addservice = new AddOnServiceManager();

        addservice.addService("r1",new AddOnService("breakfeast",100.0));
        addservice.addService("r1",new AddOnService("spa",1200.0));
        addservice.addService("r1",new AddOnService("valet",120.0));
        System.out.print(addservice.calculateTotalService("r1"));

    }
}
