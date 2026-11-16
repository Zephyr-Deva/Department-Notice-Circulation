package com.department.ui;

import javax.swing.*;
import java.sql.*;
import java.awt.Desktop;
import java.io.File;
import java.util.ArrayList;

import com.department.config.DatabaseConnection;

public class ViewCirculars {

    static ArrayList<Integer> circularIds = new ArrayList<>();

    public static void open() {

        JFrame f = new JFrame("View Circulars");

        JLabel title = new JLabel("Department Circulars");
        title.setBounds(250, 20, 200, 30);

        JLabel l1 = new JLabel("Available Circulars");
        l1.setBounds(30, 70, 150, 25);

        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> list = new JList<>(model);

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBounds(30, 100, 280, 220);

        JLabel l2 = new JLabel("Circular Details");
        l2.setBounds(350, 70, 150, 25);

        JTextField circularTitle = new JTextField();
        circularTitle.setBounds(350, 110, 300, 30);
        circularTitle.setEditable(false);

        JTextArea description = new JTextArea();
        description.setEditable(false);

        JScrollPane descScroll = new JScrollPane(description);
        descScroll.setBounds(350, 155, 300, 90);

        JTextField date = new JTextField();
        date.setBounds(350, 260, 300, 30);
        date.setEditable(false);

        JButton openPDF = new JButton("Open PDF");
        openPDF.setBounds(380, 320, 120, 35);

        JButton refresh = new JButton("Refresh");
        refresh.setBounds(80, 350, 100, 35);

        JButton close = new JButton("Close");
        close.setBounds(530, 320, 100, 35);

        // Load Circulars
        Runnable loadCirculars = () -> {

            model.clear();
            circularIds.clear();

            try {

                Connection con = DatabaseConnection.getConnection();

                if (con == null) {
                    JOptionPane.showMessageDialog(f,
                            "Database connection failed!");
                    return;
                }

                String sql = "SELECT circular_id, title FROM circulars ORDER BY circular_id DESC";

                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);

                while (rs.next()) {

                    int id = rs.getInt("circular_id");
                    String name = rs.getString("title");

                    circularIds.add(id);
                    model.addElement("ID: " + id + " - " + name);
                }

                if (model.isEmpty()) {
                    model.addElement("No Circulars Available");
                }

                rs.close();
                st.close();
                con.close();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, ex.getMessage());
            }
        };

        // Select Circular
        list.addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int index = list.getSelectedIndex();

                if (index >= 0 && index < circularIds.size()) {

                    int id = circularIds.get(index);

                    try {

                        Connection con = DatabaseConnection.getConnection();

                        if (con == null) {
                            JOptionPane.showMessageDialog(f,
                                    "Database connection failed!");
                            return;
                        }

                        String sql = "SELECT * FROM circulars WHERE circular_id=?";

                        PreparedStatement ps = con.prepareStatement(sql);
                        ps.setInt(1, id);

                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {

                            circularTitle.setText(rs.getString("title"));
                            description.setText(rs.getString("description"));
                            date.setText(String.valueOf(rs.getDate("circular_date")));
                        }

                        rs.close();
                        ps.close();
                        con.close();

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(f, ex.getMessage());
                    }
                }
            }
        });

        // Open PDF
        openPDF.addActionListener(e -> {

            int index = list.getSelectedIndex();

            if (index < 0 || index >= circularIds.size()) {
                JOptionPane.showMessageDialog(f,
                        "Please select a circular first!");
                return;
            }

            try {

                Connection con = DatabaseConnection.getConnection();

                if (con == null) {
                    JOptionPane.showMessageDialog(f,
                            "Database connection failed!");
                    return;
                }

                String sql = "SELECT file_path FROM circulars WHERE circular_id=?";

                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, circularIds.get(index));

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    String path = rs.getString("file_path");
                    File file = new File(path);

                    if (file.exists() && Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(file);
                    } else {
                        JOptionPane.showMessageDialog(f,
                                "PDF file not found or cannot be opened!");
                    }
                }

                rs.close();
                ps.close();
                con.close();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f,
                        "Error opening PDF: " + ex.getMessage());
            }
        });

        refresh.addActionListener(e -> loadCirculars.run());

        close.addActionListener(e -> f.dispose());

        // Initial loading
        loadCirculars.run();

        f.add(title);
        f.add(l1);
        f.add(scroll);
        f.add(l2);
        f.add(circularTitle);
        f.add(descScroll);
        f.add(date);
        f.add(openPDF);
        f.add(refresh);
        f.add(close);

        f.setSize(720, 450);
        f.setLayout(null);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        open();
    }
}