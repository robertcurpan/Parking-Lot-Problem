import parking.Driver;
import parking.ParkingLot;
import parking.ParkingSpot;
import parking.ParkingSpotType;
import structures.FileInputs;
import vehicles.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

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
                textArea_info.append("Trebuie sa introduceti un numar in campul Pret! -> Error: " + ex.toString() + "\r\n");
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

            Driver d = new Driver(name, vehicle, vipStatus);
            String text = parkingLot.getParkingTicket(d);
            textArea_info.append(text + "\r\n");
        }
    }

    public ParkingLotApp()
    {
        parkingLot = new ParkingLot();

        try
        {
            FileInputs fileInputs = readInputFromFile();
            parkingLot.setVariablesFromFileInputs(fileInputs);
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e.toString());
            System.exit(4);
        }


        button_getTicket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Thread getTicketThread = new CustomThread();
                getTicketThread.start();
                /*
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
                    textArea_info.append("Trebuie sa introduceti un numar in campul Pret! -> Error: " + ex.toString() + "\r\n");
                    return;
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

                Driver d = new Driver(name, vehicle, vipStatus);
                String text = parkingLot.getParkingTicket(d);
                textArea_info.append(text + "\r\n");
                */
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

        button_showAllParkingSpots.addActionListener(e -> {
            textArea_info.append("\r\n--------------------");
            String text = parkingLot.showAllParkingSpots();
            textArea_info.append(text);
            textArea_info.append("\r\n--------------------\r\n");
        });
    }

    public FileInputs readInputFromFile() throws FileNotFoundException
    {
        FileInputs fileInputs = new FileInputs();

        File file = new File("C:\\Users\\robertcur\\Desktop\\IntelliJ - Projects\\Parking-Lot-Problem\\ProblemaLocuriParcare\\input.txt");
        Scanner fileReader = new Scanner(file);

        // If the file contains the dimensions (first line)
        if(fileReader.hasNextLine())
        {
            String line = fileReader.nextLine();
            String[] aux = line.split(" ");

            int nrVehicleTypes = VehicleType.values().length;
            if(nrVehicleTypes != aux.length)
            {
                System.out.println("Nr de valori de pe prima linie a fisierului nu corespunde cu numarul de tipuri de vehicule!");
                System.exit(1);
            }

            for(int i = 0; i < aux.length; ++i)
                fileInputs.addLotDimension(Integer.parseInt(aux[i]));

            try
            {
                int k = 0;

                while(k <= ParkingSpotType.Large.ordinal())
                {
                    int currentDimension = Integer.parseInt(aux[k]);
                    for(int i = 0; i < currentDimension; ++i)
                    {
                        line = fileReader.nextLine();
                        if(line.equals("electric") || line.equals("nonelectric"))
                            fileInputs.addLotInput(line);
                        else
                        {
                            System.out.println("Incorrect input in file! (electric/nonelectric)");
                            System.out.println(line);
                            System.exit(2);
                        }
                    }

                    ++k;
                }

            }
            catch(Exception e)
            {
                System.out.println("Not enough input lines in file!");
                e.printStackTrace();
                System.exit(3);
            }
        }
        else
        {
            System.out.println("Not enough input lines in file!");
            System.exit(3);
        }

        return fileInputs;
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