package com.department.ui;

import javax.swing.*;
import java.sql.*;

import com.department.config.DatabaseConnection;

public class ViewNotices {

    public static void open() {

        JFrame f = new JFrame("View Notices");

        JLabel title = new JLabel("Department Notices");
        title.setBounds(140, 20, 200, 30);

        JTextArea area = new JTextArea();
        area.setEditable(false);

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBounds(50, 70, 320, 200);

        JButton b = new JButton("Close");
        b.setBounds(150, 290, 100, 35);

        // Fetch notices from database
        try {

            Connection con = DatabaseConnection.getConnection();

            if (con == null) {
                JOptionPane.showMessageDialog(f, "Database connection failed!");
                return;
            }

            String sql = "SELECT * FROM notices ORDER BY notice_id DESC";

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            String data = "";

            while (rs.next()) {

                data += "Notice ID: " + rs.getInt("notice_id") + "\n";
                data += "Title: " + rs.getString("title") + "\n";
                data += "Description: " + rs.getString("description") + "\n";
                data += "Date: " + rs.getDate("notice_date") + "\n";
                data += "--------------------------------\n\n";
            }

            if (data.isEmpty()) {
                data = "No Notices Available!";
            }

            area.setText(data);

            rs.close();
            st.close();
            con.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(f, "Error: " + ex.getMessage());
        }

        b.addActionListener(e -> f.dispose());

        f.add(title);
        f.add(scroll);
        f.add(b);

        f.setSize(420, 380);
        f.setLayout(null);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        open();
    }
}
