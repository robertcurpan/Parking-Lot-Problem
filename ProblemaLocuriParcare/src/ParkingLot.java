import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class ParkingLot
{
    private int _smallDimension;
    private int _mediumDimension;
    private int _largeDimension;
    private PriorityQueue<Integer>[] _parkingSpots;
    private HashMap<Integer, Driver> _drivers;

    public ParkingLot(int smallDimension, int mediumDimension, int largeDimension)
    {
        _smallDimension = smallDimension;
        _mediumDimension = mediumDimension;
        _largeDimension = largeDimension;

        _parkingSpots = new PriorityQueue[3];
        _parkingSpots[0] = new PriorityQueue<Integer>(_smallDimension);
        _parkingSpots[1] = new PriorityQueue<Integer>(_mediumDimension);
        _parkingSpots[2] = new PriorityQueue<Integer>(_largeDimension);
        _drivers = new HashMap<Integer, Driver>();

        // Initially, all the spots from the parking lot are empty
        // Small -> [0, _smallDimension - 1]
        // Medium -> [_smallDimension, _smallDimension + _mediumDimension - 1]
        // Large -> [_smallDimension + _mediumDimension, _smallDimension + _mediumDimension + _largeDimension - 1]
        for(int i = 0; i < _smallDimension; ++i)
            _parkingSpots[0].add(i);
        for(int i = _smallDimension; i < _smallDimension + _mediumDimension; ++i)
            _parkingSpots[1].add(i);
        for(int i = _smallDimension + _mediumDimension; i < _smallDimension + _mediumDimension + _largeDimension; ++i)
            _parkingSpots[2].add(i);
    }

    // INSERT ( O(log n) )
    public String getParkingTicket(Driver d)
    {
        String ans = "-";

        // Check if the driver is VIP
        boolean isVIP = d.getVipStatus();

        // Check if there is any spot left in the driver's category (in minheap we store the id of the empty spots)
        int vehicleTypeId = d.getVehicleType().ordinal();
        if(_parkingSpots[vehicleTypeId].size() != 0)        // We have an empty spot for this driver
        {
            try
            {
                Integer idSpot = _parkingSpots[vehicleTypeId].peek();
                _parkingSpots[vehicleTypeId].poll();

                // Add the driver in the hashMap in order to map him to the id of the parking spot
                _drivers.put(idSpot, d);


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
                    if(_parkingSpots[vehicleTypeId].size() != 0)
                    {
                        Integer idSpot = _parkingSpots[vehicleTypeId].peek();
                        _parkingSpots[vehicleTypeId].poll();

                        // Add the driver in the hashMap in order to map him to the id of the parking spot
                        _drivers.put(idSpot, d);

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
        if(!_drivers.containsKey(idParkingSpot))
            return "Locul cu id-ul " + idParkingSpot.toString() + " nu este ocupat!";

        String ans = "-";

        // Add the place that has been freed
        if(idParkingSpot >= 0 && idParkingSpot < _smallDimension)
            _parkingSpots[0].add(idParkingSpot);
        else if(idParkingSpot >= _smallDimension && idParkingSpot < _smallDimension + _mediumDimension)
            _parkingSpots[1].add(idParkingSpot);
        else if(idParkingSpot >= _smallDimension + _mediumDimension && idParkingSpot < _smallDimension + _mediumDimension + _largeDimension)
            _parkingSpots[2].add(idParkingSpot);

        Driver dr = _drivers.get(idParkingSpot);
        _drivers.remove(idParkingSpot);
        ans = "The driver: " + dr.toString() + " has left the parking lot (he was on spot: " + idParkingSpot + ")";

        return ans;
    }

    public String showOccupiedSpots()
    {
        StringBuilder ans = new StringBuilder();
        ans.append("\r\n");
        for(HashMap.Entry<Integer, Driver> entry : _drivers.entrySet())
        {
            ans.append(entry.getValue().toString()).append(" -> parking slot: ").append(entry.getKey().toString()).append("\r\n");
        }

        ans.append("\r\n");
        ans.append("-----> Number of free parking spots left: \r\n");
        ans.append("Motorcycle: ").append(_parkingSpots[0].size()).append(" free spots.\r\n");
        ans.append("Car: ").append(_parkingSpots[1].size()).append(" free spots.\r\n");
        ans.append("Truck: ").append(_parkingSpots[2].size()).append(" free spots.\r\n");

        return ans.toString();
    }

}
