/*
 * You are permitted to copy, distribute and/or modify this software or its source code.
 * There is no warranty for this program, implying that in no event required by applicable
 * law or agreed to in writing will the copyright owner be liable to you for damages.
 * That said, I do agree to any and all fun and awsomity this product might cause. Have Fun!:-)
 */
package helloworld;

//import com.sun.prism.paint.Color;
//import java.awt.event.MouseEvent;
import javafx.application.Application;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
/**
 *
 * @author Witwicky
 */
public class TestClass extends Application {
    
    
    
    
    //declare the start method
    @Override
    public void start(Stage stage) {
        stage.setTitle("Fifteen");
        
        //necessary variables
        String gameType = "";
        
        //create object and reference of class MainClass
        MainClass game = new MainClass();
        
        //create object and reference of class ExtraClass
        ExtraClass gameSave = new ExtraClass();
        
        gameSave.checkFolder();
        gameSave.openFile("state");
        String state = (String) gameSave.readFile().get(0);
        
        if(state.equals("false")) {
            gameType = "new";
        }
        else if(state.equals("true")) {
            int showConfirmDialog = JOptionPane.showConfirmDialog(null, "There appears to be a saved game. Load Game?" , 
                    "Game Found", YES_NO_OPTION,INFORMATION_MESSAGE);
            
            if(showConfirmDialog == 0) {
                gameType = "load";
            }
            else {
                gameType = "new";
            }
        }
        //define the scene and add it to stage
        Scene scene = game.drawGame(gameType);
        stage.setScene(scene);
        stage.setMaxHeight(320);
        stage.setMaxWidth(240);
        stage.show();
    }
    
    public void hasTouched(Button btnOne, Button btnTwo) {
        if(btnOne.getBoundsInParent().intersects(btnTwo.getBoundsInParent())) {
            System.out.println("Houston we have contact!");
        }
    }
      
}
