package View_Controller;

import Model.InhousePart;
import Model.OutsourcedPart;
import Model.Part;
import Model.Product;
import static Model.Product.getPartData;
import static View_Controller.MainScreenController.currentProduct;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML ModifyProductController class
 *
 * @author matgreten
 */
public class ModifyProductController implements Initializable {
    
    @FXML
    private TextField productID;
    
    @FXML
    private TextField productName;
    
    @FXML
    private TextField productInventory;
    
    @FXML
    private TextField price;
    
    @FXML
    private TextField productMax;
    
    @FXML
    private TextField productMin;
    
    @FXML
    private TextField searchField;
    
    @FXML
    private TableView<Part> partsTable;
                
    @FXML
    ObservableList<Part> tempPartTableData=FXCollections.observableArrayList();
    
    @FXML
    private TableColumn<Part, Number> ptPartID;
    
    @FXML
    private TableColumn<Part, String> ptPartName;
    
    @FXML
    private TableColumn<Part, Number> ptInventoryLevel;
    
    @FXML
    private TableColumn<Part, Number> ptUnitPrice;
    
    @FXML
    private TableView<Part> productPartsTable;
    
    @FXML
    public static final ObservableList<Part> tempProductPartsList = FXCollections.observableArrayList();
                    
    @FXML
    private TableColumn<Part, Number> productPartID;
    
    @FXML
    private TableColumn<Part, String> productPartName;
    
    @FXML
    private TableColumn<Part, Number> productPartInventoryLevel;
    
    @FXML
    private TableColumn<Part, Number> productPartUnitPrice;
    
    @FXML
    private Button search;
    
    @FXML
    private Button save;
    
    @FXML
    private Button cancel;
    
    @FXML
    private Button add;
    
    @FXML
    private Button delete;
    
    @FXML
    FilteredList<Part> filteredParts = new FilteredList<>(tempPartTableData, search -> true);
    
    public static Part currentPartFromList; // Which part is currently selected in the parts table.
    
    public Part currentPartFromProductList; // Which part is currently selected in the product parts table.
    
    public static Product productToClone; // Product from the product list we want to copy into the product parts table.
    
    private int clonePart(Part sourcePart, ObservableList<Part> destinationList){ //Method for cloning parts into the producPartList as adding them only adds a reference that mutates the parts list and the productPartList when modified.
        int position = -1;
        
        // Extracting all the necessarypart information.
        int newPartID = sourcePart.getPartID();
        String newPartname = sourcePart.getName();
        double newPartPrice = sourcePart.getPrice();
        int newPartInventory = sourcePart.getInstock();
        int newPartMax = sourcePart.getMax();
        int newPartMin = sourcePart.getMin();
        
        if (sourcePart.getClass() == InhousePart.class){
            //create inhouse part with machine id
            InhousePart currentInPart;
            currentInPart = (InhousePart) sourcePart; // Recasting currentPart from being a Supertype (Part) back down to being SubType (InhousePart)

            int newPartMechID = currentInPart.getMachineID();
            
           destinationList.add(new InhousePart(newPartID, newPartname, newPartPrice, newPartInventory, newPartMax, newPartMin, newPartMechID));

            
        } else if (sourcePart.getClass() == OutsourcedPart.class){
            //create outsourced part with company name
            
            OutsourcedPart currentOutPart;
            currentOutPart = (OutsourcedPart) sourcePart; // Recasting currentPart from being a Supertype (Part) back down to being SubType (InhousePart)

            String newPartComp = currentOutPart.getCompanyName();
                        
            destinationList.add(new OutsourcedPart(newPartID, newPartname, newPartPrice, newPartInventory, newPartMax, newPartMin, newPartComp));

        } else {
            
            System.out.println("The inhouse/outsourced detector is not working.");
        }
                
        position = destinationList.size() - 1;
        
        return position;
    }
    
