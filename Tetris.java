/** File name: Tetris.java
 * This file contains one class which design the Tetris game
 * Name: Jiaming Zhang
 * Date: April 30, 2018
 * */

import java.util.*;
import java.io.*;

/** Class name: Tetris
 * This class design the Tetris game.
 * */
public class Tetris {

  public int linesCleared; // how many lines cleared so far

  public boolean isGameover;  // true if the game is over
  // false if the game is not over

  public Piece activePiece;   // holds a Piece object that can be moved
  // or rotated by the player

  public Piece nextPiece; // holds a Piece object that will become the new 
  // activePiece when the activePiece consolidates

  // The following 2 variables are used for the extra credit 
  public boolean usedHold = false;    // set to true if the player already 
  // used hold once since last piece 
  // consolidated

  public Piece storedPiece = null;    // holds the stored piece 

  public char[][] grid;   // contains all consolidated pieces, each tile  
  // represented by a char of the piece's shape
  // a position stores a space char if it is empty

  private boolean canSwap = true; 
  //set to be true if the player can store the active piece or swap the 
  //activePiece with storedPiece

  //This is the no-arg constructor of Tetris
  //This constructor will initialize all instance variables to their 
  //appropriate values
  public Tetris(){
    this.linesCleared = 0;
    this.isGameover = false;
    this.activePiece = new Piece();
    this.nextPiece = new Piece();
    //Initialize the grid to be a 2D char array with default content ' '.
    this.grid = new char[20][10];
    for(int i = 0; i < grid.length; i++){
      for(int j = 0; j < grid[i].length; j++){
        this.grid[i][j] = ' ';
      }
    }
  }

  //This constructor takes in a parameter filename. Read the file's content and
  //initialize the appropriate instance variables
  public Tetris (String filename) throws IOException {
    File file = new File(filename);
    Scanner input = new Scanner(file);
    //Next three lines will read the first string as an integer and assign that
    //integer into instance variable: linesCleared.
    String lineClear = input.nextLine();
    int lineC = Integer.parseInt(lineClear);
    this.linesCleared = lineC;
    this.activePiece = new Piece(input.nextLine().charAt(0));
    this.nextPiece = new Piece(input.nextLine().charAt(0));
    this.grid = new char[20][10];
    for(int row = 0; row < grid.length; row++){
      String read = input.nextLine();
      for(int col = 0; col < grid[row].length; col++){
        this.grid[row][col] = read.charAt(col);
      }
    }
    if(hasConflict(this.activePiece)){
      this.isGameover = true;
    }
  }
  /** Method name: hasConflict
   * This method will decide whether the tiles can be moved
   * @param piece: the Piece object contains different shapes
   * @return true if theres has a conflict, no if not.
   * */
  public boolean hasConflict(Piece piece) {
    //Decide whether Piece has a visible tile that is outside of the grid.
    boolean outOfBound = false;
    for(int row = 0; row < piece.tiles.length; row++){
      for(int col = 0; col < piece.tiles[row].length; col++){
        //If the tile is visible, then check whether it have out of bound
        //error or conflict with other visible block error.
        if(piece.tiles[row][col] == 1){
          //check whether out of bound
          if(row + piece.rowOffset >= 20
              ||col + piece.colOffset >= 10
              ||row + piece.rowOffset < 0
              ||col + piece.colOffset < 0)
          {
            return true;
          }
          //If the tile in tiles is visible and the tiles in approperate
          //position is not empty, there will have a conflict.
          if(this.grid[row+piece.rowOffset][col+piece.colOffset]  != ' '){
            return true;
          }
        }
      }
    }
    return false;
  }
  /** Method name: consolidate
   * This method stores the active piece's character representation
   * into its correspoding location in the grid
   * */
  public void consolidate() {
    boolean visibleBlock = false;
    for(int row = 0; row < activePiece.tiles.length; row++){
      for(int col = 0; col < activePiece.tiles[row].length; col++){
        if(activePiece.tiles[row][col] == 0){
          visibleBlock = false;
        }
        else if(activePiece.tiles[row][col] == 1){
          visibleBlock = true;
        }
        //For each of the visible tiles of the current active piece, 
        if(visibleBlock){
          this.grid[row + activePiece.rowOffset][col + activePiece.colOffset] 
            = this.activePiece.shape;
        }
      }
    }
  }

