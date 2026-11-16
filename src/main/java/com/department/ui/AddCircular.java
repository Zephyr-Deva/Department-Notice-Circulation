package com.department.ui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.sql.*;
import java.io.File;

import com.department.config.DatabaseConnection;

public class AddCircular {

    public static void open() {

        JFrame f = new JFrame("Add Circular");

        JLabel title = new JLabel("Add Circular");
        title.setBounds(160, 20, 150, 30);

        JLabel l1 = new JLabel("Circular Title");
        l1.setBounds(50, 70, 100, 30);

        JTextField t1 = new JTextField();
        t1.setBounds(150, 70, 200, 30);

        JLabel l2 = new JLabel("Description");
        l2.setBounds(50, 120, 100, 30);

        JTextArea t2 = new JTextArea();
        JScrollPane sp = new JScrollPane(t2);
        sp.setBounds(150, 120, 200, 70);

        JLabel l3 = new JLabel("Date");
        l3.setBounds(50, 210, 100, 30);

        JTextField t3 = new JTextField();
        t3.setBounds(150, 210, 200, 30);

        JLabel l4 = new JLabel("PDF File");
        l4.setBounds(50, 250, 100, 30);

        JTextField t4 = new JTextField();
        t4.setBounds(150, 250, 130, 30);
        t4.setEditable(false);

        JButton browse = new JButton("Browse");
        browse.setBounds(285, 250, 85, 30);

        JButton b = new JButton("Add Circular");
        b.setBounds(145, 310, 130, 35);

        // Browse PDF File
        browse.addActionListener(e -> {

            JFileChooser chooser = new JFileChooser();

            FileNameExtensionFilter filter =
                    new FileNameExtensionFilter("PDF Files", "pdf");

            chooser.setFileFilter(filter);

            int result = chooser.showOpenDialog(f);

            if (result == JFileChooser.APPROVE_OPTION) {

                File file = chooser.getSelectedFile();

                if (file.getName().toLowerCase().endsWith(".pdf")) {
                    t4.setText(file.getAbsolutePath());
                } else {
                    JOptionPane.showMessageDialog(f,
                            "Please select a PDF file!");
                }
            }
        });

        // Save Circular
        b.addActionListener(e -> {

            String circularTitle = t1.getText();
            String description = t2.getText();
            String date = t3.getText();
            String filePath = t4.getText();

            if (circularTitle.isEmpty() || description.isEmpty()
                    || date.isEmpty() || filePath.isEmpty()) {

                JOptionPane.showMessageDialog(f,
                        "Please fill all fields and select PDF!");
                return;
            }

            try {

                Connection con = DatabaseConnection.getConnection();

                if (con == null) {
                    JOptionPane.showMessageDialog(f,
                            "Database connection failed!");
                    return;
                }

                String sql = "INSERT INTO circulars "
                        + "(title, description, circular_date, file_path, created_by) "
                        + "VALUES (?, ?, ?, ?, 1)";

                PreparedStatement ps = con.prepareStatement(sql);

                ps.setString(1, circularTitle);
                ps.setString(2, description);
                ps.setDate(3, Date.valueOf(date));
                ps.setString(4, filePath);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(f,
                        "Circular Added Successfully!");

                t1.setText("");
                t2.setText("");
                t3.setText("");
                t4.setText("");

                ps.close();
                con.close();

            } catch (IllegalArgumentException ex) {

                JOptionPane.showMessageDialog(f,
                        "Date format must be YYYY-MM-DD!");

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(f,
                        "Error: " + ex.getMessage());
            }
        });

        f.add(title);
        f.add(l1);
        f.add(t1);
        f.add(l2);
        f.add(sp);
        f.add(l3);
        f.add(t3);
        f.add(l4);
        f.add(t4);
        f.add(browse);
        f.add(b);

        f.setSize(420, 410);
        f.setLayout(null);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        open();
    }
}