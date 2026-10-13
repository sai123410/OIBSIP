package com.oasis.reservation;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Cancellation extends JFrame {

    JTextField pnrField;

    public Cancellation() {

        setTitle("Cancel Reservation");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        panel.add(new JLabel("Enter PNR:"));

        pnrField = new JTextField();
        panel.add(pnrField);

        JButton fetchButton = new JButton("Fetch");
        JButton cancelButton = new JButton("Cancel Booking");

        panel.add(fetchButton);
        panel.add(cancelButton);

        add(panel);

        fetchButton.addActionListener(e -> fetchBooking());

        cancelButton.addActionListener(e -> cancelBooking());
    }

    private void fetchBooking() {

        String pnr = pnrField.getText();

        if (pnr.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter PNR."
            );
            return;
        }

        String sql =
                "SELECT * FROM reservations WHERE pnr = ?";

        try (Connection con =
                     DatabaseConnection.getConnection();

             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1, pnr);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Booking Found!\n\n" +
                        "PNR: " + rs.getString("pnr") + "\n" +
                        "Passenger: " + rs.getString("passenger_name") + "\n" +
                        "Train: " + rs.getString("train_name") + "\n" +
                        "Journey Date: " + rs.getString("journey_date") + "\n" +
                        "From: " + rs.getString("source_station") + "\n" +
                        "To: " + rs.getString("destination_station")
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No booking found for this PNR."
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Database error!"
            );
        }
    }

    private void cancelBooking() {

        String pnr = pnrField.getText();

        if (pnr.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter PNR."
            );
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cancel this booking?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql =
                "DELETE FROM reservations WHERE pnr = ?";

        try (Connection con =
                     DatabaseConnection.getConnection();

             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1, pnr);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Booking cancelled successfully."
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No booking found for this PNR."
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Database error!"
            );
        }
    }

}
