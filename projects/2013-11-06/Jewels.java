/**
 * @author James Starkman, jas497
 * Programming project 3, due 2013-11-06T23:59:00 or sooner
 * 
 * This class is a malformed clone of the game Jewel Quest.  It contains all
 * needed methods to run itself.  It also takes input from the command line, to
 * determine initial parameters (if given).  Please note that data and pictures
 * have been split.
 * 
 * Extra credit:
 *     background color changes for click, icons for jewels
 */
//80-column check
//3456789_123456789_123456789_123456789_123456789_123456789_123456789_123456789!
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Jewels implements ActionListener{
    //The table has been marred by the Javadoc entries.
    //Fields - minimise usage of these
    /**This many columns; the cells that make up the width.*/
    private int         widthCells;
    /**This many rows; the cells that make up the height.*/
    private int         heightCells;
    /**This many types of rock.*/
    private int         jewelTypes;
    /**For generating random numbers.  Could be created when needed, but this
     * way is faster.*/
    private Random      RNG;
    /**Total (legal) moves made so far.*/
    private int         movesTaken;
    /**What type of gem in a given cell, and flag.  Structure: high bits have
     * flags (16 and 32's place), low bits have type (0x00 to 0x0F).*/
    private byte[][]    data;
    /**For finding neighbors; a pair of ints packed as ( ((long)x)<<32) + y*/
    private long        lastLocation;
    /**Makes returning win condition easier.*/
    private boolean     youHaveWon;
    //graphics-related fields
    /**For rendering the pictures to the user.*/
    private JPanel      board;
    /**Main window visible to the user.*/
    private JFrame      window;
    /**For rendering data.  Buttons stay, icons are re-set to update.*/
    private JButton[][] buttons;
    /**Cheaper to slurp the icons in all at once than to reload each time.*/
    private ImageIcon[] gemTypes;
    //magic numbers below - these could be #defined as such (in C[++])
    //could also be in a special options.txt file ...
    /**Under the rules of the assignment, only one cell can make triplets.
     * Set this flag to change that to both.*/
    private final boolean BOTH_CELLS = false;
    /**Lower Boundary for types of gem for this game.*/
    private final int     LB = 2;
    /**Upper Boundary for types of gem.*/
    private final int     UB = 8;     //upper bound
    /**Side length of each cell, initially.  */
    private final int     SCALER = 64;
    /**Color of done cells, ones that have already been marked.*/
    private final Color   COL_DONE     = new Color(0xFFFFFF);//RRGGBB, standard
    /**Color of not yet marked cells.  The whole board starts this way.*/
    private final Color   COL_NOTYET   = new Color(0x000000);
    /**Color of last-clicked cell, the one stored by lastLocation.*/
    private final Color   COL_LASTCLICK= new Color(0xC8C806);

    //Constructors
    /**Primary constructor, where rows, columns, and quantity of different
     * types of gem are defined.
     * 
     * @param rows      The number of rows.
     * @param columns   The number of columns.
     * @param numJewels The quantity of different types of gem.
     */
    public Jewels(int rows, int columns, int numJewels){
	this.widthCells  = columns;
	this.heightCells = rows;
	this.jewelTypes  = (numJewels<LB ? LB :(numJewels>UB ? UB : numJewels));
	//the rest of it:
	this.initAll();
    }

    /**Simple constructor that provides defaults.
     */
    public Jewels(){
	this(8, 10, 4); //default config
    }

    //methods
    /**Initialisation method.  Called once.*/
    private void initAll(){
	//data
	RNG          = new Random();
	movesTaken   = 0;
	data         = new byte[heightCells][widthCells];
	lastLocation = (long)-1;
	youHaveWon   = false;
	//pictures
	board        = new JPanel(new GridLayout(heightCells, widthCells));
	window       = new JFrame("JewelQuest clone");
	buttons      = new JButton[heightCells][widthCells];
	gemTypes     = new ImageIcon[jewelTypes]; //image object repository
	
	//window: formatting
	window.getContentPane().add(board, "Center");	
	window.setSize(widthCells * SCALER, heightCells * SCALER);
	window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	//gemTypes: files were named systematically; no need to glob
	for(int name = 0; name < jewelTypes; name++){
	    //this works on my (windows) machine; do not need a \\
	    gemTypes[name] = new ImageIcon("images/" + name + ".png");
	}

	//button-init: do once per cell, to generate contents.
	for(int i = 0; i < (widthCells * heightCells); i++){
	    int y = i / widthCells; int x = i % widthCells; //saves math
	    data[y][x] = makeRock();
 	    //structure of data[][]: low hex digit is type, high is flags
	    //flags: null, null, to-be-cleared, normal/cleared. High-to-low.

	    buttons[y][x] = new JButton(gemTypes[data[y][x] & 0x0F]);
	    buttons[y][x].addActionListener(this);
	    buttons[y][x].setBackground(COL_NOTYET);
	    board.add(buttons[y][x]); //left-right-top-down adding order
	}
	window.setVisible(true);
    }
    
    /**Random number <span style="text-decoration: line-through">god</span>
     * generator that randomly selects a value that corresponds to a type of
     * gem and returns said value.
     *
     * @return a valid number for index of gemTypes
     */
    public byte makeRock(){
	int r = RNG.nextInt(); //temporary holding
	return (byte)( ((r < 0) ? r*-1 : r) % jewelTypes);
    }

    /**Captures input and calls the click processor.
     * @param e info about button click that happened
     */
    @Override
    public void actionPerformed(ActionEvent e){
	JButton b = (JButton) e.getSource();                //ptr to clicked btn
	boolean lowProcessing = true;                       //flag
	//loop over the cells once (or less w/ flag, not break)
	for(int i = 0; lowProcessing && i < (heightCells * widthCells); i++){
	    int y = i / widthCells; int x = i % widthCells; //saves math
	    if(buttons[y][x] == b){                         //both ptrs, works
		if(lastLocation == (long)-1){               //first click
		    lastLocation = (( (long)x )<<32) + y;
		    //pictures
		    buttons[y][x].setBackground(COL_LASTCLICK);
		}
		else{                                       //second click
		    //pictures.  Line has not been wrapped.
		    //Revert to old color, instead of being COL_LASTCLICK
		    buttons[(int)lastLocation][(int)(lastLocation>>32)].setBackground( ((data[y][x] & 0x10) == 0x10)? COL_DONE : COL_NOTYET );
		    processClick(x, y);
		}
		//should break here, to save processing.  Break is verboten,
		//so this flag is used instead.
		lowProcessing = false;
	    }
	}
    }

    /**Checks to see if the two clicks were in adjecent cells.
     *
     * @param x x-coordinate of last-clicked cell
     * @param y y-coordinate of last-clicked cell
     */
    public void processClick(int x, int y){
	/* Checks if cell is first, or if second is neighbor of first.  If not,
	 * is a bad value, and lastLocation is forced to -1.  If they are
	 * neighbors, calls exchange() on them, which does the important part.
	 */
	//check the 4 neighbors for a previously-clicked one
	int x_ll = (int)(lastLocation>>32); //x sub lastLocation
	int y_ll = (int)(lastLocation);     //y sub lastLocation
	if((y == y_ll+1 && x == x_ll  ) ||
	   (y == y_ll-1 && x == x_ll  ) ||
	   (y == y_ll   && x == x_ll+1) ||
	   (y == y_ll   && x == x_ll-1) )
	    exchange(x, y);
	//always clear when done, or not neighbors
	lastLocation = (long) -1;
    }

    /**Effects the exchange by swapping the cells, then calling the
     * change-effector on each.  
     * 
     * @param x x-coord of click
     * @param y y-coord of click
     */
    public void exchange(int x, int y){ //also decides what to kill
	/* After swapping the cells, updates the GUI and checks for a win.  If
	 * no matches are found, instead swaps cells back.
	 */
	//store and swap
	byte temp  = data[y][x];    //temporary storage, for the swap
	data[y][x] = data[(int)lastLocation][(int)(lastLocation>>32)];
	data[(int)lastLocation][(int)(lastLocation>>32)] = temp;

	//finding time
	if(kill(x, y)){             //worked!
	    movesTaken++;
	    updatePictures();
	    checkWin();
	}
	else{                       //no work - swap back
	    //note: this is now what used to be in lastLocaiton
	    //self-undoing, like foo *= -1;
	    byte temp2 = data[y][x];//temporary storage
	    data[y][x] = data[(int)lastLocation][(int)(lastLocation>>32)];
	    data[(int)lastLocation][(int)(lastLocation>>32)] = temp2;
	}
    }

    /**Kills groups of three or more, and then shifts the rocks downward.
     * 
     * @param  x x-coord of start
     * @param  y y-coord of start
     * @return Whether anything was cleared (true) or not (false).
     */
    public boolean kill(int x, int y){
	/* This calls the flagger, search(), on each potential ray to trace.  It
	 * then shifts the cells down and makes a new rock.  If a shift occured,
	 * the shifter is run again on that cell (loop passes bottom -> up).
	 */
	int stuffToKill = 0; //OR uses lazy evaluation.  This helps avoid that.
	byte c = (byte)(data[y][x] & 0x0F);   //c is abbr. for color, or type
	stuffToKill += search(x, y, c, 0, 1); //down  from current click-pt
	stuffToKill += search(x, y, c, 0, -1);//up    from current click-pt
	stuffToKill += search(x, y, c, 1, 0); //right from current click-pt
	stuffToKill += search(x, y, c, -1, 0);//left  from current click-pt
	stuffToKill += search(x+1,y,c, -1, 0);//step right and shoot from there
	stuffToKill += search(x,y+1,c, 0, -1);//step down and shoot from there
	//this makes the game better (if BOTH_CELLS is true).
	if(BOTH_CELLS){
	    int x_ll = (int)(lastLocation>>32);         //x sub lastLocation
	    int y_ll = (int)lastLocation;               //y sub lastLocation
	    byte C   = (byte)(data[y_ll][x_ll] & 0x0F); //C is abbr. for color
	    stuffToKill += search(x_ll, y_ll, C, 0, 1); //down
	    stuffToKill += search(x_ll, y_ll, C, 0, -1);//up
	    stuffToKill += search(x_ll, y_ll, C, 1, 0); //right
	    stuffToKill += search(x_ll, y_ll, C, -1, 0);//left
	    stuffToKill += search(x_ll+1,y_ll,C, -1, 0);//step back and shoot
	    stuffToKill += search(x_ll,y_ll+1,C, 0, -1);//step back and shoot
	}
	if(stuffToKill != 0){
	    /* now for clearing; scan bottom-up, shift if needed
	     * This could be more efficient due to at most two lines being
	     * removed, but this is more extensible.
	     */
	    int i = (heightCells * widthCells)-1;              //counter
	    //not a for loop because may need multiple iterations per cell
	    //could go bottom-up first, but this is easier given data[][]
	    while(i >= 0){
		int Y = i / widthCells; int X = i % widthCells;//saves math
		if((data[Y][X] & 0x20) == 0x20){ //if to-be-cleared flag is on
		    //shift everyone down
		    int stop = Y;                //know when to stop
		    //each pass moves current mark up the grid by one cell.
		    while(stop > 0){
			//need to preserve mark while copy to-be-cleared flags
			//all of upper cell, of course
			data[stop][X] = (byte)( (data[stop][X]   & 0x10) + 
						(data[--stop][X] & 0x2F) );
		    }
		    data[0][X] &= 0x10;          //keep one flag
		    data[0][X] += makeRock();    //set content to random rock
		}
		else{   //finally have a stable ceiling, no more falling
		    i--;
		}
	    }
	    return true;
	}
	else{
	    return false;
	}
    }

    /**Finds groups of three or more, and removes them.
     * 
     * @param x      x-coord of start of single-direction floodfinder
     * @param y      y-coord of start of single-direction floodfinder
     * @param master type/color of clicked-on rock; "data[yClick][xClick]&0x0F"
     * @param delX   offset direction of X
     * @param delY   offset direction of Y
     * @return A zero if found nothing, a one if something
     */
    public int search(int x, int y, byte master, int delX, int delY){
	/* Traces a ray from the point given in the direction given, stopping
	 * when it runs out of like-typed cells (or hits a wall).
	 */
	master &= 0x0F;                  //just in case
	try {
	    boolean dead    = false;     //tells when done executing
	    int     goodYet = ((data[y][x] & 0x0F) == master ?1:0);
	    //goodYet is number of like-typed rocks in a row
	    //loop: move current cell, check, kill if needed 
	    while(!dead){
		x += delX; y += delY;    //no need to check start cell
		//needs to be in bounds
		if(x < widthCells && x >= 0 && y < heightCells && y >=0){
		    //is gem type correct?
		    if((data[y][x] & 0x0F) == master){
			goodYet++;
		    }
		    else dead = true;
		}
		else dead = true;
	    }
	    if(goodYet >= 3){
		//this direction worked.  Now to do it in reverse
		//this will do same # iterations as goodYet's old value
		while(goodYet-- != 0){
		    x -= delX; y -= delY;//trace the ray back to init
		    data[y][x] |= 0x30;  //set two flags
		}
		return 1;
		//must survive that to get here && be considered relevant
	    }
	    else return 0;
	}
	catch(ArrayIndexOutOfBoundsException e){
	    return 0;
	}
    }

    /**Checks to see if win condition has been met. */
    public void checkWin(){
	/* This procedure loops over the data, checking for bit 0x10 to be on at
	 * each one.  If any are off, the user has not won.  
	 */
	boolean keepGoing = true; //to end early
	//loop over every cell once.  keepGoing is only in condition for
	//processing-power reasons; could be removed.
	for(int i = 0; keepGoing && (i < (heightCells*widthCells)); i++){
	    int y = i / widthCells; int x = i % widthCells; //cleaner code
	    if((data[y][x] & 0x10) != 0x10){
		keepGoing = false;//break, continue are verboten
	    }
	}
	if(keepGoing){
	    youHaveWon = true;
	    //this should be in main, but how?
	    System.out.printf("Won in %d moves\n", movesTaken);
	    JOptionPane.showMessageDialog(window, "Winner!");
	}
    }

    /**Show pictures to user.*/
    public void updatePictures(){
	/*Call me after modifying the data, to update the display.*/
	//update every cell's icon and background to match data
	for(int i = 0; i < (heightCells*widthCells); i++){
	    int y = i / widthCells; int x = i % widthCells; //saves math
	    buttons[y][x].setIcon(gemTypes[data[y][x] & 0x0F]);
	    buttons[y][x].setBackground( ((data[y][x] & 0x10) == 0x10) ?
						  COL_DONE : COL_NOTYET);
	}
    }

    /**Main method.
     * This method interfaces with the OS.  If bad input parameters are given, 
     * a stock model of the game is used instead.
     * 
     * @param args The arguments that the OS passes in.
     */
    public static void main(String[] args){
	Jewels game;
	if(args.length == 3){
	    try{
		game = new Jewels(Integer.parseInt(args[0]), 
				  Integer.parseInt(args[1]),
				  Integer.parseInt(args[2]));
	    }
	    catch(NumberFormatException e){ //if inputs are not parseInt-able.
		game = new Jewels();
	    }
	}
	else{
	    game = new Jewels();
	}
    }

    //get set - only here for testing
    public void setLL(long in){
	this.lastLocation = in;
    }
    public long getLL(){
	return lastLocation;
    }
    public int dataProbe(int x, int y){
	return (int)data[y][x];
    }
    public void dataForce(int x, int y, byte value){
	data[y][x] = value;
    }
    public JButton getButtons(int x, int y){
	return buttons[y][x];
    }
    public ImageIcon getGemTypes(int which){
	return gemTypes[which];
    }
    public Color getCOL_DONE(){
	return COL_DONE;
    }
    public boolean getYouHaveWon(){
	return youHaveWon;
    }
}
