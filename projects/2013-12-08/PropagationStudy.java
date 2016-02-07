import java.util.Random;
import java.io.*;
/**Runs the simulation.  
 * @author James Starkman, jas497
 */
public class PropagationStudy{
    /**The Network object used by this study.*/
    private Network _Network;
    /**The rate of infection, on range [0,1]*/
    private double _InfectionRate;
    /**The rate of healing, on range [0,1]*/
    private double _HealRate;
    /**The rate of wear, on the range [0,1]*/
    private double _WearRate;
    /**The High Random Number God (or Generator)*/
    private Random _HighRNG;

    /**The constructor*/
    public PropagationStudy(Network net, double rateInfect, double rateHeal,
			    double rateWear){
	setNetwork(net);
	setInfectionRate(rateInfect);
	setHealRate(rateHeal);
	setWearRate(rateWear);
	_HighRNG = new Random();
    }

    /**Sicken the targetted Entity.  
     * @param victim The target Entity.
     * @throws NoSuchElementException
     */
    public void infect(Entity victim) throws NoSuchElementException{
	Entity condemned = ((LinkedListIterator<Entity>) getNetwork().iterator()
			    ).find(victim); //ptr to Entity to be sickened
	if(condemned != null){
	    condemned.setHealthStatus(Health.Infected);
	}
	else{
	    throw new NoSuchElementException();
	}
    }

    /**Infect this fraction of the population of the Network.  
     * Note: infect(0.25) means that about 1 in 4 will become infected.
     * @param chance The fraction.
     */
    public void infect(double chance){
	//walk the network
	for(Entity each : getNetwork()){
	    //if each rolls too low (below chance), ends up sick.
	    if(each.getHealthStatus() != Health.Infected
	       && _HighRNG.nextDouble() < chance){
		each.setHealthStatus(Health.Infected);
	    }
	}
    }

    /**Protect the targetted Entity.  
     * @param victim The target Entity.
     * @throws NoSuchElementException
     */
    public void inoculate(Entity victim) throws NoSuchElementException{
	Entity condemned = ((LinkedListIterator<Entity>) getNetwork().iterator()
			    ).find(victim);//Entity to be inoculated
	if(condemned != null){
	    condemned.setHealthStatus(Health.Inoculated);
	}
	else{
	    throw new NoSuchElementException();
	}
    }

    /**Inoculate this fraction of the population of the Network.  
     * Note: inoculate(0.25) means that about 1 in 4 will become inoculated.
     * @param chance The fraction.
     */
    public void inoculate(double chance){
	//walk the network
	for(Entity each : getNetwork()){
	    //if each rolls too low (below chance), ends up sick.
	    if(each.getHealthStatus() != Health.Inoculated
	       && _HighRNG.nextDouble() < chance){
		each.setHealthStatus(Health.Inoculated);
	    }
	}
    }
    
    /**Walk the Network and count how many Entity-s match the characteristic.  
     * @return The final count
     */
    public int getNumUninfected(){
	int count = 0; //how many so far
	//walk the network
	for(Entity each : getNetwork()){
	    if(each.getHealthStatus() == Health.Uninfected){
		count++;
	    }
	}
	return count;
    }

    /**Walk the Network and count how many Entity-s match the characteristic.  
     * @return The final count
     */
    public int getNumInoculated(){
	int count = 0; //how many so far
	//walk the network
	for(Entity each : getNetwork()){
	    if(each.getHealthStatus() == Health.Inoculated){
		count++;
	    }
	}
	return count;
    }

    /**Walk the Network and count how many Entity-s match the characteristic.  
     * @return The final count
     */
    public int getNumInfected(){
	int count = 0; //how many so far
	//walk the network
	for(Entity each : getNetwork()){
	    if(each.getHealthStatus() == Health.Infected){
		count++;
	    }
	}
	return count;
    }

    /**Walks the Network and forces every Entity to Uninfected.  
     */
    public void resetNetwork(){
	//walk the network
	for(Entity each : getNetwork()){
	    each.setHealthStatus(Health.Uninfected);
	}
    }

    /**Walks the Network and prints the name and status of every Entity, in
     * alphabetical order.
     */
    public void printNetwork(){
	( (LinkedListIterator<Entity>) getNetwork().iterator() ).mergeSort();
	//loop over the new list, printing as needed
	for(Entity each : getNetwork()){
	    System.out.println(each.getName() + ": " + //better with printf
			       each.getHealthStatus());
	}
    }
    
    /**Run the simulation.  
     */
    public void runPropagation(){
	/*First, for every entity that is Infected, run through all of its relations, and for every relation that is Uninfected call Math.random() or similar pseudorandom number generator and if the value is less than the infection rate, set the relation's health status to BeingInfected.*/
	//walk the Network
	for(Entity each : getNetwork()){
	    if(each.getHealthStatus() == Health.Infected){
		//walk each sick Entity's relations
		for(Entity eachRel : each.getRelations()){
		    if(eachRel.getHealthStatus() == Health.Uninfected){
			if(_HighRNG.nextDouble() < getInfectionRate()){
			    eachRel.setHealthStatus(Health.BeingInfected);
			}
		    }
		}
	    }
	}
	/*Second, for every entity that is Infected call your pseudorandom number generator and if the value is less than the heal rate, set the entity's health status to BeingInoculated.*/
	//walk the Net again.  Is a separate pass than the above for a reason
	for(Entity each : getNetwork()){
	    if(each.getHealthStatus() == Health.Infected){
		if(_HighRNG.nextDouble() < getHealRate()){
		    each.setHealthStatus(Health.BeingInoculated);
		}
	    }
	}
	/*Third, for every entity that is Inoculated call your pseudorandom number generator and if the value is less than the wear rate, set the entity's health status to Uninfected.*/
	for(Entity each : getNetwork()){
	    if(each.getHealthStatus() == Health.Inoculated){
		if(_HighRNG.nextDouble() < getWearRate()){
		    each.setHealthStatus(Health.Uninfected);
		}
	    }
	}
	/*Fourth, for every entity that is BeingInfected set it's health status to Infected and for every entity that is BeingInoculated set its health status to Inoculated.*/
	for(Entity each : getNetwork()){
	    if(each.getHealthStatus() == Health.BeingInfected){
		each.setHealthStatus(Health.Infected);
	    }
	    else if(each.getHealthStatus() == Health.BeingInoculated){
		each.setHealthStatus(Health.Inoculated);
	    }
	}
    }

