import exceptions.*;
import parking.*;
import structures.ParkingLotStatus;
import structures.Ticket;
import utils.PrinterUtil;
import vehicles.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class ParkingLotApp
{
    private JPanel panel_main;
    private JComboBox comboBox_vehicle;
    private JComboBox comboBox_vip;
    private JButton button_getTicket;
    private JTextArea textArea_info;
    private JTextField textField_idParkingSpot;
    private JButton button_leaveParkingLot;
    private JButton button_showVehicles;
    private JTextField textField_name;
    private JScrollPane scroll;
    private JTextField textField_culoare;
    private JTextField textField_pret;
    private JComboBox comboBox_electric;
    private JButton button_showAllParkingSpots;
    ParkingLotService parkingLotService;
    ParkingLotStatus parkingLotStatus; // In felul asta facem interfata sa fie stateful

    public class CustomThread extends Thread
    {
        @Override
        public void run()
        {
            String name = textField_name.getText();
            String vehicleTypeString = comboBox_vehicle.getSelectedItem().toString();
            String vip = comboBox_vip.getSelectedItem().toString();
            String color = textField_culoare.getText();
            String isElectric = comboBox_electric.getSelectedItem().toString();
            int price;
            try
            {
                price = Integer.parseInt(textField_pret.getText());
            }
            catch(Exception ex)
            {
                textArea_info.append("You have to introduce a number in the price field! -> Error: " + ex.toString() + "\r\n");
                return;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            boolean vipStatus = vip.equals("Yes");
            Driver driver = new Driver(name, vipStatus);
            boolean electric = isElectric.equals("Yes");
            VehicleType vehicleType = VehicleType.valueOf(vehicleTypeString);
            VehicleJson vehicleJson = new VehicleJson(vehicleType.toString(), driver, color, price, electric);


            Ticket ticket = null;
            boolean errorWhilePerformingOperation = true;
            try {
                ticket = parkingLotService.generateParkingTicket(vehicleJson);
                parkingLotStatus = parkingLotService.updateParkingLotStatusAfterDriverParks(parkingLotStatus, ticket);
                textArea_info.append(ticket.getVehicle().getDescription() + " received the following parking slot: " + ticket.getSpotId() + "\r\n");
                textArea_info.append(PrinterUtil.getParkingLotStatusString(parkingLotStatus, parkingLotService.getNoOfEmptyParkingSpots(parkingLotStatus)));
                errorWhilePerformingOperation = false;
            } catch (ParkingSpotNotAvailableException e) {
                textArea_info.append("Could not find a parking spot for the current vehicle!" + "\r\n");
            } catch (ParkingSpotNotFoundException e) {
                textArea_info.append("The parking spot with the given id does not exist!" + "\r\n");
            } catch (VehicleTooExpensiveException e) {
                textArea_info.append(e.toString() + "\r\n");
            } catch (ParkingLotGeneralException e) {
                textArea_info.append("Error communicating with REST microservice: " + e.toString() + "\r\n");
            } finally {
                if(errorWhilePerformingOperation) {
                    try {
                        updateAndPrintParkingLotStatus();
                    } catch (ParkingLotGeneralException e) {
                        textArea_info.append(e.toString() + "\r\n");
                    }
                }
            }

        }
    }

    public ParkingLotApp() {
        parkingLotService = new ParkingLotService();

        try {
            updateAndPrintParkingLotStatus();
        } catch (ParkingLotGeneralException e) {
            textArea_info.append(e.toString() + "\r\n");
        }

        button_getTicket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread getTicketThread = new CustomThread();
                getTicketThread.start();
            }
        });

        button_leaveParkingLot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idParkingSpot = textField_idParkingSpot.getText();

                Ticket ticket = null;
                boolean errorWhilePerformingOperation = true;
                try {
                    ticket = parkingLotService.leaveParkingLot(parkingLotStatus.getParkingSpotById(Integer.parseInt(idParkingSpot)));
                    parkingLotStatus = parkingLotService.updateParkingLotStatusAfterDriverLeaves(parkingLotStatus, ticket);
                    textArea_info.append(ticket.getVehicle().getDescription() + " has left the parking lot (it was on spot: " + ticket.getSpotId() + ")\r\n");
                    textArea_info.append(PrinterUtil.getParkingLotStatusString(parkingLotStatus, parkingLotService.getNoOfEmptyParkingSpots(parkingLotStatus)));
                    errorWhilePerformingOperation = false;
                } catch (ParkingSpotNotFoundException ex) {
                    textArea_info.append("Parking spot with given id does not exist!\r\n");
                } catch (ParkingSpotNotOccupiedException ex) {
                    textArea_info.append("The parking spot is already free!\r\n");
                } catch (ParkingLotGeneralException ex) {
                    textArea_info.append("Error communicating with REST microservice: " + ex.toString() + "\r\n");
                } finally {
                    if(errorWhilePerformingOperation) {
                        try {
                            updateAndPrintParkingLotStatus();
                        } catch (ParkingLotGeneralException ex) {
                            textArea_info.append(ex.toString() + "\r\n");
                        }
                    }
                }

            }
        });

        button_showVehicles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<Ticket> tickets = parkingLotService.getTickets();
                    Map<ParkingSpotType, Integer> noOfEmptyParkingSpots = parkingLotService.getNoOfEmptyParkingSpots(parkingLotStatus);
                    textArea_info.append(PrinterUtil.getTicketsString(tickets, noOfEmptyParkingSpots));
                } catch (ParkingLotGeneralException ex) {
                    textArea_info.append(ex.toString() + "\r\n");
                }

            }
        });

        button_showAllParkingSpots.addActionListener(e -> {
            try {
                List<ParkingSpot> parkingSpots = parkingLotService.getParkingSpots();
                textArea_info.append(PrinterUtil.getAllParkingSpotsString(parkingSpots));
            } catch (ParkingLotGeneralException ex) {
                textArea_info.append(ex.toString() + "\r\n");
            }

        });

    }

    public void updateAndPrintParkingLotStatus() throws ParkingLotGeneralException {
        parkingLotStatus = parkingLotService.getParkingLotStatus();
        textArea_info.append(PrinterUtil.getParkingLotStatusString(parkingLotStatus, parkingLotService.getNoOfEmptyParkingSpots(parkingLotStatus)));
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("ParkingLotApp");
        frame.setContentPane(new ParkingLotApp().panel_main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}


/*  TASK

    "Implement a parking lot management app in Java, that can generate tickets for vehicle owners based on the available spots.
    The parking lot can accept for parking motorcycles, cars and trucks. The parking spots are of three different sizes:
    small, medium and large. A regular user can park a motorcycle on a small spot, a car on a medium spot and a truck on the
    large one. A VIP user can park its vehicle on a bigger spot also if there are no available spots for that vehicle category."
 */