  /** Method name: clearLines
   * This method checks each row in the grid and clears the row if it is 
   * fully occupied and moves everything above the row down by one.
   * */
  public void clearLines() {

    for(int row = 0; row < this.grid.length; row++){
      boolean occupied = true;
      for(int col = 0; col < this.grid[row].length; col++){
        //If any tiles in the grid is empty, means the entire row is not
        //being fully occupied.
        if(this.grid[row][col] == ' ')
        {
          occupied = false;
          break;
        }
      }
      //If the row if fully occupied, moves everything above the row down
      //by one.
      if(occupied){
        for(int index = row; index >= 1; index--){
          this.grid[index] = this.grid[index-1];
        }
        //The top row will be filled with spaces
        for(int j = 0; j < this.grid[0].length; j++){
          this.grid[0][j] = ' ';
        }
        this.linesCleared++;
      }
    }
  }

  /** Method name: move
   * This method attempts to move the active piece in a certain direction
   * @param direction Direction object which shows the direction the Piece 
   * will move based on the instruction
   * @return True if the body move, false if doesn'ti
   * */
  public boolean move(Direction direction) {
    Piece copy = new Piece(this.activePiece);
    copy.move(direction);
    if(!hasConflict(copy))
    {
      this.activePiece = copy; 
      return true;
    }
    //If the direction is down, and moving downwards fail, then we try to
    //consolidate.
    if(direction == Direction.DOWN){
      if(hasConflict(copy)){
        this.consolidate();
        //Every time a tile is consolidate, make sure the player can swap
        //the storedPiece and activePiece again.
        this.canSwap = true;
        this.clearLines();
        this.activePiece = this.nextPiece;
        this.nextPiece = new Piece();
        if(hasConflict(activePiece)){
          this.isGameover = true;
        }
      }
    }
    return false;
  }

  /** Method name: drop
   * This method will cause the current active piece to continue moving down
   * until it fails.
   * */
  public void drop() {
    while(this.move(Direction.DOWN)){
    }
  }

  /** Method name: rotate
   * This method will rotate the current active piece if there's no conflicts
   * */
  public void rotate() {
    //Rotate the copy
    Piece copy = new Piece(this.activePiece);
    copy.rotate();
    //If there's no conflict, let activePiece be the copy.
    if(!hasConflict(copy))
    {
      this.activePiece = copy;
    }
  }

  /** Method name: outputToFile
   * This method writes some of the tetris instance variables to a file
   * named "output.txt"
   * */
  public void outputToFile() throws IOException {
    PrintWriter output = new PrintWriter("output.txt");
    output.println(this.linesCleared);
    output.println(this.activePiece.shape);
    output.println(this.nextPiece.shape);
    //Print the figure of the game in the grid.
    for(int row = 0; row < grid.length; row++){
      for(int col = 0; col < grid[row].length; col++){
        output.print(this.grid[row][col]);
      }
      output.println();
    } 
    output.close();
    System.out.println("Saved to output.txt");
  }

