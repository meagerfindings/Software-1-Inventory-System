package Model;

import javafx.beans.property.*;

/**
 *
 * @author matgreten
 */
public class InhousePart extends Part {
    
    private final IntegerProperty machineID;

    /**
     *
     * @param partID
     * @param name
     * @param price
     * @param instock
     * @param max
     * @param min
     * @param machineID 
     */

    public InhousePart(Integer partID, String name, double price, int instock, int max, int min, int machineID){
        this.partID = new SimpleIntegerProperty(partID);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.instock = new SimpleIntegerProperty(instock);
        this.max = new SimpleIntegerProperty(max);
        this.min = new SimpleIntegerProperty(min);
        this.machineID = new SimpleIntegerProperty(machineID);
    }
    
    
    public IntegerProperty machineIDProperty(){
        return machineID;
    }
    
    
    public void setMachineID(int machineID){
        machineIDProperty().set(machineID);
        
    }
    
    public int getMachineID(){
        return machineIDProperty().get();
    }
    
   
 
    
}
