package lk.ijse.thogakade.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.thogakade.dto.Customer;
import lk.ijse.thogakade.dto.tm.CustomerTM;
import lk.ijse.thogakade.model.CustomerModel;

import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class CustomerFormController {

    private static final String URL="jdbc:mysql://localhost:3306/thogakade";
    private static final Properties props=new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "80221474");
    }

    @FXML
    public TableView tblCustomer;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colSalary;

    @FXML
    private AnchorPane root;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtSalary;

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        Parent anchorPane = FXMLLoader.load(getClass().getResource("/view/newdashbord_form.fxml"));

        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setTitle("Item Manage");
        stage.setScene(scene);
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        txtSalary.clear();
        txtName.clear();
        txtId.clear();
        txtAddress.clear();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText();

        Customer customer = new Customer();
        customer.setId(id);

        try {
            boolean update = CustomerModel.delete(customer);
            if(update){
                new Alert(Alert.AlertType.CONFIRMATION,"Customer Deleted ! :)").show();
            }
        } catch (SQLException troubles) {
            new Alert(Alert.AlertType.WARNING,"Something happened :(").show();
            troubles.printStackTrace();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event)  {
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        double  salary = Double.parseDouble(txtSalary.getText());
        Customer customer = new Customer(id, name, address, salary);

        try {
            boolean save = CustomerModel.save(customer);
            if(save){
                new Alert(Alert.AlertType.CONFIRMATION,"customer saved ! :)").show();
            }
        } catch (SQLException throwable) {
            new Alert(Alert.AlertType.WARNING,"something happened ! :(").show();
            throwable.printStackTrace();
        }
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        double salary = Double.parseDouble(txtSalary.getText());
        Customer customer=new Customer(id,name,address,salary);

        try {
            boolean update = CustomerModel.update(customer);
            if(update){
                new Alert(Alert.AlertType.CONFIRMATION,"customer updated ! :)").show();
            }
        } catch (SQLException throwables) {
            new Alert(Alert.AlertType.WARNING,"something happened ! :(").show();
            throwables.printStackTrace();
        }
    }

    @FXML
    void codeSearchOnAction(ActionEvent event)  {
        String id = txtId.getText();

        Customer customer=new Customer();
        customer.setId(id);

        try {
            Customer search = CustomerModel.search(customer);
            if(!(search == null)){
                txtId.setText(search.getId());
                txtName.setText(search.getName());
                txtAddress.setText(search.getAddress());
                txtSalary.setText(String.valueOf(search.getSalary()));
            }
        } catch (SQLException throwables) {
            new Alert(Alert.AlertType.WARNING,"Something happened :(").show();
            throwables.printStackTrace();
        }
    }

    @FXML
    void initialize() throws SQLException {

        setCellValueFactory();

        ObservableList<CustomerTM> objects = FXCollections.observableArrayList();
        List<Customer> all = CustomerModel.getAll();

        for (Customer customer: all) {
            objects.add(new CustomerTM(customer.getId()
                    ,customer.getName(),
                    customer.getAddress(),
                    customer.getSalary()
            ));
        }
        tblCustomer.setItems(objects);

    }
    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("addres"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
    }

}



