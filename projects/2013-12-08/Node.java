/**A node in a double-linked list.  Effectively a renamed copy of DLNode.java
 * @author Originally Harold Connamacher, but modified by James Starkman
 */
public class Node<T>{
    /**The element stored in the node */
    private T _Element;
    
    /**A reference to the next node of the list */
    private Node<T> _Next;

    /**Node Before this one.*/
    private Node<T> _Prev;
    
    /**The constructor
     * @param element  the element to store in the node
     * @param next     a reference to the next node of the list 
     * @param previous a pointer to the previous node of the list
     */
    public Node(T element, Node<T> next, Node<T> previous){
	this._Element = element;
	setNext(next);
	setPrev(previous);

	if(next != null){
	    getNext().setPrev(this);
	}
	if(previous != null){
	    getPrev().setNext(this);
	}
    }
    
    /*Get set*/
    /**Returns the element stored in the node
     * @return the element stored in the node
     */
    public T getElement() {
	return _Element;
    }
    
    /**Return the field Prev.  
     * Node Before this one.
     * @return The field named Prev.
     */
    public Node<T> getPrev(){
	return _Prev;
    }
    
    /**Set the field Prev to value.  
     * Node Before this one.
     * @param value The value to set the field named Prev to.
     */
    public void setPrev(Node<T> value){
	_Prev = value;
    }

    /**Return the field Next.  
     * A reference to the next node of the list.  
     * @return The field named Next.
     */
    public Node<T> getNext(){
	return _Next;
    }
    
    /**Set the field Next to value.  
     * A reference to the next node of the list. 
     * @param value The value to set the field named Next to.
     */
    public void setNext(Node<T> value){
	_Next = value;
    }
}
