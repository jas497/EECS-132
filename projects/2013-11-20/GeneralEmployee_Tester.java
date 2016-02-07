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


/**Test class for most Employee (and descendants) aspects.  
 * No individual testing on constructors or get/set because they will be tested
 * otherwise.
 */
public class GeneralEmployee_Tester{
    private double delta = 0.001;//this for assertEquals(double, double, delta);
    /*First, to test class Employee.  If this works, there should be fewer bugs
     *to be found in later tests.  */
    
    /**Testing trivial methods common to all classes.  
     * This includes get/set HR data (name, bonus, etc) and comparators.  They 
     * have not been overriden, and only need to be tested once.
     */
    @Test
    public void testGeneralGetSet(){
	HourlyEmployee joe = new HourlyEmployee("Joe", "Smith", 0, 7.85);
	SalesSupervisor bob = new SalesSupervisor("Bob", "Cohen",1,1234.5,64.0);
	//nomenclature
	assertEquals(joe.getFirstName(), "Joe");
	assertEquals(joe.getLastName(),  "Smith");
	joe.setName("Joey", "Cohen");
	assertEquals(joe.getFirstName(), "Joey");
	assertEquals(joe.getLastName(),  "Cohen");
	assertEquals(joe.getNumber(),    0);
	/*Constructor tested also, by doing this*/
	//bonus
	bob.setBonus(69.105);
	assertEquals(bob.getBonus(), 69.105, delta);
	//amountEarned
	bob.setNumSales(16);
	assertEquals(bob.getAmountEarned(), (1024.0 + 1234.5), delta);
	//supervisor
	joe.setSupervisor(bob); //bob now boss of joe
	assertEquals(joe.getSupervisor(), bob);
	bob.setSupervisor(joe);
	assertEquals(bob.getSupervisor(), null);
	/*Type-specific get/set*/
	//salary
	assertEquals(bob.getSalary(), 1234.5, delta);
	bob.setSalary(42.0);
	assertEquals(bob.getSalary(), 42.0, delta);
	//commission
	assertEquals(bob.getCommission(), 64.0, delta);
	bob.setCommission(32.0);
	assertEquals(bob.getCommission(), 32.0, delta);
	//wage
	assertEquals(joe.getHourlyRate(), 7.85, delta);
	joe.setHourlyRate(9.19);
	assertEquals(joe.getHourlyRate(), 9.19, delta);
	//hours
	assertEquals(joe.getHoursWorked(), 0.0, delta);
	joe.setHoursWorked(50.0);
	assertEquals(joe.getHoursWorked(), 50.0, delta);
    }

    /**test method adjustPay(double)
     * This MUST be overridden by descendants.  Unless they want an error.*/
    @Test
    public void testAdjustPay(){
	//salaried and salary
	SalariedEmployee saul = new SalariedEmployee("Saul","Levine",3,12345.6);
	double temp = saul.getSalary();
	saul.adjustPay(0.5); //up by 1/2
	assertEquals(temp + (temp/2), saul.getSalary(), delta);
	temp = saul.getSalary();
	saul.adjustPay(-0.75); //down by 3/4, or to 1/4 of original
	assertEquals(temp/4, saul.getSalary(), delta);
	temp = saul.getSalary();
	saul.adjustPay(0.0); //no change
	assertEquals(temp, saul.getSalary(), delta);
	//sales and commission
	SalesEmployee aaron=new SalesEmployee("Aaron","Cohen",4,12345.6,1001.0);
	temp = aaron.getCommission();
	aaron.adjustPay(0.5); //up by 1/2
	assertEquals(temp + (temp/2), aaron.getCommission(), delta);
	temp = aaron.getCommission();
	aaron.adjustPay(-0.75); //down by 3/4, or to 1/4 of original
	assertEquals(temp/4, aaron.getCommission(), delta);
	temp = aaron.getCommission();
	aaron.adjustPay(0.0); //no change
	assertEquals(temp, aaron.getCommission(), delta);
	//hourly and hourly rate
	HourlyEmployee jacob = new HourlyEmployee("Jacob","Coen",3,42.0);
	temp = jacob.getHourlyRate();
	jacob.adjustPay(0.5); //up by 1/2
	assertEquals(temp + (temp/2), jacob.getHourlyRate(), delta);
	temp = jacob.getHourlyRate();
	jacob.adjustPay(-0.75); //down by 3/4, or to 1/4 of original
	assertEquals(temp/4, jacob.getHourlyRate(), delta);
	temp = jacob.getHourlyRate();
	jacob.adjustPay(0.0); //no change
	assertEquals(temp, jacob.getHourlyRate(), delta);
	//not overriden by supervisor subclasses
    }

