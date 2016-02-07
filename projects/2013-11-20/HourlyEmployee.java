/**Worker who earns an hourly wage.
 * @author James Starkman, jas497
 */
public class HourlyEmployee extends Employee{
    /**Amount paid to employee.*/
    private double _HourlyRate;
    /**Sum of time spent working, in hours.*/
    private double _HoursWorked;
    
    public HourlyEmployee(String firstName, String lastName, int number,
			  double hourlyRate){
	super(firstName, lastName, number); //Employee's init
	//what makes him/her special
	setHourlyRate(hourlyRate);
	setHoursWorked(0.0);
    }

    /*Methods - inherits many from Employee*/
    /**Does what you think it does.  
     * @return (hrs * rate) + bonus
     */
    @Override
    public double getAmountEarned(){
	return ((getHourlyRate() * getHoursWorked()) + getBonus());
    }

    /**Scales wage by given proportion (down by 10% = -0.1).  
     * Adjusts only the hourly rate of the employee.
     * @param percentage The given proportion (down by 10% = -0.1).
     */
    @Override
    public void adjustPay(double percentage){
	setHourlyRate( getHourlyRate() * (percentage + 1.0) );
    }

    /**Special output formatter.  
     * Converts important data to a human-friendly string.
     * @return Important data in a human-friendly string.
     */
    @Override
    public String toString(){
	return toStringBase() + "Hourly Employee";
    }

    /*get set*/
    public double getHourlyRate(){
	return _HourlyRate;
    }
    public void   setHourlyRate(double value){
	_HourlyRate = value;
    }
    public double getHoursWorked(){
	return _HoursWorked;
    }
    public void setHoursWorked(double hoursWorked){
	_HoursWorked = hoursWorked;
    }
}
