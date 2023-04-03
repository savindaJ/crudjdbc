package lk.ijse.thogakade.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.thogakade.dto.CartDTO;
import lk.ijse.thogakade.dto.Customer;
import lk.ijse.thogakade.dto.Item;
import lk.ijse.thogakade.dto.tm.CartTM;
import lk.ijse.thogakade.model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlaceOrderFormController {

    @FXML
    public Label lblNetTotal;

    @FXML
    private JFXComboBox<String> cmbCustomerId;

    @FXML
    private JFXComboBox<String> cmbItemCode;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colItemCode;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private Label lblCustomerName;

    @FXML
    private Label lblDescription;

    @FXML
    private Label lblOrderDate;

    @FXML
    private Label lblOrderId;

    @FXML
    private Label lblQtyOnHand;

    @FXML
    private Label lblUnitPrice;

    @FXML
    private AnchorPane pane;

    @FXML
    private TableView<CartTM> tblOrderCart;

    @FXML
    private TextField txtQty;

    private ObservableList<CartTM> obList = FXCollections.observableArrayList();

    @FXML
    void initialize() throws SQLException {
        setCellValueFactory();
        setOrderDate();
        loadCustomerIds();
        setItemCode();
        generateOrderID();
    }

    private void generateOrderID()  {
        String id= null;
        try {
            id = OrderModel.getNextOrderID();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        lblOrderId.setText(id);
    }

    private void setItemCode() throws SQLException {
        ObservableList<String> code = FXCollections.observableArrayList();
        List<String> itemCode= ItemModel.getItemCode();

        for (String itcode:itemCode){
            code.add(itcode);
        }
        cmbItemCode.setItems(code);
    }

    private void loadCustomerIds() throws SQLException {
        ObservableList<String> obList = FXCollections.observableArrayList();
        List<String> ids = CustomerModel.loadIds();

        for (String id : ids) {
            obList.add(id);
        }
        cmbCustomerId.setItems(obList);
    }

    private void setOrderDate() {
        lblOrderDate.setText(String.valueOf(LocalDate.now()));
    }

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {

        String code = cmbItemCode.getValue();
        String description = lblDescription.getText();
        int qty = Integer.parseInt(txtQty.getText());
        double unitPrice = Double.parseDouble(lblUnitPrice.getText());
        double total = qty * unitPrice;

        Button btn = new Button("Remove");
        setRemoveBtnOnAction(btn); /* set action to the btnRemove */

        if (!obList.isEmpty()) {
            for (int i = 0; i < tblOrderCart.getItems().size(); i++) {
                if (colItemCode.getCellData(i).equals(code)) {
                    qty += (int) colQty.getCellData(i);
                    total = qty * unitPrice;

                    obList.get(i).setQty(qty);
                    obList.get(i).setTotal(total);

                    tblOrderCart.refresh();
                    calculateNetTotal();
                    return;
                }
            }
        }

        CartTM tm = new CartTM(code, description, qty, unitPrice, total, btn);

        obList.add(tm);
        tblOrderCart.setItems(obList);

        calculateNetTotal();

        txtQty.setText("");
    }

    private void setRemoveBtnOnAction(Button btn) {
        btn.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (result.orElse(no) == yes) {

                int index = tblOrderCart.getSelectionModel().getSelectedIndex();
                System.out.println(index);
                obList.remove(index);

                tblOrderCart.refresh();
                //calculateNetTotal();
            }

        });
    }

    private void calculateNetTotal() {
        System.out.println(tblOrderCart.getItems().size());
        double netTotal = 0.0;
        for (int i = 0; i < tblOrderCart.getItems().size(); i++) {
            double total = (double) colTotal.getCellData(i);
            netTotal += total;
        }
        lblNetTotal.setText(String.valueOf(netTotal));
    }

    @FXML
    void btnBackOnAction(ActionEvent event) {

    }

    @FXML
    void btnNewCustomerOnAction(ActionEvent event) throws IOException {
        Parent anchorPane = FXMLLoader.load(getClass().getResource("/view/customer_form.fxml"));
        Scene scene = new Scene(anchorPane);

        Stage stage = new Stage();
        stage.setTitle("Customer Manage");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {

        String oId = lblOrderId.getText();
        String cusId = cmbCustomerId.getValue();

        List<CartDTO> cartDTOList = new ArrayList<>();

        for (int i = 0; i < tblOrderCart.getItems().size(); i++) {
            CartTM cartTM = obList.get(i);

            CartDTO dto = new CartDTO(
                    cartTM.getCode(),
                    cartTM.getQty(),
                    cartTM.getUnitPrice()
            );
            cartDTOList.add(dto);
        }

        boolean isPlaced = false;
        try {
            isPlaced = PlaceOrderModel.placeOrder(oId, cusId, cartDTOList);

            if(isPlaced) {
                new Alert(Alert.AlertType.CONFIRMATION, "Order Placed").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Order Not Placed").show();
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    @FXML
    void cmbCustomerOnAction(ActionEvent event) {
        String id = (String) cmbCustomerId.getValue();

        Customer customer = null;
        try {
            customer = CustomerModel.searchById(id);
        } catch (SQLException throwables) {
            new Alert(Alert.AlertType.WARNING,"Sql Error!").show();
        }
        lblCustomerName.setText(customer.getName());
    }

    @FXML
    void cmbItemOnAction(ActionEvent event) {
        String code=cmbItemCode.getValue();
        Item item=null;
//        fillItemFields(item);

        try {
            item=ItemModel.searchByCode(code);
            txtQty.requestFocus();
        }catch (Exception e){

        }
        lblDescription.setText(item.getDescription());
        lblUnitPrice.setText(String.valueOf(item.getNitPrice()));
        lblQtyOnHand.setText(String.valueOf(item.getQtyONHand()));
        txtQty.setText(String.valueOf(item.getQtyONHand()));
    }

    @FXML
    void txtQtyOnAction(ActionEvent event) {
        btnAddToCartOnAction(event);
    }

    void setCellValueFactory() {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }

}