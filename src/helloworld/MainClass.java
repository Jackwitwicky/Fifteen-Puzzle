/*
 * You are permitted to copy, distribute and/or modify this software or its source code.
 * There is no warranty for this program, implying that in no event required by applicable
 * law or agreed to in writing will the copyright owner be liable to you for damages.
 * That said, I do agree to any and all fun and awsomity this product might cause. Have Fun!:-)
 */
package helloworld;

import java.util.ArrayList;
import java.util.Random;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;

/**
 *
 * @author Witwicky
 */
public class MainClass {

    //declaration of necessary variables
    private Text helpText = new Text();
    ArrayList< Button > tiles = new ArrayList< Button >();
    ArrayList< Integer> numbers = new ArrayList< Integer >();
    ArrayList< Button > moveList = new ArrayList< Button >();
    
    int rowCell = 0;
    int columnCell = 0;
    int moveCount = 0;
    
    Text moveCounter;
    Scene scene;
    
    //create object and reference of class Translate
    Translate mvButton = new Translate(0, -60);
    
    //create object and reference of class Extra
    ExtraClass saveGame = new ExtraClass();
    
    //method to draw and setup the game
    public Scene drawGame(String gameType) {
        //create object and reference of class Random
        Random randomNumbers = new Random();
        
        //layouts panes used in the program
        GridPane tilePane = new GridPane();
        VBox root = new VBox();
        
        //the menu bar uptop
        HBox menu = new HBox();
        
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        
        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(e -> {
            int showConfirmDialog = JOptionPane.showConfirmDialog(null, "Start a new game? All unsaved progress will be lost", 
                    "Restart Game", YES_NO_OPTION, QUESTION_MESSAGE);
            
            //execute selected option
            if(showConfirmDialog == 0) {
                scene = resetGame();
            }
        });
        
        MenuItem save = new MenuItem("Save");
        save.setOnAction(e -> {
            save();
        });
        
        MenuItem saveAndExit = new MenuItem("Save and Exit");
        saveAndExit.setOnAction(e -> {
            save();
            Platform.exit();
        });
        
        MenuItem close = new MenuItem("Close");
        close.setOnAction(e -> {
            int showConfirmDialog = JOptionPane.showConfirmDialog(null, "Save game before exiting? All unsaved progress will be lost", 
                    "Unsaved Game", YES_NO_OPTION, QUESTION_MESSAGE);
            
            //execute selected option
            if(showConfirmDialog == 0) {
                save();
                Platform.exit();
            }
            else {
                Platform.exit();
            }
        });
        
        MenuItem undo = new MenuItem("Undo");
        
        undo.setOnAction(e -> {
            if(!moveList.isEmpty()) {
                int buttonPos = moveList.size() - 1;
                Button lastButton = moveList.get(buttonPos);
                moveList.remove(buttonPos);
                
                lastButton.fire();
                moveCount -= 2;
                moveCounter.setText("Number of moves: " + moveCount);
                moveList.remove(buttonPos);
                
            }
        });
        fileMenu.getItems().addAll(newGame, save, undo, saveAndExit, close);
        
        Menu aboutMenu = new Menu("About");
        
        MenuItem help = new MenuItem("Help");
        
        MenuItem about = new MenuItem("About");
        
        aboutMenu.getItems().addAll(help, about);
        
        menuBar.getMenus().addAll(fileMenu, aboutMenu);
        menu.getChildren().add(menuBar);
        root.getChildren().add(menuBar);
        
        if(gameType.equals("new")) {
            //logic to draw the 15 tiles
        int tileCounter = 1;
        for(int rowCounter = 1; rowCounter < 5; rowCounter++) {
            for(int columnCounter = 1; columnCounter < 5; columnCounter++) {
                do {
                    tileCounter = 1 + randomNumbers.nextInt(16);
                }while (exists(tileCounter));
                if(tileCounter == 16) {
                    numbers.add(tileCounter);
                    rowCell = rowCounter;
                    columnCell = columnCounter;
                }
                else {
                    tilePane.add(generateTile(tileCounter), columnCounter, rowCounter);
                }
                numbers.add(tileCounter);
            }
        }
        }
        
        else if(gameType.equals("load")) {
            saveGame.openFile("game");
            ArrayList< String > tilesOrder = saveGame.readFile();
            
            saveGame.closeFile();
            
            for(String tileOrder : tilesOrder) {
                String[] tileValues = tileOrder.split("\\s");
                //Button tile = generateTile(Integer.parseInt(tileValues[0]));
                
                //check if tile sixteen and add as blank space
                if(tileValues[0].equals("16")) {
                    rowCell = Integer.parseInt(tileValues[1]);
                    columnCell = Integer.parseInt(tileValues[2]);
                }
                else if(tileValues[0].equals("17")) {
                    moveCount = Integer.parseInt(tileValues[1]);
                }
                else {
                    tilePane.add(generateTile(Integer.parseInt(tileValues[0])), 
                        Integer.parseInt(tileValues[2]), Integer.parseInt(tileValues[1]));
                }
            }
            
            //remove record of a saved game
            saveGame.writeFile("state");
            saveGame.addRecord("false");
            saveGame.closeFile();
        }
        
        //  root.setGridLinesVisible(true);
        root.getChildren().add(tilePane);
        
        
        //counter textfield
        moveCounter = new Text();
        moveCounter.setText("Number of moves: " + moveCount);
        
        root.getChildren().add(moveCounter);
        
        //define the scene and add it to stage
        scene = new Scene(root, 240, 280);
        
        return scene;
    } //end of method to draw the main game
    
