import java.util.ArrayList;

public enum ParkingSpotType
{
    Small, Medium, Large;

    public static int getParkingSpotType(int idParkingSpot, ArrayList<Integer> dimensions)
    {
        int smallDimension = dimensions.get(ParkingSpotType.Small.ordinal());
        int mediumDimension = dimensions.get(ParkingSpotType.Medium.ordinal());
        int largeDimension = dimensions.get(ParkingSpotType.Large.ordinal());

        int type = -1;

        if(idParkingSpot < smallDimension)
            type = ParkingSpotType.Small.ordinal();
        else if(idParkingSpot < smallDimension + mediumDimension)
            type = ParkingSpotType.Medium.ordinal();
        else if(idParkingSpot < smallDimension + mediumDimension + largeDimension)
            type = ParkingSpotType.Large.ordinal();

        return type;
    }

}
