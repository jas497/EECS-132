/**Master DB for accessing and manipulating the workers.
 * Uses a linked list for storage, so access times will vary.
 * @author James Starkman, jas497
 */
import java.util.*; //for linked lists
public class EmployeeDatabase{
    /**Container of <span style="text-decoration:line-through">livestock</span>
     * valuable employees.  This pointer is only to head of list.  */
    private LinkedList<Employee> _Data; //from java.util

    /**Generic constructor that makes a new, empty list.*/
    public EmployeeDatabase(){
	//use linked list from API
	setData(new LinkedList());
    }

    /*Methods*/
    /**Pass in (a ptr to) a worker to add it to the DB.  
     * New datum can be any variant of Employee, such as supervisors or
     * normal employees.
     * @param newGuy A new Employee object to be added to front of list.
     */
    public void add(Employee newGuy){
	getData().add(newGuy); //API makes this too easy ...
    }

    /**Removes first member of the DB who matches the input.
     * @param firstName Given name of employee.
     * @param lastName  Surname of employee.
     * @param number    ID number of employee.
     * @return The removed employee (s pointer).
     * @throws NoSuchEmployeeException If DB does not have a matching employee.
     */
    public Employee remove(String firstName, String lastName, int number)
	throws NoSuchEmployeeException{
	//iter makes the walking easier; just store iter.next()
	ListIterator<Employee> iter = getData().listIterator(0);
	/*only construct once.  Cuts down on cost.  Should not be like this, but
	  we have .equals(), which makes this more appealing. Is only an
	  HourlyEmployee because Employee is abstract.*/
	Employee target = new HourlyEmployee(firstName, lastName, number, 7.85);
	boolean  alive  = true;           //walk the list as needed
	int      i      = 0;              //counter
	Employee store;
	if(iter.hasNext()){
	    store = iter.next(); //stores the current value
	}
	else{
	    throw new NoSuchEmployeeException();
	}
	//walk until find matching data.  If that never happens, throw NSEE.
	while(alive){
	    if(target.equals(store)){ //if they match
		alive = false;
		getData().remove(i);
	    }
	    else{
		i++;
		//store = (hasNext() ? iter.next() : null);
		if(iter.hasNext()){
		    store = iter.next();
		}
		else{
		    store = null;
		    alive = false;
		}
	    }
	}
	if(store != null) return store;
	else throw new NoSuchEmployeeException();
    }

    /**Seek out the first match of this employee in the DB and return it.  
     * Does not alter the DB.
     * @param firstName Given name of employee.
     * @param lastName  Surname of employee.
     * @param number    ID number of employee.
     * @return The found employee.
     * @throws NoSuchEmployeeException If no employee fitting the input is in
     *     the DB.
     */
    public Employee find(String firstName, String lastName, int number)
	throws NoSuchEmployeeException{
	//compare others to this.  HourlyEmployee b/c Employee is abstract
	Employee ideal = new HourlyEmployee(firstName, lastName, number, 7.85);
	//check each employee if match.
	for(Employee fred : getData() ){
	    //everyone gets a turn at being Fred.
	    if(ideal.equals(fred)){
		return fred;//done with array
	    }
	}
	throw new NoSuchEmployeeException();
    }

    /**Total of amounts earned.  
     * @return The sum.
     */
    public double getPayrollAmount(){
	double out = 0.0; //output
	//for each and every resource
	for(Employee each : getData() ){
	    out += each.getAmountEarned();
	}
	return out;
    }

    /**Seeks out the Employee with the highest amount earned and returns the 
     * associated pointer.  
     * @return The highest-earnings Employee in the DB.
     */
    public Employee getMaximumEarned(){
	double   most = 0.0;  //highest amount earned encountered so far
	Employee out  = null; //output
	//for each worker, if amt earned is higher, adjust.  Otherwise, ignore.
	for(Employee each : getData() ){
	    if(most < each.getAmountEarned()){
		most = each.getAmountEarned();
		out = each;
	    }
	}
	return out;
    }

