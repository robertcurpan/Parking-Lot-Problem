import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    private ParkingLot parkingLot;

    public ParkingLotApp()
    {
        parkingLot = new ParkingLot(2, 3, 4);

        button_getTicket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String name = textField_name.getText();
                String vehicle = comboBox_vehicle.getSelectedItem().toString();
                String vip = comboBox_vip.getSelectedItem().toString();

                VehicleType vehicleType = VehicleType.Car;
                switch(vehicle)
                {
                    case "Motorcycle": vehicleType = VehicleType.Motorcycle; break;
                    case "Car": vehicleType = VehicleType.Car; break;
                    case "Truck": vehicleType = VehicleType.Truck; break;
                    default: break;
                }

                boolean vipStatus = false;
                switch(vip)
                {
                    case "Yes": vipStatus = true; break;
                    case "No": vipStatus = false; break;
                    default: break;
                }

                Driver d = new Driver(name, vehicleType, vipStatus);
                String text = parkingLot.getParkingTicket(d);
                textArea_info.append(text + "\r\n");
            }
        });


        button_leaveParkingLot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String idParkingSpot = textField_idParkingSpot.getText();
                String text = parkingLot.leaveParkingLot(Integer.parseInt(idParkingSpot));
                textArea_info.append(text + "\r\n");
            }
        });


        button_showVehicles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                textArea_info.append("\r\n--------------------");
                String text = parkingLot.showOccupiedSpots();
                textArea_info.append(text);
                textArea_info.append("\r\n--------------------\r\n");
            }
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
    - We will also use a HashMap that maps the parking spot id to the Driver that occupies it.

    - We will have a class that represents a Driver (String name, VehicleType vehicleType, boolean vipStatus)
    - We will also have a class ParkingLot that has the minheaps and the hashmap as attributes. It will also have the insertion
    and deletion methods.
    - We will use an Enumeration to map the vehicleType to an integer.
 */