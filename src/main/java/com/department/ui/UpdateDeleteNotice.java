package com.department.ui;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

import com.department.config.DatabaseConnection;

public class UpdateDeleteNotice {

    static ArrayList<Integer> noticeIds = new ArrayList<>();

    public static void open() {

        JFrame f = new JFrame("Update / Delete Notice");

        JLabel title = new JLabel("Manage Department Notices");
        title.setBounds(250, 15, 250, 30);

        // Left side: Notice List
        JLabel l1 = new JLabel("Existing Notices");
        l1.setBounds(30, 60, 150, 25);

        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> list = new JList<>(model);

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBounds(30, 90, 300, 300);

        // Right side: Edit Notice
        JLabel l2 = new JLabel("Notice ID");
        l2.setBounds(370, 70, 100, 25);

        JTextField idField = new JTextField();
        idField.setBounds(480, 70, 200, 30);
        idField.setEditable(false);

        JLabel l3 = new JLabel("Notice Title");
        l3.setBounds(370, 120, 100, 25);

        JTextField titleField = new JTextField();
        titleField.setBounds(480, 120, 200, 30);

        JLabel l4 = new JLabel("Description");
        l4.setBounds(370, 170, 100, 25);

        JTextArea descriptionField = new JTextArea();

        JScrollPane descriptionScroll =
                new JScrollPane(descriptionField);
        descriptionScroll.setBounds(480, 170, 200, 100);

        JButton update = new JButton("Update");
        update.setBounds(400, 310, 120, 35);

        JButton delete = new JButton("Delete");
        delete.setBounds(550, 310, 120, 35);

        JButton refresh = new JButton("Refresh");
        refresh.setBounds(110, 410, 120, 30);

        // Load notices from database
        Runnable loadNotices = () -> {

            model.clear();
            noticeIds.clear();

            try {
                Connection con = DatabaseConnection.getConnection();

                if (con == null) {
                    JOptionPane.showMessageDialog(f,
                            "Database connection failed!");
                    return;
                }

                String sql = "SELECT notice_id, title FROM notices ORDER BY notice_id DESC";

                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);

                while (rs.next()) {

                    int id = rs.getInt("notice_id");
                    String noticeTitle = rs.getString("title");

                    noticeIds.add(id);
                    model.addElement("ID: " + id + " - " + noticeTitle);
                }

                rs.close();
                st.close();
                con.close();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, ex.getMessage());
            }
        };

        // Select a notice
        list.addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int index = list.getSelectedIndex();

                if (index != -1) {

                    int id = noticeIds.get(index);

                    try {
                        Connection con = DatabaseConnection.getConnection();

                        if (con == null) {
                            JOptionPane.showMessageDialog(f,
                                    "Database connection failed!");
                            return;
                        }

                        String sql = "SELECT * FROM notices WHERE notice_id=?";

                        PreparedStatement ps = con.prepareStatement(sql);
                        ps.setInt(1, id);

                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {
                            idField.setText(String.valueOf(id));
                            titleField.setText(rs.getString("title"));
                            descriptionField.setText(rs.getString("description"));
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

        // Update Notice
        update.addActionListener(e -> {

            if (idField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(f, "Please select a notice!");
                return;
            }

            if (titleField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(f, "Please enter notice title!");
                return;
            }

            try {
                Connection con = DatabaseConnection.getConnection();

                if (con == null) {
                    JOptionPane.showMessageDialog(f,
                            "Database connection failed!");
                    return;
                }

                String sql = "UPDATE notices SET title=?, description=? WHERE notice_id=?";

                PreparedStatement ps = con.prepareStatement(sql);

                ps.setString(1, titleField.getText());
                ps.setString(2, descriptionField.getText());
                ps.setInt(3, Integer.parseInt(idField.getText()));

                int result = ps.executeUpdate();

                if (result > 0) {
                    JOptionPane.showMessageDialog(f,
                            "Notice Updated Successfully!");

                    idField.setText("");
                    titleField.setText("");
                    descriptionField.setText("");

                    loadNotices.run();
                }

                ps.close();
                con.close();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, ex.getMessage());
            }
        });

        // Delete Notice
        delete.addActionListener(e -> {

            if (idField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(f, "Please select a notice!");
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                    f,
                    "Are you sure you want to delete this notice?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {

                try {
                    Connection con = DatabaseConnection.getConnection();

                    if (con == null) {
                        JOptionPane.showMessageDialog(f,
                                "Database connection failed!");
                        return;
                    }

                    String sql = "DELETE FROM notices WHERE notice_id=?";

                    PreparedStatement ps = con.prepareStatement(sql);

                    ps.setInt(1, Integer.parseInt(idField.getText()));

                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(f,
                            "Notice Deleted Successfully!");

                    idField.setText("");
                    titleField.setText("");
                    descriptionField.setText("");

                    loadNotices.run();

                    ps.close();
                    con.close();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(f, ex.getMessage());
                }
            }
        });

        // Refresh Notice List
        refresh.addActionListener(e -> loadNotices.run());

        // Initial loading
        loadNotices.run();

        f.add(title);
        f.add(l1);
        f.add(scroll);
        f.add(l2);
        f.add(idField);
        f.add(l3);
        f.add(titleField);
        f.add(l4);
        f.add(descriptionScroll);
        f.add(update);
        f.add(delete);
        f.add(refresh);

        f.setSize(750, 510);
        f.setLayout(null);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        open();
    }
}