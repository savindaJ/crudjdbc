package lk.ijse.thogakade.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class NewdashbordFormController {
    @FXML
    private AnchorPane root;

    @FXML
    private FXMLLoader fxmlLoader;

    @FXML
    void btnCustomerOnAction(ActionEvent event) throws IOException {
        Parent anchorPane = FXMLLoader.load(getClass().getResource("/view/customer_form.fxml"));

        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setTitle("Item Manage");
        stage.setScene(scene);
    }

    @FXML
    void btnItemOnAction(ActionEvent event) throws IOException {
        Parent anchorPane = FXMLLoader.load(getClass().getResource("/view/newitem_form.fxml"));

        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setTitle("Customer Mange");
        stage.setScene(scene);
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) throws IOException {
        Parent anchorPane = FXMLLoader.load(getClass().getResource("/view/order_frm.fxml"));

        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setTitle("Customer Mange");
        stage.setScene(scene);
    }
    private void setUi(String fileName) throws IOException {
        fxmlLoader = new FXMLLoader(getClass().getResource("/view/"+fileName));
        Pane root1 = fxmlLoader.load();
        try {
            root.getChildren().clear();
            root.getChildren().setAll(root1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
