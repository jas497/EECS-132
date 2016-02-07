import org.junit.*;
import static org.junit.Assert.*;
import java.util.Iterator;
/**
 * A class that tests the methods of the LinkedList class. 
 * Also tests classes: Node, LinkedListIterator.
 * @author Originally Harold Connamacher, although minor changes have been made.
 */
public class LinkedList_Tester {
    //warning: no lines wrapped
    /**
     * Tests the addToFront method of LinkedList.
     */
    @Test
    public void testAddToFront() {
	LinkedList<Integer> list = new LinkedList<Integer>();
	list.addToFront(3);
	list.addToFront(2);
	list.addToFront(1);
	Node<Integer> head = list.getFront();
	Node<Integer> tail = list.getBack();
    
	assertEquals("Testing first node of list", new Integer(1), head.getElement());
	assertEquals("Testing second node of list", new Integer(2), head.getNext().getElement());
	assertEquals("Testing third node of list", new Integer(3), head.getNext().getNext().getElement());
	assertEquals("Testing end of list", null, head.getNext().getNext().getNext());
    
	assertEquals("Testing node at back of list", new Integer(3), tail.getElement());
	assertEquals("Testing next to last node", new Integer(2), tail.getPrev().getElement());
	assertEquals("Testing third to last node", new Integer(1), tail.getPrev().getPrev().getElement());
	assertEquals("Testing front of list", null, tail.getPrev().getPrev().getPrev());
    }

    /**
     * Tests the addToBack method of LinkedList.
     */
    @Test
    public void testAddToBack() {
	LinkedList<Integer> list = new LinkedList<Integer>();
	list.addToBack(1);
	list.addToBack(2);
	list.addToBack(3);
	Node<Integer> head = list.getFront();
	Node<Integer> tail = list.getBack();
      
	assertEquals("Testing last node of list", new Integer(3), tail.getElement());
	assertEquals("Testing next to last node", new Integer(2), tail.getPrev().getElement());
	assertEquals("Testing third to last node", new Integer(1), tail.getPrev().getPrev().getElement());
	assertEquals("Testing front of list", null, tail.getPrev().getPrev().getPrev());
    
	assertEquals("Testing node at front of list", new Integer(1), head.getElement());
	assertEquals("Testing second node of list", new Integer(2), head.getNext().getElement());
	assertEquals("Testing third node of list", new Integer(3), head.getNext().getNext().getElement());
	assertEquals("Testing end of list", null, head.getNext().getNext().getNext());
    }
  
    /**
     * Tests the removeFromFront method of LinkedList.
     */
    @Test
    public void testRemoveFromFront() {
	LinkedList<Integer> list = new LinkedList<Integer>();
	list.addToFront(1);
	list.addToFront(2);
	list.addToFront(3);
	try{
	    //these should not need it, but the compiler complained
	    assertEquals("Removing element of list", new Integer(3), list.removeFromFront());
	    assertEquals("Removing a second element", new Integer(2), list.removeFromFront());
	    assertEquals("Removing a third element", new Integer(1), list.removeFromFront());
	    assertEquals("Removed last element of list", true, list.isEmpty());
	    //this should throw it
	    list.removeFromFront();
	    fail("Removing from empty list did not throw an exception.");
	}
	catch (java.util.NoSuchElementException e) {
	    /* everything is good */
	}
	catch (Exception e) {
	    fail("Removing from empty list threw the wrong type of exception.");
	}
    
	list.addToBack(6);
	list.addToBack(7);
	try{
	    assertEquals("Removing element added to back of list", new Integer(6), list.removeFromFront());
	    assertEquals("Removing second element added to back", new Integer(7), list.removeFromFront());
	}
	catch (java.util.NoSuchElementException e) {
	    /* everything is good */
	}
	catch (Exception e) {
	    fail("Removing from empty list threw the wrong type of exception.");
	}
    }
  
    /**
     * Tests the removeFromBack method of LinkedList.
     */
    @Test
    public void testRemoveFromBack() {
	LinkedList<Integer> list = new LinkedList<Integer>();
	list.addToBack(5);
	list.addToFront(4);
	list.addToBack(6);
	try{
	    //these should not need it, but the compiler complained
	    assertEquals("Removing element from back of list", new Integer(6), list.removeFromBack());
	    assertEquals("Removing second element from back of list", new Integer(5), list.removeFromBack());
	    assertEquals("Removing element from back that was added to front", new Integer(4), list.removeFromBack());
	    assertEquals("Removing last element of list", true, list.isEmpty());
	    //this should throw it
	    list.removeFromBack();
	    fail("Removing from empty list did not throw an exception.");
	}
	catch (java.util.NoSuchElementException e) {
	    /* everything is good */
	}
	catch (Exception e) {
	    fail("Removing from empty list threw the wrong type of exception.");
	}
    }
  
