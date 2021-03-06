package View_Controller;

import Model.InhousePart;
import Model.OutsourcedPart;
import static Model.Product.parts;
import static View_Controller.MainScreenController.currentPart;
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
public class ModifyPartOutsourcedController implements Initializable {
    
    @FXML
    private RadioButton modifyPartInHouse;
    
    @FXML
    private ToggleGroup modifyPartToggleGroup;
    
    @FXML
    private RadioButton modifyPartOutsourced;
    
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
    private TextField compName;
    
    @FXML
    private Button save;
    
    @FXML
    private Button cancel;
    
    
    @FXML      //Switches to inhouse when inhouse radio button is selected
    private void inhouse(ActionEvent event) throws IOException {
        System.out.println("Opening Modify Part inhouse Screen.");
                
        Parent modifyPartOutParent = FXMLLoader.load(getClass().getResource("ModifyPartInHouse.fxml"));
        Scene modifyPartOutScene = new Scene(modifyPartOutParent);
        Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        newStage.setScene(modifyPartOutScene);
        newStage.show();
    }
    
    @FXML
    public void returnToMain(ActionEvent event) throws IOException {
        FXMLLoader mainLoader = new FXMLLoader(MainScreenController.class.getResource("/View_Controller/MainScreen.fxml"));
        AnchorPane mainScreen = (AnchorPane) mainLoader.load();
        Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                
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
        } else if (compName.getText() == null || compName.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Field Error");
            alert.setHeaderText("Company Name Field is empty.");
            alert.setContentText("Please place a value in the Company Name field. All fields in the form must be filled out.");
            
            alert.showAndWait();
        } else {
        
            int position = currentPart.getPartPosition(currentPart);

            int id = currentPart.getPartID();
            double price = Double.valueOf(priceCost.getText());
            String name = String.valueOf(partName.getText());
            int instock = Integer.valueOf(partInventory.getText());
            int max = Integer.valueOf(partMax.getText());
            int min = Integer.valueOf(partMin.getText());
            String companyName = String.valueOf(compName.getText());

            if (max <= min || min >= max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Maximum/Minimum Error");
                alert.setContentText("Maximum must have a value greater than minimum.\nMinimum must have a value less than maximum.");

                alert.showAndWait();

            } else if(instock > max){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Inventory Error");
                alert.setHeaderText("To many parts entered for Inventory:");
                alert.setContentText("The inventory cannot be set higher than the Maximum: " + max + ".");

                alert.showAndWait();

            } else if (instock < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Inventory Error");
                alert.setHeaderText("To few parts entered for Inventory:");
                alert.setContentText("The inventory cannot be set lower than the Minimum: " + min + ".");

                alert.showAndWait();

            } else {

                OutsourcedPart modifedPart = new OutsourcedPart(id, name, price, instock, max, min, companyName);

                parts.set(position, modifedPart);

                returnToMain(event);

            }
        }
    }
    
    @FXML      //Exits to mainscreen when Cancel is clicked.
    private void cancelButtonClicked(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Confirmation");
        alert.setHeaderText("Cancelling Part Modification:");
        alert.setContentText("If you choose OK you will be returned to the main screen and your changes will NOT be saved.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // OK was selected, move back to the main screen.
        
            System.out.println("Cancelling modification of part.");
            returnToMain(event);
        } else {
            // Cancel was selected, do nothing.
        }
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initilialize text fields in the modify part fields based on the currentPart selected in the mainscreen.
        
        OutsourcedPart currentOutPart;
        
        if (currentPart.getClass() == InhousePart.class){ //If the part is actually an Inhouse part, we strip the data, create a new Outsource part and pass this to the initializer.
            int id = currentPart.getPartID();
            double price = currentPart.getPrice();
            String name = currentPart.getName();
            int instock = currentPart.getInstock();
            int max = currentPart.getMax();
            int min = currentPart.getMin();
            String companyName = "No Company Name assigned yet.";

            currentOutPart = new OutsourcedPart(id, name, price, instock, max, min, companyName);
        } else { // The part already is an Outsourced part, but showing as supertype, so recast.
            currentOutPart = (OutsourcedPart) currentPart; // Recasting currentPart from being a Supertype (Part) back down to being SubType (OutsourcedPart)
        }
        
        partID.setText(Integer.toString(currentOutPart.getPartID()));
        partName.setText(currentOutPart.getName());
        partInventory.setText(Integer.toString(currentOutPart.getInstock()));
        partMax.setText(Integer.toString(currentOutPart.getMax()));
        partMin.setText(Integer.toString(currentOutPart.getMin()));
        priceCost.setText(Double.toString(currentOutPart.getPrice()));
        if (currentOutPart.getCompanyName() == null || currentOutPart.getCompanyName().isEmpty()){
            compName.setText("No Company Name assigned yet.");
        } else {
        compName.setText(currentOutPart.getCompanyName());
        }
    }    
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert save != null : "fx:id=\"save\" was not injected: check your FXML file 'AddPartInHouse.fxml'.";
        assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'AddPartInHouse.fxml'.";
        assert modifyPartInHouse != null : "fx:id=\"addPartInHouse\" was not injected: check your FXML file 'AddPartInHouse.fxml'.";
        assert modifyPartToggleGroup != null : "fx:id=\"addPartToggleGroup\" was not injected: check your FXML file 'AddPartInHouse.fxml'.";
        assert modifyPartOutsourced != null : "fx:id=\"addPartOutsourced\" was not injected: check your FXML file 'AddPartInHouse.fxml'.";
        assert partID != null : "fx:id=\"partID\" was not injected: check your FXML file 'AddPartInHouse.fxml'.";
        assert partName != null : "fx:id=\"partName\" was not injected: check your FXML file 'AddPartInHouse.fxml'.";
        assert priceCost != null : "fx:id=\"priceCost\" was not injected: check your FXML file 'AddPartInHouse.fxml'.";
        assert partMax != null : "fx:id=\"partMax\" was not injected: check your FXML file 'AddPartInHouse.fxml'.";
        assert compName != null : "fx:id=\"machineID\" was not injected: check your FXML file 'AddPartInHouse.fxml'.";
        assert partMin != null : "fx:id=\"partMin\" was not injected: check your FXML file 'AddPartInHouse.fxml'.";
        assert partInventory != null : "fx:id=\"partInventory\" was not injected: check your FXML file 'AddPartInHouse.fxml'.";
    }
}
