import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class ParkingLot
{
    private final int smallDimension;
    private final int mediumDimension;
    private final int largeDimension;
    private PriorityQueue<Integer>[] parkingSpots;
    private HashMap<Integer, Driver> drivers;

    public ParkingLot(int smallDimension, int mediumDimension, int largeDimension)
    {
        this.smallDimension = smallDimension;
        this.mediumDimension = mediumDimension;
        this.largeDimension = largeDimension;

        parkingSpots = new PriorityQueue[3];
        parkingSpots[0] = new PriorityQueue<Integer>(smallDimension);
        parkingSpots[1] = new PriorityQueue<Integer>(mediumDimension);
        parkingSpots[2] = new PriorityQueue<Integer>(largeDimension);
        drivers = new HashMap<Integer, Driver>();

        // Initially, all the spots from the parking lot are empty
        // Small -> [0, smallDimension - 1]
        // Medium -> [smallDimension, smallDimension + mediumDimension - 1]
        // Large -> [smallDimension + mediumDimension, smallDimension + mediumDimension + largeDimension - 1]
        for(int i = 0; i < smallDimension; ++i)
            parkingSpots[0].add(i);
        for(int i = smallDimension; i < smallDimension + mediumDimension; ++i)
            parkingSpots[1].add(i);
        for(int i = smallDimension + mediumDimension; i < smallDimension + mediumDimension + largeDimension; ++i)
            parkingSpots[2].add(i);
    }

    // INSERT ( O(log n) )
    public String getParkingTicket(Driver d)
    {
        String ans = "-";

        // Check if the driver is VIP
        boolean isVIP = d.getVipStatus();

        // Check if there is any spot left in the driver's category (in minheap we store the id of the empty spots)
        int vehicleTypeId = d.getVehicleType().ordinal();
        if(parkingSpots[vehicleTypeId].size() != 0)        // We have an empty spot for this driver
        {
            try
            {
                Integer idSpot = parkingSpots[vehicleTypeId].peek();
                parkingSpots[vehicleTypeId].poll();

                // Add the driver in the hashMap in order to map him to the id of the parking spot
                drivers.put(idSpot, d);


                ans = "The driver " + d.toString() + " received the following parking slot: " + idSpot.toString();
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }

        }
        else        // There is no empty spot in this category. If the driver is VIP, we will check in the upper category
        {
            if(isVIP && vehicleTypeId < 2)
            {
                ++vehicleTypeId;
                boolean foundSpot = false;

                while(vehicleTypeId < 3 && !foundSpot)
                {
                    if(parkingSpots[vehicleTypeId].size() != 0)
                    {
                        Integer idSpot = parkingSpots[vehicleTypeId].peek();
                        parkingSpots[vehicleTypeId].poll();

                        // Add the driver in the hashMap in order to map him to the id of the parking spot
                        drivers.put(idSpot, d);

                        ans = "The driver " + d.toString() + " received the following parking slot: " + idSpot.toString();
                        foundSpot = true;
                    }
                    ++vehicleTypeId;
                }

                if(!foundSpot)
                    ans = "The driver " + d.toString() + " does not have an empty parking spot!";
            }
            else
            {
                ans = "The driver " + d.toString() + " does not have an empty parking spot!";
            }
        }

        return ans;
    }

    // DELETE ( O(log n) )
    public String leaveParkingLot(Integer idParkingSpot)
    {
        if(!drivers.containsKey(idParkingSpot))
            return "Locul cu id-ul " + idParkingSpot.toString() + " nu este ocupat!";

        String ans = "-";

        // Add the place that has been freed
        if(idParkingSpot >= 0 && idParkingSpot < smallDimension)
            parkingSpots[0].add(idParkingSpot);
        else if(idParkingSpot >= smallDimension && idParkingSpot < smallDimension + mediumDimension)
            parkingSpots[1].add(idParkingSpot);
        else if(idParkingSpot >= smallDimension + mediumDimension && idParkingSpot < smallDimension + mediumDimension + largeDimension)
            parkingSpots[2].add(idParkingSpot);

        Driver dr = drivers.get(idParkingSpot);
        drivers.remove(idParkingSpot);
        ans = "The driver: " + dr.toString() + " has left the parking lot (he was on spot: " + idParkingSpot + ")";

        return ans;
    }

    public String showOccupiedSpots()
    {
        StringBuilder ans = new StringBuilder();
        ans.append("\r\n");
        for(HashMap.Entry<Integer, Driver> entry : drivers.entrySet())
        {
            ans.append(entry.getValue().toString()).append(" -> parking slot: ").append(entry.getKey().toString()).append("\r\n");
        }

        ans.append("\r\n");
        ans.append("-----> Number of free parking spots left: \r\n");
        ans.append("Motorcycle: ").append(parkingSpots[0].size()).append(" free spots.\r\n");
        ans.append("Car: ").append(parkingSpots[1].size()).append(" free spots.\r\n");
        ans.append("Truck: ").append(parkingSpots[2].size()).append(" free spots.\r\n");

        return ans.toString();
    }

}
