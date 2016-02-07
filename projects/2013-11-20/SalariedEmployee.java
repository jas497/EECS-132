/**Worker who earns a salary.
 * @author James Starkman, jas497
 */
public class SalariedEmployee extends Employee{
    /**Amount paid to employee.*/
    private double _Salary;
    
    public SalariedEmployee(String firstName, String lastName, int number,
			    double salary){
	super(firstName, lastName, number); //Employee's init
	//what makes him/her special
	setSalary(salary);
    }

    /*Methods - inherits many from Employee*/
    /**Does what you think it does.  
     * @return Salary + bonus
     */
    @Override
    public double getAmountEarned(){
	return ( getSalary() + getBonus() );
    }

    /**Scales salary by given proportion.  
     * Adjusts only the salary of the employee.
     * @param percentage The given proportion (down by 10% = -0.1).
     */
    @Override
    public void adjustPay(double percentage){
	setSalary( getSalary() * (percentage + 1.0) );
    }

    /**Special output formatter.  
     * Converts important data to a human-friendly string.
     * @return Important data in a human-friendly string.
     */
    @Override
    public String toString(){
	return toStringBase() + "Salaried Employee";
    }
    
    /*get set*/
    public double getSalary(){
	return _Salary;
    }
    public void   setSalary(double value){
	_Salary = value;
    }
}
