import java.lang.Iterable;
import java.util.Iterator;
import java.io.*;
/**A big linked-list bucket of Entity-s.  
 * @author James Starkman, jas497
 */
public class Network implements Iterable<Entity>{
    /**The DB for the Network.  Get/set is verboten.*/
    private LinkedList<Entity> _Data;

    /**Creates a new, empty Network.*/
    public Network(){
	_Data = new LinkedList<Entity>();
    }

    /**Allows the user to iterate over the data in the Network.  
     * @return an Iterator.
     */
    @Override
    public Iterator<Entity> iterator(){
	return new LinkedListIterator<Entity>(_Data);
    }
    
    /**Writes a pointer to each Entity to the relations part of the other.  
     * Any Entity-s not in the Network are added.
     * @param one One of the input Entity-s.
     * @param two The other input.
     */
    public void addRelation(Entity one, Entity two){
	/*this implementation more efficient than find()*/
	//these two are for if a given Entity is needed in the Network
	boolean oneNeeded = true;
	boolean twoNeeded = true;
	//first, walk the net and check for duplicates
	for(Entity each : _Data){
	    if(oneNeeded && each.equals(one)){
		oneNeeded = false;
		one = each; //one now points to the entry in _Data
	    }
	    if(twoNeeded && each.equals(two)){
		twoNeeded = false;
		two = each; //two now points to the entry in _Data
	    }
	    if(!(oneNeeded || twoNeeded)){ // NOR
		//No need to loop further
		one.getRelations().addToBack(two);
		two.getRelations().addToBack(one);
		return; //like break, but not verboten.
	    }
	}
	//At least one is not in _Data
	if(oneNeeded){
	    _Data.addToBack(one);
	}
	if(twoNeeded){
	    _Data.addToBack(two);
	}
	//now, connect them
	one.getRelations().addToBack(two);
	two.getRelations().addToBack(one);
    }

    /**Given that the Entity-s are in the Nework, removes the relation between
     * the two inputs.  
     * They are not removed from the Network, however.
     * @param one One of the input Entity-s.
     * @param two The other input.
     */
    public void removeRelation(Entity one, Entity two){
	//search the relations of one for a match for two
	LinkedListIterator<Entity> itOne = 
	    (LinkedListIterator<Entity>) one.getRelations().iterator();
	itOne.find(two);
	itOne.remove();
	//search the relations of two for a match for one
	LinkedListIterator<Entity> itTwo = 
	    (LinkedListIterator<Entity>) two.getRelations().iterator();
	itTwo.find(one);
	itTwo.remove();
    }

    /**Parses the file and calls addRelation method on each pairing.  
     * File must be to spec; any errors will kill this.  IOExceptions will be
     * stifled.
     * @param batch The file of inputs, to be used; it must end in a new, empty
        line.
     */
    public void addRelations(File batch){
	try{//because File IO problems
	    //river is for reading the file of Infected Entitys
	    BufferedReader river = new BufferedReader(new FileReader(batch));
	    String line = river.readLine(); //each line of the file
	    //loop over the input and build the names
	    while(line != null){            //a do-while could clean this up
		boolean alive = true;       //to end the for loop early
		//find the comma, addRelation the names
		for(int i = 0; alive && i < line.length(); i++){
		    if(line.charAt(i) == ','){
			addRelation(new Entity(line.substring(0, i)),
				    new Entity(line.substring(i+2)) );
			alive = false;
		    }
		}
		line = river.readLine();
	    }
	    river.close();
	}
	catch(IOException e){/*stifle*/}
	/*What used to be here, indentation accidentally ruined
	  FileReader river = new FileReader(batch);//a stream for reading
	  if(river.ready()){
	  long          size   = batch.length();     //save fxn calls
	  StringBuilder first  = new StringBuilder();//name of 1st Entity
	  StringBuilder second = new StringBuilder();//name of 2nd Entity
	  StringBuilder target = first; //which currently writing to
	  long          i      = 0;                  //counter
	  //loop over the input and build the names
	  //if there is a standard parser, I am unaware of it
	  while(i < size){
	  int letter = river.read(); //auto-advance the cursor
	  switch(letter){
	  case '\n': //could also use  //note: C-q RET used here
	  //clean out old, no need to check for empty lines
	  addRelation(new Entity(first.toString()),
	  new Entity(second.toString()) );
	  first  = new StringBuilder();//old one is GCed
	  second = new StringBuilder();//same
	  //prepare for next
	  target = first;
	  break;
	  case ',' :
	  i++; //skip the space
	  target = second;
	  break;
	  default:
	  target.append(i);
	  break;
	  }
	  i++; //for the loop
	  }
	  }
	  river.close();
	  }
	*/
    }

    /**Reads the input strings and calls addRelation on each pairing.  
     * @param in The input String-s, even in number.
     * @throws IllegalArgumentException
     */
    public void addRelations(String... in) throws IllegalArgumentException{
	if((in.length & 1) == 0){ //if even qty
	    //adds each pair of Strings
	    for(int i = 0; i < in.length; i++){
		addRelation(new Entity(in[i++]), new Entity(in[i]));
	    }
	}
	else{
	    throw new IllegalArgumentException();
	}
    }
}
