/**Boss who earns a salary and a commission.
 * Note that bosses can have bosses.
 * @author James Starkman, jas497
 */
public class SalesSupervisor extends SalesEmployee implements ISupervisor{

    public SalesSupervisor(String firstName, String lastName, int number,
			   double salary, double commission){
	super(firstName, lastName, number, salary, commission);
    }

    /**Special output formatter.  
     * Converts important data to a human-friendly string.
     * @return Important data in a human-friendly string.
     */
    @Override
    public String toString(){
	return toStringBase() + "Sales Supervisor";
    }
}