    /**Seek out the Employee with the lowest earnings and return such.  
     * @return The Employee with the lowest amount earned.
     */
    public Employee getMinimumEarned(){	
	double   least = Double.MAX_VALUE; //lowest amount earned
	Employee out   = null;             //output
	//for each worker, if amt earned is higher, adjust.  Otherwise, ignore.
	for(Employee each : getData() ){
	    if(least > each.getAmountEarned()){
		//two calls to a function easier than one call and store result,
		//which is only slightly cheaper
		least = each.getAmountEarned();
		out = each;
	    }
	}
	return out;
    }

    /**Returns the worker with the highest quantity of sales.  
     * @return The SalesEmployee.
     */
    public Employee getMaxSales() throws NoSuchEmployeeException {
	int      most = 0;    //highest amount earned encountered so far
	Employee out = null;  //storage
	//for each worker, if amt earned is higher, adjust.  Otherwise, ignore.
	for(Employee fred : getData() ){
	    if(fred instanceof SalesEmployee){
		SalesEmployee each = (SalesEmployee) fred;
		if(most < each.getNumSales()){
		    most = each.getNumSales();
		    out = each;
		}
	    }
	}
	if(out != null) return out;
	else throw new NoSuchEmployeeException();
    }

    /**Returns the worker with the fewest sales.  
     * @return The worker with the fewest sales.
     */
    public Employee getMinSales() throws NoSuchEmployeeException{
	int      least = Integer.MAX_VALUE; //This will be lowered later.
	Employee out   = null;              //output
	//for each employee in the dataset
	for(Employee fred : getData() ){
	    if(fred instanceof SalesEmployee){
		SalesEmployee each = (SalesEmployee) fred;
		if(least >  each.getNumSales()){
		    least = each.getNumSales();
		    out   = each;
		}
	    }
	}
	if(out != null) return out;
	else throw new NoSuchEmployeeException();
    }

    /**Returns the worker who has worked longest.  
     * @return The worker with the highest number of hours worked.
     */
    public Employee getMaxHoursWorked() throws NoSuchEmployeeException {
	double   most = 0.0; //most hours worked (so far) is this many
	Employee out  = null;//output
	//for each employee in the DB, compare
	for(Employee fred : getData() ){
	    if(fred instanceof HourlyEmployee){
		HourlyEmployee each = (HourlyEmployee) fred;
		if(most < each.getHoursWorked()){
		    most = each.getHoursWorked();
		    out  = each;
		}
	    }
	}
	if(out != null) return out;
	else throw new NoSuchEmployeeException();
    }

    /**Returns the worker who has worked least.  
     * @return The worker with the lowest number of hours worked.
     */
    public Employee getMinHoursWorked() throws NoSuchEmployeeException {
	double   least = Double.MAX_VALUE; //least hours worked (so far)
	Employee out   = null;             //output
	//for each employee in the DB, compare
	for(Employee fred : getData() ){
	    if(fred instanceof HourlyEmployee){
		HourlyEmployee each = (HourlyEmployee) fred;
		if(least > each.getHoursWorked()){
		    least = each.getHoursWorked();
		    out   = each;
		}
	    }
	}
	if(out != null) return out;
	else throw new NoSuchEmployeeException();
    }

    /**Fetch the workers who are directly supervised by the input (no recursive
     * calling itself).  
     * @param boss The supervisor whose underlings are to be found.
     * @return An array of the underlings.
     */
    public Employee[] getSupervisees(Employee boss){
	if(boss instanceof ISupervisor){
	    LinkedList<Employee> soFar = new LinkedList();//those found so far
	    //check everyone for a supervisor ptr matching the input
	    for(Employee each : getData() ){
		if(boss.equals(each.getSupervisor()) ){
		    soFar.add(each);
		}
	    }
	    return soFar.toArray(new Employee[0]);
	}
	else{
	    return new Employee[0]; //empty list
	}
    }


    /*get set*/
    public LinkedList<Employee> getData(){
	return _Data;
    }
    public void setData(LinkedList value){
	_Data = value;
    }
}