    /**Calls the other runPropagation rounds-many times.  
     * @param rounds This many rounds of the sim.
     */
    public void runPropagation(int rounds){
	//Call runPropagation() until it is zero; rounds-many times.
	while(rounds-- > 0){
	    runPropagation();
	}
    }

    /*get and set*/
    /**Return the field _Network.  
     * The Network object used by this study.
     * @return The field named _Network.
     */
    public Network getNetwork(){
	return _Network;
    }
    
    /**Set the field _Network to value.  
     * The Network object used by this study.
     * @param value The value to set the field named _Network to.
     */
    public void setNetwork(Network value){
	_Network = value;
    }
    
    /**Return the field _InfectionRate.  
     * The rate of infection, on range [0,1]
     * @return The field named _InfectionRate.
     */
    public double getInfectionRate(){
	return _InfectionRate;
    }
    
    /**Set the field _InfectionRate to value.  
     * The rate of infection, on range [0,1]
     * @param value The value to set the field named _InfectionRate to.
     */
    public void setInfectionRate(double value){
	_InfectionRate = value;
    }

    /**Return the field _HealRate.  
     * The rate of healing, on range [0,1]
     * @return The field named _HealRate.
     */
    public double getHealRate(){
	return _HealRate;
    }
    
    /**Set the field _HealRate to value.  
     * The rate of healing, on range [0,1]
     * @param value The value to set the field named _HealRate to.
     */
    public void setHealRate(double value){
	_HealRate = value;
    }
    
    /**Return the field _WearRate.  
     * The rate of wear, on the range [0,1]
     * @return The field named _WearRate.
     */
    public double getWearRate(){
	return _WearRate;
    }
    
    /**Set the field _WearRate to value.  
     * The rate of wear, on the range [0,1]
     * @param value The value to set the field named _WearRate to.
     */
    public void setWearRate(double value){
	_WearRate = value;
    }

    /**The main method.  
     * @param args The input, in the form of:<br /><ol>
     * <li>File name</li>
     * <li>Rate of infection</li>
     * <li>Rate of healing</li>
     * <li>Rate of wear</li>
     * <li>Number of rounds</li>
     * <li>List of initially-infected Entity</li>
     * <li>Optional: Initially-inoculated Entity</li></ol><br />
     * All files are to end in an empty line, for ease of parsing.
     */
    public static void main(String[] args){
	PropagationStudy study; //assign ptr later
	//begin setting up
	try{// Need this to catch IO exceptions
	    /*make a Network*/
	    Network net = new Network();
	    net.addRelations(new File(args[0]));
	    /*make a new study*/
	    study = new PropagationStudy(net, Double.parseDouble(args[1]),
					 Double.parseDouble(args[2]),
					 Double.parseDouble(args[3])   );
	    /*initial infect/inoc.*/
	    //file the first
	    //river is for reading the file of Infected Entitys
	    BufferedReader river = new BufferedReader(new FileReader(args[5]));
	    String line = river.readLine(); //each line of the file
	    //loop over the input and build the names
	    while(line != null){            //a do-while could clean this
		//make an ideal Entity, for which to search
		Entity missing = new Entity(line);
		//create an iterator, for use in searching
		LinkedListIterator<Entity> iterator = 
		    (LinkedListIterator<Entity>)
		    study.getNetwork().iterator();
		//find the Entity and stash the ptr here
		Entity found = iterator.find(missing);

		if(found != null){
		    found.setHealthStatus(Health.Infected);
		}
		else{
		    System.out.printf("%s is not in the network.\n", line);
		}
		line = river.readLine(); //auto-advance
	    }
	    river.close();

	    //file the second.  Copy of above, but with different target.
	    //the asignment forbids all non-listed functions, including
	    //code-saving private ones.
	    if(args.length == 7){ //because last file is optional
		//river is for reading the file of Infected Entitys
		river = new BufferedReader(new FileReader(args[5]));
		line = river.readLine(); //each line of the file
		//loop over the input and build the names
		while(line != null){
		    //make an ideal Entity, for which to search
		    Entity missing = new Entity(line);
		    //create an iterator, for use in searching
		    LinkedListIterator<Entity> iterator = 
			(LinkedListIterator<Entity>)
			study.getNetwork().iterator();
		    //find the Entity and stash the ptr here
		    Entity found = iterator.find(missing);

		    if(found != null){
			found.setHealthStatus(Health.Inoculated);
		    }
		    else{
			System.out.printf("%s is not in the network.\n",
					  line);
		    }
		    line = river.readLine(); //auto-advance
		}
		river.close();
	    }
	}
	catch(IOException e){
	    System.out.printf("File IO error, program aborted\n" );
	    return;
	}
	//end of setup
	/*go!*/
	study.runPropagation(Integer.parseInt(args[4]));
	/*now spit out the number of each type*/
	System.out.printf("Quantity uninfected: %d\n",study.getNumUninfected());
	System.out.printf("Quantity infected: %d\n",  study.getNumInfected());
	System.out.printf("Quantity inoculated: %d\n",study.getNumInoculated());
    }
}
