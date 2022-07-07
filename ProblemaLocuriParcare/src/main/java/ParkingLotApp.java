import database.Database;
import database.VehiclesCollection;
import database.ParkingSpotsCollection;
import exceptions.VehicleNotFoundException;
import exceptions.ParkingSpotNotFoundException;
import exceptions.ParkingSpotNotOccupiedException;
import exceptions.SimultaneousOperationInDatabaseCollectionException;
import factory.VehicleCreatorGenerator;
import org.json.JSONException;
import org.json.JSONObject;
import parking.*;
import structures.Ticket;
import utils.DeserializerUtil;
import utils.HttpRequestCreatorUtil;
import vehicles.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;

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
    private ParkingLotService parkingLotService;
    private HttpClient httpClient;

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
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            boolean vipStatus = vip.equals("Yes");
            Driver driver = new Driver(name, vipStatus);
            boolean electric = isElectric.equals("Yes");
            VehicleType vehicleType = VehicleType.getVehicleTypeByName(vehicleTypeString);

            VehicleJson vehicleJson = new VehicleJson(vehicleType.toString(), driver, color, price, electric);
            //Vehicle vehicle = VehicleCreatorGenerator.getVehicleCreator(vehicleType).getVehicle(driver, color, price, electric);

            HttpRequest request = HttpRequestCreatorUtil.createPostRequestWithVehicleBody("http://localhost:8080/getParkingTicket", vehicleJson);
            HttpResponse<String> response = null;
            try {
                response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                textArea_info.append("Could not add a vehicle to the parking lot!\r\n");
            }

            //Ticket ticket = parkingLotService.getParkingTicket(vehicle);
            Ticket ticket = DeserializerUtil.getTicket(response.body());

            // Daca nu am ajuns pe ramura catch (daca nu s-a aruncat o exceptie), atunci s-a gasit un loc de parcare liber
            textArea_info.append(ticket.getVehicle().getDescription() + " received the following parking slot: " + ticket.getSpotId() + "\r\n");
        }
    }

    public ParkingLotApp() {
        Database database = new Database("parkingLotDB");
        parkingLotService = new ParkingLotService(new TicketGeneratorCreator(), new ParkingSpotsCollection(database), new VehiclesCollection(database));
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

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

                HttpRequest request = HttpRequestCreatorUtil.createPostRequest("http://localhost:8080/leaveParkingLot/" + idParkingSpot);
                HttpResponse<String> response = null;
                Vehicle vehicle = null;
                try {
                    response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                    vehicle = DeserializerUtil.getVehicleFromJsonObject(response.body());
                    textArea_info.append(vehicle.getDescription() + " has left the parking lot (it was on spot: " + idParkingSpot + ")\r\n");
                } catch (IOException | InterruptedException | JSONException ex) {
                    textArea_info.append("Did not find the vehicle!\r\n");
                }

            }
        });

        button_showVehicles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HttpRequest request = HttpRequestCreatorUtil.createGetRequest("http://localhost:8080/getTickets");
                HttpResponse<String> response = null;
                List<Ticket> tickets = null;
                try {
                    response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                    tickets = DeserializerUtil.getTickets(response.body());

                } catch (IOException | InterruptedException ex) {
                    textArea_info.append("Could not get vehicles!\r\n");
                }

                textArea_info.append("\r\n--------------------");
                StringBuilder ans = new StringBuilder();
                ans.append("\r\n");

                for (Ticket ticket : tickets) {
                    ans.append(ticket.getVehicle().getDescription()).append(" -> parking spot: ").append(ticket.getSpotId()).append("\r\n");
                }
                ans.append("\r\n");
                ans.append("-----> Number of free parking spots left: \r\n");
                ans.append("Small: ").append(parkingLotService.getNumberOfEmptySpotsForParkingSpotType(ParkingSpotType.SMALL)).append(" free spots.\r\n");
                ans.append("Medium: ").append(parkingLotService.getNumberOfEmptySpotsForParkingSpotType(ParkingSpotType.MEDIUM)).append(" free spots.\r\n");
                ans.append("Large: ").append(parkingLotService.getNumberOfEmptySpotsForParkingSpotType(ParkingSpotType.LARGE)).append(" free spots.\r\n");
                String text = ans.toString();

                textArea_info.append(text);
                textArea_info.append("--------------------\r\n");

            }
        });

        button_showAllParkingSpots.addActionListener(e -> {
            HttpRequest request = HttpRequestCreatorUtil.createGetRequest("http://localhost:8080/getParkingSpots");

            HttpResponse<String> response = null;
            List<ParkingSpot> parkingSpots = null;
            try {
                response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                parkingSpots = DeserializerUtil.getParkingSpots(response.body());
            } catch (IOException | InterruptedException ex) {
                textArea_info.append("Could not get parking spots!\r\n");
            }

            textArea_info.append("\r\n--------------------");
            StringBuilder ans = new StringBuilder();
            ans.append("\r\n");

            for (ParkingSpot parkingSpot : parkingSpots) {
                ans.append(parkingSpot.getId()).append(" [").append(parkingSpot.getSpotType()).append("]").append(" -> eletric: ").append(parkingSpot.getElectric()).append("\r\n");
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
