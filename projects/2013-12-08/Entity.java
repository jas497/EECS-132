import java.lang.Comparable;
/**Class representing one entity in the web.  Not allowed to use the LinkedList
 * in the API.
 * 
 * @author James Starkman, jas497
 */
public class Entity implements Comparable<Entity>{
    /**Name of this Entity.*/
    private String _Name;
    /**Status of the Entity, as a Health enum.*/
    private Health _HealthStatus;
    /**Other Entity-s to which this Entity is linked.*/
    private LinkedList<Entity> _Relations;

    public Entity(String name){
	setName(name);
	_Relations = new LinkedList<Entity>();
	setHealthStatus(Health.Uninfected);
    }

    //get/set at end
    /**Compares two Entity-s on the basis of name.  
     * @param o the Object to be compared
     * @return neg. if this Entity is earlier in the alphabet ("lesser") than
     *  the parameter.  Pos. if this is after.  Zero if the names match.
     */
    @Override
    public int compareTo(Entity o){
	//these two local vars should be self-explanatory
	String  me   = this.getName();
	String  you  = o.getName();
	boolean same = true;     //for the loop
	int     out  = 0;        //need to return 1, 0, or -1.

	/* Walk the strs until short one runs out, checking if same.  When find
	 * discrepancy, stop and check who came first.  Note that short names
	 * come before long in dictionary; Smith before Smithson.  */
	for(int i = 0; same && i < ( ((me.length() - you.length() ) > 0) ?
				     you.length() : me.length() ); i++){
	    if( (me.charAt(i)&0x1F) != (you.charAt(i)&0x1F) ){
		//only 5-least-significant-bits matter (b/c case independant)
		same = false;//save processing; they are no longer the same
		if( me.charAt(i) > you.charAt(i) ){
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

    /**Checks the two Entity-s by name only.  
     * @param obj The other Entity (will be cast to such).
     * @return true if the names match, false if they do not.
     */
    @Override
    public boolean equals(Object obj){
	Entity otherOne = (Entity) obj; //This is the proper type.
	//using String.equals(), not this.equals()
	return this.getName().equals(otherOne.getName());
    }

    /**Returns the name and health status of this Entity.  
     * @return The human-friendly data dump.
     */
    public String toString(){
	return this.getName() + ": " + this.getHealthStatus();
    }

    /*get set*/
    /**Returns the LinkedList of Entity in the field _Relations.
     * @return The list.*/
    public LinkedList<Entity> getRelations(){
	return _Relations;
    }

    /**Return the field _Name.  
     * Name of this Entity.
     * @return The field named _Name.
     */
    public String getName(){
	return _Name;
    }
    
    /**Set the field _Name to value.  
     * Name of this Entity.
     * @param value The value to set the field named _Name to.
     */
    public void setName(String value){
	_Name = value;
    }
    
    /**Return the field _HealthStatus.  
     * Status of the Entity, as a Health enum value.
     * @return The field named _HealthStatus.
     */
    public Health getHealthStatus(){
	return _HealthStatus;
    }
    
    /**Set the field _HealthStatus to value.  
     * Status of the Entity, as a Health enum value.
     * @param value The value to set the field named _HealthStatus to.
     */
    public void setHealthStatus(Health value){
	_HealthStatus = value;
    }
}
