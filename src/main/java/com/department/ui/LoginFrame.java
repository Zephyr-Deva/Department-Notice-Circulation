package com.department.ui;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

import com.department.config.DatabaseConnection;

public class LoginFrame {

    public static void open() {

        JFrame f = new JFrame("Login");

        JLabel l1 = new JLabel("Email");
        l1.setBounds(50, 50, 100, 30);

        JTextField t1 = new JTextField();
        t1.setBounds(150, 50, 180, 30);

        JLabel l2 = new JLabel("Password");
        l2.setBounds(50, 100, 100, 30);

        JPasswordField t2 = new JPasswordField();
        t2.setBounds(150, 100, 180, 30);

        JButton b = new JButton("Login");
        b.setBounds(150, 150, 100, 30);

        // Login Button Action
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String email = t1.getText();
                String password = new String(t2.getPassword());

                try {
                    Connection con = DatabaseConnection.getConnection();

                    if (con == null) {
                        JOptionPane.showMessageDialog(f, "Database connection failed!");
                        return;
                    }

                    String sql = "SELECT role FROM users WHERE email=? AND password=?";

                    PreparedStatement ps = con.prepareStatement(sql);

                    ps.setString(1, email);
                    ps.setString(2, password);

                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {

                        String role = rs.getString("role");

                        JOptionPane.showMessageDialog(f, "Login Successful!");

                        f.dispose();

                        if (role.equals("Admin")) {
                            Dashboard.open();
                        }
                        else if (role.equals("Student")) {
                            StudentDashboard.open();
                        }
                        else if (role.equals("Faculty")) {
                            FacultyDashboard.open();
                        }

                    } else {
                        JOptionPane.showMessageDialog(f, "Invalid Email or Password!");
                    }

                    rs.close();
                    ps.close();
                    con.close();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(f, ex.getMessage());
                }
            }
        });

        f.add(l1);
        f.add(t1);
        f.add(l2);
        f.add(t2);
        f.add(b);

        f.setSize(400, 250);
        f.setLayout(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        open();
    }
}