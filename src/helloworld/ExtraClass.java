/*
 * You are permitted to copy, distribute and/or modify this software or its source code.
 * There is no warranty for this program, implying that in no event required by applicable
 * law or agreed to in writing will the copyright owner be liable to you for damages.
 * That said, I do agree to any and all fun and awsomity this product might cause. Have Fun!:)
 */
package helloworld;

import java.io.File;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
/**
 *
 * @author Witwicky
 */
public class ExtraClass {
    //class to save progress of the game
    
    //declaration of necessary variables
    Scanner scannerInput;
    Formatter formatterInput;
    Boolean scannerOpen = false;
    Boolean formatterOpen = false;
    
    //file variables
    File saveFile;
    File stateFile;
    String path = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Fifteen";
    String savePath = path + File.separator + "savePath.txt";
    String statePath = path + File.separator + "gameState.txt";
    String name = System.getProperty("user.name");
    String firstRunMessage = "Hi " + name + ",\n" + 
            "It appears this is the first time running this application.\nSome necessary files must be created before we start.";
    
    //method to check if folder exists, and if not create it
    public void checkFolder() {
        File customDir = new File(path);
        
        if(!customDir.exists()) {
            int showConfirmDialog = JOptionPane.showConfirmDialog(null, firstRunMessage, "First Run", YES_NO_OPTION,INFORMATION_MESSAGE);
            //perform operation that has been chosen
            if(showConfirmDialog == 0) {
                customDir.mkdirs();
            saveFile = new File(savePath);
            stateFile = new File(statePath);
            
            //try to create the files
            try{
                saveFile.createNewFile();
                stateFile.createNewFile();
                writeFile("state");
                addRecord("false");
                closeFile();
                JOptionPane.showMessageDialog(null, "The necessary files have successfully been created.", "All Done", INFORMATION_MESSAGE);
                
            }
            catch(Exception e) {
                JOptionPane.showMessageDialog(null, "The files could not be created, you could try doing it manually.", "Unfortunate Error", ERROR_MESSAGE);
            } //end of catch block
            
            } //end of if selection was okay
            else {
                JOptionPane.showMessageDialog(null, "The necessary files are needed to run. The application will now exit.", "Goodbye", INFORMATION_MESSAGE);
                System.exit(0);
            } //else the selection was no
        }
    }
    
    //method to open the file
    public void openFile(String file) {
        //try to open the file
        try {
            if(file.equals("game")) {
                scannerInput = new Scanner(new File(savePath));
            }
            else if(file.equals("state")) {
                scannerInput = new Scanner(new File(statePath));
            }
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, "This is embarrassing, the file could not be opened", ":-(", ERROR_MESSAGE);
        }
    }
    
    //method to read the file
    public ArrayList readFile() {
        ArrayList< String > output = new ArrayList< String >();
        //String output = scannerInput.toString();
        while(scannerInput.hasNext()) {
            output.add(scannerInput.nextLine());
        }
        scannerOpen = true;
        return output;
    }
    
    
    //method to open write access
    public void writeFile(String file) {
        //try to gain write access
        try {
            if(file.equals("game")) {
                formatterInput = new Formatter(savePath);
            }
            else if(file.equals("state")) {
                formatterInput = new Formatter(statePath);
            }
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, "This is embarrassing, the file could be written to", ":-(", ERROR_MESSAGE);
        }
    }
    
    //method to add data to the file
    public void addRecord(String gameSave) {
        formatterInput.format("%s", gameSave);
        formatterOpen = true;
    }
    
    //method to close the files
    public void closeFile() {
        if(scannerOpen) {
            scannerInput.close();
        }
        if(formatterOpen) {
            formatterInput.close();
        }
    }
    
}
