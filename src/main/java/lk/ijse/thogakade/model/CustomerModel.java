package lk.ijse.thogakade.model;

import lk.ijse.thogakade.db.DBConnection;
import lk.ijse.thogakade.dto.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerModel {

    public static boolean save(Customer customer) throws SQLException {

        try (Connection connection= DBConnection.getInstance().getConnection()){
            String sql = "INSERT INTO Customer(id, name, address, salary)" +
                    "VALUES(?, ?, ?, ?)";
            PreparedStatement pstm=connection.prepareStatement(sql);
            pstm.setString(1,customer.getId());
            pstm.setString(2,customer.getName());
            pstm.setString(3,customer.getAddress());
            pstm.setDouble(4,customer.getSalary());
            return pstm.executeUpdate()>0;
        }
    }

    public static boolean update(Customer customer) throws SQLException {

        try (Connection connection=DBConnection.getInstance().getConnection()){
            String sql = "UPDATE Customer SET name = ?, address = ?, salary = ? WHERE id = ?";
            PreparedStatement pstm=connection.prepareStatement(sql);
            pstm.setString(1,customer.getName());
            pstm.setString(2,customer.getAddress());
            pstm.setDouble(3,customer.getSalary());
            pstm.setString(4,customer.getId());
            return pstm.executeUpdate()>0;
        }
    }

    public static boolean delete(Customer customer) throws SQLException {

        try(Connection connection=DBConnection.getInstance().getConnection()){
            String sql="DELETE FROM customer WHERE id=?;";
            PreparedStatement pstm=connection.prepareStatement(sql);
            pstm.setString(1, customer.getId());
            return pstm.executeUpdate()>0;
        }
    }
    public static Customer search(Customer customer) throws SQLException {

        try(Connection connection=DBConnection.getInstance().getConnection()){
            String sql ="SELECT * FROM Customer WHERE id = ?";
            PreparedStatement pstm=connection.prepareStatement(sql);
            pstm.setString(1,customer.getId());
            ResultSet resultSet = pstm.executeQuery();
            if (resultSet.next()) {
                return new Customer(resultSet.getString(1)
                                    ,resultSet.getString(2)
                                    ,resultSet.getString(3)
                                    ,resultSet.getDouble(4));
            }
            return null;
        }
    }

    public static List<Customer> getAll() throws SQLException {

        try(Connection connection=DBConnection.getInstance().getConnection()){
            String sql = "SELECT * FROM customer";
            PreparedStatement pstm=connection.prepareStatement(sql);

            ResultSet resultSet=pstm.executeQuery();

            ArrayList<Customer> customers =new ArrayList<>();

            while (resultSet.next()){
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String address = resultSet.getString(3);
                double salary = resultSet.getDouble(4);

                Customer customer = new Customer(id, name, address, salary);
                customers.add(customer);
            }
            return customers;
        }
    }

    public static Customer searchById(String id) throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();

        PreparedStatement pstm = con.prepareStatement("SELECT * FROM Customer WHERE id = ?");
        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return  new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4)
            );
        }
        return null;
    }
    public static List<String> loadIds() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        ResultSet resultSet = con.createStatement().executeQuery("SELECT id FROM Customer");

        List<String> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(resultSet.getString(1));
        }
        return data;
    }
}
