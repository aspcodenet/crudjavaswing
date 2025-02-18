package se.systementor;

import se.systementor.models.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;

public class EmployeesFrame {
    private JPanel panel1;
    private JButton addNewButton;
    private JButton editButton;
    private JButton deleteButton;
    private JTable table1;

    private Database database = new Database();

    public EmployeesFrame() {
        setupTable();
        loadEmployees();

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table1.getSelectedRow();
                if(row == -1){
                    JOptionPane.showMessageDialog(null, "Select a row"); // get the value of a row and column.
                    return;
                }
                int id = (int) table1.getValueAt(row, 0);
                onDelete(id);
            }

        });
        addNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNew();
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table1.getSelectedRow();
                if(row == -1){
                    JOptionPane.showMessageDialog(null, "Select a row"); // get the value of a row and column.
                    return;
                }
                int id = (int) table1.getValueAt(row, 0);
                onEdit(id);

            }
        });
    }


    private void onEdit(int id) {
        EditForm form = new EditForm();
        form.id = id;
        form.pack();
        form.setLocationRelativeTo(null);
        boolean ok = form.showDialog();
        if(ok){
            loadEmployees();
        }


    }

    private void onNew() {
        NewForm form = new NewForm();
        form.pack();
        form.setLocationRelativeTo(null);
        boolean ok = form.showDialog();
        if(ok){
            loadEmployees();

        }
    }

    private void onDelete(int id) {
        int answer = JOptionPane.showConfirmDialog(panel1,"Are you sure?", "Ta bort " +id, YES_NO_OPTION );
        if(answer == YES_OPTION){
            database.deleteEmployee(id);
            loadEmployees();
        }
    }

    private void loadEmployees() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        while(model.getRowCount() > 0) {
            model.removeRow(0);
        }
        for (Employee employee : database.allEmployees()){
            model.addRow(new Object[]{
                    employee.EmployeeId,
                    employee.FirstName,
                    employee.LastName, employee.Title,
                    employee.City,
                    employee.Country
            });
        }


    }

    public void show() {
        JFrame frame = new JFrame("EmployeesFrame");
        frame.setContentPane(new EmployeesFrame().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void setupTable() {
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //Only the third column
                return false;
            }
        };
        tableModel.addColumn("Id");
        tableModel.addColumn("First Name");
        tableModel.addColumn("Last Name");
        tableModel.addColumn("Title");
        tableModel.addColumn("City");
        tableModel.addColumn("Country");


        table1.setModel(tableModel);
    }

}
