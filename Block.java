import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

public class Block {
 private int xCoord;
 private int yCoord;
 private int size; // height/width of the square
 private int level; // the root (outer most block) is at level 0
 private int maxDepth;
 private Color color;

 private Block[] children; // {UR, UL, LL, LR}

 public static Random gen = new Random(2);



 /*
  * These two constructors are here for testing purposes.
  */
 public Block() {}

 public Block(int x, int y, int size, int lvl, int  maxD, Color c, Block[] subBlocks) {
  this.xCoord=x;
  this.yCoord=y;
  this.size=size;
  this.level=lvl;
  this.maxDepth = maxD;
  this.color=c;
  this.children = subBlocks;
 }
 
 

 /*
  * Creates a random block given its level and a max depth. 
  * 
  * xCoord, yCoord, size, and highlighted should not be initialized
  * (i.e. they will all be initialized by default)
  */
 public void helper (Block hBlock){
  hBlock.color = GameColors.BLOCK_COLORS[gen.nextInt(4)];
  hBlock.children = new Block[0];

 }

 public Block(int lvl, int maxDepth) {
  /*
   * ADD YOUR CODE HERE
   */
  this.level = lvl;
  this.maxDepth = maxDepth;

  if(lvl>maxDepth){
   throw new IllegalArgumentException();
  }

  if (lvl == maxDepth) {
   helper(this);
  }

  else{

   if (gen.nextDouble(0, 1) < Math.exp(-0.25 * lvl)) {

    this.children = new Block[4];

    for (int i = 0; i < 4; i++) {
     this.children[i] = new Block(lvl + 1, maxDepth);
    }
   }

   else{helper(this);}
  }
 }




 /*
  * Updates size and position for the block and all of its sub-blocks, while
  * ensuring consistency between the attributes and the relationship of the 
  * blocks. 
  * 
  *  The size is the height and width of the block. (xCoord, yCoord) are the 
  *  coordinates of the top left corner of the block. 
  */

 public void updateSizeAndPosition (int size, int xCoord, int yCoord) {
   // validate input size
   if (size <= 0 || xCoord <0 || yCoord<0 || !(size%2 == 0 || size ==1)) {
    throw new IllegalArgumentException();
   }
   // update this block's fields
   // recursively update sub-blocks

  if (this.children.length != 0) {
    int subSize = size / 2;
    children[0].updateSizeAndPosition(subSize, xCoord + subSize, yCoord); // UR
    children[1].updateSizeAndPosition(subSize, xCoord, yCoord); // UL
    children[2].updateSizeAndPosition(subSize, xCoord, yCoord + subSize); // LL
    children[3].updateSizeAndPosition(subSize, xCoord + subSize, yCoord + subSize); // LR
   }

  this.size = size;
  this.xCoord = xCoord;
  this.yCoord = yCoord;

  }

// helper method to check if a number is a power of two

 //private boolean isPowerOfTwo(int n){

  //return (n & (n - 1)) == 0;

 /*
  * Returns a List of blocks to be drawn to get a graphical representation of this block.
  * 
  * This includes, for each undivided Block:
  * - one BlockToDraw in the color of the block
  * - another one in the FRAME_COLOR and stroke thickness 3
  * 
  * Note that a stroke thickness equal to 0 indicates that the block should be filled with its color.
  *  
  * The order in which the blocks to draw appear in the list does NOT matter.
  */
 public ArrayList<BlockToDraw> getBlocksToDraw() {
  ArrayList<BlockToDraw> blocksToDraw = new ArrayList<>();

  return helperBlockToDraw(blocksToDraw);
 }

 private ArrayList<BlockToDraw> helperBlockToDraw(ArrayList<BlockToDraw> lst) {
  if (this.children.length != 0) {
   for (Block child : this.children) {
    child.helperBlockToDraw(lst);
   }
  } else {

   BlockToDraw b1 = new BlockToDraw(this.color, this.xCoord, this.yCoord, this.size, 0);
   BlockToDraw b2 = new BlockToDraw(GameColors.FRAME_COLOR, this.xCoord, this.yCoord, this.size, 3);

   lst.add(b1);
   lst.add(b2);}

   return lst;
  }

  // If the block has not been divided, add two BlockToDraws
  //if (children == null) {
   // Add the undivided block in its color
   //BlockToDraw undividedBlock = new BlockToDraw(color, xCoord, yCoord, size, 0);
   //blocksToDraw.add(undividedBlock);

