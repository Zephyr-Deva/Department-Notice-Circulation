package com.department.ui;

import javax.swing.*;
import java.sql.*;
import java.awt.Desktop;
import java.io.File;
import java.util.ArrayList;

import com.department.config.DatabaseConnection;

public class Search {

    static ArrayList<String> types = new ArrayList<>();
    static ArrayList<Integer> ids = new ArrayList<>();
    static ArrayList<String> paths = new ArrayList<>();

    public static void open() {

        JFrame f = new JFrame("Search Notice / Circular");

        JLabel title = new JLabel("Search Notice / Circular");
        title.setBounds(250, 20, 250, 30);

        JLabel l1 = new JLabel("Enter Title");
        l1.setBounds(40, 70, 100, 30);

        JTextField t1 = new JTextField();
        t1.setBounds(140, 70, 300, 30);

        JButton search = new JButton("Search");
        search.setBounds(460, 70, 100, 30);

        JLabel l2 = new JLabel("Search Results");
        l2.setBounds(40, 120, 150, 25);

        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> list = new JList<>(model);

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBounds(40, 150, 300, 200);

        JLabel l3 = new JLabel("Details");
        l3.setBounds(380, 120, 100, 25);

        JTextArea details = new JTextArea();
        details.setEditable(false);

        JScrollPane detailScroll = new JScrollPane(details);
        detailScroll.setBounds(380, 150, 300, 200);

        JButton openPDF = new JButton("Open Circular PDF");
        openPDF.setBounds(380, 370, 160, 35);

        JButton close = new JButton("Close");
        close.setBounds(550, 370, 100, 35);

        // Search Button
        search.addActionListener(e -> {

            String keyword = "%" + t1.getText().trim() + "%";

            model.clear();
            types.clear();
            ids.clear();
            paths.clear();
            details.setText("");

            if (t1.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(f,
                        "Please enter something to search!");
                return;
            }

            try {

                Connection con = DatabaseConnection.getConnection();

                if (con == null) {
                    JOptionPane.showMessageDialog(f,
                            "Database connection failed!");
                    return;
                }

                String sql =
                        "SELECT 'Notice' AS type, notice_id AS item_id, "
                                + "title, description, notice_date AS item_date, "
                                + "NULL AS file_path FROM notices "
                                + "WHERE title LIKE ? OR description LIKE ? "
                                + "UNION ALL "
                                + "SELECT 'Circular' AS type, circular_id AS item_id, "
                                + "title, description, circular_date AS item_date, "
                                + "file_path FROM circulars "
                                + "WHERE title LIKE ? OR description LIKE ?";

                PreparedStatement ps = con.prepareStatement(sql);

                ps.setString(1, keyword);
                ps.setString(2, keyword);
                ps.setString(3, keyword);
                ps.setString(4, keyword);

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {

                    String type = rs.getString("type");
                    int id = rs.getInt("item_id");
                    String name = rs.getString("title");
                    String path = rs.getString("file_path");

                    types.add(type);
                    ids.add(id);
                    paths.add(path);

                    model.addElement(type + " - " + name);
                }

                if (model.isEmpty()) {
                    JOptionPane.showMessageDialog(f,
                            "No Results Found!");
                }

                rs.close();
                ps.close();
                con.close();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f,
                        "Error: " + ex.getMessage());
            }
        });

        // Select Search Result
        list.addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int index = list.getSelectedIndex();

                if (index >= 0 && index < ids.size()) {

                    try {

                        Connection con = DatabaseConnection.getConnection();

                        if (con == null) {
                            JOptionPane.showMessageDialog(f,
                                    "Database connection failed!");
                            return;
                        }

                        String type = types.get(index);
                        int id = ids.get(index);

                        String sql;

                        if (type.equals("Notice")) {
                            sql = "SELECT * FROM notices WHERE notice_id=?";
                        } else {
                            sql = "SELECT * FROM circulars WHERE circular_id=?";
                        }

                        PreparedStatement ps = con.prepareStatement(sql);
                        ps.setInt(1, id);

                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {

                            details.setText(
                                    "Type: " + type + "\n\n"
                                            + "Title: " + rs.getString("title") + "\n\n"
                                            + "Description: " + rs.getString("description") + "\n\n"
                                            + "Date: " + (type.equals("Notice")
                                            ? rs.getDate("notice_date")
                                            : rs.getDate("circular_date"))
                            );
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

        // Open Circular PDF
        openPDF.addActionListener(e -> {

            int index = list.getSelectedIndex();

            if (index == -1 || index >= ids.size()) {
                JOptionPane.showMessageDialog(f,
                        "Please select a circular!");
                return;
            }

            if (!types.get(index).equals("Circular")) {
                JOptionPane.showMessageDialog(f,
                        "PDF is available only for circulars!");
                return;
            }

            try {

                String path = paths.get(index);
                File file = new File(path);

                if (file.exists() && Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                } else {
                    JOptionPane.showMessageDialog(f,
                            "PDF file not found or cannot be opened!");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f,
                        "Error opening PDF: " + ex.getMessage());
            }
        });

        close.addActionListener(e -> f.dispose());

        f.add(title);
        f.add(l1);
        f.add(t1);
        f.add(search);
        f.add(l2);
        f.add(scroll);
        f.add(l3);
        f.add(detailScroll);
        f.add(openPDF);
        f.add(close);

        f.setSize(730, 480);
        f.setLayout(null);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        open();
    }
}