package com.department.ui;

import javax.swing.*;

public class Dashboard {

    public static void open() {

        JFrame f = new JFrame("Department Notice Management");

        JLabel title = new JLabel("Admin Dashboard");
        title.setBounds(150, 30, 200, 30);

        JButton b1 = new JButton("Add Notice");
        b1.setBounds(70, 90, 120, 40);

        JButton b2 = new JButton("View Notices");
        b2.setBounds(220, 90, 120, 40);

        JButton b3 = new JButton("Update/Delete Notice");
        b3.setBounds(70, 150, 150, 40);

        JButton b4 = new JButton("Add Circular");
        b4.setBounds(230, 150, 120, 40);

        JButton b5 = new JButton("View Circulars");
        b5.setBounds(70, 210, 120, 40);

        JButton b6 = new JButton("Search");
        b6.setBounds(230, 210, 120, 40);

        JButton b7 = new JButton("Finish");
        b7.setBounds(150, 270, 100, 35);

        b1.addActionListener(e -> AddNotice.open());

        b2.addActionListener(e -> ViewNotices.open());

        b3.addActionListener(e -> UpdateDeleteNotice.open());

        b4.addActionListener(e -> AddCircular.open());

        b5.addActionListener(e -> ViewCirculars.open());

        b6.addActionListener(e -> Search.open());

        b7.addActionListener(e -> {
            f.dispose();
            new LoginFrame();
            f.dispose();
        });

        f.add(title);
        f.add(b1);
        f.add(b2);
        f.add(b3);
        f.add(b4);
        f.add(b5);
        f.add(b6);
        f.add(b7);

        f.setSize(430, 370);
        f.setLayout(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        open();
    }
}