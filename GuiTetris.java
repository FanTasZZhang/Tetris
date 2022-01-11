/** File name: GuiTetris.java
 * This file contains one class which design the Tetris game
 * Student name: Jiaming Zhang
 * Date: May 19, 2018
 * */
import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;

/** Class name: GuiTetris
 * This class extends from the Application and design the Tetris game
 * */
public class GuiTetris extends Application {

  // final variables
  private static final int PADDING = 10;
  private static final int TILE_GAP = 2;
  private final int SIDE = 4;
  private final Color BACKGROUND3 = Color.PINK;
  private final int WIDTH = 10;
  private final int HEIGHT = 20;
  private final Color BACKGROUND4 = Color.GRAY;
  private final Color BACKGROUND5 = Color.WHITE;
  private final Color COLORO = Color.BLACK;
  private final Color COLORI = Color.BLUE;
  private final Color COLORS = Color.CYAN;
  private final Color COLORZ = Color.GREEN;
  private final Color COLORJ = Color.RED;
  private final Color COLORL = Color.ORANGE;
  private final Color COLORT = Color.YELLOW;
  private final Color COLORGHOST = Color.PINK;

  // instance variables
  private Tetris tetris;
  private GridPane pane;
  private MyKeyHandler myKeyHandler;
  private Text label1;
  private Text label2;
  private Rectangle rec1;
  private String str1;
  private String str2;
  private Rectangle[][] cells3;
  private Rectangle[][] cells4;
  private Rectangle[][] cells5;
  private Piece ghostPiece;