    /**test method toString() for all occurences.  
     * Always overridden ... sort of.
     */
    @Test
    public void testToString(){
	SalariedEmployee foo0 = new SalariedEmployee("Al","Capp",0,69105.0);
	assertEquals(foo0.toString(), "0: Capp, Al.  Salaried Employee");
	SalesEmployee    foo1 = new SalesEmployee("Al","Capp",0,69105.0, 64.0);
	assertEquals(foo1.toString(), "0: Capp, Al.  Sales Employee");
	HourlyEmployee   foo2 = new HourlyEmployee("Al","Capp",0, 7.85);
	assertEquals(foo2.toString(), "0: Capp, Al.  Hourly Employee");

	SalariedSupervisor foo3 = new SalariedSupervisor("Al","Capp",0,69105.0);
	assertEquals(foo3.toString(), "0: Capp, Al.  Salaried Supervisor");
	SalesSupervisor    foo4 = new SalesSupervisor("Al","Capp",0,69105.0,64.0);
	assertEquals(foo4.toString(), "0: Capp, Al.  Sales Supervisor");
	HourlySupervisor   foo5 = new HourlySupervisor("Al","Capp",0, 7.85);
	assertEquals(foo5.toString(), "0: Capp, Al.  Hourly Supervisor");

    }

    /**test method equals(Object); input will be cast to Employee.  
     * Need this workaround to make it an overRIDE, not an overLOAD.
     */
    @Test
    public void testEquals(){
	HourlyEmployee joe = new HourlyEmployee("Joe", "Smith", 0, 7.85);
	//same type
	HourlyEmployee bob = new HourlyEmployee("Joe", "Smith", 0, 7.85);
	assertEquals(joe.equals(bob), true);
	assertEquals(bob.equals(joe), true); //just in case
	//different levels in hierarchy
	SalesEmployee fred = new SalesEmployee("Joe", "Smith", 0, 1.01, 32.0);
	assertEquals(fred.equals(joe), true);
	assertEquals(joe.equals(fred), true); //just in case
	//across the inheritance tree
	HourlyEmployee bill = new HourlyEmployee("Joe", "Smith", 0, 7.85);
	assertEquals(bill.equals(fred), true);
	assertEquals(fred.equals(bill), true); //just in case
    }

    /**test method compareToByName(Employee).  
     */
    @Test
    public void testCompareToByName(){
	HourlyEmployee joe = new HourlyEmployee("Joe", "Smith", 0, 7.85);
	HourlyEmployee bob = new HourlyEmployee("Bob", "Cohen", 1, 7.85);
	assertEquals(joe.compareToByName(bob) < 0, true);//Cohen before Smith
	assertEquals(bob.compareToByName(joe) > 0, true);//Cohen before Smith

	HourlyEmployee joey = new HourlyEmployee("Joe", "Smith", 0, 7.85);
	assertEquals(joe.compareToByName(joey) == 0, true);
	
	//Supervisors are ("is-a") Employees.  
	SalesSupervisor joseph = new SalesSupervisor("Joe","Smith",0,1.01,32.0);
	assertEquals(joseph.compareToByName(joe) == 0, true);
	assertEquals(joseph.compareToByName(bob) < 0, true);
    }

    /**test method compareToByEarnings(Employee).  
     */
    @Test
    public void testCompareToByEarnings(){
	HourlyEmployee joe = new HourlyEmployee("Joe", "Smith", 0, 7.85);
	joe.setHoursWorked(10.0);
	HourlyEmployee bob = new HourlyEmployee("Bob", "Cohen", 1, 7.85);
	bob.setHoursWorked(5.0);
	assertEquals(joe.compareToByEarnings(bob) > 0, true);
	assertEquals(bob.compareToByEarnings(joe) < 0, true);

	HourlyEmployee joey = new HourlyEmployee("Joe", "Smith", 0, 7.85);
	joey.setHoursWorked(10.0);
	assertEquals(joe.compareToByEarnings(joey) == 0, true);
	
	SalesSupervisor joseph = new SalesSupervisor("Joe","Smith",7, 78.5, 0);
	assertEquals(joseph.compareToByEarnings(joe) == 0, true);
	assertEquals(joseph.compareToByEarnings(bob) > 0, true);
    }
}
    