   // Add the undivided block in the frame color with stroke thickness equal to 3
   //BlockToDraw frameBlock = new BlockToDraw(GameColors.FRAME_COLOR, xCoord, yCoord, size, 3);
   //blocksToDraw.add(frameBlock);

   //return blocksToDraw;
  //}

  // If the block has been divided, recursively call getBlocksToDraw() on each child block
  //for (int i = 0; i < children.length; i++) {
   //ArrayList<BlockToDraw> childBlocksToDraw = children[i].getBlocksToDraw();
   //blocksToDraw.addAll(childBlocksToDraw);
  //}

  //return blocksToDraw;
 //}


 /*
  * This method is provided and you should NOT modify it. 
  */
 public BlockToDraw getHighlightedFrame() {
  return new BlockToDraw(GameColors.HIGHLIGHT_COLOR, this.xCoord, this.yCoord, this.size, 5);
 }

 /*
  * Return the Block within this Block that includes the given location
  * and is at the given level. If the level specified is lower than 
  * the lowest block at the specified location, then return the block 
  * at the location with the closest level value.
  * 
  * The location is specified by its (x, y) coordinates. The lvl indicates 
  * the level of the desired Block. Note that if a Block includes the location
  * (x, y), and that Block is subdivided, then one of its sub-Blocks will 
  * contain the location (x, y) too. This is why we need lvl to identify 
  * which Block should be returned. 
  * 
  * Input validation: 
  * - this.level <= lvl <= maxDepth (if not throw exception)
  * - if (x,y) is not within this Block, return null.
  */
 public Block getSelectedBlock(int x, int y, int lvl) {
   if (x<0 || y <0 || this.level >lvl || lvl > maxDepth) {
    throw new IllegalArgumentException();
   }
   if (xCoord > x || xCoord + size <= x || yCoord > y || yCoord + size <= y) {
    return null; // position is not within this block
   }
   if (level == lvl || children.length ==  0) {
    return this; // found block at desired level or leaf
   }
   // search for children
   for (Block child : children) {
    Block selected = child.getSelectedBlock(x, y, lvl);
    if (selected != null) {
     return selected;
    }
   }
   return null; // no child contains the level
  }

 
 

 /*
  * Swaps the child Blocks of this Block. 
  * If input is 1, swap vertically. If 0, swap horizontally. 
  * If this Block has no children, do nothing. The swap 
  * should be propagate, effectively implementing a reflection
  * over the x-axis or over the y-axis.
  * 
  */
 public void reflect(int direction) {


  if (direction == 0) {

//                reflected over the x-axis (if the input is 0)

   if (this.children.length != 0) {


    Block temp0 = this.children[0];
    Block temp1 = this.children[1];
    Block temp2 = this.children[2];
    Block temp3 = this.children[3];

    this.children[0] = temp3;
    this.children[1] = temp2;
    this.children[2] = temp1;
    this.children[3] = temp0;

    this.children[0].updateSizeAndPosition(temp3.size, temp3.xCoord, temp3.yCoord);
    this.children[1].updateSizeAndPosition(temp2.size, temp2.xCoord, temp2.yCoord);
    this.children[2].updateSizeAndPosition(temp1.size, temp1.xCoord, temp1.yCoord);
    this.children[3].updateSizeAndPosition(temp0.size, temp0.xCoord, temp0.yCoord);

   }
  }

  else if (direction == 1) {

   if (this.children.length != 0) {

    Block tmp1 = this.children[1];
    Block tmp0 = this.children[0];
    Block tmp2 = this.children[2];
    Block tmp3 = this.children[3];

    this.children[0] = tmp1;
    this.children[1] = tmp0;
    this.children[2] = tmp3;
    this.children[3] = tmp2;

    this.children[1].updateSizeAndPosition(tmp0.size, tmp0.xCoord, tmp0.yCoord);
    this.children[0].updateSizeAndPosition(tmp1.size, tmp1.xCoord, tmp1.yCoord);
    this.children[2].updateSizeAndPosition(tmp3.size, tmp3.xCoord, tmp3.yCoord);
    this.children[3].updateSizeAndPosition(tmp2.size, tmp2.xCoord, tmp2.yCoord);
   }

  }
  else{throw new IllegalArgumentException();}


  for (Block child : this.children) {

   child.reflect(direction);
  }
  this.updateSizeAndPosition(this.size,this.xCoord,this.yCoord);

 }

 

 
 /*
  * Rotate this Block and all its descendants. 
  * If the input is 1, rotate clockwise. If 0, rotate 
  * counterclockwise. If this Block has no children, do nothing.
  */
 public void rotate(int direction) {
  if (direction == 0) {
   if (this.children.length != 0) {

    Block temp1 = this.children[0];
    Block temp2 = this.children[1];
    Block temp3 = this.children[2];
    Block temp4 = this.children[3];


    this.children[0] = temp4;
    this.children[1] = temp1;
    this.children[2] = temp2;
    this.children[3] = temp3;

    this.children[1].updateSizeAndPosition(temp1.size, temp1.xCoord, temp1.yCoord);
    this.children[0].updateSizeAndPosition(temp4.size, temp4.xCoord, temp4.yCoord);
    this.children[2].updateSizeAndPosition(temp2.size, temp2.xCoord, temp2.yCoord);
    this.children[3].updateSizeAndPosition(temp3.size, temp3.xCoord, temp3.yCoord);
   }
  }
  else if (direction == 1) {

   if (this.children.length != 0) {

    Block temp1 = this.children[0];
    Block temp2 = this.children[1];
    Block temp3 = this.children[2];
    Block temp4 = this.children[3];

    this.children[0] = temp4;
    this.children[1] = temp3;
    this.children[2] = temp2;
    this.children[3] = temp1;
   }
  }else {
   throw new IllegalArgumentException();
  }
  for (Block child : this.children) {
   child.reflect(direction);
  }
 }
 


