/**General superclass for subclasses in this DB programming project.  
 * This class works like Object, but with basic personal details useful to a DB,
 * in addition to everything from Object.  
 * 
 * @author James Starkman, jas497
 */
public abstract class Employee{
    /**The family name of the employee.*/
    private String   _LastName;
    /**The given name of the employee.*/
    private String   _FirstName;
    /**ID number of the employee.*/
    private int      _Number;
    /**Bonus paid to employee.*/
    private double   _Bonus;
    /**Amount earned, in USD.  This is used to compare incomes accross the DB.*/
    private double   _AmountEarned;
    /**Boss of this employee.*/
    private Employee _Supervisor; //it's rude to point (at the boss).

    // include? /**Standard, bare-minimum constructor.*/
    public Employee(String firstName, String lastName, int number){
	setName(firstName, lastName);
	setNumber(number);
	//could init these above, but here is not cluttered by JavaDoc comments
	setBonus(0.0);
	_AmountEarned = 0.0; //no public get/set allowed for this on
	_Supervisor   = null;//same here
    }

    /*Override us!*/
    /* Old JavaDoc
     **Hacked-up interfacing.  There is a reason for this.
     * @throws UnsupportedOperationException Override me to make this go away.
     */
    /**Adjusts pay.  Children must override.*/
    public abstract void adjustPay(double percentage);
    /*This was the old version.
      public void   adjustPay(double percentage){
	throw new UnsupportedOperationException("Override this!");
	}*/
    
    /**Calculates amount to be earned.  Must be overridden.*/
    public abstract double getAmountEarned();
    
    /*Old version:
     * Hacked-up interfacing.  There is a reason for this.
     * @throws UnsupportedOperationException Override me to make this go away.
     *
    public double getAmountEarned(){
	throw new UnsupportedOperationException("Override this!");
    }*/

    /*Interesting methods*/
    /**Generic information-extractor.  
     * @return A human-friendly data dump.
     */
    @Override
    public String toString(){
	return toStringBase();
    }

    /**For use by descendants.  
     * This is a special method.  Returns through the period; still need to
     * append the type of employee.
     * @param passUp This is here to make this an overload, not an override.
     * @return The default string.
     */
    public String toStringBase(){
	return getNumber() +": "+ getLastName() +", "+ getFirstName() +".  ";
    }
    
    /**Checks both names and number for equality.  
     * Does this by overriding the one from Object.
     * @param in The Employee to be input; is only Object to overRIDE, and avoid
     *  an overLOAD.  
     * @return True if they match on all three fronts, false otherwise.
     */
    @Override
    public boolean equals(Object in){
	Employee otherGuy = (Employee) in;
	return ( this.getNumber()    == otherGuy.getNumber()   &&
		 this.getLastName()  == otherGuy.getLastName() &&
		 this.getFirstName() == otherGuy.getFirstName() );
    }

    /**Alphabetical comparison.  
     * @param otherGuy The other Employee to be compared to this one.
     * @return If input is before this, returns -1.  Input after, 1.  Same, 0.
     */
    public int compareToByName(Employee otherGuy){
	//these two local vars should be self-explanatory
	String  me   = this.getLastName()     + ' ' + this.getFirstName();
	String  you  = otherGuy.getLastName() + ' ' + otherGuy.getFirstName();
	boolean same = true;     //for the loop
	int     out  = 0;        //need to return 1, 0, or -1.
	me  = me.toUpperCase();  //choice of case is arbitrary
	you = you.toUpperCase(); //they must match, though

	/* Walk the strs until short one runs out, checking if same.  When find
	 * discrepancy, stop and check who came first.  Note that short names
	 * come before long in dictionary; Smith before Smithson.  Space is
	 * 0x20, any letter here will be 0x41 or bigger. */
	for(int i = 0; same && i < ( ((me.length() - you.length() ) > 0) ?
				     you.length() : me.length() ); i++){
	    if( me.charAt(i) != you.charAt(i) ){
		same = false;//save processing; they are no longer the same
		if( me.charAt(i) < you.charAt(i) ){
		    out = 1;
		}
		else{
		    out = -1;
		}
	    }
	}
	//if names were identical, out should still be zero.  Else, is +/- 1.
	return out;
    }

    /**Amount-earned comparison.  
     * @param otherGuy The other Employee to be compared to this one.
     * @return If input earnings are below this, returns positive.  If below, 
        negative.  Same, 0.
     */
    public int compareToByEarnings(Employee otherGuy){
	/*Originally, this used subtraction.  But that needs rounding...*/
	double me  = this.getAmountEarned(); 
	double you = otherGuy.getAmountEarned();
	return ( (me < you)? -1 : ((me > you)? 1 : 0)) ;

    }

    /**Pass an Employee that is a supervisor in to make that worker the boss
     * of this one.  
     * @param newSuper The new boss of this Employee.
     */
    public void setSupervisor(Employee newSuper){
	if(newSuper instanceof ISupervisor){
	    _Supervisor = newSuper;
	}
    }

    /**Retrive the boss.
     * @return The Employee that is the Supervisor, or null if this Employee
     *  has no boss.
     */
    public Employee getSupervisor(){
	return _Supervisor;
    }
	

    /*The getter-setter table of functions.*/
    //First name is the given name, last name is the surname or family name.
    public String  getFirstName() {
	return _FirstName;
    }
    public String  getLastName() {
	return _LastName;
    }
    public void    setName(String firstName, String lastName){
	_LastName  = lastName;
	_FirstName = firstName;
    }
    public int     getNumber(){
	return _Number;
    }
    public void    setNumber(int value){
	_Number = value;
    }
    public double  getBonus(){
	return _Bonus;
    }
    public void    setBonus(double value){
	_Bonus = value;
    }
}
