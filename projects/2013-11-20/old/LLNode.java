/**Linked list for use by DB for storage.  
 * Not as efficient as it could be, but close.
 */
public class LLNode<T>{
    /*copy from notes, with minimal changes (Ex: T used to be G (for Generic))*/
    private T element; //ptr to datum.  Remember to cast.
    private LLNode<T> ptrNext;
    //ptr must have same type as main
    
    //construct
    public LLNode(T element, LLNode<T> ptrNext){
	this.element = element;
	this.ptrNext = ptrNext;
    }

    //methodology
    public T getElement(){//generic is placeholder, compiler will fix
	return element;
    }
    public LLNode<T> getNext(){
	return ptrNext;
    }
    public void setNext(LLNode<T> next){
	this.ptrNext = next;
    }
}


