package lk.ijse.thogakade.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.thogakade.dto.Item;
import lk.ijse.thogakade.model.ItemModel;

import java.io.IOException;
import java.sql.SQLException;

public class NewItemFormController {


    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colCode;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colQtyOnHand;

    @FXML
    private AnchorPane root;

    @FXML
    private TextField txtCode;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtQtyOnHand;

    @FXML
    private TextField txtUnitPrice;

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        Parent anchorPane = FXMLLoader.load(getClass().getResource("/view/newdashbord_form.fxml"));

        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setTitle("Item Manage");
        stage.setScene(scene);

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event)  {
        String code = txtCode.getText();

        try{
            int affectedRow= ItemModel.delete(code);
            if(affectedRow>0)
                new Alert(Alert.AlertType.CONFIRMATION,"Deleted :)").show();
        }catch (Exception e){
            new Alert(Alert.AlertType.WARNING,"somethimg wrong :(").show();
        }

    }

    @FXML
    void btnGetAllOnAction(ActionEvent event) {

    }

    @FXML
    void btnSaveOnAction(ActionEvent event)  {

        String code = txtCode.getText();
        String discrip = txtDescription.getText();
        int qtyon = Integer.parseInt(txtQtyOnHand.getText());
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());

        Item item = new Item(code, discrip, unitPrice, qtyon);


        try{
            //int affectedRow= ItemController.save(code,discrip,qtyon,unitPrice);
            int affectedRow= ItemModel.save(item);
            if(affectedRow>0){
                new Alert(Alert.AlertType.CONFIRMATION,"item saved :)").show();
            }
        }catch (Exception e){
            new Alert(Alert.AlertType.WARNING,"something wrong :(").show();
        }

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException {
        String code = txtCode.getText();
        String discrip = txtDescription.getText();
        int qtyon = Integer.parseInt(txtQtyOnHand.getText());
        int unitPrice = Integer.parseInt(txtUnitPrice.getText());

        boolean affectedRow= ItemModel.update(code,discrip,qtyon,unitPrice);

    }

    @FXML
    void codeSearchOnAction(ActionEvent event) {

    }

}
