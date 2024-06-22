import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TrainTicketBooking {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TrainTicketBooking::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Train Ticket Booking");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Flash message
        JLabel flashMessageLabel = new JLabel("BOOKING TRAIN TICKETS", SwingConstants.CENTER);
        flashMessageLabel.setForeground(Color.BLUE);
        frame.add(flashMessageLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Passenger Name
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameTextField = new JTextField(20);
        addToPanel(panel, nameLabel, nameTextField, gbc, 0);

        // Date of Birth
        JLabel dobLabel = new JLabel("Date of Birth (MM/dd/yyyy):");
        JTextField dobTextField = new JTextField(10);
        addToPanel(panel, dobLabel, dobTextField, gbc, 1);

        // Age
        JLabel ageLabel = new JLabel("Age:");
        JTextField ageTextField = new JTextField(3);
        ageTextField.setEditable(false);
        addToPanel(panel, ageLabel, ageTextField, gbc, 2);

        // Gender
        JLabel genderLabel = new JLabel("Gender:");
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"Select", "Male", "Female"});
        addToPanel(panel, genderLabel, genderComboBox, gbc, 3);

        // Berth Preference
        JLabel berthLabel = new JLabel("Berth Preference:");
        JComboBox<String> berthComboBox = new JComboBox<>(new String[]{"Select", "Lower", "Middle", "Upper"});
        addToPanel(panel, berthLabel, berthComboBox, gbc, 4);

        // Submit Button
        JButton submitButton = new JButton("Submit");

        // Text area for displaying results or error messages
        JTextArea resultTextArea = new JTextArea(10, 30);
        resultTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(scrollPane, gbc);

        // Adding the submit button
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(submitButton, gbc);

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);

        // ActionListener for Submit Button
        submitButton.addActionListener(e -> {
            try {
                String validationMessage = validateInputs(
                        nameTextField.getText(),
                        dobTextField.getText(),
                        genderComboBox.getSelectedIndex(),
                        berthComboBox.getSelectedItem().toString()
                );

                if (!validationMessage.isEmpty()) {
                    resultTextArea.setText("Error: " + validationMessage);
                } else {
                    resultTextArea.setText(getExtractedInputsText(
                            nameTextField.getText(),
                            dobTextField.getText(),
                            genderComboBox.getSelectedItem().toString(),
                            berthComboBox.getSelectedItem().toString()
                    ));
                }
            } catch (Exception ex) {
                resultTextArea.setText("Error: " + ex.getMessage());
            }
        });

        // Multithreading for flash message
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(200);
                    flashMessageLabel.setText(shiftStringLeft(flashMessageLabel.getText()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // Listener to update age when date of birth is entered
        dobTextField.addActionListener(e -> {
            String dob = dobTextField.getText();
            try {
                ageTextField.setText(String.valueOf(calculateAge(dob)));
            } catch (Exception ex) {
                ageTextField.setText("Invalid DOB");
            }
        });
    }

    private static void addToPanel(JPanel panel, JComponent label, JComponent field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private static String validateInputs(String name, String dob, int genderIndex, String berth) {
        if (name.isEmpty()) {
            return "Name is required.";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat.setLenient(false);
        try {
            Date dateOfBirth = dateFormat.parse(dob);
            Calendar dobCalendar = Calendar.getInstance();
            dobCalendar.setTime(dateOfBirth);

            int age = calculateAge(dobCalendar);
            if (age > 60) {
                return "You are a Senior Citizen.";
            }
        } catch (ParseException e) {
            return "Invalid Date of Birth format. Please use MM/dd/yyyy.";
        }

        if (genderIndex == 0) {
            return "Please select a Gender.";
        }

        if (berth.equals("Select")) {
            return "Please select a Berth Preference.";
        }

        return "";
    }

    private static int calculateAge(Calendar dob) {
        Calendar currentDate = Calendar.getInstance();
        int age = currentDate.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (currentDate.get(Calendar.MONTH) < dob.get(Calendar.MONTH) ||
                (currentDate.get(Calendar.MONTH) == dob.get(Calendar.MONTH) &&
                        currentDate.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }
        return age;
    }

    private static int calculateAge(String dob) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat.setLenient(false);
        Date dateOfBirth = dateFormat.parse(dob);
        Calendar dobCalendar = Calendar.getInstance();
        dobCalendar.setTime(dateOfBirth);
        return calculateAge(dobCalendar);
    }

    private static String getExtractedInputsText(String name, String dob, String gender, String berth) {
        StringBuilder result = new StringBuilder();
        result.append("Name: ").append(name).append("\n");
        result.append("Date of Birth: ").append(dob).append("\n");

        try {
            int age = calculateAge(dob);
            result.append("Age: ").append(age).append("\n");
            if (age > 60) {
                result.append("You are a Senior Citizen.\n");
            }
        } catch (ParseException e) {
            result.append("Age: Invalid DOB\n");
        }

        result.append("Gender: ").append(gender).append("\n");
        result.append("Berth Preference: ").append(berth).append("\n");

        return result.toString();
    }

    private static String shiftStringLeft(String input) {
        return input.substring(1) + input.charAt(0);
    }
}
