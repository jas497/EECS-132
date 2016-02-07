import java.util.Iterator;
/**An iterator that will run through the contents of a linked list, forwards.
 * @author Harold Connamacher (originally, has been modified)
 */
public class LinkedListIterator<T extends Comparable<? super T>>
    implements Iterator<T> {
    /**Stores the next node whose contents need to be returned.
     * Note: "n" is short for "nodeptr".*/
    private Node<T> n;

    /**The main list, from which the node is taken.  Needed for getFront(),
     * getBack().*/
    private LinkedList<T> list;
    
    /**The constructor.  Sets the first node for the iteration.
     * @param firstNode  the node whose contents will be returned first
     */
    public LinkedListIterator(LinkedList<T> list){
	n = list.getFront(); //forwards by default
	this.list = list;
    }
    
    /**Returns true if there are more elements in the linked list
     * @return true if the are more elements in the list
     */
    @Override
    public boolean hasNext(){
	return n != null;
    }
  
    /**Returns the next element of the list and increments.
     * @return the next elements of the list
     */
    @Override
    public T next(){
	T saveElement = n.getElement(); //for output
	n = n.getNext(); //next
	return saveElement;
    }
  
    /**Kills the last Node, leaving it to the GC to free() it.  
     * Never call this before >=1 call to next().  This is normal behaviour for
     * a for-each loop.*/
    @Override
    public void remove() throws IllegalStateException{
	Node<T> target; //Node to be killed
	if(hasNext() ){ //n != null
	    target = n.getPrev();
	    if(target == null){ //could be an error, if no next() first
		throw new IllegalStateException();
	    }
	    if(target.getPrev() != null){ //two back; if not the beginning
		//normal middle-of-list removal
		target.getPrev().setNext(n);
		n.setPrev(target.getPrev());
	    }
	    else{//removing 1st node of many in LL
		//old front goes away, n is new front
		n.setPrev(null);  //break second
		list.setFront(n); //target should soon be GCed.
	    }
	}
	else{ //must be last element.  n = null
	    target = list.getBack();
	    if(target == null){ //could be an error, if no next() first
		throw new IllegalStateException();
	    }
	    if(list.getFront() == list.getBack()){ //what if one node left?
		list.setFront(null);
		list.setBack( null);
	    }
	    else{ //normal removing of last node
		list.setBack(target.getPrev());
		list.getBack().setNext(null);
	    }
	}
	target.setNext(null); //target used to think that it belonged
	target.setPrev(null); //now it does not
    } //hooray for automatic garbage collection!

    /**Finds the input in the list and returns it.  
     * Does not alter the list.
     * @param target The target of the search
     * @return The target, or null if not in list.
     */
    public T find(T target){
	//check every member, returning early when done
	while(hasNext()){
	    T temp = next(); //save this ptr for next few lines
	    if(target.equals(temp)){
		return temp;
	    }
	}
	//if it made it this far, target not in list
	return null;
    }

    /**Sort the list using a merge sort.  
     * Please create a separate LLIterator for this.
     */
    public void mergeSort(){
	list.sort();
    }
}
