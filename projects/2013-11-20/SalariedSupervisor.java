/**Boss who earns a salary.
 * Note that bosses can have bosses.
 * @author James Starkman, jas497
 */
public class SalariedSupervisor extends SalariedEmployee implements ISupervisor{

    public SalariedSupervisor(String firstName, String lastName, int number,
			      double salary){
	super(firstName, lastName, number, salary);
    }

    /**Special output formatter.  
     * Converts important data to a human-friendly string.
     * @return Important data in a human-friendly string.
     */
    @Override
    public String toString(){
	return toStringBase() + "Salaried Supervisor";
    }
}
