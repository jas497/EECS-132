/**Boss who earns a wage.  
 * Note that bosses can have bosses.
 * @author James Starkman, jas497
 */
public class HourlySupervisor extends HourlyEmployee implements ISupervisor{

    public HourlySupervisor(String firstName, String lastName, int number,
			      double hourlyRate){
	super(firstName, lastName, number, hourlyRate);
    }

    /**Special output formatter.  
     * Converts important data to a human-friendly string.
     * @return Important data in a human-friendly string.
     */
    @Override
    public String toString(){
	return toStringBase() + "Hourly Supervisor";
    }
}
