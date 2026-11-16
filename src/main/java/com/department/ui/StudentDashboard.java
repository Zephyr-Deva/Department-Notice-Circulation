package com.department.ui;

import javax.swing.*;

public class StudentDashboard {

    public static void open() {

        JFrame f = new JFrame("Student Dashboard");

        JLabel title = new JLabel("Student Dashboard");
        title.setBounds(150, 30, 200, 30);

        JButton b1 = new JButton("View Notices");
        b1.setBounds(80, 90, 120, 40);

        JButton b2 = new JButton("View Circulars");
        b2.setBounds(220, 90, 120, 40);

        JButton b3 = new JButton("Search");
        b3.setBounds(80, 150, 120, 40);

        JButton b4 = new JButton("Logout");
        b4.setBounds(220, 150, 120, 40);

        // View Notices
        b1.addActionListener(e -> {
            ViewNotices.open();
        });

        // View Circulars
        b2.addActionListener(e -> {
            ViewCirculars.open();
        });

        // Search
        b3.addActionListener(e -> {
            Search.open();
        });

        // Logout
        b4.addActionListener(e -> {
            f.dispose();
            LoginFrame.open();
        });

        f.add(title);
        f.add(b1);
        f.add(b2);
        f.add(b3);
        f.add(b4);

        f.setSize(420, 280);
        f.setLayout(null);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        open();
    }
}