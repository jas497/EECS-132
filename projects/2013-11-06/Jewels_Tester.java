import org.junit.*;
import static org.junit.Assert.*;

//Abbreviations sometimes used:
// f - first
// m - middle, many (use context)
// l - last
// n - none
// o - one
//always in form: [nom], [fml]

//80-column check
//3456789_123456789_123456789_123456789_123456789_123456789_123456789_123456789!

/*What these mean: (quoted, minor formatting changes)
Remember the tips for testing: test 0, test 1, test many and test first, test
middle, test last.
For example, first, middle, last can refer to the locations
on the game board (ex: on the edge or in the middle). Your testing report should
describe what you consider to be "units" of the game and what "test 0, test 1,
test many" and "test first, test middle, test last" means for each unit you test

Test 0, test 1, test many: This means you have to test cases where the
parameters, if they are integers, are 0, 1 or some value other than 1. If the
parameters are strings, you have to test strings of length 0, 1, and more than 1
If the strings must contain certain data, you need to test cases where they 
contain 0, 1, and more than 1 of the desired data.

Test first, last, and middle: In cases where you have to search in or modify a
string, you need to test cases where the item to be found or modified is the
first character of the string, the last character of the string, or somewhere in
the middle of the string.
*/

/**
 * Test class Jewels
 * No testing on following: main, constructors and their private helper methods
 * (which exist because code encapsulation is a Good Thing), actionListener.
 */
public class Jewels_Tester{
    Jewels foo    = new Jewels(10, 8, 8); //10 wide, 8 tall, 8 types (0-7)
    long   negOne = -1; //note the type
    /**
     * Test method makeRock()
     */
    @Test
    public void testMakeRock(){
	//this is a randomiser method
	//testing will be ... different
	int trials = 1000;
	byte[] out = new byte[trials];
	for(int i = 0; i < trials; i++){
	    out[i] = foo.makeRock();
	}
	boolean good = true;
	for(int i = 0; good && i < trials; i++){
	    if(out[i] < 0 || out[i] > 7){//7 = maximum value for type of rock,
		                         //    which is private
		good = false; //makeRock() was out of range
	    }
	}
	assertEquals(good, true);
    }

    /**
     * Test method processClick(int, int)
     */
    @Test
    public void testProcessClick(){
	//none, middle
	foo.setLL(negOne);
	foo.processClick(2,3);
	assertEquals(foo.getLL(), negOne);
	//first
	foo.setLL( ((long)1 << 32) + 0); //(1, 0)
	foo.processClick(0,0);
	assertEquals(foo.getLL(), negOne); //also tests if has not crashed
	//last
	foo.setLL( ((long)8 << 32) + 7); //(8,7)
	foo.processClick(7, 9);
	assertEquals(foo.getLL(), negOne); //also tests if has not crashed
    }

    /**
     * Test method exchange(int, int)
     */
    @Test
    public void testExchange(){
	//this prevents uninitialised value errors
	for(int i = 0; i < 80; i++){ //10 wide, 8 tall
	    foo.dataForce(i/10, i%10, (byte)0x00);
	}
	//no swap
	foo.dataForce(4, 5, (byte)0x02); //force cell (4,5) to red
	foo.dataForce(4, 6, (byte)0x03); //force cell below to orange
	foo.setLL(( ((long)4) <<32) + 5);//last click was (4,5)
	foo.exchange(4, 6);              //swap

	assertEquals(foo.dataProbe(4,5), 0x02);//should not swap - no match made
	assertEquals(foo.dataProbe(4,6), 0x03);
	foo.dataForce(4, 5, (byte)0x00); //clean up
	foo.dataForce(4, 6, (byte)0x00);
	
	//with swap
	foo.dataForce(0, 5, (byte)0x02);
	foo.dataForce(1, 5, (byte)0x02);
	foo.dataForce(3, 5, (byte)0x02);
	//now do the swap
	foo.setLL(( ((long)3) <<32) + 5);//last click
	foo.exchange(2, 5);              //swap
	//now, should be all black
	for(int i = 0; i < 6; i++){
	    assertEquals(foo.dataProbe(i, 2), 0x00);
	}
    }

    /**
     * Test method kill(int, int)
     */
    @Test
    public void testKill(){
	//this prevents uninitialised value errors
	for(int i = 0; i < 80; i++){ //10 wide, 8 tall
	    foo.dataForce(i/10, i%10, (byte)0x00);
	}
	//make the resistor rainbow, black down to purple
	for(int i = 0; i < 8; i++){
	    foo.dataForce(0, i, (byte)i);
	}
	foo.dataForce(1, 7, (byte)0x07);
	foo.dataForce(3, 7, (byte)0x07);
	foo.setLL(( ((long)3) <<32) + 7);//last click
	foo.exchange(2, 7);              //swap
	
	//should now be rainbow, from y=1 being black to y=7 being blue
	for(int i = 1; i < 7; i++){
	    assertEquals(foo.dataProbe(0, i) , (byte)(i-1) );
	}
    }

    /**
     * Test method search(int, int, byte, int, int)
     */
    @Test
    public void testSearch(){
	//this prevents uninitialised value errors
	for(int i = 0; i < 80; i++){    //10 wide, 8 tall
	    foo.dataForce(i/10, i%10, (byte)0x00);
	}

	foo.dataForce(0, 3, (byte)0x05);//center left wall green
	assertEquals(foo.search(0, 3, (byte)0x05, -1, 0), 0); //left - wall
	assertEquals(foo.search(0, 3, (byte)0x05,  1, 0), 0); //right - black
	//middle
	foo.dataForce(1, 3, (byte)0x05);    //now there are two greens
	assertEquals(foo.search(1, 3, (byte)0x05, -1, 0), 0); //left - two-row
    }

    /**
     * Test method checkWin()
     */
    @Test
    public void testCheckWin(){
	//this prevents uninitialised value errors
	for(int i = 0; i < 80; i++){  //10 wide, 8 tall
	    foo.dataForce(i/10, i%10, (byte)0x00);
	}
	//none
	foo.checkWin();
	assertEquals(foo.getYouHaveWon(), false);
	
	//one (really _some_)
	for(int i = 0; i < 40; i++){
	    foo.dataForce(i/10, i%10, (byte)0x10);//it's on
	}
	foo.checkWin();
	assertEquals(foo.getYouHaveWon(), false);

	//many
	for(int i = 40; i < 80; i++){ //the rest
	    foo.dataForce(i/10, i%10, (byte)0x10); //on
	}
	foo.checkWin();
	assertEquals(foo.getYouHaveWon(), true);
    }

    /**
     * Test method updatePictures()
     */
    @Test
    public void testUpdatePictures(){
	//this prevents uninitialised value errors
	for(int i = 0; i < 80; i++){  //10 wide, 8 tall
	    foo.dataForce(i/10, i%10, (byte)0x00);
	}
	//first, one
	foo.dataForce(0, 0, (byte)0x10); //bg to black
	assertTrue(foo.getButtons(0, 0).getBackground().equals(foo.getCOL_DONE()));
	
	//middle, many
	foo.dataForce(5, 6, (byte)0x15); //bg to black, rock to number 5
	assertTrue(foo.getButtons(5, 6).getBackground().equals(foo.getCOL_DONE()));
	assertEquals(foo.getButtons(5, 6).getIcon(), foo.getGemTypes(5));

	//last	
	foo.dataForce(9, 7, (byte)0x10); //bg to black
	assertTrue(foo.getButtons(9, 7).getBackground().equals(foo.getCOL_DONE()));
    }
}