  /** Method name: play
   * This method handles the interactive part.
   * */
  public void play () {
    while(this.isGameover == false){
      System.out.println(this.toString());
      System.out.print("> ");
      Scanner input = new Scanner(System.in);
      String moveOrder = input.nextLine();
      //Key "a" means move the piece to the left by one
      if(moveOrder.equals("a")){
        this.move(Direction.LEFT);
      }
      //Key "d" means move the piece to the right by one
      else if(moveOrder.equals("d")){
        this.move(Direction.RIGHT);
      }
      //Key "s" means move the piece downward by one
      else if(moveOrder.equals("s")){
        this.move(Direction.DOWN);
      }
      //Key "w" means rotate the piece
      else if(moveOrder.equals("w")){
        this.rotate();
      }
      //Key space means drop the piece
      else if(moveOrder.equals(" ")){
        this.drop();
      }
      //Save and print current state into file named "output.txt".
      else if(moveOrder.equals("o")){
        try{
          this.outputToFile();
        } catch (IOException e) {}
      }
      //Key "q" means end the game
      else if(moveOrder.equals("q")){
        System.out.println(this.toString());
        break;
      }
      else if(moveOrder.equals("z")){     
        this.hold();
      }
    }
    System.out.println(this.toString());
    System.out.println("Game over! " + "Total Lines cleared: "
        + this.linesCleared);
  }

  /**
   * returns the string representation of the Tetris game state in the 
   * following format:
   *  Lines cleared: [number]
   *  Next piece: [char]  (Stored piece: [char])
   *  char[20][10]
   * @return string representation of the Tetris game
   */
  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();

    str.append("\nLines cleared: " + this.linesCleared + '\n');

    str.append("Next piece: " + this.nextPiece.shape);
    if (this.storedPiece == null) str.append("\n");
    else str.append("  Stored piece: " + this.storedPiece.shape + '\n');

    str.append("| - - - - - - - - - - |\n");

    /*deep copy the grid*/
    char[][] temp_grid = new char[this.grid.length][this.grid[0].length];
    for (int row=0; row<this.grid.length; row++)
      for (int col=0; col<this.grid[0].length; col++)
        temp_grid[row][col] = this.grid[row][col];

    /*put the active piece in the temp grid*/
    for (int row=0; row<this.activePiece.tiles.length; row++)
      for (int col=0; col<this.activePiece.tiles[0].length; col++)
        if (activePiece.tiles[row][col] == 1)
          temp_grid[row+activePiece.rowOffset]
            [col+activePiece.colOffset] = 
            activePiece.shape;

    /*print the temp grid*/
    for (int row=0; row<temp_grid.length; row++){
      str.append('|');
      for (int col=0; col<temp_grid[0].length; col++){
        str.append(' ');
        str.append(temp_grid[row][col]);
      }
      str.append(" |\n");
    }

    str.append("| - - - - - - - - - - |\n");
    return str.toString();        
  }


  /** Method name: hold
   * This method handles when the player wants to hold a piece for future use,
   * or swap out the previously stored piece
   * */
  public void hold() {
    //If the instance variable canSwap is true, then player can use this method
    //once
    if(this.canSwap){
      //If there is no previously stored storePiece, store activePiece into
      //storedPiece, use nextPiece to be the new activePiece, generate a new
      //nextPiece.
      if(this.storedPiece == null){
        this.storedPiece = this.activePiece;
        this.activePiece = this.nextPiece;
        this.nextPiece = new Piece();
        //Change the canSwap to be false so that this method cannot be called
        //until something has consolidated
        this.canSwap = false;
      }
      //If there is a previously stored storedPiece, swap storedPiece and 
      //activePiece.
      else
      {
        //reset the activePiece
        this.activePiece = new Piece(this.activePiece.shape);
        Piece swapPiece = new Piece();
        swapPiece = this.storedPiece;
        this.storedPiece = this.activePiece;
        this.activePiece = swapPiece;
        //Change the canSwap to be false so that this method cannot be called
        //until something has consolidated
        this.canSwap = false;
      }
    }
  }
  /**
   * first method called during program execution
   * @param args: an array of String when running the program from the 
   * command line, either empty, or contains a valid filename to load
   * the Tetris game from
   */
  public static void main(String[] args) {
    if (args.length != 0 && args.length != 1) {
      System.err.println("Usage: java Tetris / java Tetris <filename>");
      return;
    }
    try {
      Tetris tetris;
      if (args.length == 0) tetris = new Tetris();
      else tetris = new Tetris(args[0]);
      tetris.play();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
