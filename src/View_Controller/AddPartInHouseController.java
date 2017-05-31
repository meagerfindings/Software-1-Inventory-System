package View_Controller;

import Model.InhousePart;
import Model.Part;
import static Model.Product.parts;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author matgreten
 */
public class AddPartInHouseController implements Initializable {

    @FXML
    private RadioButton addPartInHouse;
    
    @FXML
    private ToggleGroup addPartToggleGroup;
    
    @FXML
    private RadioButton addPartOutsourced;
    
    @FXML
    private TextField partID;
    
    @FXML
    private TextField partName;
    
    @FXML
    private TextField partInventory;
    
    @FXML
    private TextField priceCost;
    
    @FXML
    private TextField partMax;
    
    @FXML
    private TextField partMin;
    
    @FXML
    private TextField machineID;
    
    @FXML
    private Button save;
    
    @FXML
    private Button cancel;
    
    @FXML      //Switches to outsourced when outsourced radio button is selected
    private void outSourced(ActionEvent event) throws IOException {
        System.out.println("Opening Add Part Outsourced Screen.");
        
        Parent addPartOutParent = FXMLLoader.load(getClass().getResource("AddPartOutsourced.fxml"));
        Scene addPartOutScene = new Scene(addPartOutParent);
        Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        newStage.setScene(addPartOutScene);
        newStage.show();
    }
    
    @FXML
    public void returnToMain(ActionEvent event) throws IOException {
        FXMLLoader mainLoader = new FXMLLoader(MainScreenController.class.getResource("/View_Controller/MainScreen.fxml"));
        AnchorPane mainScreen = (AnchorPane) mainLoader.load();
        Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                
        newStage.setTitle("Inventory Management System");
        Scene sceneMainScreen = new Scene(mainScreen);
        
        newStage.setScene(sceneMainScreen);
        
        MainScreenController controller = mainLoader.getController();
        controller.setupMainScreen();

        newStage.show();
    }
    
    @FXML      //Saves data and returns to mainscreen when Save is clicked.
    private void saveButtonClicked(ActionEvent event) throws IOException {
        // Check for null or empty values in fields
        if (partName.getText() == null || partName.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Field Error");
            alert.setHeaderText("Name Field is empty.");
            alert.setContentText("Please place a value in the Name field. All fields in the form must be filled out.");
            
            alert.showAndWait();
        } else if (partInventory.getText() == null || partInventory.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Field Error");
            alert.setHeaderText("Inventory Field is empty.");
            alert.setContentText("Please place a value in the Inventory field. All fields in the form must be filled out.");
            
            alert.showAndWait();
        } else if (priceCost.getText() == null || priceCost.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Field Error");
            alert.setHeaderText("Price Field is empty.");
            alert.setContentText("Please place a value in the Price field. All fields in the form must be filled out.");
            
            alert.showAndWait();
        } else if (partMax.getText() == null || partMax.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Field Error");
            alert.setHeaderText("Max Field is empty.");
            alert.setContentText("Please place a value in the Max field. All fields in the form must be filled out.");
            
            alert.showAndWait();
        } else if (partMin.getText() == null || partMin.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Field Error");
            alert.setHeaderText("Min Field is empty.");
            alert.setContentText("Please place a value in the Min field. All fields in the form must be filled out.");
            
            alert.showAndWait();
        } else if (machineID.getText() == null || machineID.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Field Error");
            alert.setHeaderText("Machine ID Field is empty.");
            alert.setContentText("Please place a value in the Machine ID field. All fields in the form must be filled out.");
            
            alert.showAndWait();
        } else {
            int id = Part.generatePartID();
            double price = Double.valueOf(priceCost.getText());
            String name = String.valueOf(partName.getText());
            int instock = Integer.valueOf(partInventory.getText());
            int max = Integer.valueOf(partMax.getText());
            int min = Integer.valueOf(partMin.getText());
            int machID = Integer.valueOf(machineID.getText());

            if (max <= min || min >= max) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Maximum/Minimum Error");
                alert.setContentText("Maximum must have a value greater than minimum.\nMinimum must have a value less than maximum.");

                alert.showAndWait();

            } else if (instock < min) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Inventory Error");
                alert.setHeaderText("To few parts entered for Inventory:");
                alert.setContentText("The inventory cannot be set lower than the Minimum: " + min + ".");

                alert.showAndWait();

            }else if (instock > max){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Inventory Error");
                alert.setHeaderText("To many parts entered for Inventory:");
                alert.setContentText("The inventory cannot be set higher than the Maximum: " + max + ".");

                alert.showAndWait();

            } else {

                parts.add( new InhousePart(id, name, price, instock, max, min, machID));
                System.out.println("Saved part: " + name + ".");

                returnToMain(event);
            }
        }
    }
    
    @FXML      //Exits to mainscreen when Cancel is clicked.
    private void cancelButtonClicked(ActionEvent event) throws IOException {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Confirmation");
        alert.setHeaderText("Cancelling Part Addition:");
        alert.setContentText("If you choose OK you will be returned to the main screen and your changes will NOT be saved.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // OK was selected, move back to the main screen.
        
            System.out.println("Cancelling addition of part.");
            returnToMain(event);
        } else {
            // Cancel was selected, do nothing.
        }
        
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 
    }    
       
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert save != null : "fx:id=\"save\" was not injected: check your FXML file 'AddPartInHouse.fxml'.";
        assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'AddPartInHouse.fxml'.";
        assert addPartInHouse != null : "fx:id=\"addPartInHouse\" was not injected: check your FXML file 'AddPartInHouse.fxml'.";
        assert addPartToggleGroup != null : "fx:id=\"addPartToggleGroup\" was not injected: check your FXML file 'AddPartInHouse.fxml'.";
        assert addPartOutsourced != null : "fx:id=\"addPartOutsourced\" was not injected: check your FXML file 'AddPartInHouse.fxml'.";
        assert partID != null : "fx:id=\"partID\" was not injected: check your FXML file 'AddPartInHouse.fxml'.";
        assert partName != null : "fx:id=\"partName\" was not injected: check your FXML file 'AddPartInHouse.fxml'.";
        assert priceCost != null : "fx:id=\"priceCost\" was not injected: check your FXML file 'AddPartInHouse.fxml'.";
        assert partMax != null : "fx:id=\"partMax\" was not injected: check your FXML file 'AddPartInHouse.fxml'.";
        assert machineID != null : "fx:id=\"machineID\" was not injected: check your FXML file 'AddPartInHouse.fxml'.";
        assert partMin != null : "fx:id=\"partMin\" was not injected: check your FXML file 'AddPartInHouse.fxml'.";
        assert partInventory != null : "fx:id=\"partInventory\" was not injected: check your FXML file 'AddPartInHouse.fxml'.";
    }
}
