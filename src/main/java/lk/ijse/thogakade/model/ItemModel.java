package lk.ijse.thogakade.model;

import lk.ijse.thogakade.CrudUtil.CrudUtil;
import lk.ijse.thogakade.db.DBConnection;
import lk.ijse.thogakade.dto.CartDTO;
import lk.ijse.thogakade.dto.Customer;
import lk.ijse.thogakade.dto.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ItemModel {
    private static final String URL="jdbc:mysql://localhost:3306/thogakade";
    private static Properties PROPS=new Properties();

    static {
        PROPS.setProperty("user", "root");
        PROPS.setProperty("password", "80221474");
    }

    public static int save(Item item) throws SQLException {

        try(Connection con= DriverManager.getConnection(URL,PROPS)){
            String sql = "INSERT INTO item (code, description, unitPrice, qtyOnHand)" +
                    "VALUES (?,?,?,?)";
            PreparedStatement pstm= con.prepareStatement(sql);

            pstm.setString(1,item.getCode());
            pstm.setString(2,item.getDescription());
            pstm.setDouble(3,item.getNitPrice());
            pstm.setDouble(4,item.getQtyONHand());
            return pstm.executeUpdate();
        }
    }

    public static boolean update(String code, String description, double unitPrice, int qtyOnHand) throws SQLException {
        try (Connection con = DriverManager.getConnection(URL, PROPS)) {
            String sql = "UPDATE Item SET description = ?, unitPrice = ?, " +
                    "qtyOnHand = ? WHERE code = ?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, description);
            pstm.setDouble(2, unitPrice);
            pstm.setInt(3, qtyOnHand);
            pstm.setString(4, code);

            return pstm.executeUpdate() > 0;
        }
    }
    public static int delete(String code) throws SQLException {

        try(Connection con= DBConnection.getInstance().getConnection()){// singletone used...:).
            String sql="DELETE FROM item WHERE code=?;";
            PreparedStatement pstm=con.prepareStatement(sql);
            pstm.setString(1,code);
            int effectedRow = pstm.executeUpdate();
            return effectedRow;
        }
    }

    public static List<String> getItemCode() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();

        ResultSet resultSet = con.createStatement().executeQuery("SELECT code FROM item ");

        List<String> code = new ArrayList<>();

        while (resultSet.next()) {
            code.add(resultSet.getString(1));
        }
        return code;
    }

    public static Item searchByCode(String code) throws SQLException {

        Connection con = DBConnection.getInstance().getConnection();

        PreparedStatement pstm = con.prepareStatement("SELECT * FROM item WHERE code = ?");
        pstm.setString(1, code);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return  new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4)
            );
        }
        return null;
    }

    public static boolean updateQty(List<CartDTO> cartDTOList) throws SQLException {
        for(CartDTO dto : cartDTOList) {
            if(!updateQty(dto)) {
                return false;
            }
        }
        return true;
    }

    private static boolean updateQty(CartDTO dto) throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();

        String sql = "UPDATE Item SET qtyOnHand = (qtyOnHand - ?) WHERE code = ?";
        PreparedStatement pstm = con.prepareStatement(sql);
        pstm.setInt(1, dto.getQty());
        pstm.setString(2, dto.getCode());

        return pstm.executeUpdate() > 0;
//        return CrudUtil.execute(sql,dto);
    }

}
