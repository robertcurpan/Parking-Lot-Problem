package utils;

import parking.ParkingSpot;
import parking.ParkingSpotType;
import structures.ParkingLotStatus;
import structures.Ticket;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PrinterUtil {

    public static String getParkingLotStatusString(ParkingLotStatus parkingLotStatus, Map<ParkingSpotType, Integer> noOfEmptyParkingSpots) {
        StringBuilder parkingLotStatusString = new StringBuilder();

        List<ParkingSpot> parkingSpots = parkingLotStatus.getParkingSpots();
        List<Ticket> tickets = parkingLotStatus.getTickets();

        parkingLotStatusString.append("\r\n--------------------");
        parkingLotStatusString.append("\r\n");
        for (ParkingSpot parkingSpot : parkingSpots) {
            parkingLotStatusString.append(parkingSpot.getId()).append(" [").append(parkingSpot.getSpotType()).append("]").append(" -> eletric: ").append(parkingSpot.getElectric()).append("\r\n");
        }
        parkingLotStatusString.append("--------------------\r\n");

        parkingLotStatusString.append("\r\n--------------------");
        parkingLotStatusString.append("\r\n");
        for (Ticket ticket : tickets) {
            parkingLotStatusString.append(ticket.getVehicle().getDescription()).append(" -> parking spot: ").append(ticket.getSpotId()).append("\r\n");
        }
        parkingLotStatusString.append("\r\n");
        parkingLotStatusString.append("-----> Number of free parking spots left: \r\n");
        parkingLotStatusString.append("Small: ").append(noOfEmptyParkingSpots.get(ParkingSpotType.SMALL)).append(" free spots.\r\n");
        parkingLotStatusString.append("Medium: ").append(noOfEmptyParkingSpots.get(ParkingSpotType.MEDIUM)).append(" free spots.\r\n");
        parkingLotStatusString.append("Large: ").append(noOfEmptyParkingSpots.get(ParkingSpotType.LARGE)).append(" free spots.\r\n");
        parkingLotStatusString.append("--------------------\r\n");

        return parkingLotStatusString.toString();
    }

    public static String getAllParkingSpotsString(List<ParkingSpot> parkingSpots) {
        StringBuilder allParkingSpotsString = new StringBuilder();

        allParkingSpotsString.append("\r\n--------------------");
        allParkingSpotsString.append("\r\n");
        for (ParkingSpot parkingSpot : parkingSpots) {
            allParkingSpotsString.append(parkingSpot.getId()).append(" [").append(parkingSpot.getSpotType()).append("]").append(" -> eletric: ").append(parkingSpot.getElectric()).append("\r\n");
        }
        allParkingSpotsString.append("--------------------\r\n");

        return allParkingSpotsString.toString();
    }

    public static String getTicketsString(List<Ticket> tickets, Map<ParkingSpotType, Integer> noOfEmptyParkingSpots) {
        StringBuilder ticketsString = new StringBuilder();

        ticketsString.append("\r\n--------------------");
        ticketsString.append("\r\n");

        for (Ticket ticket : tickets) {
            ticketsString.append(ticket.getVehicle().getDescription()).append(" -> parking spot: ").append(ticket.getSpotId()).append("\r\n");
        }
        ticketsString.append("\r\n");
        ticketsString.append("-----> Number of free parking spots left: \r\n");
        ticketsString.append("Small: ").append(noOfEmptyParkingSpots.get(ParkingSpotType.SMALL)).append(" free spots.\r\n");
        ticketsString.append("Medium: ").append(noOfEmptyParkingSpots.get(ParkingSpotType.MEDIUM)).append(" free spots.\r\n");
        ticketsString.append("Large: ").append(noOfEmptyParkingSpots.get(ParkingSpotType.LARGE)).append(" free spots.\r\n");
        ticketsString.append("--------------------\r\n");

        return ticketsString.toString();
    }
}
