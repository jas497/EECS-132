import java.lang.Iterable;
import java.util.Iterator;
/**LL from the API is verboten, so have to implement own.  
 * This list is double-linked.  It can act like a deque, but fully implementing
 * that would be overkill.
 * @author Harold Connamacher (originally, there have been numerous changes)
 */
public class LinkedList<T extends Comparable<? super T>> implements Iterable<T>{
    /**Front of the list. */
    private Node<T> _Front;
    /**Other end of the List*/
    private Node<T> _Back;

    /**Make a new, empty list.*/
    public LinkedList(){
	setFront(null);
	setBack(null);
    }

    /**Add an element to the front of the linked list
     * @param element The new element to add
     */
    public void addToFront(T element){
	Node<T> newbie = new Node<T>(element, getFront(), null);//new entry
	setFront(newbie);
	if(getBack() == null){ //if this is first entry
	    setBack(newbie);
	}
	else{ //not the first entry
	    newbie.getNext().setPrev(newbie);
	}
    }

    /**Add an element to the front of the linked list
     * @param element The new element to add
     */
    public void addToBack(T element){
	Node<T> newbie = new Node<T>(element, null, getBack());
	setBack(newbie);
	if(getFront() == null){ //first!
	    setFront(newbie);
	}
	else{ //not the first entry
	    newbie.getPrev().setNext(newbie);
	}
    }

    /**Pop the front-most element and return it.  
     * @return The removed element.
     */
    public T removeFromFront() throws java.util.NoSuchElementException{
	Node<T> out = getFront();   //save the ptr
	if(out == null){ //if empty list
	    throw new java.util.NoSuchElementException();
	}
	if(getFront() != getBack()){//if more than one node
	    out.getNext().setPrev(null);//break the other
	    setFront(out.getNext());    //correct the head
	    out.setNext(null);          //break one link
	}
	else{ //one node --> no nodes
	    setFront(null);
	    setBack(null);
	}
	return out.getElement();	    
    }

    /**Pop the front-most element and return it.  
     * @return The removed element.
     */
    public T removeFromBack() throws java.util.NoSuchElementException{
	Node<T> out = getBack();    //save
	if(out == null){
	    throw new java.util.NoSuchElementException();
	}
	if(getFront() != getBack()){//if more than one node
	    out.getPrev().setNext(null);//break the other
	    setBack(out.getPrev());     //correct the head
	    out.setPrev(null);    //break one link
	}
	else{
	    setFront(null);
	    setBack(null);
	}
	return out.getElement();
    }

    /**Return whether the list is empty
     * @return true if the list is empty
     */
    public boolean isEmpty() {
	return (getFront() == null); //if front null, back should be too
    }

    /**Merge sort the list.  
     * O(n * (log_2(n)+1))
     */
    public void sort(){
	setFront(mergeSortNodes(getFront()));
	//re-link
	Node<T> buffer = getFront(); //stores last node
	//here, have a sorted single linked list forwards, and a mess backwards
	//this fixes that
	while(buffer.getNext() != null){
	    buffer.getNext().setPrev(buffer);
	    buffer = buffer.getNext();
	}
    }
    
    /**Merge sort, using the power of recursive calls.  
     * @param given A non-null list header, to be sorted.
     * @return The same, but sorted.
     */
    private Node<T> mergeSortNodes(Node<T> given){
	// if only 1 node, then the list is already sorted
	if (given.getNext() == null)
	    return given;
    
	// 1. split the list into 2
	Node<T> half = split(given);
    
	// 2. merge sort each half
	given = mergeSortNodes(given); //cut in half
	half  = mergeSortNodes(half);
    
	// 3. merge the two sorted halves together
	return merge(given, half);
    }

    /**Remove half of the nodes and return em.  
     * @param given The given node chain, to be halved.  Every second node will
     *  be in output
     * @return A list of the removed nodes.
     */
    private Node<T> split(Node<T> given){
	Node<T> list2 = given.getNext();  // the first removed node
	Node<T> nodeptr = list2;          // the current node in list
	Node<T> prevptr = given;          // the previous node in list
	//this could be cleaned up (b/c double-linked), but this is faster
    
	// until at the end of the list, have the nodeptr's next skip over the
	//  next node in the list
	while (nodeptr != null) {
	    prevptr.setNext(nodeptr.getNext());
	    prevptr = nodeptr;
	    nodeptr = nodeptr.getNext();
	}
	return list2;
    }

    /**Merges two sorted lists of nodes into one sorted list
     * @param list1  a list of nodes in sorted order
     * @param list2  a list of nodes in sorted order
     * @return the nodes of list1 and list2 combined and in sorted order
     */
    private Node<T> merge(Node<T> list1, Node<T> list2) {
	Node<T> result;    // the first node of the returned list
	Node<T> nodeptr;   // the last node of the returned list
    
	// first determine which is the first node of the returned list
	if (list1.getElement().compareTo(list2.getElement()) < 0) {
	    result = list1;
	    list1 = list1.getNext();
	}
	else {
	    result = list2;
	    list2 = list2.getNext();
	}
    
	// while both list1 and list2 are not null, remove the smaller of the
	// two front nodes and place it at the end of the returned list
	nodeptr = result;
	while (list1 != null && list2 != null) {
	    if (list1.getElement().compareTo(list2.getElement()) < 0) {
		nodeptr.setNext(list1);
		list1 = list1.getNext();
	    }
	    else {
		nodeptr.setNext(list2);
		list2 = list2.getNext();
	    }
	    nodeptr = nodeptr.getNext();
	}  // either list1 or list2 is null
    
	// one of list1 or list2 will still have nodes so put it at the end
	if (list1 == null)
	    nodeptr.setNext(list2);
	else
	    nodeptr.setNext(list1);
    
	return result;
    }

    /* Given that input is in the list, searches for it and snips it out.  
     * @param doomed The entry to be removed.
     *
     public void remove(T doomed){
     // maybe? needed?
     //Outsourced to LLIterator
     }
    */

    /**Returns an Iterator to run through the contents of the linked list
     * (forwards).  
     * @return an Iterator for the list
     */
    @Override
	public Iterator<T> iterator(){
	//changed
	return new LinkedListIterator<T>(this); //pass in (ptr to) this LL
    }

    /*Get and set*/
    /**Return the field _Front.  
     * Front of the list.
     * @return The field named _Front.
     */
    public Node<T> getFront(){
	return _Front;
    }
    
    /**Set the field _Front to value.  
     * Front of the list.
     * @param value The value to set the field named _Front to.
     */
    public void setFront(Node<T> value){
	_Front = value;
    }

    /**Return the field _Back.  
     * Back of the list.
     * @return The field named _Back.
     */
    public Node<T> getBack(){
	return _Back;
    }
    
    /**Set the field _Back to value.  
     * Back of the list.
     * @param value The value to set the field named _Back to.
     */
    public void setBack(Node<T> value){
	_Back = value;
    }
}
