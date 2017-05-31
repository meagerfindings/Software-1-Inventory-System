package View_Controller;

import Model.InhousePart;
import Model.Inventory;
import Model.OutsourcedPart;
import Model.Part;
import Model.Product;
import static Model.Product.parts;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;

/* FXML Controller class
 *
 * @author matgreten
 */

public class MainScreenController implements Initializable {
       
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    
    @FXML
    private TextField partSearchField;
    
    @FXML
    private TextField productSearchField;
    
    @FXML
    private Button partSearchButton;
    
    @FXML
    private Button productSearchButton;
    
    @FXML
    private Button addPart;
    
    @FXML
    private Button modifyPart;
    
    @FXML
    private Button deletePart;
    
    @FXML
    private Button addProduct;
    
    @FXML
    private Button modifyProduct;
    
    @FXML
    private Button deleteProduct;
    
    @FXML
    private TableView<Part> partsTable;
            
    @FXML
    ObservableList<Part> partTableData=FXCollections.observableArrayList(Product.parts);
    
    @FXML
    private TableColumn<Part, Number> ptPartID;
    
    @FXML
    private TableColumn<Part, String> ptPartName;
    
    @FXML
    private TableColumn<Part, Number> ptInventoryLevel;
    
    @FXML
    private TableColumn<Part, Number> ptUnitPrice;
    
    @FXML
    private TableView<Product> productTable;
            
    @FXML
    private ObservableList<Product> productTableData=FXCollections.observableArrayList(Inventory.products);
    
    @FXML
    private TableColumn<Product, Number> protPoductID;
    
    @FXML
    private TableColumn<Product, String> protPartName;
    
    @FXML
    private TableColumn<Product, Number> protInventoryLevel;
    
    @FXML
    private TableColumn<Product, Number> protUnitPrice;

    @FXML
    private Button exit;
    
    
    @FXML
    private void deletePartButtonCliked(ActionEvent event){
        
        if (currentProduct == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Part Selected");
            alert.setHeaderText("No Part selected to delete.");
            alert.setContentText("Pressing delete without choosing a Part will not work.");
            
            alert.showAndWait();
            
        } else {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Deletion Confirmation");
            alert.setHeaderText("You are about to delete a part.");
            alert.setContentText("If you choose OK, " + currentPart.getName() + " will be permanently deleted.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                // OK was selected, delete the part.

                int position = currentPart.getPartPosition(currentPart);
                parts.remove(position);

            } else {
                // Cancel was selected, do nothing.
            }
        }
    }
    
    @FXML
    private void deleteProductButtonCliked(ActionEvent event){
        
        if (currentProduct == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Product Selected");
            alert.setHeaderText("No Product selected to delete.");
            alert.setContentText("Pressing delete without choosing a Product will not work.");
            
            alert.showAndWait();
            
        } else {
        
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Deletion Confirmation");
            alert.setHeaderText("You are about to delete a product.");
            alert.setContentText("If you choose OK, " + currentProduct.getName() + " will be permanently deleted.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                // OK was selected, delete the product.

                int targetID = currentProduct.getProductID();

                    // Prevent products with even one part from being deleted. (The only time a product can be deleted is if it was added prior to the application starting as no products can have less than 1 part and no products with at least 1 part can be deleted.
                    if(currentProduct.productParts.size() > 0){
                        Alert chastise = new Alert(Alert.AlertType.ERROR);
                        chastise.setTitle("Product Has Parts Error");

                        if (currentProduct.productParts.size() == 1){
                            chastise.setContentText("You cannot delete a Product that has parts in it.\n\n" + currentProduct.getName() + " has " + currentProduct.productParts.size() + " part.");
                        } else {
                            chastise.setContentText("You cannot delete a Product that has parts in it.\n\n" + currentProduct.getName() + " has " + currentProduct.productParts.size() + " parts.");
                        }

                        chastise.showAndWait();

                    } else {
                        // Actually delete the Product.
                        Inventory.removeProduct(targetID);
                    }

            } else {
                // Cancel was selected, do nothing.
            }
        }
    }
    
    @FXML
    FilteredList<Part> filteredParts = new FilteredList<>(partTableData, search -> true);
    
    public static Part currentPart; // Which part is currently selected in the parts table.
    
    @FXML
    FilteredList<Product> filteredProducts = new FilteredList<>(productTableData, search -> true);
    
    public static Product currentProduct; // Which product is currently selected in the products table. Also doubles as temporary product when adding a new product, so we can hold the parts list for the product.