  /** Method name: start
   * This method start the Tetris game
   * @param primaryStage the starting stage of Tetris game
   * */
  @Override
  public void start(Stage primaryStage) {
    this.tetris = new Tetris();

    pane = new GridPane();
    pane.setAlignment(Pos.CENTER);
    pane.setPadding(new Insets(PADDING,PADDING,PADDING,PADDING));
    pane.setStyle("-fx-background-color: rgb(255,255,255)");
    pane.setHgap(TILE_GAP); 
    pane.setVgap(TILE_GAP);

    // generate the first area which contains the name of the game
    label1 = new Text("Tetris");
    label1.setFont(Font.font("Times New Roman",
          FontWeight.BOLD, FontPosture.ITALIC, 18));
    pane.add(label1, 0, 0, 8, 2);
    pane.setHalignment(label1, HPos.CENTER);
    pane.setValignment(label1, VPos.CENTER);

    // generate the second area which will show how many lines being cleaned
    str1 = new String("" + tetris.linesCleared);
    label2 = new Text(str1);
    label2.setFont(Font.font("Times New Roman",
          FontWeight.BOLD, FontPosture.ITALIC, 18));
    pane.add(label2, 8, 0, 2, 2);

    // generate the third area shows the next piece
    cells3 = new Rectangle[SIDE][];
    for(int index = 0; index < SIDE; index++){
      cells3[index] = new Rectangle[SIDE];
    }

    // Firstly create a blanked 4*4 grid
    for(int row = 0; row < SIDE; row++){
      for(int col = 0; col < SIDE; col++){
        cells3[row][col] = new Rectangle(25, 25, BACKGROUND3);
        pane.add(cells3[row][col],6+col,2+row);
      }
    }

    // Re-colored the grid based on the shape of the tile.
    for(int row = 0; row < tetris.nextPiece.tiles.length; row++){
      for(int col = 0; col < tetris.nextPiece.tiles.length; col++){
        switch(tetris.nextPiece.shape){
          case 'O':
            if(tetris.nextPiece.tiles[row][col] == 1){
              cells3[row][col].setFill(COLORO);
            }
            break;
          case 'T':
            if(tetris.nextPiece.tiles[row][col] == 1){
              cells3[row][col].setFill(COLORT);
            }
            break;
          case 'L':
            if(tetris.nextPiece.tiles[row][col] == 1){
              cells3[row][col].setFill(COLORL);
            }
            break;
          case 'J':
            if(tetris.nextPiece.tiles[row][col] == 1){
              cells3[row][col].setFill(COLORJ);
            }
            break;
          case 'I':
            if(tetris.nextPiece.tiles[row][col] == 1){
              cells3[row][col].setFill(COLORI);

            }
            break;
          case 'S':
            if(tetris.nextPiece.tiles[row][col] == 1){
              cells3[row][col].setFill(COLORS);
            }
            break;
          case 'Z':
            if(tetris.nextPiece.tiles[row][col] == 1){
              cells3[row][col].setFill(COLORZ);
            }
            break;
        }
      }
    }

    // generate the fourth area shows the main part of Tetris game
    cells4 = new Rectangle[HEIGHT][];
    for(int index = 0; index < HEIGHT; index++){
      cells4[index] = new Rectangle[WIDTH];
    }

    // firstly create a blanked 10*20 grid
    for(int row = 0; row < HEIGHT; row++){
      for(int col = 0; col < WIDTH; col++){
        cells4[row][col] = new Rectangle(25, 25, BACKGROUND4);
        pane.add(cells4[row][col], col, 6+row);
      }
    }

    // deep copy the grid
    char[][] temp_grid = new char[tetris.grid.length][tetris.grid[0].length];
    for (int row=0; row<tetris.grid.length; row++){
      for (int col=0; col<tetris.grid[0].length; col++){
        temp_grid[row][col] = tetris.grid[row][col];
      }
    }

    // extra credit: ghost piece
    ghostPiece = new Piece(tetris.activePiece);
    while(!tetris.hasConflict(ghostPiece)){
      ghostPiece.rowOffset++;
    }
    ghostPiece.rowOffset--;
    //put the ghostPiece in the temp grid
    for (int row=0; row<ghostPiece.tiles.length; row++){
      for (int col=0; col<ghostPiece.tiles[0].length; col++){
        if (ghostPiece.tiles[row][col] == 1 && row+ghostPiece.rowOffset >= 0){
          temp_grid[row+ghostPiece.rowOffset]
            [col+ghostPiece.colOffset] = '1';
        }
      }
    }

    // put the active piece in the temp grid
    for (int row=0; row<tetris.activePiece.tiles.length; row++){
      for (int col=0; col<tetris.activePiece.tiles[0].length; col++){
        if (tetris.activePiece.tiles[row][col] == 1){
          temp_grid[row+tetris.activePiece.rowOffset]
            [col+tetris.activePiece.colOffset] = 
            tetris.activePiece.shape;
        }
      }
    }

    // Re-colored the grid based on the shape of activePiece and consolidated
    // pieces
    for(int row = 0; row < HEIGHT; row++){
      for(int col = 0; col < WIDTH; col++){
        switch(temp_grid[row][col]){
          case 'O':
            cells4[row][col].setFill(COLORO);
            break;
          case 'T':
            cells4[row][col].setFill(COLORT);
            break;
          case 'L':
            cells4[row][col].setFill(COLORL);
            break;
          case 'J':
            cells4[row][col].setFill(COLORJ);
            break;
          case 'I':
            cells4[row][col].setFill(COLORI);
            break;
          case 'S':
            cells4[row][col].setFill(COLORS);
            break;
          case 'Z':
            cells4[row][col].setFill(COLORZ);
            break;
          case '1':
            cells4[row][col].setFill(COLORGHOST);
            break;
        }
      }
    }

    // generate the fifth area which show the storedPiece
    cells5 = new Rectangle[SIDE][];
    for(int index = 0; index < SIDE; index++){
      cells5[index] = new Rectangle[SIDE];
    }

    // firstly generate a blanked 4*4 grid
    for(int row = 0; row < SIDE; row++){
      for(int col = 0; col < SIDE; col++){
        cells5[row][col] = new Rectangle(25, 25, BACKGROUND5);
        pane.add(cells5[row][col],col,2+row);
      }
    }

    // Re-colored the grid based on the shape of storedPiece
    if(tetris.storedPiece != null){
      for(int row = 0; row < tetris.storedPiece.tiles.length; row++){
        for(int col = 0; col < tetris.storedPiece.tiles.length; col++){
          switch(tetris.storedPiece.shape){
            case 'O':
              if(tetris.storedPiece.tiles[row][col] == 1){
                cells5[row][col].setFill(COLORO);
              }
              break;
            case 'T':
              if(tetris.storedPiece.tiles[row][col] == 1){
                cells5[row][col].setFill(COLORT);
              }
              break;
            case 'L':
              if(tetris.storedPiece.tiles[row][col] == 1){
                cells5[row][col].setFill(COLORL);
              }
              break;
            case 'J':
              if(tetris.storedPiece.tiles[row][col] == 1){
                cells5[row][col].setFill(COLORJ);
              }
              break;
            case 'I':
              if(tetris.storedPiece.tiles[row][col] == 1){
                cells5[row][col].setFill(COLORI);

              }
              break;
            case 'S':
              if(tetris.storedPiece.tiles[row][col] == 1){
                cells5[row][col].setFill(COLORS);
              }
              break;
            case 'Z':
              if(tetris.storedPiece.tiles[row][col] == 1){
                cells5[row][col].setFill(COLORZ);
              }
              break;
          }
        }
      }
    }

    Scene scene = new Scene(pane);
    primaryStage.setTitle("Tetris");
    primaryStage.setScene(scene);
    primaryStage.show();

    myKeyHandler = new MyKeyHandler();
    scene.setOnKeyPressed(myKeyHandler);
    MoveDownWorker worker = new MoveDownWorker();
    worker.start();

  }