    @FXML
    private void addPartButtonCliked(ActionEvent event){
        if (currentPartFromList == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Part Selected");
            alert.setHeaderText("No Part selected to add.");
            alert.setContentText("Pressing Add without choosing a part will not work.");
            
            alert.showAndWait();
    
        } else if(currentPartFromList.getInstock() > 1){
            
            // Decrement inventory for the part in the parts list.
            int currentStock = currentPartFromList.getInstock();
            currentPartFromList.setInstock(currentStock - 1);
            
            // Setup search for part in Products Part list.
            boolean found = false;
            int selectedPartID = currentPartFromList.getPartID();

            for (int i = 0; i < tempProductPartsList.size(); i++) {

                int partID = tempProductPartsList.get(i).getPartID();

                if (selectedPartID == partID){
                    
                    //increment inventory by 1
                    currentStock = tempProductPartsList.get(i).getInstock();
                    int newstock = currentStock + 1; // Set the new stock to 1 more than the current stock.
                    
                    tempProductPartsList.get(i).setInstock(newstock); // Save the new stock amount.
                    found = true;
                    break;
                }
           }

           if (found == false){ // If the part was not found in the Product part list, clone it into the Product part list.
                int newPartPosition = clonePart(currentPartFromList, tempProductPartsList); // add the part
                tempProductPartsList.get(newPartPosition).setInstock(1); // set the inventory to 1 as this is the first part being added of this type.
           }            
            
        } else if (currentPartFromList.getInstock() == 1) {

            currentPartFromList.setInstock(0); //Since the current stock of the part in the Parts list is 1, set this to 0.

            // Setup search for part in Products Part list.
            boolean found = false;
            int selectedPartID = currentPartFromList.getPartID();
            
            for (int i = 0; i < tempProductPartsList.size(); i++) {

                int partID = tempProductPartsList.get(i).getPartID();

                if (selectedPartID == partID){
                    
                    //increment inventory by 1
                    int currentStock = tempProductPartsList.get(i).getInstock();
                    int newstock = currentStock + 1;
                    
                    tempProductPartsList.get(i).setInstock(newstock);
                    found = true;
                    break;
                }
           }

           if (found == false){ // If the part was not found in the Product part list, clone it into the Product part list.
                int newPartPosition = clonePart(currentPartFromList, tempProductPartsList); // add the part
                tempProductPartsList.get(newPartPosition).setInstock(1); // set the inventory to 1 as this is the first part being added of this type.
           }         
        }
    }
    
    @FXML
    private void searchPartButtonCliked(ActionEvent event){
        String searchValue = String.valueOf(searchField.getText());
                
        System.out.println("Searching for Part by the value: " + searchValue);
        
        filteredParts.setPredicate((Part part) -> {
            
                if (searchValue == null || searchValue.isEmpty()) {
                    return true;
                }

                // Check if the searchValue entry is a part number or part name
                boolean isInteger = false;

                try {
                    Integer.parseInt(searchValue); //If the integer parser can parse the string then we have an integer
                    isInteger = true;
                }
                catch (Exception e){
                    isInteger = false; // If the integer parser failed this was a true string.
                }

                if (isInteger == true){ //Handle searchvalue as an Integer.

                    if (Integer.toString(part.getPartID()).contains(searchValue)){ //Cast the PartID to a string to allow for contains to run agains searchValue string.
                        return true;
                    }

                } else { //Handle searchvalue as a String.

                    String upCaseSearch = searchValue.toUpperCase();

                    if (part.getName().toUpperCase().contains(upCaseSearch)) {
                        return true; //searchValue matches a partName.
                    }
                }
                return false; // searchValue must not have matched any values in our table

                });
    }
    
    @FXML
    private void deletePartButtonCliked(ActionEvent event){
        
        if (currentPartFromProductList == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Part Selected");
            alert.setHeaderText("No Part selected to Delete.");
            alert.setContentText("Pressing Delete without choosing a part will not work.");
            
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Deletion Confirmation");
            alert.setHeaderText("You are about to delete a part.");
            alert.setContentText("If you choose OK, the " + currentPartFromProductList.getName() + " will be removd from this product.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                // OK was selected, delete the part.
                deletePart(currentPartFromProductList);

            } else {
                // Cancel was selected, do nothing.
            }
        }
    }
    