    @FXML
    private void searchPartButtonCliked(ActionEvent event){
        
        String searchValue = String.valueOf(partSearchField.getText());
                
        System.out.println("Searching for Part by the value: " + searchValue);
        
        if (searchValue.contains("88")){
            System.out.println(timeTravel);
        }

        filteredParts.setPredicate((Part part) -> {
            
                if (searchValue.isEmpty()) {
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
    private void searchProductButtonCliked(ActionEvent event){
        
        String productSearchValue = String.valueOf(productSearchField.getText());
                
//        System.out.println("Searching for Product by the value: " + productSearchValue);
        
        if (productSearchValue.contains("88")){
            System.out.println(timeTravel);
        }
        
        filteredProducts.setPredicate((Product product) -> {
            
                if (productSearchValue.isEmpty()) {
                    return true;
                }

                // Check if the productSearchValue entry is a part number or part name
                boolean isInteger = false;

                try {
                    Integer.parseInt(productSearchValue); //If the integer parser can parse the string then we have an integer
                    isInteger = true;
                }
                catch (Exception e){
                    isInteger = false; // If the integer parser failed this was a true string.
                }

                if (isInteger == true){ //Handle productSearchValue as an Integer.

                    if (Integer.toString(product.getProductID()).contains(productSearchValue)){ //Cast the PartID to a string to allow for contains to run agains searchValue string.
                        return true;
                    }

                } else { //Handle productSearchValue as a String.

                    String upCaseSearch = productSearchValue.toUpperCase();

                    if (product.getName().toUpperCase().contains(upCaseSearch)) {
                        return true; //productSearchValue matches a partName.
                    }
                }
                return false; // productSearchValue must not have matched any values in our table

                });
    }
   
    @FXML      //Switches to addPartScreen when Add is clicked in the Part section
    private void addPartButtonClicked(ActionEvent event) throws IOException {
        System.out.println("Opening Add Part Screen.");
        
        Parent addPartParent = FXMLLoader.load(getClass().getResource("AddPartInHouse.fxml"));
        Scene addPartScene = new Scene(addPartParent);
        Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        newStage.setScene(addPartScene);
        newStage.show();
    }
    
    @FXML      //Switches to modifyPartScreen when Modify is clicked in the Part section
    private void modifyPartButtonClicked(ActionEvent event) throws IOException {
      
        if (currentPart != null){

            String inOrOut = "No Class detected.";

            if (currentPart.getClass() == InhousePart.class){
                inOrOut = "ModifyPartInHouse";
            } else if (currentPart.getClass() == OutsourcedPart.class) {
                inOrOut = "ModifyPartOutsourced";
            } else {
                System.out.println("The inhouse/outsourced detector is not working.");
            }
            
            System.out.println("Opening the " + inOrOut + " screen.");

            Parent modifyPartParent = FXMLLoader.load(getClass().getResource(inOrOut + ".fxml"));
            Scene modifyPartScene = new Scene(modifyPartParent);
            Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            newStage.setScene(modifyPartScene);
            newStage.show();
        } else {
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Part Selected");
            alert.setHeaderText("No Part selected to modify.");
            alert.setContentText("Pressing Modify without choosing a Part will not work.");
            
            alert.showAndWait();
            
        }
        
    }
    
    @FXML      //Switches to addProductScreen when Add is clicked in the Product section
    private void addProductButtonClicked(ActionEvent event) throws IOException {
        System.out.println("Opening Add Product Screen.");
       

        FXMLLoader productLoader = new FXMLLoader(AddProductController.class.getResource("AddProduct.fxml"));
        GridPane addProduct = (GridPane) productLoader.load();
        Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                
        Scene sceneMainScreen = new Scene(addProduct);
        
        newStage.setScene(sceneMainScreen);
        
        AddProductController controller = productLoader.getController();
        controller.setupAddProductScreen();

        newStage.show();
      
    }
    
    @FXML      //Switches to modifyProductScreen when Modify is clicked in the Product section
    private void modifyProductButtonClicked(ActionEvent event) throws IOException {
        System.out.println("Opening Modify Product Screen.");
        
        if (currentProduct == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Product Selected");
            alert.setHeaderText("No Product selected to modify.");
            alert.setContentText("Pressing Modify without choosing a Product will not work.");
            
            alert.showAndWait();
            
        } else {
            Inventory.updateProduct(currentProduct.getProductID());

            FXMLLoader productLoader = new FXMLLoader(ModifyProductController.class.getResource("ModifyProduct.fxml"));
            GridPane modifyProduct = (GridPane) productLoader.load();
            Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene sceneMainScreen = new Scene(modifyProduct);

            newStage.setScene(sceneMainScreen);

            ModifyProductController controller = productLoader.getController();
            controller.setupModifyProductScreen();
            newStage.show();
        }
    }
    
    @FXML      //Exits Program.
    private void exitButtonClicked(ActionEvent event) throws IOException {
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setContentText("Are you sure you want to exit?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // Exit was chosen again. Quit the program.
            System.out.println("Closing up shop. Exiting the program.");

            //Exact command for exit found at http://docs.oracle.com/javafx/2/ui_controls/menu_controls.htm?id=titleinexample#BABGIIGB
            System.exit(0);
            

        } else {
            // Cancel was chosen, let the confirmation window exit and do nothing.
        }
        
        
                
    }  
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Initializes display of componenets for Parts Table
        ptPartID.setCellValueFactory(cellData -> cellData.getValue().partIDProperty());
        ptPartName.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        ptUnitPrice.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty());
        ptInventoryLevel.setCellValueFactory(cellData -> cellData.getValue().partInventoryProperty());
        
        //Initializes search componenets for Parts Table
        partSearchField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String searchValue) -> {
            SortedList<Part> sortedPartsList = new SortedList<>(filteredParts);
            sortedPartsList.comparatorProperty().bind(partsTable.comparatorProperty());             
            partsTable.setItems(sortedPartsList);
        });  
        
        //Initializes listener for selections in the Parts Table
        partsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, currentSelection) -> currentPart = currentSelection);    

        
        //Initializes display of componenets for Products Table
        protPoductID.setCellValueFactory(cellData -> cellData.getValue().productIDProperty());
        protPartName.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        protUnitPrice.setCellValueFactory(cellData -> cellData.getValue().productPriceProperty());
        protInventoryLevel.setCellValueFactory(cellData -> cellData.getValue().productInventoryProperty());
        
        //Initializes search componenets for Products Table
        productSearchField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String productSearchValue) -> {
            SortedList<Product> sortedProductsList = new SortedList<>(filteredProducts);
            sortedProductsList.comparatorProperty().bind(productTable.comparatorProperty());             
            productTable.setItems(sortedProductsList);
        }); 
        
        //Initializes listener for selections in the Parts Table
        productTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, currentSelection) -> currentProduct = currentSelection);    
        

    }  
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    private void initialize() {
        assert partSearchField != null : "fx:id=\"partSearchField\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert partSearchButton != null : "fx:id=\"partSearchButton\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert partsTable != null : "fx:id=\"partsTable\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert ptPartID != null : "fx:id=\"ptPartID\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert ptPartName != null : "fx:id=\"ptPartName\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert ptInventoryLevel != null : "fx:id=\"ptInventoryLevel\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert ptUnitPrice != null : "fx:id=\"ptUnitPrice\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert deletePart != null : "fx:id=\"deletePart\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert modifyPart != null : "fx:id=\"modifyPart\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert addPart != null : "fx:id=\"addPart\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert productSearchField != null : "fx:id=\"productSearchField\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert productSearchButton != null : "fx:id=\"productSearchButton\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert productTable != null : "fx:id=\"productTable\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert protPoductID != null : "fx:id=\"protPartID\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert protPartName != null : "fx:id=\"protPartName\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert protInventoryLevel != null : "fx:id=\"protInventoryLevel\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert protUnitPrice != null : "fx:id=\"protUnitPrice\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert deleteProduct != null : "fx:id=\"deleteProduct\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert modifyProduct != null : "fx:id=\"modifyProduct\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert addProduct != null : "fx:id=\"addProduct\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert exit != null : "fx:id=\"exit\" was not injected: check your FXML file 'MainScreen.fxml'.";
    }
    
    // Great Scot! It's an easter egg.
    private final String timeTravel = "\n\nHey, Doc, we better back up. We don't have enough road to get up to 88.\n\n  Roads? Where we're going, we don't need roads.\n";
        
    public void setupMainScreen() {
        // Add observable list data to the part table
        partsTable.setItems(Model.Product.getPartData());

        // Add observable list data to the product table
        productTable.setItems(Model.Inventory.getProductData());
    }
}