  /** Class name: MyKeyHandler
   * This class let the Tetris game interact with users and implements from
   * EventHandler<KeyEvent>
   * */
  private class MyKeyHandler implements EventHandler<KeyEvent> {

    /** Method name: handle
     * This method will interact with users based on different keys users
     * tapped and update the GUI
     * @param e the key users tapped
     * */
    @Override
    public void handle(KeyEvent e) {
      if(!tetris.isGameover){
        switch(e.getCode()){
          case DOWN: tetris.move(Direction.DOWN);break;
          case LEFT: tetris.move(Direction.LEFT);break;
          case RIGHT: tetris.move(Direction.RIGHT);break;
          case UP: tetris.rotate();break;
          case SPACE: tetris.drop();break;
          case Z: tetris.hold();break;
          case O:         
                  try{
                    tetris.outputToFile();
                  } catch (IOException f) {}
                  break;
        }

        // update the GUI

        // update area 1 if the game is over, change the title
        if(tetris.isGameover){
          label1.setText("Game over!");
        }

        // update area 2
        str2 = new String("" + tetris.linesCleared);
        label2.setText(str2);
        label2.setFont(Font.font("Times New Roman",
              FontWeight.BOLD, FontPosture.ITALIC, 15));

        // update area 3
        // empty the grid
        for(int row = 0; row < SIDE; row++){
          for(int col = 0; col < SIDE; col++){
            cells3[row][col].setFill(BACKGROUND3);
          }
        }

        //re-color the 4*4 grid of nextPiece based on the shape 
        for(int row = 0; row < tetris.nextPiece.tiles.length; row++){
          for(int col = 0; col < tetris.nextPiece.tiles.length; col++){
            switch(tetris.nextPiece.shape){
              case 'O':
                if(tetris.nextPiece.tiles[row][col] == 1){
                  cells3[row][col].setFill(COLORO);
                }
                break;
              case 'T':
                if(tetris.nextPiece.tiles[row][col] == 1){
                  cells3[row][col].setFill(COLORT);
                }
                break;
              case 'L':
                if(tetris.nextPiece.tiles[row][col] == 1){
                  cells3[row][col].setFill(COLORL);
                }
                break;
              case 'J':
                if(tetris.nextPiece.tiles[row][col] == 1){
                  cells3[row][col].setFill(COLORJ);
                }
                break;
              case 'I':
                if(tetris.nextPiece.tiles[row][col] == 1){
                  cells3[row][col].setFill(COLORI);

                }
                break;
              case 'S':
                if(tetris.nextPiece.tiles[row][col] == 1){
                  cells3[row][col].setFill(COLORS);
                }
                break;
              case 'Z':
                if(tetris.nextPiece.tiles[row][col] == 1){
                  cells3[row][col].setFill(COLORZ);
                }
                break;
            }
          }
        }

        // update area 4
        // deep copy the grid
        char[][] temp_grid = 
          new char[tetris.grid.length][tetris.grid[0].length];
        for (int row=0; row<tetris.grid.length; row++){
          for (int col=0; col<tetris.grid[0].length; col++){
            temp_grid[row][col] = tetris.grid[row][col];
          }
        }

        //extra credit
        //update the ghostPiece
        ghostPiece = new Piece(tetris.activePiece);
        while(!tetris.hasConflict(ghostPiece)){
          ghostPiece.rowOffset++;
        }
        ghostPiece.rowOffset--;
        // put the ghostPiece in the temp grid
        for (int row=0; row<ghostPiece.tiles.length; row++){
          for (int col=0; col<ghostPiece.tiles[0].length; col++){
            if (ghostPiece.tiles[row][col] == 1 
                && row+ghostPiece.rowOffset >= 0){
              temp_grid[row+ghostPiece.rowOffset]
                [col+ghostPiece.colOffset] = '1';
            }
          }
        }

        // put the active piece in the temp grid
        for (int row=0; row<tetris.activePiece.tiles.length; row++){
          for (int col=0; col<tetris.activePiece.tiles[0].length; col++){
            if (tetris.activePiece.tiles[row][col] == 1){
              temp_grid[row+tetris.activePiece.rowOffset]
                [col+tetris.activePiece.colOffset] = 
                tetris.activePiece.shape;
            }
          }
        }

        // empty the grid first
        for(int row = 0; row < HEIGHT; row++){
          for(int col = 0; col < WIDTH; col++){
            cells4[row][col].setFill(Color.GRAY);
          }
        }

        // re-color the grid
        for(int row = 0; row < HEIGHT; row++){
          for(int col = 0; col < WIDTH; col++){
            switch(temp_grid[row][col]){
              case 'O':
                cells4[row][col].setFill(COLORO);
                break;
              case 'T':
                cells4[row][col].setFill(COLORT);
                break;
              case 'L':
                cells4[row][col].setFill(COLORL);
                break;
              case 'J':
                cells4[row][col].setFill(COLORJ);
                break;
              case 'I':
                cells4[row][col].setFill(COLORI);
                break;
              case 'S':
                cells4[row][col].setFill(COLORS);
                break;
              case 'Z':
                cells4[row][col].setFill(COLORZ);
                break;
              case '1':
                cells4[row][col].setFill(COLORGHOST);
                break;
            }
          }
        }

        // update area 5
        for(int row = 0; row < SIDE; row++){
          for(int col = 0; col < SIDE; col++){
            cells5[row][col].setFill(BACKGROUND5);
          }
        }
        // re-color the storedPiece grid
        if(tetris.storedPiece != null){
          for(int row = 0; row < tetris.storedPiece.tiles.length; row++){
            for(int col = 0; col < tetris.storedPiece.tiles.length; col++){
              switch(tetris.storedPiece.shape){
                case 'O':
                  if(tetris.storedPiece.tiles[row][col] == 1){
                    cells5[row][col].setFill(COLORO);
                  }
                  break;
                case 'T':
                  if(tetris.storedPiece.tiles[row][col] == 1){
                    cells5[row][col].setFill(COLORT);
                  }
                  break;
                case 'L':
                  if(tetris.storedPiece.tiles[row][col] == 1){
                    cells5[row][col].setFill(COLORL);
                  }
                  break;
                case 'J':
                  if(tetris.storedPiece.tiles[row][col] == 1){
                    cells5[row][col].setFill(COLORJ);
                  }
                  break;
                case 'I':
                  if(tetris.storedPiece.tiles[row][col] == 1){
                    cells5[row][col].setFill(COLORI);

                  }
                  break;
                case 'S':
                  if(tetris.storedPiece.tiles[row][col] == 1){
                    cells5[row][col].setFill(COLORS);
                  }
                  break;
                case 'Z':
                  if(tetris.storedPiece.tiles[row][col] == 1){
                    cells5[row][col].setFill(COLORZ);
                  }
                  break;
              }
            }
          }
        }
      }
    } // end of method handle
  } // end of class MyKeyHandler

  /**
   * private class GuiTetris.MoveDownWorker
   * a thread that simulates a downwards keypress every some interval
   * @author Jiaming Zhang
   */
  private class MoveDownWorker extends Thread{

    private static final int DROP_INTERVAL = 500; // millisecond
    private int move_down_timer = DROP_INTERVAL; 

    /**
     * method run
     * called when the thread begins, decrements the timer every iteration
     * of a loop, reset the timer and sends a keydown when timer hits 0
     */
    @Override
    public void run(){

      // run forever until returned
      while (true) {
        // stop the thread if the game is over
        if (tetris.isGameover) return; 

        // wait 1ms per iteration
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
          break;
        }

        move_down_timer -= 1;
        if (move_down_timer <= 0 ) {

          // simulate one keydown by calling the 
          // handler.handle()
          myKeyHandler.handle(
              new KeyEvent(null, "", "", KeyCode.DOWN, 
                false, false, false, false)
              );

          move_down_timer = DROP_INTERVAL;
        }
      }
    }
  } // end of private class MoveDownWorker

}


