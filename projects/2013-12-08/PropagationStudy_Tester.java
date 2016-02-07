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


/**Test class for the study.  
 * This assumes successful completion of the other tests.
 */
public class PropagationStudy_Tester{
    /**Testing get/set.  
     */
    @Test
    public void testGetSet(){
	PropagationStudy study = new PropagationStudy(new Network(), 
						      0.01, 0.01, 0.01);
	Entity joe = new Entity("Joe");
	Entity abc = new Entity("Abc");
	Entity bob = new Entity("Bob");
	double delta = 0.0001; //need this for assertEquals
	study.getNetwork().addRelation(joe, abc);
	//check if the Network was added, can be used correctly
	assertEquals(joe, ( (LinkedListIterator<Entity>)
			    study.getNetwork().iterator()).find(joe));
	assertEquals(abc, ( (LinkedListIterator<Entity>)
			    study.getNetwork().iterator()).find(abc));
	assertEquals(null,( (LinkedListIterator<Entity>)
			    study.getNetwork().iterator()).find(bob));
	
	study.setNetwork(new Network());
	//new Network if he is not in it
	assertEquals(null, ( (LinkedListIterator<Entity>)
			     study.getNetwork().iterator()).find(joe));
	//infect rate
	assertEquals(0.01, study.getInfectionRate(), delta);
	study.setInfectionRate(0.95);
	assertEquals(0.95, study.getInfectionRate(), delta);
	//heal rate
	assertEquals(0.01, study.getHealRate(), delta);
	study.setHealRate(0.95);
	assertEquals(0.95, study.getHealRate(), delta);
	//wear rate
	assertEquals(0.01, study.getWearRate(), delta);
	study.setWearRate(0.95);
	assertEquals(0.95, study.getWearRate(), delta);
    }

    /**Test the infect and inoculate methods.  
     * Note that the methods that accept doubles use the RNG, and as such are
     * not tested. Also does not test printNetwork.
     * This also tests the counters (getNum*) and resetNetwork.
     */
    @Test
    public void testSickenAndCure(){
	PropagationStudy study = new PropagationStudy(new Network(), 
						      0.01, 0.01, 0.01);
	Entity joe = new Entity("Joe");
	Entity abc = new Entity("Abc");
	Entity bob = new Entity("Bob");
	try{
	    study.infect(abc); //choice is arbitrary
	    fail();
	}
	catch(NoSuchElementException e){
	    //good
	    try{
		study.inoculate(joe);
		fail();
	    }
	    catch(NoSuchElementException e2){
		//good
	    }
	}
	study.resetNetwork(); //if does not crash, is cleared for none
	try{
	    study.getNetwork().addRelation(joe, abc);
	    study.getNetwork().addRelation(abc, bob);
	    //getNum*
	    assertEquals(3, study.getNumUninfected()); //many
	    assertEquals(0, study.getNumInoculated()); //none
	    assertEquals(0, study.getNumInfected());
	    //new Entity are created as Uninfected
	    study.infect(joe);
	    assertEquals(Health.Infected, joe.getHealthStatus());
	    study.inoculate(bob);
	    assertEquals(Health.Inoculated, bob.getHealthStatus());
	    //getNum* again
	    assertEquals(1, study.getNumUninfected()); //one
	    assertEquals(1, study.getNumInoculated());
	    assertEquals(1, study.getNumInfected());
	    //reset
	    study.resetNetwork();
	    assertEquals(3, study.getNumUninfected());
	    assertEquals(0, study.getNumInoculated());
	    assertEquals(0, study.getNumInfected());
	    //print cannot be tested because it involves System.out.println();
	}
	catch(Exception e){
	    fail();
	}
    }
}