    private void deletePart(Part inputPart){
        if (inputPart == null){
            System.out.println("there was no part selected");
    
        } else if(inputPart.getInstock() > 1){
            
            // Decrement inventory for the part in the Product Parts list.
            int currentStock = inputPart.getInstock();
            inputPart.setInstock(currentStock - 1);
            
            // Setup search for part in Parts list.
            boolean found = false;
            int selectedPartID = inputPart.getPartID();

            for (int i = 0; i < tempPartTableData.size(); i++) {

                int partID = tempPartTableData.get(i).getPartID();

                if (selectedPartID == partID){
                    
                    //increment inventory by 1
                    currentStock = tempPartTableData.get(i).getInstock();
                    int newstock = currentStock + 1;
                    
                    tempPartTableData.get(i).setInstock(newstock);
                    found = true;
                    break;
                }
           }

           if (found == false){ // If the part was not found in the Part list, clone it into the part list.
                int newPartPosition = clonePart(inputPart, tempPartTableData); // add the part
                tempPartTableData.get(newPartPosition).setInstock(1); // set the inventory to 1 as this is the first part being added of this type.
           }            
            
        } else if (inputPart.getInstock() == 1) {

            // Setup search for part in Products Part list.
            boolean found = false;
            int selectedPartID = inputPart.getPartID();
            
            for (int i = 0; i < tempPartTableData.size(); i++) {

                int partID = tempPartTableData.get(i).getPartID();

                if (selectedPartID == partID){
                    
                    //increment inventory by 1
                    int currentStock = tempPartTableData.get(i).getInstock();
                    int newstock = currentStock + 1;
                    
                    tempPartTableData.get(i).setInstock(newstock);
                    tempProductPartsList.remove(inputPart); //Since the current stock of the part in the Product Parts list is 1, remove the part from the Product Parts list.
                    found = true;
                    break;
                }
           }

           if (found == false){ // If the part was not found in the parts list, clone it into the parts list.
                int newPartPosition = clonePart(inputPart, tempPartTableData); // add the part
                tempPartTableData.get(newPartPosition).setInstock(1); // set the inventory to 1 as this is the first part being added of this type.
                tempProductPartsList.remove(inputPart); //Since the current stock of the part in the Product Parts list is 1, remove the part from the Product Parts list.
           }         
        }
    }
    
