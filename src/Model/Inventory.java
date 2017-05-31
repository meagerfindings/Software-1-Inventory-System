package Model;

import View_Controller.ModifyProductController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author matgreten
 */
public class Inventory {
    
    public static final ObservableList<Product> products = FXCollections.observableArrayList(); // ObservableArrayList that holds all created Products.
    
    public static ObservableList<Product> getProductData() { // Method to return all products. This is used for populating the mainscreen's products table.
        return products;
    }
    
    public static void addProduct(Product product){
        products.add(product);
    }
    
    public static boolean removeProduct(int productID){
        products.remove(lookupProduct(productID));
        return false;
    }
    
    public static Product lookupProduct(int productID){  // Go through each product, checking for the ID, return the postion (index) of the product.

        int indexOfProduct = -1;
        
        for (int i = 0; i < products.size(); i++) {
            if (productID == products.get(i).getProductID()){

                indexOfProduct = i;
            }
        }
        
        Product productToUpdate = products.get(indexOfProduct);
        return productToUpdate;
    }
    
    public static void updateProduct(int productID){ // Sets up handing over control of the selected Product in the mainscreen to the Modify Product screen.
        ModifyProductController.productToClone = lookupProduct(productID); 

        }
}
