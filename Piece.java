/** File name: Piece.java
 * This file contains a class which define a Piece object
 * name: Jiaming Zhang
 * Date: April 21, 2018
 * */

import java.util.*;

/** Class name: Piece
 * This class define a Piece object
 * */
public class Piece {

  // all possible char representation of a piece
  public static final char[] possibleShapes = 
  {'O', 'I', 'S', 'Z', 'J', 'L', 'T'}; 

  // initial state of all possible shapes, notice that this array's 
  // content shares index with the possibleShapes array 
  public static final int[][][] initialTiles = {
    {{1,1},
      {1,1}}, // O

    {{0,0,0,0},
      {1,1,1,1},
      {0,0,0,0},
      {0,0,0,0}}, // I

    {{0,0,0},
      {0,1,1},
      {1,1,0}}, // S

    {{0,0,0},
      {1,1,0},
      {0,1,1}}, // Z

    {{0,0,0},
      {1,1,1},
      {0,0,1}}, // J

    {{0,0,0},
      {1,1,1},
      {1,0,0}}, // L

    {{0,0,0},
      {1,1,1},
      {0,1,0}} // T
  };

  // random object used to generate a random shape
  public static Random random = new Random(); 

  // char representation of shape of the piece, I, J, L, O, S, Z, T
  public char shape;

  // the position of the upper-left corner of the tiles array 
  // relative to the Tetris grid
  public int rowOffset;
  public int colOffset;

  // used to determine 2-state-rotations for shapes S, Z, I
  // set to true to indicate the next call to rotate() should
  // rotate clockwise
  public boolean rotateClockwiseNext = false;

  // an array marking where the visible tiles are
  // a 1 indicates there is a visible tile in that position
  // a 0 indicates there is no visible tile in that position
  public int[][] tiles;


  // No-argument constructor of Piece
  // This constructor will randomly select a shape from the seven possibilities
  // set the instance variable shape to the appropriate char, and deep copy the
  // corresponding initial tiles array to the instance variable tiles.
  public Piece(){
    int number = random.nextInt(7);
    tiles = new int[initialTiles[number].length][initialTiles[number][0].length];
    this.shape = possibleShapes[number];
    for(int i = 0; i < this.initialTiles[number].length; i++){
      for(int j = 0; j < this.initialTiles[number][i].length;j++){
        this.tiles[i][j] = this.initialTiles[number][i][j];
      }
    }
    //If the shape selected is 'O', the default rowOffset and colOffset 
    //should be 0,4
    if(number == 0){
      this.rowOffset = 0;
      this.colOffset = 4;
    }
    //Except for shape 'O', the default rowOffset and colOffset will all be -1,3
    else
    {
      this.rowOffset = -1;
      this.colOffset = 3;
    }
  }
  // This constructor creates a new Piece object based on the parameter
  // character passed in
  public Piece(char shape) {
    this.shape = shape;
    int index = 0;
    // Selected proper shape based on the parameter passed in
    switch(shape){
      case 'O': index = 0; break;
      case 'I': index = 1; break;
      case 'S': index = 2; break;
      case 'Z': index = 3; break;
      case 'J': index = 4; break;
      case 'L': index = 5; break;
      case 'T': index = 6; break;
    }
    this.shape = possibleShapes[index];
    tiles = new int[initialTiles[index].length][initialTiles[index][0].length];
    for(int i = 0; i < initialTiles[index].length; i++){
      for(int j = 0; j < initialTiles[index][i].length;j++){
        this.tiles[i][j] = initialTiles[index][i][j];
      }
    }
    //If the parameter is 'O' then the default value of rowOffset and colOffset
    //will be 0 and 4
    if(index == 0){
      this.rowOffset = 0;
      this.colOffset = 4;
    }
    //Except for the parameter is 'O', the default value of rowOffset and
    //colOffset will be -1 and 3
    else
    {
      this.rowOffset = -1;
      this.colOffset = 3;
    }
  }

  // This is the copy constructor of Piece.
  public Piece(Piece other){
    this.shape = other.shape;
    this.rowOffset = other.rowOffset;
    this.colOffset = other.colOffset;
    this.rotateClockwiseNext = other.rotateClockwiseNext;
    int index = 0;
    for(int num = 0; num < possibleShapes.length; num++){
      if(possibleShapes[num] == other.shape){
        index = num;
      }
    }
    
    tiles = new int[initialTiles[index].length][initialTiles[index].length];
    for(int i = 0; i < other.tiles.length; i++){
      for(int j = 0; j < other.tiles[i].length; j++){
        this.tiles[i][j] = other.tiles[i][j];
      }
    }


  }

  // This method will rotate the tile array based on the shape of the current
  // piece.
  public void rotate(){
    switch(this.shape){
      //If the current shape is O,T,L,J, always rotate clockwise
      //If the shape is I,S,Z, the first call to this method rotates counter
      //clockwise, then the second call to this method should rotate clockwise
      //then counterclockwise, and so on.
      case 'O':this.rotateClockwise();break;
      case 'T':this.rotateClockwise();break;
      case 'L':this.rotateClockwise();break;
      case 'J':this.rotateClockwise();break;
      case 'I':if(this.rotateClockwiseNext == false)
               {
                 this.rotateCounterClockwise();
                 this.rotateClockwiseNext = true;
               }
               else
               {
                 this.rotateClockwise();
                 this.rotateClockwiseNext = false;
               }
               break;
      case 'S':if(this.rotateClockwiseNext == false)
               {
                 this.rotateCounterClockwise();
                 this.rotateClockwiseNext = true;
               }
               else
               {
                 this.rotateClockwise();
                 this.rotateClockwiseNext = false;
               }
               break;
      case 'Z':if(this.rotateClockwiseNext == false)
               {
                 this.rotateCounterClockwise();
                 this.rotateClockwiseNext = true;
               }
               else
               {
                 this.rotateClockwise();
                 this.rotateClockwiseNext = false;
               }
               break;
    }
  }