    /**
     * Tests the linked list iterator.
     */
    @Test
    public void testListIterator() {
	LinkedList<Integer> list = new LinkedList<Integer>();
	for (int i = 5; i > 0; i--)
	    list.addToFront(i);
    
	/* checking that we get out what we put it */
	int i = 1;
	for (Integer x: list)
	    assertEquals("Testing value returned by iterator", new Integer(i++), x);
    
	if (i != 6)
	    fail("The iterator did not run through all the elements of the list");
    }
  
    /**
     * Tests the remove feature of the linked list iterator.
     */
    @Test
    public void testListIteratorRemove() {
	LinkedList<Integer> list = new LinkedList<Integer>();
	for (int i = 5; i > 0; i--)
	    list.addToFront(i);
    
	/* testing removing the first element through the iterator */
	Iterator<Integer> listIterator = list.iterator();
	listIterator.next();
	listIterator.remove();
    
	/* the list should now be 2 through 5 */
	int i = 2;
	for (Integer x: list)
	    assertEquals("The iterator failed when removing the first element", new Integer(i++), x);
	if (i != 6)
	    fail("The iterator failed when removing the first element");
    
	/* testing removing element 3 */
	listIterator = list.iterator();
	listIterator.next();
	listIterator.next();
	try{listIterator.remove();} //keep compiler quiet
	catch (Exception e) {fail();}
    
	Node<Integer> head = list.getFront();
	Node<Integer> tail = list.getBack();
    
	assertEquals("Iterator failed to remove middle element", new Integer(2), head.getElement());
	assertEquals("Iterator failed to remove middle element", new Integer(4), head.getNext().getElement());
	assertEquals("Iterator failed to remove middle element", new Integer(5), head.getNext().getNext().getElement());
	assertEquals("Iterator failed to remove middle element", null, head.getNext().getNext().getNext());
    
	assertEquals("Iterator failed to remove middle element", new Integer(5), tail.getElement());
	assertEquals("Iterator failed to remove middle element", new Integer(4), tail.getPrev().getElement());
	assertEquals("Iterator failed to remove middle element", new Integer(2), tail.getPrev().getPrev().getElement());
	assertEquals("Iterator failed to remove middle element", null, tail.getPrev().getPrev().getPrev());
    
	/* testing removing the last element */
	while (listIterator.hasNext())
	    listIterator.next();
	try{listIterator.remove();} //this keeps the compiler quiet
	catch (Exception e) {fail();}
    
	head = list.getFront();
	tail = list.getBack();
	
	//reminder: list is now 2, 4
	assertEquals("Iterator failed to remove middle element", new Integer(2), head.getElement());
	assertEquals("Iterator failed to remove middle element", new Integer(4), head.getNext().getElement());
	assertEquals("Iterator failed to remove middle element", null, head.getNext().getNext());
    
	assertEquals("Iterator failed to remove middle element", new Integer(4), tail.getElement());
	assertEquals("Iterator failed to remove middle element", new Integer(2), tail.getPrev().getElement());
	assertEquals("Iterator failed to remove middle element", null, tail.getPrev().getPrev());
    
	/* testing removing before calling next */
	listIterator = list.iterator();
	try {
	    listIterator.remove();
	    fail("Calling iterator's remove() before calling next() should throw an IllegalStateException");
	}
	catch (IllegalStateException e) {
	    // all is good
	}
	catch (Exception e) {
	    fail("The wrong exception thrown by the iterator remove() method.");
	}
    }

    /**
     * tests the find feature of the list.  Note that this exists as a
     * code-saver, and is used by the setup of the study.  A given iterator is
     * only meant to be run once.
     */
    @Test
    public void testListIteratorFind(){
	LinkedList<Integer> list = new LinkedList<Integer>();
	LinkedListIterator<Integer> iteratorNone = 
	    (LinkedListIterator<Integer>) list.iterator();
	assertEquals(iteratorNone.find(new Integer(7)), null);//7 is arbitrary
	
	list.addToFront(5);
	LinkedListIterator<Integer> iteratorOne = 
	    (LinkedListIterator<Integer>) list.iterator();
	assertEquals(iteratorOne.find(new Integer(5)), new Integer(5));
	assertEquals(iteratorOne.find(new Integer(7)), null);


	for (int i = 4; i > 0; i--){
	    list.addToFront(i);
	} //now 1,2,3,4,5
	for (int i = 5; i > 0; i--){
	    LinkedListIterator<Integer> iterator = 
		(LinkedListIterator<Integer>) list.iterator();
	    assertEquals(new Integer(i), iterator.find(new Integer(i)));
	    //covers first, middle, and last
	}
    }
}
