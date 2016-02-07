import org.junit.*;
import static org.junit.Assert.*;

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


/**Test class for each Entity.  
 * This assumes successful completion of the LinkedList test.
 * This also tests the Health enum.
 */
public class Entity_Tester{
    /**Testing [gs]etter methods.  
     * Simple call-and-check.
     */
    @Test
    public void testGetSet(){
	Entity fred = new Entity("Fred");
	//_Name
	assertEquals(fred.getName(), "Fred");
	fred.setName("Frederick");
	assertEquals(fred.getName(), "Frederick");
	//_HealthStatus
	assertEquals(fred.getHealthStatus(), Health.Uninfected);
	fred.setHealthStatus(Health.Infected);
	assertEquals(fred.getHealthStatus(), Health.Infected);
	//_Relations; output is-a LL of Entity
	LinkedList<Entity> castCheck;
	try{
	    castCheck = fred.getRelations();
	    assertEquals(castCheck.getFront(), null); //empty
	    castCheck.addToFront(new Entity("Joe"));
	}
	catch(Exception e){ //Such as ClassCastException
	    fail();
	}
    }

    /**Testing equals, toString, compareTo.  
     */
    @Test
    public void testOther(){
	Entity fred = new Entity("Frederick");
	fred.setHealthStatus(Health.Infected);
	//equals
	assertEquals(true, fred.equals(new Entity("Frederick")));
	assertEquals(false, fred.equals(new Entity("Freddie")));
	//toString
	assertEquals("Frederick: Infected", fred.toString());
	assertEquals(false, fred.toString() == "Fred: Infected");
	assertEquals(false, fred.toString() == "Frederick: Inoculated");
	//compareTo
	assertEquals(true, fred.compareTo(new Entity("Al")) > 0);
	assertEquals(true, fred.compareTo(new Entity("Frederick")) == 0);
	assertEquals(true, fred.compareTo(new Entity("Zune")) < 0);
    }
}