 /*
  * Smash this Block.
  * 
  * If this Block can be smashed,
  * randomly generate four new children Blocks for it.  
  * (If it already had children Blocks, discard them.)
  * Ensure that the invariants of the Blocks remain satisfied.
  * 
  * A Block can be smashed iff it is not the top-level Block 
  * and it is not already at the level of the maximum depth.
  * 
  * Return True if this Block was smashed and False otherwise.
  * 
  */
 public boolean smash() {
  if(level>0 && level<maxDepth){

   this.children = new Block[4];

   for (int i = 0; i < 4; i++) {
    this.children[i] = new Block(this.level + 1, this.maxDepth);

   }
   this.updateSizeAndPosition(this.size, this.xCoord, this.yCoord);

   return true;
  }
  return false;
 }

 /*
  * Return a two-dimensional array representing this Block as rows and columns of unit cells.
  *
  * Return and array arr where, arr[i] represents the unit cells in row i,
  * arr[i][j] is the color of unit cell in row i and column j.
  *
  * arr[0][0] is the color of the unit cell in the upper left corner of this Block.
  */
 public Color[][] flatten() {

  int tmp= this.size;

  this.updateSizeAndPosition((int) Math.pow(2,this.maxDepth),0,0);

  Color[][] grid= new Color[size][size];

  for (int i = 0; i < size; i++) {

   for (int j = 0; j <size; j++) {

    grid[i][j] = this.getSelectedBlock(j, i, maxDepth).color;}
  }
  this.size= tmp;
  return grid;
 }
 
 // These two get methods have been provided. Do NOT modify them. 
 public int getMaxDepth() {
  return this.maxDepth;
 }
 
 public int getLevel() {
  return this.level;
 }


 /*
  * The next 5 methods are needed to get a text representation of a block. 
  * You can use them for debugging. You can modify these methods if you wish.
  */
 public String toString() {
  return String.format("pos=(%d,%d), size=%d, level=%d"
    , this.xCoord, this.yCoord, this.size, this.level);
 }

 public void printBlock() {
  this.printBlockIndented(0);
 }

 private void printBlockIndented(int indentation) {
  String indent = "";
  for (int i=0; i<indentation; i++) {
   indent += "\t";
  }

  if (this.children.length == 0) {
   // it's a leaf. Print the color!
   String colorInfo = GameColors.colorToString(this.color) + ", ";
   System.out.println(indent + colorInfo + this);   
  } else {
   System.out.println(indent + this);
   for (Block b : this.children)
    b.printBlockIndented(indentation + 1);
  }
 }
 
 private static void coloredPrint(String message, Color color) {
  System.out.print(GameColors.colorToANSIColor(color));
  System.out.print(message);
  System.out.print(GameColors.colorToANSIColor(Color.WHITE));
 }

 public void printColoredBlock() {
  Color[][] colorArray = this.flatten();
  for (Color[] colors : colorArray) {
   for (Color value : colors) {
    String colorName = GameColors.colorToString(value).toUpperCase();
    if (colorName.length() == 0) {
     colorName = "\u2588";
    } else {
     colorName = colorName.substring(0, 1);
    }
    coloredPrint(colorName, value);
   }
   System.out.println();
  }
 }

}