    //method to check if a number for a tile has already been used
    private boolean exists(int num) {
        return numbers.contains(num);
    }
    
    //public method to generate and return a button
    public Button generateTile(int num) {
        Button tile = new Button(Integer.toString(num));
        tile.setPrefSize(60, 60);
        tiles.add(tile);
        tile.setOnAction(e -> {
            int initialRow = GridPane.getRowIndex(tile);
            int initialColumn = GridPane.getColumnIndex(tile);
            
            //code to check if next to empty cell
            if((((initialRow +1) == rowCell) || ((initialRow - 1) == rowCell)) 
                    && (initialColumn == columnCell)) {
                //tile.getTransforms().add(mvButton);
                GridPane.setRowIndex(tile, rowCell);
                GridPane.setColumnIndex(tile, initialColumn);
                
                //add button to move list
                moveList.add(tile);
                
                //increment moveCounter
                moveCount++;
                moveCounter.setText("Number of moves: " + moveCount);
                
                //update empty cell
                rowCell = initialRow;
                columnCell = initialColumn;
            }
            else if((((initialColumn +1) == columnCell) || ((initialColumn - 1) == columnCell)) 
                    && (initialRow == rowCell)) {
                GridPane.setRowIndex(tile, initialRow);
                GridPane.setColumnIndex(tile, columnCell);
                
                //add button to move list
                moveList.add(tile);
                
                //increment move counter
                moveCount++;
                moveCounter.setText("Number of moves: " + moveCount);
                
                //update empty cell
                rowCell = initialRow;
                columnCell = initialColumn;
        }
        });
        
        
        return tile;
    }
    
    //method to get button order and send to save file
    public void save() {
        ArrayList< String > tileOrderList = new ArrayList< String >();
        String tileCord = "";
        String tileOrder = "";
        for(Button tile : tiles) {
            tileCord = String.format("%s %d %d", tile.getText(), GridPane.getRowIndex(tile), 
                    GridPane.getColumnIndex(tile));
            tileOrderList.add(tileCord);
            tileOrder = tileOrder.concat(String.format("%s\n", tileCord));
        }
        
        //save the blank cell and number of moves
        tileOrder = tileOrder.concat(String.format("%d %d %d\n", 16, rowCell, columnCell));
        tileOrder = tileOrder.concat(String.format("%d %d", 17, moveCount));
        
        saveGame.writeFile("game");
        saveGame.addRecord(tileOrder);
        saveGame.closeFile();
        
        saveGame.writeFile("state");
        saveGame.addRecord("true");
        saveGame.closeFile();
    }
    
    private Scene resetGame() {
        return drawGame("new");
    }
}
