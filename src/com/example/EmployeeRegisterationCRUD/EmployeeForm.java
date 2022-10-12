package com.example.EmployeeRegisterationCRUD;

import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class EmployeeForm {
    private JTextField txtName;
    private JTextField txtSalary;
    private JTextField txtPhone;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton searchButton;
    private JTable tableEmployee;
    private JTextField txtSearchID;
    private JPanel mainPanel;
    Connection con;
    PreparedStatement prepareStmt;

    public static void main(String[] args) {
        JFrame frame = new JFrame("EmployeeForm");
        frame.setContentPane(new EmployeeForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    //DB connection
    public void connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/employeeDB", "root", "root");
            System.out.println("success");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //Display employee table from the DB
    private void loadTable() {
        try {
            prepareStmt = con.prepareStatement("SELECT * from employee");
            ResultSet resultSet = prepareStmt.executeQuery();
            tableEmployee.setModel(DbUtils.resultSetToTableModel(resultSet));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public EmployeeForm() {
        connectDB();
        loadTable();

        //Add new Method
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String employeeName, employeeSalary, employeePhoneNumber;
                employeeName = txtName.getText();
                employeeSalary = txtSalary.getText();
                employeePhoneNumber = txtPhone.getText();

                try {
                    prepareStmt = con.prepareStatement("INSERT INTO employee (name,salary,phoneNumber) VALUES (?,?,?)");
                    prepareStmt.setString(1, employeeName);
                    prepareStmt.setString(2, employeeSalary);
                    prepareStmt.setString(3, employeePhoneNumber);
                    prepareStmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record is added to the DB!!!");
                    loadTable();
                    txtName.setText("");
                    txtSalary.setText("");
                    txtPhone.setText("");
                    txtName.requestFocus();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                    //ex.printStackTrace();
                }

            }
        });

        //Search Method
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String employeeID = txtSearchID.getText();

                try {
                    prepareStmt = con.prepareStatement("SELECT * FROM employee WHERE id = ?");
                    prepareStmt.setString(1, employeeID);
                    ResultSet resultSet = prepareStmt.executeQuery();

                    if (resultSet.next()) {
                        String employeeName = resultSet.getString(2);
                        String employeeSalary = resultSet.getString(3);
                        String employeePhoneNumber = resultSet.getString(4);

                        txtName.setText(employeeName);
                        txtSalary.setText(employeeSalary);
                        txtPhone.setText(employeePhoneNumber);

                    } else {
                        txtName.setText("");
                        txtSalary.setText("");
                        txtPhone.setText("");
                        JOptionPane.showMessageDialog(null, "Employee ID does not exist!");
                    }

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }


            }
        });

        // Update Method
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String employeeId, employeeName, employeeSalary, employeePhoneNumber;
                employeeId = txtSearchID.getText();
                employeeName = txtName.getText();
                employeeSalary = txtSalary.getText();
                employeePhoneNumber = txtPhone.getText();

                try {
                    prepareStmt = con.prepareStatement("UPDATE employee SET name = ? , salary = ? , phoneNumber = ? WHERE id = ?");
                    prepareStmt.setString(1, employeeName);
                    prepareStmt.setString(2, employeeSalary);
                    prepareStmt.setString(3, employeePhoneNumber);
                    prepareStmt.setString(4, employeeId);
                    prepareStmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record is updated in the DB!!!");
                    loadTable();
                    txtName.setText("");
                    txtSalary.setText("");
                    txtPhone.setText("");
                    txtName.requestFocus();

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
    }


}
