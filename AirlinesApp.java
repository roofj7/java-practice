import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Airlines {
    private String passengerName;
    private int passengerID;
    private int flightNumber;
    private String source;
    private String destination;

    // Constructor
    public Airlines(String passengerName, int passengerID, int flightNumber, String source, String destination) {
        this.passengerName = passengerName;
        this.passengerID = passengerID;
        this.flightNumber = flightNumber;
        this.source = source;
        this.destination = destination;
    }

    public int getPassengerID() {
        return passengerID;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public String toString() {
        return "Passenger Name: " + passengerName + "\nPassenger ID: " + passengerID +
                "\nFlight Number: " + flightNumber + "\nSource: " + source + "\nDestination: " + destination + "\n";
    }
}

class AirlinesManagement {
    private ArrayList<Airlines> passengerList = new ArrayList<>();

    public void addPassenger(Airlines airline) {
        passengerList.add(airline);
    }

    public Airlines searchPassenger(int passengerID) throws PassengerNotFoundException {
        for (Airlines airline : passengerList) {
            if (airline.getPassengerID() == passengerID) {
                return airline;
            }
        }
        throw new PassengerNotFoundException("Passenger Not Found");
    }

    public ArrayList<Airlines> displayPassengers() {
        Collections.sort(passengerList, Comparator.comparing(Airlines::getPassengerName));
        return passengerList;
    }
}

class PassengerNotFoundException extends Exception {
    public PassengerNotFoundException(String message) {
        super(message);
    }
}

public class AirlinesApp {
    private static AirlinesManagement airlinesManagement = new AirlinesManagement();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AirlinesApp::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("JACK & JILL Airlines Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JTextArea outputTextArea = new JTextArea(10, 40);
        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField passengerIDField = new JTextField(10);
        JTextField passengerNameField = new JTextField(10);
        JTextField flightNumberField = new JTextField(10);
        JTextField sourceField = new JTextField(10);
        JTextField destinationField = new JTextField(10);

        addToPanel(inputPanel, new JLabel("Passenger ID: "), passengerIDField, gbc, 0);
        addToPanel(inputPanel, new JLabel("Passenger Name: "), passengerNameField, gbc, 1);
        addToPanel(inputPanel, new JLabel("Flight Number: "), flightNumberField, gbc, 2);
        addToPanel(inputPanel, new JLabel("Source: "), sourceField, gbc, 3);
        addToPanel(inputPanel, new JLabel("Destination: "), destinationField, gbc, 4);

        JButton addPassengerButton = new JButton("Add Passenger");
        JButton searchPassengerButton = new JButton("Search Passenger");
        JButton displayPassengerButton = new JButton("Display Passengers");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addPassengerButton);
        buttonPanel.add(searchPassengerButton);
        buttonPanel.add(displayPassengerButton);

        addPassengerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int passengerID = Integer.parseInt(passengerIDField.getText());
                    int flightNumber = Integer.parseInt(flightNumberField.getText());
                    Airlines airline = new Airlines(passengerNameField.getText(), passengerID, flightNumber,
                            sourceField.getText(), destinationField.getText());

                    airlinesManagement.addPassenger(airline);

                    outputTextArea.setText("Passenger added successfully.");
                } catch (NumberFormatException ex) {
                    outputTextArea.setText("Invalid input. Please enter valid numeric values.");
                }
            }
        });

        searchPassengerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int passengerID = Integer.parseInt(passengerIDField.getText());
                    Airlines foundPassenger = airlinesManagement.searchPassenger(passengerID);
                    outputTextArea.setText(foundPassenger.toString());
                } catch (NumberFormatException ex) {
                    outputTextArea.setText("Please enter a valid Passenger ID.");
                } catch (PassengerNotFoundException ex) {
                    outputTextArea.setText(ex.getMessage());
                }
            }
        });

        displayPassengerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArrayList<Airlines> passengerList = airlinesManagement.displayPassengers();

                StringBuilder result = new StringBuilder();
                for (Airlines airline : passengerList) {
                    result.append(airline.toString()).append("\n");
                }
                outputTextArea.setText(result.toString());
            }
        });

        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(scrollPane, BorderLayout.EAST);

        frame.pack();
        frame.setVisible(true);
    }

    private static void addToPanel(JPanel panel, Component label, Component field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(label, gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }
}
