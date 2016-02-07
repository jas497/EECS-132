/**Worker who earns a salary and a commission.
 * @author James Starkman, jas497
 */
public class SalesEmployee extends SalariedEmployee{
    /**Mini-bonus that is paid once per sucessful sale.*/
    private double _Commission;
    /**Total quantity of sales since this was last cleared.*/
    private int    _NumSales;

    public SalesEmployee(String firstName, String lastName, int number, 
			 double salary, double commission){
	//start with no salary
	super(firstName, lastName, number, salary);
	setCommission(commission);
    }

    /*Methods - inherits many from Employee*/
    /**Does what you think it does.  
     * @return (total sales * commission per sale) + salary
     */
    @Override
    public double getAmountEarned(){
	return ( (getNumSales() * getCommission()) + getSalary() );
    }

    /**Scales commission by given proportion (down by 10% = -0.1).  
     * Adjusts only the commission of the employee.
     * @param percentage The given proportion (down by 10% = -0.1).
     */
    @Override
    public void adjustPay(double percentage){
	setCommission( getCommission() * (percentage + 1.0) );
    }

    /**Special output formatter.  
     * Converts important data to a human-friendly string.
     * @return Important data in a human-friendly string.
     */
    @Override
    public String toString(){
	return toStringBase() + "Sales Employee";
    }

    /*get set*/
    public double getCommission(){
	return _Commission;
    }
    public void setCommission(double value){
	_Commission = value;
    }
    public int getNumSales(){
	return _NumSales;
    }
    public void setNumSales(int value){
	_NumSales = value;
    }
}
