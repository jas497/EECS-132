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


/**Test class for the Employee database
 * No individual testing on constructors or get/set because they will be tested
 * otherwise.  
 */
public class EmployeeDatabase_Tester{   
    private double delta = 0.001;//this for assertEquals(double, double, delta);

    /**test method add(Employee)
     * test method remove(String, String, int).  
     */
    @Test
    public void testAddAndRemove(){
	EmployeeDatabase db = new EmployeeDatabase();
	Employee joe = new HourlyEmployee("Joe", "Smith", 0, 7.85);
	Employee bob = new HourlyEmployee("Bob", "Cohen", 1, 7.85);
	db.add(joe);
	db.add(bob);
	try{
	    db.remove("Joe", "Johnson", 7);
	    fail(); //should have caught the above
	}
	catch(NoSuchEmployeeException e){
	    //good
	}
	catch(Exception e){
	    fail();
	}

	//NSEException must be caught
	try{
	    assertEquals(bob, db.remove("Bob", "Cohen", 1));
	    assertEquals(joe, db.remove("Joe", "Smith", 0));
	}
	catch(NoSuchEmployeeException e){/*stifle*/}

	//DB is now empty
	try{
	    db.remove("Joe", "Smith", 0);
	    fail(); //should have caught the above
	}
	catch(NoSuchEmployeeException e){
	    //good
	}
	catch(Exception e){
	    fail();
	}
    }

    /**test method find(String, String, int).  
     */
    @Test
    public void testFind(){
	EmployeeDatabase db = new EmployeeDatabase();
	Employee joe = new HourlyEmployee("Joe", "Smith", 0, 7.85);
	Employee bob = new SalesSupervisor("Bob", "Cohen", 1, 6543.2, 128.0);

	//empty DB
	try{
	    db.find("Joe", "Johnson", 7);
	    fail(); //should have caught the above
	}
	catch(NoSuchEmployeeException e){
	    //good
	}
	catch(Exception e){
	    fail();
	}
	db.add(joe);
	db.add(bob);
	//need this b/c NSEE
	try{
	    assertEquals(bob, db.find("Bob", "Cohen", 1));
	    assertEquals(joe, db.find("Joe", "Smith", 0));
	}
	catch(NoSuchEmployeeException e){/*stifle*/}
	//done w/ NSEE
	
	try{
	    db.find("Joe", "Johnson", 7); //not in DB
	    fail(); //should have caught the above
	}
	catch(NoSuchEmployeeException e){
	    //good
	}
	catch(Exception e){
	    fail();
	}
    }

    /**test method getPayrollAmount().  
     */
    @Test
    public void testGetPayrollAmount(){
	EmployeeDatabase db = new EmployeeDatabase();
	assertEquals(db.getPayrollAmount(), 0.0, delta);

	HourlyEmployee joe = new HourlyEmployee("Joe", "Smith", 0, 7.85);
	joe.setHoursWorked(10.0);
	SalesSupervisor bob = new SalesSupervisor("Bob", "Cohen", 1,1.01,128.0);
	bob.setNumSales(512);
	db.add(joe);
	db.add(bob);

	double money = 78.5 + 65536.0 + 1.01; //joe + bob + bob; no bonuses
	assertEquals(db.getPayrollAmount(), money, delta);
    }

    /**test method getMaximumEarned().  
     * test method getMinimumEarned().  
     */
    @Test
    public void testGetEarnings(){
	EmployeeDatabase db = new EmployeeDatabase();
	assertEquals(db.getMaximumEarned(), null);
	assertEquals(db.getMinimumEarned(), null);

	HourlyEmployee joe = new HourlyEmployee("Joe", "Smith", 0, 7.85);
	joe.setHoursWorked(100.0); //(7850)
	SalariedEmployee abe = new SalariedEmployee("Abraham", "NoName",2,1.01);
	abe.setBonus(1000000.0); //highest (1e6)
	SalesSupervisor bob = new SalesSupervisor("Bob", "Cohen",1, 1.01, 64.0);
	bob.setNumSales(2); //lowest (128)
	db.add(joe);
	db.add(abe);
	db.add(bob);

	assertEquals(db.getMaximumEarned(), abe);
	assertEquals(db.getMinimumEarned(), bob);
    }