  // Rotate the tiles array clockwise
  public void rotateClockwise() {
    int[][] temp = new int[this.tiles.length][this.tiles.length];
    //rotate the original array and copy it to a new array
    for(int i = 0; i < this.tiles.length; i++){
      for(int j = 0; j < this.tiles[i].length; j++){
        temp[i][j] = this.tiles[this.tiles[i].length - j - 1][i];
      }
    }
    //copy the rotated new array back into the old array
    for(int m = 0; m < this.tiles.length; m++){
      for(int n = 0; n < this.tiles[m].length; n++){
        this.tiles[m][n] = temp[m][n];
      }
    }
  }
  // Rotate the tiles array counterclockwise
  public void rotateCounterClockwise () {

    int[][] temp = new int[this.tiles.length][this.tiles.length];
    //rotate the original array and copy it to a new array
    for(int i = 0; i < this.tiles.length; i++){
      for(int j = 0; j < this.tiles[i].length; j++){
        temp[i][j] = this.tiles[j][this.tiles[i].length - 1 - i];
      }
    }
    //copy the rotated new array back into the old array
    for(int m = 0; m < this.tiles.length; m++){
      for(int n = 0; n < this.tiles[m].length; n++){
        this.tiles[m][n] = temp[m][n];
      }
    }
  }

  // Use the parameter direction to increment/decrement the instance variables
  // rowOffset and colOffset accordingly.
  public void move(Direction direction) {
    switch(direction){
      case DOWN: this.rowOffset += 1; break;
      case LEFT: this.colOffset -= 1; break;
      case RIGHT: this.colOffset += 1; break;
    }
  }

  //main method to test the code
  /*
     public static void main(String[] args){
     System.out.println("This is the tester for ramdonly create a shape");
     Piece theFirst = new Piece();
     System.out.println("The default rowOffset " + theFirst.rowOffset);
     System.out.println("The default colOffset " + theFirst.colOffset);
     System.out.println();

     System.out.println("This is the tester for specific shape");
     Piece theSecond = new Piece('I');
     System.out.println("The default rowOffset " + theSecond.rowOffset);
     System.out.println("The default colOffset " + theSecond.colOffset);
     System.out.println(theSecond.rotateClockwiseNext);
     theSecond.rotate();
     System.out.println(theSecond.rotateClockwiseNext);
     theSecond.rotate();
     System.out.println(theSecond.rotateClockwiseNext);
     System.out.println();

     System.out.println("This is the tester for rotating 'L'");
     Piece theFourth = new Piece('L');
     System.out.println("This is the default figure");
     for(int i = 0; i < theFourth.tiles.length; i++){
     for(int j = 0; j < theFourth.tiles[i].length; j++){
     System.out.print(theFourth.tiles[i][j]);
     }
     System.out.println();
     }
     System.out.println();
     System.out.println("After first rotation...");
     theFourth.rotate();
     for(int a = 0; a < theFourth.tiles.length; a++){
     for(int b = 0; b < theFourth.tiles[a].length; b++){
     System.out.print(theFourth.tiles[a][b]);
     }
     System.out.println();
     }
     System.out.println();
     System.out.println("After second rotation...");
     theFourth.rotate();
     for(int c = 0; c < theFourth.tiles.length; c++){
     for(int d = 0; d < theFourth.tiles[c].length; d++){
     System.out.print(theFourth.tiles[c][d]);
     }
     System.out.println();
     }
     System.out.println();
     System.out.println("After third rotation...");
     theFourth.rotate();
     for(int e = 0; e < theFourth.tiles.length; e++){
     for(int f = 0; f < theFourth.tiles[e].length; f++){
     System.out.print(theFourth.tiles[e][f]);
     }
     System.out.println();
     }
     System.out.println();
     System.out.println();

     System.out.println("This is the tester for rotating 'Z'");
     Piece theFifth = new Piece('Z');
     System.out.println("This is the default figure");
     for(int za = 0; za < theFifth.tiles.length; za++){
     for(int zb = 0; zb < theFifth.tiles[za].length; zb++){
     System.out.print(theFifth.tiles[za][zb]);
     }
     System.out.println();
     }
     System.out.println();
     System.out.println("After first rotation...");
     theFifth.rotate();
     for(int zc = 0; zc < theFifth.tiles.length; zc++){
for(int zd = 0; zd < theFifth.tiles[zc].length; zd++){
  System.out.print(theFifth.tiles[zc][zd]);
}
System.out.println();
     }
System.out.println();
System.out.println("After second rotation...");
theFifth.rotate();
for(int ze = 0; ze < theFifth.tiles.length; ze++){
  for(int zf = 0; zf < theFifth.tiles[ze].length; zf++){
    System.out.print(theFifth.tiles[ze][zf]);
  }
  System.out.println();
}
System.out.println();
     }
*/
}