    @FXML
    private void returnToMain(ActionEvent event) throws IOException {
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
        if (productName.getText() == null || productName.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Field Error");
            alert.setHeaderText("Name Field is empty.");
            alert.setContentText("Please place a value in the Name field. All fields in the form must be filled out.");
            
            alert.showAndWait();
        } else if (productInventory.getText() == null || productInventory.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Field Error");
            alert.setHeaderText("Inventory Field is empty.");
            alert.setContentText("Please place a value in the Inventory field. All fields in the form must be filled out.");
            
            alert.showAndWait();
        } else if (price.getText() == null || price.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Field Error");
            alert.setHeaderText("Price Field is empty.");
            alert.setContentText("Please place a value in the Price field. All fields in the form must be filled out.");
            
            alert.showAndWait();
        } else if (productMax.getText() == null || productMax.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Field Error");
            alert.setHeaderText("Max Field is empty.");
            alert.setContentText("Please place a value in the Max field. All fields in the form must be filled out.");
            
            alert.showAndWait();
        } else if (productMin.getText() == null || productMin.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Field Error");
            alert.setHeaderText("Min Field is empty.");
            alert.setContentText("Please place a value in the Min field. All fields in the form must be filled out.");
            
            alert.showAndWait();
        } else {
        
            // Record text input from fields.
            String name = String.valueOf(productName.getText());
            int inventory = Integer.valueOf(productInventory.getText());
            
            if (inventory < 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Inventory Field Error");
                alert.setHeaderText("Inventory Field cannot be less than zero.");
                alert.setContentText("Inventory value entered: " + inventory + ".\nChanging your input value to 0.");

                alert.showAndWait();
                
                productInventory.setText("0");
            }
            
            double priceCost = Double.valueOf(price.getText());
            int max = Integer.valueOf(productMax.getText());
            int min = Integer.valueOf(productMin.getText());

            double partsTotalPrice = 0;
            for(int i = 0; i < tempProductPartsList.size();  i++){
            partsTotalPrice = partsTotalPrice + tempProductPartsList.get(i).getPrice();
            }
            
        
            if (tempProductPartsList.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No Parts Error");
                alert.setHeaderText("No Parts in Product.");
                alert.setContentText("Products must have at least one part. Please add one.");

                alert.showAndWait();

            } else if (priceCost < partsTotalPrice) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Product Price Error");
                alert.setHeaderText("Product Price is too low.");
                alert.setContentText("The price of a product cannot be lower than the price of its parts.\n\nCost of Parts: $" + partsTotalPrice + "\nProduct price entered: $" + priceCost);

                alert.showAndWait();

            } else if (max <= min || min >= max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Maximum/Minimum Error");
                alert.setContentText("Maximum must have a value greater than minimum.\nMinimum must have a value less than maximum.");

                alert.showAndWait();

            } else if(inventory > max){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Inventory Error");
                alert.setHeaderText("To many parts entered for Inventory:");
                alert.setContentText("The inventory cannot be set higher than the Maximum: " + max + ".");

                alert.showAndWait();

            } else if (inventory < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Inventory Error");
                alert.setHeaderText("To few parts entered for Inventory:");
                alert.setContentText("The inventory cannot be set lower than the Minimum: " + min + ".");

                alert.showAndWait();

            } else {

                for(int i = 0; i < tempPartTableData.size();  i++){

                    int targetID = tempPartTableData.get(i).getPartID();
                    int newInventory = tempPartTableData.get(i).getInstock();

                    int targetIndex = Product.getPartPositionByID(targetID);

                    Part targetPart = Product.parts.get(targetIndex);

                    int targetPartCurrentInventory = targetPart.getInstock();

                    if (targetPartCurrentInventory != newInventory){
                        targetPart.setInstock(newInventory);

                    }

                }

                if (productToClone.productParts == tempProductPartsList){
                    // No changes were made to the parts list. These aren't the parts you are looking to modify.
                } else {
                    //Adds parts to Product that were added in the modify screen
                    for(int x = 0; x < tempProductPartsList.size(); x++){
                        int tempID = tempProductPartsList.get(x).getPartID();
                        boolean partFound = false;


                        for(int i = 0; i < productToClone.productParts.size(); i++){
                            int targetID = productToClone.productParts.get(i).getPartID();

                            // If the ID's match, we've found our part.
                            if (tempID == targetID){
                                partFound = true;
                                break;
                            }
                        }

                        // Part was not found, add it in!
                        if (partFound == false){
                            clonePart(tempProductPartsList.get(x), productToClone.productParts);
                        }
                    }

                    //Updates inventory counts or deletes prexisiting parts if they were removed.
                    for(int i = 0; i < productToClone.productParts.size(); i++){
                        boolean partFound = false;
                        int targetID = productToClone.productParts.get(i).getPartID();


                        // Look through each part in the temp product parts list.
                        innerloop:
                        for(int x = 0; x < tempProductPartsList.size(); x++){

                            int tempID = tempProductPartsList.get(x).getPartID();

                            // If the ID's match, we've found our part.
                            if (tempID == targetID){
                                partFound = true;
                            }

                            // Part was found, update the inventory.
                            if (partFound == true){
                               int targetInventory = productToClone.productParts.get(i).getInstock();
                               int newInventory = tempProductPartsList.get(x).getInstock();

                               if (targetInventory != newInventory){
                                   productToClone.productParts.get(i).setInstock(newInventory);

                               }
                               break innerloop;
                            }
                        }

                        // Part was not found, remove it.
                        if (partFound == false){
                            productToClone.productParts.remove(i);
                        }
                    }
                }

                // Update product metadata.
                productToClone.setName(name);
                productToClone.setInstock(inventory);
                productToClone.setPrice(priceCost);
                productToClone.setMax(max);
                productToClone.setMin(min);


                cleanTempLists();
                returnToMain(event);
            }
        }
    }
    
    @FXML      //Returns to mainscreen when Cancel is clicked.
    private void cancelButtonClicked(ActionEvent event) throws IOException {
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Cancel Modification of Product");
        alert.setHeaderText("Cancelling Product Modication:");
        alert.setContentText("If you choose OK you will be returned to the main screen and your changes will NOT be saved.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // OK was selected, clear the lists and move back to the main screen.
            cleanTempLists();
        
            System.out.println("Cancelling modification of product.");
            returnToMain(event);
        } else {
            // Cancel was selected, do nothing.
        }
    }
    
    @FXML
    private void cleanTempLists(){
        // Remove the cloned parts from the temporary parts list holder.
        while (tempPartTableData.size() > 0) {
        for (int i = 0; i < tempPartTableData.size(); ++i) {
            
            tempPartTableData.remove(i); 
            
            }  
        }
        
        // Remove the cloned parts from the temporary productParts list holder.
        while (tempProductPartsList.size() > 0) {
        for (int i = 0; i < tempProductPartsList.size(); ++i) {
            
            tempProductPartsList.remove(i); 
            
            }  
        }
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Initializes text field values
        productID.setText(Integer.toString(currentProduct.getProductID()));
        productName.setText(currentProduct.getName());
        productInventory.setText(Integer.toString(currentProduct.getInstock()));
        productMax.setText(Integer.toString(currentProduct.getMax()));
        productMin.setText(Integer.toString(currentProduct.getMin()));
        price.setText(Double.toString(currentProduct.getPrice()));
        
        // Setup copy of Actual Parts list to the top table
        ObservableList<Part> partsToClone = getPartData();

        for(int i = 0; i < partsToClone.size();  i++) {
           clonePart(partsToClone.get(i), tempPartTableData);
        }
        
        //Setup copy of Actual Product Parts list to the bottom table
        ObservableList<Part> productPartsToClone = productToClone.getProductParts();
        for(int i = 0; i < productToClone.getProductParts().size();  i++) { 
           clonePart(productPartsToClone.get(i), tempProductPartsList);
        }
                
        //Initializes display of componenets for Parts Table
        ptPartID.setCellValueFactory(cellData -> cellData.getValue().partIDProperty());
        ptPartName.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        ptUnitPrice.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty());
        ptInventoryLevel.setCellValueFactory(cellData -> cellData.getValue().partInventoryProperty());
        
        //Initializes search componenets for Parts Table
        searchField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String searchValue) -> {
            SortedList<Part> sortedPartsList = new SortedList<>(filteredParts);
            sortedPartsList.comparatorProperty().bind(partsTable.comparatorProperty());             
            partsTable.setItems(sortedPartsList);
        });  
        
        //Initializes listener for selections in the Parts Table
        partsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, currentSelection) -> currentPartFromList = currentSelection);    
        
        //Initializes display of componenets for ProductParts Table
        productPartID.setCellValueFactory(cellData -> cellData.getValue().partIDProperty());
        productPartName.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        productPartUnitPrice.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty());
        productPartInventoryLevel.setCellValueFactory(cellData -> cellData.getValue().partInventoryProperty());
        
        //Initializes listener for selections in the ProductParts Table
        productPartsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, currentSelection) -> currentPartFromProductList = currentSelection);    
        
    }    
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert productID != null : "fx:id=\"productID\" was not injected: check your FXML file 'ModifyProduct.fxml'.";
        assert productName != null : "fx:id=\"productName\" was not injected: check your FXML file 'ModifyProduct.fxml'.";
        assert productInventory != null : "fx:id=\"productInventory\" was not injected: check your FXML file 'ModifyProduct.fxml'.";
        assert price != null : "fx:id=\"price\" was not injected: check your FXML file 'ModifyProduct.fxml'.";
        assert productMax != null : "fx:id=\"productMax\" was not injected: check your FXML file 'ModifyProduct.fxml'.";
        assert productMin != null : "fx:id=\"productMin\" was not injected: check your FXML file 'ModifyProduct.fxml'.";
        assert search != null : "fx:id=\"search\" was not injected: check your FXML file 'ModifyProduct.fxml'.";
        assert searchField != null : "fx:id=\"searchField\" was not injected: check your FXML file 'ModifyProduct.fxml'.";
        assert partsTable != null : "fx:id=\"partsTable\" was not injected: check your FXML file 'ModifyProduct.fxml'.";
        assert ptPartID != null : "fx:id=\"ptPartID\" was not injected: check your FXML file 'ModifyProduct.fxml'.";
        assert ptPartName != null : "fx:id=\"ptPartName\" was not injected: check your FXML file 'ModifyProduct.fxml'.";
        assert ptInventoryLevel != null : "fx:id=\"ptInventoryLevel\" was not injected: check your FXML file 'ModifyProduct.fxml'.";
        assert ptUnitPrice != null : "fx:id=\"ptUnitPrice\" was not injected: check your FXML file 'ModifyProduct.fxml'.";
        assert productPartsTable != null : "fx:id=\"productTable\" was not injected: check your FXML file 'ModifyProduct.fxml'.";
        assert productPartID != null : "fx:id=\"protPartID\" was not injected: check your FXML file 'ModifyProduct.fxml'.";
        assert productPartName != null : "fx:id=\"protPartName\" was not injected: check your FXML file 'ModifyProduct.fxml'.";
        assert productPartInventoryLevel != null : "fx:id=\"protInventoryLevel\" was not injected: check your FXML file 'ModifyProduct.fxml'.";
        assert productPartUnitPrice != null : "fx:id=\"protUnitPrice\" was not injected: check your FXML file 'ModifyProduct.fxml'.";
        assert add != null : "fx:id=\"add\" was not injected: check your FXML file 'ModifyProduct.fxml'.";
        assert delete != null : "fx:id=\"delete\" was not injected: check your FXML file 'ModifyProduct.fxml'.";
        assert save != null : "fx:id=\"save\" was not injected: check your FXML file 'ModifyProduct.fxml'.";
        assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'ModifyProduct.fxml'.";
    }
    
    public void setupModifyProductScreen() {
        // Add observable list data to the parts table
        partsTable.setItems(tempPartTableData); //tempPartList
        
        // Add observable list data to the parts in product table, this is a copy of the Product's Parts data
        productPartsTable.setItems(tempProductPartsList); 
    }
}
