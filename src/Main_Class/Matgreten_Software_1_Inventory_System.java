package Main_Class;

import Model.InhousePart;
import Model.OutsourcedPart;
import static Model.Product.parts;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import View_Controller.MainScreenController;


/**
 *
 * @author matgreten
 */
public class Matgreten_Software_1_Inventory_System extends Application {
     
    @Override
    public void start(Stage stage) throws Exception {
        //Sample data for Parts Table:
        parts.add( new InhousePart(1, "HydroSpanner", 3.50, 1, 20, 1, 1101));
        parts.add( new InhousePart(4, "HyperDrive", 828772, 0, 2, 1, 1102));
        parts.add( new InhousePart(2, "Flux Capicitor", 898, 2, 100, 1, 1103));
        parts.add( new InhousePart(3, "Proton Torpedo", 465, 32, 100, 1, 1104));
        parts.add( new InhousePart(5, "Kyber Crystal", 99999.99, 1, 1, 0, 1105));
        parts.add( new InhousePart(6, "ErgoDox", 200, 6, 100, 1, 001));
        // part 7 left out intentionally to test out part ID generator.
        parts.add( new InhousePart(8, "Gateron Brown Keyswitch", 6.00, 76, 100, 1, 102));
        parts.add( new OutsourcedPart(9, "Valk 3", 20, 1, 10, 1, "Weilong"));
        parts.add( new OutsourcedPart(10, "Still Suit", 700, 10, 55, 1, "Fremen"));
        // part 11 left out intentionally to test out part ID generator.
        parts.add( new OutsourcedPart(12, "DeLorean DMC-12", 541200, 1, 0, 12, "DeLorean Motor Company"));
        parts.add( new OutsourcedPart(13, "Dasher SA Keycaps", 148.84, 1, 0, 1337, "Signature Plastics"));
        parts.add( new OutsourcedPart(14, "Xenomorph Egg", 1979, 0, 0, 0, "Weyland Industries"));

        
        //Sample data for Product Table:
//        products.add(new Product(parts, 1,"Time Machine",0.2,0,0,0));
        
        FXMLLoader mainLoader = new FXMLLoader(MainScreenController.class.getResource("/View_Controller/MainScreen.fxml"));
        AnchorPane mainScreen = (AnchorPane) mainLoader.load();
                
        stage.setTitle("Inventory Management System");
        Scene sceneMainScreen = new Scene(mainScreen, 1127, 775);
        
        stage.setScene(sceneMainScreen);
        
        MainScreenController controller = mainLoader.getController();
        controller.setupMainScreen();

        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        
    }
    
}