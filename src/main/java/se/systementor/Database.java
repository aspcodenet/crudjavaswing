package se.systementor;

import se.systementor.models.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


//             conn.setAutoCommit(false);

public class Database {

    String url = "jdbc:mysql://localhost:3306/northwind";
    String user = "root";
    String password = "hejsan123";

    public void testTransaction(){
        try {
            Connection conn = getConnection();

            conn.setAutoCommit(false); // START TRANSACTION;

            Statement stmt = conn.createStatement();
            stmt.execute("UPDATE account set saldo = saldo - 100 where accountno='111-222-333'");
            stmt.execute("UPDATE account set saldo = saldo + 100 where accountno='555-999-222'");
            conn.commit(); // COMMIT;
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace(); // Hamnar vi här görs automatiskt en rollback
        }


    }


    public List<Employee> allEmployees() {
        ArrayList<Employee> results = new ArrayList<Employee>();

        try {
            Connection conn = getConnection();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Employees ");

            while (rs.next()) {
                Employee employee = new Employee();
                employee.EmployeeId = rs.getInt("EmployeeId");
                employee.City = rs.getString("City");
                employee.Country = rs.getString("Country");
                employee.FirstName = rs.getString("FirstName");
                employee.LastName = rs.getString("LastName");
                employee.Title = rs.getString("Title");
                results.add(employee);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;


    }



    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url,user,password);
    }

    public void deleteEmployee(int id) {
        Connection conn = null;
        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            // WHAT !!!! Är detta fel - han plussar ju en sträng istället för frågetecken
            // ? för alla STRING
            stmt.executeUpdate("DELETE FROM Employees WHERE EmployeeId = " + id);
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addEmployee(String firstName, String lastName) {
        Connection conn = null;
        try {
            conn = getConnection();

            // DENNA KOD ÄR OSÄKER !!!
            //String sql = "INSERT INTO Employees(FirstName,LastName) VALUES('" + firstName + "','" + lastName + "')";
            // KLAR ATT KÖRA!!!

            // MÅSTE GÖRA SÅ HÄR FÖR ATT VARA SÄKER MOT SQL INJECTION
            //                                                               1,2
            //String sql = "INSERT INTO Employees(FirstName,LastName) VALUES(?,?)";
//            stmt.setString(2, lastName);
//            stmt.setString(1, firstName);


            PreparedStatement  stmt = conn.prepareStatement("INSERT INTO Employees\n" +
                    "( LastName, FirstName, Title, TitleOfCourtesy, BirthDate, HireDate, Address, City, Region, PostalCode, Country, HomePhone, Extension, Photo, Notes, ReportsTo, PhotoPath, Salary)\n" +
                    "VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?);");
            stmt.setString(1, lastName);
            stmt.setString(2, firstName);
            stmt.setString(3, "");
            stmt.setString(4, "");
            stmt.setString(5, null);
            stmt.setString(6, null);
            stmt.setString(7, null);
            stmt.setString(8, null);
            stmt.setString(9, null);
            stmt.setString(10, null);
            stmt.setString(11, null);
            stmt.setString(12, null);
            stmt.setString(13, null);
            stmt.setString(14, null);
            stmt.setString(15, "");
            stmt.setString(16, null);
            stmt.setString(17, null);
            stmt.setString(18, null);

            int row = stmt.executeUpdate();

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Employee getEmployee(int id) {
        Employee employee = null;
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Employees where EmployeeId = " + id);

            if (rs.next()) {
                employee = new Employee();
                employee.EmployeeId = rs.getInt("EmployeeId");
                employee.City = rs.getString("City");
                employee.Country = rs.getString("Country");
                employee.FirstName = rs.getString("FirstName");
                employee.LastName = rs.getString("LastName");
                employee.Title = rs.getString("Title");
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employee;
    }

    public void updateEmployee(int id, String firstName, String lastName) {
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement  stmt = conn.prepareStatement("UPDATE Employees\n" +
                    " SET LastName=?, FirstName=? WHERE EmployeeId=?");
            stmt.setString(1, lastName);
            stmt.setString(2, firstName);
            stmt.setInt(3, id);

            int row = stmt.executeUpdate();

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