    /**test method getMaxSalesEarned().  
      *test method getMinSalesEarned().         
     */
    @Test
    public void testGetSales(){
	EmployeeDatabase db = new EmployeeDatabase();
	try{
	    db.getMaxSales();
	    fail(); //should have caught the above
	}
	catch(NoSuchEmployeeException e){
	    //good
	}
	catch(Exception e){
	    fail();
	}

	try{
	    db.getMinSales();
	    fail();
	}
	catch(NoSuchEmployeeException e){/*good*/}
	catch(Exception e){
	    fail();
	}

	HourlyEmployee joe = new HourlyEmployee("Joe", "Smith", 0, 7.85);
	SalesEmployee  abe = new SalesEmployee("Abraham","NoName",2,128.0,64.0);
	abe.setNumSales(512); //highest
	SalesSupervisor bob = new SalesSupervisor("Bob","Cohen",1,256.0, 128.0);
	bob.setNumSales(256); //lowest
	db.add(joe);
	db.add(abe);
	db.add(bob);

	//need this b/c NSEE
	try{
	    assertEquals(db.getMaxSales(), abe);
	    assertEquals(db.getMinSales(), bob);
	}
	catch(NoSuchEmployeeException e){/*stifle*/}
	//done w/ NSEE
    }

    /**test method getMaxHoursWorkedEarned().  
     * test method getMinHoursWorkedEarned().  
     */
    @Test
    public void testGetHoursWorked(){
	EmployeeDatabase db = new EmployeeDatabase();
	try{
	    db.getMaxHoursWorked();
	    fail(); //should have caught the above
	}
	catch(NoSuchEmployeeException e){
	    //good
	}
	catch(Exception e){
	    fail();
	}

	try{
	    db.getMinHoursWorked();
	    fail(); //should have caught the above
	}
	catch(NoSuchEmployeeException e){
	    //good
	}
	catch(Exception e){
	    fail();
	}

	HourlyEmployee joe = new HourlyEmployee("Joe", "Smith", 0, 7.85);
	joe.setHoursWorked(512.0);
	HourlySupervisor abe = new HourlySupervisor("Abraham","NoName",2,9.20);
	abe.setHoursWorked(512.001); //highest
	SalesSupervisor bob = new SalesSupervisor("Bob","Cohen",1,256.0,128.0);
	db.add(joe);
	db.add(abe);
	db.add(bob);

	//need this b/c NSEE
	try{
	    assertEquals(db.getMaxHoursWorked(), abe);
	    assertEquals(db.getMinHoursWorked(), joe);
	}
	catch(NoSuchEmployeeException e){/*stifle*/}
	//done w/ NSEE

    }


    /**test method getSupervisees(Employee).  
     * If input is not instance of ISupervisor, returns empty list.
     */
    @Test
    public void testGetSupervisees(){
	EmployeeDatabase  db = new EmployeeDatabase();	
	SalesSupervisor  bob = new SalesSupervisor("Bob","Cohen",1,256.0,128.0);

	HourlyEmployee   joe = new HourlyEmployee(  "Joe", "Smith", 0, 7.85);
	HourlySupervisor abe = new HourlySupervisor("Abraham","NoName",2, 9.20);
	SalariedEmployee al0 = new SalariedEmployee("Al", "The n-th",30,1234.5);
	SalariedEmployee al1 = new SalariedEmployee("Al", "The n-th",31,1234.5);
	SalariedEmployee al2 = new SalariedEmployee("Al", "The n-th",32,1234.5);
	SalariedEmployee al3 = new SalariedEmployee("Al", "The n-th",33,1234.5);

	joe.setSupervisor(abe); db.add(joe);
	abe.setSupervisor(bob); db.add(abe);
	al0.setSupervisor(bob); db.add(al0);
	al1.setSupervisor(bob); db.add(al1);
	al2.setSupervisor(bob); db.add(al2);
	al3.setSupervisor(bob); db.add(al3);
	
	//joe is 0th and only
	assertEquals(db.getSupervisees(abe)[0], (Employee)joe);
	assertEquals(db.getSupervisees(joe), new Employee[0]);
	Employee[] bobWorkers = {abe, al0, al1, al2, al3};//expected out
	assertArrayEquals(bobWorkers, db.getSupervisees(bob));
	assertEquals(db.getSupervisees(bob).length, 5);
    }
	
}
