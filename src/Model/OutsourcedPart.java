package Model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author matgreten
 */
public class OutsourcedPart extends Part{
        
    private StringProperty companyName;
    
    public OutsourcedPart(int partID, String name, double price, int instock, int max, int min, String companyName){
        this.partID = new SimpleIntegerProperty(partID);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.instock = new SimpleIntegerProperty(instock);
        this.max = new SimpleIntegerProperty(max);
        this.min = new SimpleIntegerProperty(min);
        this.companyName = new SimpleStringProperty(companyName);
    }
    
    public StringProperty companyNameProperty(){
        return companyName;
    }
    
    public String getCompanyName(){
        
        return companyNameProperty().get();
    }
    
    public void setCompanyName(String companyName){
        companyNameProperty().set(companyName);
    }
    
}
