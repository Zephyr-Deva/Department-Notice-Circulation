package com.department.ui;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

import com.department.config.DatabaseConnection;

public class AddNotice {

    public static void open() {

        JFrame f = new JFrame("Add Notice");

        JLabel title = new JLabel("Add Notice");
        title.setBounds(170, 20, 150, 30);

        JLabel l1 = new JLabel("Notice Title");
        l1.setBounds(50, 70, 100, 30);

        JTextField t1 = new JTextField();
        t1.setBounds(150, 70, 200, 30);

        JLabel l2 = new JLabel("Description");
        l2.setBounds(50, 120, 100, 30);

        JTextArea t2 = new JTextArea();
        t2.setBounds(150, 120, 200, 80);

        JLabel l3 = new JLabel("Date (YYYY-MM-DD)");
        l3.setBounds(30, 220, 120, 30);

        JTextField t3 = new JTextField();
        t3.setBounds(150, 220, 200, 30);

        JButton b = new JButton("Add Notice");
        b.setBounds(150, 270, 120, 35);

        // Add Notice Button
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String noticeTitle = t1.getText();
                String description = t2.getText();
                String date = t3.getText();

                if (noticeTitle.isEmpty() || description.isEmpty() || date.isEmpty()) {
                    JOptionPane.showMessageDialog(f, "Please fill all fields!");
                    return;
                }

                try {
                    Connection con = DatabaseConnection.getConnection();

                    if (con == null) {
                        JOptionPane.showMessageDialog(f, "Database connection failed!");
                        return;
                    }

                    String sql = "INSERT INTO notices (title, description, notice_date, created_by) VALUES (?, ?, ?, 1)";

                    PreparedStatement ps = con.prepareStatement(sql);

                    ps.setString(1, noticeTitle);
                    ps.setString(2, description);
                    ps.setDate(3, Date.valueOf(date));

                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(f, "Notice Added Successfully!");

                    t1.setText("");
                    t2.setText("");
                    t3.setText("");

                    ps.close();
                    con.close();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(f, "Error: " + ex.getMessage());
                }
            }
        });

        f.add(title);
        f.add(l1);
        f.add(t1);
        f.add(l2);
        f.add(t2);
        f.add(l3);
        f.add(t3);
        f.add(b);

        f.setSize(420, 370);
        f.setLayout(null);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        open();
    }
}