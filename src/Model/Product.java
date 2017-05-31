package Model;
import static Model.Inventory.products;
import java.util.ArrayList;
import java.util.Collections;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import static javafx.collections.FXCollections.observableArrayList;

/**
 *
 * @author matgreten
 */
public class Product {
    
    public static final ObservableList<Part> parts = FXCollections.observableArrayList();
    
    public static final ObservableList<Part> productPartsList = FXCollections.observableArrayList();
    
    StringProperty productName;
    IntegerProperty productID;
    DoubleProperty price;
    IntegerProperty instock;
    IntegerProperty min;
    IntegerProperty max;
    public ObservableList<Part> productParts;
    
    public Product(){
        this(0,"",0.0,0,0,0);
    }
    
    public Product(int productID, String name, double price, int instock, int min, int max){
        
        this.productParts = observableArrayList();
        this.productID = new SimpleIntegerProperty(productID);
        this.productName = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.instock = new SimpleIntegerProperty(instock);
        this.max = new SimpleIntegerProperty(max);
        this.min = new SimpleIntegerProperty(min);
    }
    
    //Return method for populating parts tables in mainscreen, add product and modify product screens. 
    public static ObservableList<Part> getPartData() {
        return parts;
    }
    
    // Method used to return the index of the part​ from the product.parts list. This is used in the remove and update methods​. 
    public static int getPartPositionByID(int partID){
        for (int i = 0; i < parts.size(); ++i) {
            if (parts.get(i).getPartID() == partID){
                return i;
            } 
        }
        return -1;   
     }
    
    // Method to return the parts in the productsPartsList
    public static ObservableList<Part> getProductPartsListData() {
        return productPartsList;
    }
   
    public IntegerProperty productIDProperty(){
        return productID;
    }
    
    public int getProductID() {
        return productIDProperty().get();
    }

    public void setProductID(int productID) {
        productIDProperty().set(productID);
    }
    
    public ObservableList<Part> productPartsProperty(){
        return productParts;
    }
    
    public  ObservableList<Part> getProductParts() {
        return productPartsProperty();
    }

    
    public StringProperty productNameProperty(){
        return productName;
    }

    
    public String getName() {
        return productNameProperty().get();
    }

    
    public void setName(String productName) {
        productNameProperty().set(productName);
    }
    
    public DoubleProperty productPriceProperty(){
        return price;
    }
    
    public double getPrice() {
        return productPriceProperty().get();
    }

    
    public void setPrice(double price) {
        productPriceProperty().set(price);
    }

    public IntegerProperty productInventoryProperty(){
        return instock;
    }    
    
    public int getInstock() {
        return productInventoryProperty().get();
    }

    
    public void setInstock(int instock) {
        productInventoryProperty().set(instock);
    }
    
    public IntegerProperty producttMinProperty(){
        return min;
    }
    
    public int getMin() {
        return producttMinProperty().get();
    }

    
    public void setMin(int min) {
        producttMinProperty().set(min);
    }

    public IntegerProperty partMaxProperty(){
        return max;
    }
    
    
    public int getMax() {
        return partMaxProperty().get();
    }

    
    public void setMax(int max) {
        partMaxProperty().set(max);
    }
        
    private static ArrayList<Integer> currentProductIDs = new ArrayList<Integer>();  //Initialize empty arraylist to hold used IDs

    
    public static int generateProductID(){
        if (currentProductIDs.isEmpty() && products.isEmpty() == false){ // If the list is emty and their ar parts already in the system, add the parts in. This is for when we input stock data.
            for (int i = 0; i < parts.size(); ++i){
                currentProductIDs.add(i, products.get(i).getProductID());
            }
        } 
        
        if (currentProductIDs.isEmpty()){ // If the array list of current id's is empty, then this part should be part 1.
            currentProductIDs.add(1);
            return 1;
        }
        Collections.sort(currentProductIDs); //Sorts part ID's so we can evaluate agains current position in arraylist
        
        int largestID = currentProductIDs.get(currentProductIDs.size() - 1); //Get the largest id entry. Not using length as preinput parts may have non sequential ids, and we want to skip these numbers.
        
        currentProductIDs.add(largestID + 1);
        
        return largestID + 1;
    }       
}
