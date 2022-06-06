import exceptions.ParkingSpotNotFoundException;
import exceptions.ParkingSpotNotOccupiedException;
import input.ReadInputFromFile;
import parking.*;
import structures.Ticket;
import vehicles.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

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
    private ParkingLot parkingLot;
    private ParkingLotService parkingLotService;


    public class CustomThread extends Thread
    {
        @Override
        public void run()
        {
            String name = textField_name.getText();
            String vehicleType = comboBox_vehicle.getSelectedItem().toString();
            String vip = comboBox_vip.getSelectedItem().toString();
            String color = textField_culoare.getText();
            String electric = comboBox_electric.getSelectedItem().toString();

            int price = 0;
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
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


            boolean isElectric = false;
            if(electric == "Yes")
                isElectric = true;

            Vehicle vehicle = null;
            switch(vehicleType)
            {
                case "Motorcycle": vehicle = new Motorcycle(color, price, isElectric); break;
                case "Car": vehicle = new Car(color, price, isElectric); break;
                case "Truck": vehicle = new Truck(color, price, isElectric); break;
                default: break;
            }

            boolean vipStatus = false;
            if(vip == "Yes")
                vipStatus = true;

            Driver driver = new Driver(name, vehicle, vipStatus);

            try
            {
                Ticket ticket = parkingLotService.getParkingTicket(parkingLot, driver);

                // Daca nu am ajuns pe ramura catch (daca nu s-a aruncat o exceptie), atunci s-a gasit un loc de parcare liber
                textArea_info.append("The driver " + driver.toString() + " received the following parking slot: " + ticket.getSpotId() + "\r\n");
            }
            catch(ParkingSpotNotFoundException ex)
            {
                textArea_info.append("There is no free parking spot available!\r\n");
            }

        }
    }

    public ParkingLotApp()
    {
        ReadInputFromFile fileReader = new ReadInputFromFile();

        try {
            parkingLot = fileReader.initializeAndGetParkingLot();
        } catch (RuntimeException e) {
            System.out.println(e.toString());
            System.exit(1);
        }

        parkingLotService = new ParkingLotService(new TicketGeneratorCreator());

        button_getTicket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Thread getTicketThread = new CustomThread();
                getTicketThread.start();
            }
        });

        button_leaveParkingLot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String idParkingSpot = textField_idParkingSpot.getText();

                try
                {
                    Driver driver = parkingLotService.leaveParkingLot(parkingLot, Integer.parseInt(idParkingSpot));
                    textArea_info.append("The driver: " + driver.toString() + " has left the parking lot (he was on spot: " + idParkingSpot + ")\r\n");
                }
                catch(ParkingSpotNotOccupiedException ex)
                {
                    textArea_info.append("The spot with id: " + idParkingSpot + " is not occupied!" + "\r\n");
                }
            }
        });


        button_showVehicles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                textArea_info.append("\r\n--------------------");

                StringBuilder ans = new StringBuilder();
                ans.append("\r\n");
                for (HashMap.Entry<Integer, Driver> entry : parkingLot.getAssignedParkingSpots().entrySet()) {
                    ans.append(entry.getValue().toString()).append(" -> parking slot: ").append(entry.getKey().toString()).append("\r\n");
                }
                ans.append("\r\n");
                ans.append("-----> Number of free parking spots left: \r\n");
                ans.append("Motorcycle: ").append(parkingLot.getEmptySpotsNumber().get(VehicleType.Motorcycle)).append(" free spots.\r\n");
                ans.append("Car: ").append(parkingLot.getEmptySpotsNumber().get(VehicleType.Car)).append(" free spots.\r\n");
                ans.append("Truck: ").append(parkingLot.getEmptySpotsNumber().get(VehicleType.Truck)).append(" free spots.\r\n");
                String text = ans.toString();

                textArea_info.append(text);
                textArea_info.append("--------------------\r\n");
            }
        });

        button_showAllParkingSpots.addActionListener(e -> {
            textArea_info.append("\r\n--------------------");

            StringBuilder ans = new StringBuilder();
            ans.append("\r\n");
            for(VehicleType vehicleType : VehicleType.values())
            {
                for(ParkingSpot parkingSpot : parkingLot.getParkingSpots().get(vehicleType))
                {
                    ans.append(parkingSpot.getId()).append(" -> eletric: ").append(parkingSpot.hasElectricCharger()).append("\r\n");
                }
            }
            String text = ans.toString();

            textArea_info.append(text);
            textArea_info.append("--------------------\r\n");
        });
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

/*  FRONTEND


 */

/*  IMPLEMENTATION

    Minheaps have O(log n) complexity for both insertion and deletion.
    - We will use MinHeaps to model each category of parking lots (small, medium large). These MinHeaps will contain the
    id(index) of the free parking spots.
    - We will also use a HashMap that maps the parking spot id to the Parking.Driver that occupies it.

    - We will have a class that represents a Parking.Driver (String name, Vehicles.VehicleType vehicleType, boolean vipStatus)
    - We will also have a class Parking.ParkingLot that has the minheaps and the hashmap as attributes. It will also have the insertion
    and deletion methods.
    - We will use an Enumeration to map the vehicleType to an integer.
 */