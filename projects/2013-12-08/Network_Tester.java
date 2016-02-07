import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
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


/**Test class for the Network.  
 * This assumes successful completion of the Entity and LinkedList tests.
 */
public class Network_Tester{
    /**Testing adding and removing relations.  
     * Also tests the iterator(), by usage.
     */
    @Test
    public void testRelation(){
	Network net = new Network();
	Entity fred = new Entity("Fred");
	Entity joey = new Entity("Joey");
	Entity saul = new Entity("Saul");
	/* The lists inside the Entity (and in the Network) are considered to be
	 * without first/last errors, as the test of such shows.  Thus, while 
	 * these could be tested for, they are not. */
	//add two Entity, and be glad of existence of find()
	net.addRelation(fred, joey);
	assertEquals(fred,
		     ((LinkedListIterator<Entity>)net.iterator()).find(fred));
	assertEquals(joey,
		     ((LinkedListIterator<Entity>)net.iterator()).find(joey));
	assertEquals(null, //this one is not in the Network yet
		     ((LinkedListIterator<Entity>)net.iterator()).find(saul));

	assertEquals(joey,
		     ( (LinkedListIterator<Entity>)
		       fred.getRelations().iterator()).find(joey));
	assertEquals(fred,
		     ( (LinkedListIterator<Entity>)
		       joey.getRelations().iterator()).find(fred));
	//last entry
	net.addRelation(fred, saul);
	assertEquals(saul,
		     ((LinkedListIterator<Entity>)net.iterator()).find(saul));
	assertEquals(fred, //should already be here
		     ((LinkedListIterator<Entity>)net.iterator()).find(fred));
	assertEquals(saul,
		     ( (LinkedListIterator<Entity>)
		      fred.getRelations().iterator()).find(saul));
	assertEquals(fred,
		     ( (LinkedListIterator<Entity>)
		       saul.getRelations().iterator()).find(fred));
	//remove them
	net.removeRelation(fred, saul);
	assertEquals(fred, //should still be here
		     ((LinkedListIterator<Entity>)net.iterator()).find(fred));
	assertEquals(saul,
		     ((LinkedListIterator<Entity>)net.iterator()).find(saul));
	assertEquals(null,
		     ( (LinkedListIterator<Entity>)
		       saul.getRelations().iterator()).find(fred));//all gone
	assertEquals(null,
		     ( (LinkedListIterator<Entity>)
		       fred.getRelations().iterator()).find(saul));
    }
    /**Testing mass-adding relations.  
     * File used will be the (bugged) cities hypercube, for reasons of a simple,
     * predictable structure.  Bug mentioned is due to accidental duplicate
     * entries of city names, four doubles in total.  Thus, only 124 unique 
     * Entity, not 128 (=2^7).
     */
    @Test
    public void testAddRelations(){
	//testing both overloads in same test function
	Network net = new Network();
	//first, the file
	net.addRelations(new File("US_Cities_Hypercube.txt"));
	int i = 0;
	for(Entity each : net){ //auto-calls .iterator()
	    i++;
	}
	assertEquals(124, i);   //all of them
	i = 0;//reset
	for(Entity each : ( (LinkedListIterator<Entity>) net.iterator()
			    ).find(new Entity("Columbus")).getRelations()){
	    i++;
	}
	assertEquals(14, i);    //one of the duplicates
	i = 0;//reset
	for(Entity each : ( (LinkedListIterator<Entity>) net.iterator()
			    ).find(new Entity("Cleveland")).getRelations()){
	    i++;
	}
	assertEquals(7, i);     //not one of the duplicates
	/*Duplicates were found by a separate script.  The hypercube has seven
	* dimensions, so the number of links per vertex is seven.*/
	//second, the String[]
	net = new Network(); //old data will be GCed
	net.addRelations();  //none
	i = 0;//reset
	for(Entity each : net){ //auto-calls .iterator()
	    i++;
	}
	assertEquals(0, i);  //none in, none out
	//many below
	net.addRelations("Foo", "Bar",
			 "Bar", "Baz",
			 "Baz", "Qux",
			 "Great", "Quux" );
	i = 0;//reset
	for(Entity each : net){ //auto-calls .iterator()
	    i++;
	}
	assertEquals(6, i);   //all of them
	i = 0;//reset
	for(Entity each : ( (LinkedListIterator<Entity>) net.iterator()
			    ).find(new Entity("Baz")).getRelations()){
	    i++;
	}
	assertEquals(2, i);    //part of a chain
	
	try{
	    net.addRelations("Fred"); //one
	    fail();
	}
	catch(IllegalArgumentException e){
	    //good
	    try{
		net.addRelations("Fred", "George", "Charlie"); //many, odd
		fail();
	    }
	    catch(IllegalArgumentException eThree){
		//good
	    }
	}
    }
}
