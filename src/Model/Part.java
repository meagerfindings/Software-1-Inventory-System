package Model;

import static Model.Product.parts;
import java.util.ArrayList;
import java.util.Collections;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author matgreten
 */
public abstract class Part {
    
    // Initialize​ variables for the part
    StringProperty name;
    IntegerProperty partID;
    DoubleProperty price;
    IntegerProperty instock;
    IntegerProperty min;
    IntegerProperty max;
       
    public IntegerProperty partIDProperty(){
        return partID;
    }

    public final int getPartID() {
        return partIDProperty().get();
    }

    public final void setPartID(Integer partID) {
        partIDProperty().set(partID);
    }
    
    public StringProperty partNameProperty(){
        return name;
    }
    
    public final void setName(String name){
        partNameProperty().set(name);
        
    }
    
    public final String getName(){
        
        return partNameProperty().get();
    }
    
    public DoubleProperty partPriceProperty(){
        return price;
    }
    
    public final void setPrice(double price){
        partPriceProperty().set(price);
    }
    
    public final double getPrice(){
        
        return partPriceProperty().get();
    }
    
    public IntegerProperty partInventoryProperty(){
        return instock;
    }
    
    public final void setInstock(int instock){
        partInventoryProperty().set(instock);
        
    }
    
    public final int getInstock(){
        
        return partInventoryProperty().get();        
    }
    
    public IntegerProperty partMinProperty(){
        return min;
    }
    
    public final void setMin(int min){
        partMinProperty().set(min);
        
    }
    
    public final int getMin(){
        
        return partMinProperty().get();
    }
    
    public IntegerProperty partMaxProperty(){
        return max;
    }
    
    public final void setMax(int max){
        partMaxProperty().set(max);
    }
    
    public final int getMax(){
        
        return partMaxProperty().get();
    }
    
    public int getPartPosition(Part part){
       for (int i = 0; i < parts.size(); ++i) {
           if (parts.get(i) == part){
               return i;
           } 
       }
       return -1;
   }

    private static final ArrayList<Integer> currentPartIDs = new ArrayList<Integer>();  //Initialize empty arraylist to hold used IDs

    public static int generatePartID(){
        if (currentPartIDs.isEmpty() && parts.isEmpty() == false){ // If the list is empty and there​ are parts already in the system, add the parts in. This is for when we input stock data.
            for (int i = 0; i < parts.size(); ++i){
                currentPartIDs.add(i, parts.get(i).getPartID());
            }
        } 
        
        if (currentPartIDs.isEmpty()){ // If the array list of current id's is empty, then this part should be part 1.
            currentPartIDs.add(1);
            return 1;
        }
        Collections.sort(currentPartIDs); //Sorts part ID's so we can evaluate against​ current position in arraylist
        
        int largestID = currentPartIDs.get(currentPartIDs.size() - 1); //Get the largest id entry. Not using length as preinput parts may have non sequential ids, and we want to skip these numbers.
        
        currentPartIDs.add(largestID + 1);
        
        return largestID + 1;
    }
}