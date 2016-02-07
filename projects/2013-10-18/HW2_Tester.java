import org.junit.*;
import static org.junit.Assert.*;

//Abbreviations sometimes used:
// f - first
// m - middle, many (use context)
// l - last
// n - none
// o - one
//always in form: [nom], [fml]

/*What these mean: (quoted, minor formatting changes)
Test 0, test 1, test many: This means you have to test cases where the parameters,
if they are integers, are 0, 1 or some value other than 1. If the parameters are
strings, you have to test strings of length 0, 1, and more than 1. If the strings
must contain certain data, you need to test cases where they contain 0, 1, and more
than 1 of the desired data.

Test first, last, and middle: In cases where you have to search in or modify a
string, you need to test cases where the item to be found or modified is the first
character of the string, the last character of the string, or somewhere in the
middle of the string.
*/

/**
 * Test class HW2
 */
public class HW2_Tester {
    /**
     * Test method matchingParentheses(String)
     * Somewhat exhaustive.
     */
    @Test
    public void testMatchParens(){
	String good = "This is a (test (of the) (matching)) parentheses";
	String bad  = "The (second closing) parenthesis) does not match";
	
	assertTrue("Incorrectly claims parens do not match", 
		   HW2.matchingParentheses(good)); //test many, middle	
	assertTrue("Incorrectly claims parens do not match", 
		   HW2.matchingParentheses("( "+good+" )"));//test many, first/last

	assertTrue("Incorrectly claims only parens do not match",
		   HW2.matchingParentheses("(foo)")); //test one, first/last
	assertTrue("Incorrectly claims only pair of parens does not match",
		   HW2.matchingParentheses("foo (bar) baz")); //test one, middle

	assertTrue("Incorrectly claims a bad match when there are none to be made",
		   HW2.matchingParentheses("foo"));   //test none

	assertFalse("Incorrectly claims parens match", 
		   HW2.matchingParentheses(bad)); //test many, middle
	assertFalse("Incorrectly claims parens match", 
		   HW2.matchingParentheses("((("+bad)); //test many, first
	assertFalse("Incorrectly claims parens match", 
		   HW2.matchingParentheses(bad+")))")); //test many, last

    }

    /**
     * Test method everyNthExcept(String, int, int, int)
     */
    @Test
    public void testEveryNthExcept(){
	String stdin = "abcdefghijklmnop";

	assertEquals(HW2.everyNthExcept("", 0, 0, 0), "");
	assertEquals(HW2.everyNthExcept(stdin, 3, 2, 4), "ceim");//many, mid
	assertEquals(HW2.everyNthExcept(stdin, 1, 3, 2), "adjp");//many, f/l
	assertEquals(HW2.everyNthExcept(stdin, 1, 1, 1), "a");//one, fir/mid
	assertEquals(HW2.everyNthExcept(stdin, 1, 5, 2), "afp");//one, last
	assertEquals(HW2.everyNthExcept(stdin, 9, 1, 2), "ijlnp");//one v2
	assertEquals(HW2.everyNthExcept(stdin, 1, 2, 1), "a");//one v3
    }

    /**
     * Test method reverseDigits(String)
     */
    @Test
    public void testReverseDigits(){
	//this looks like 1337 5p34k, sort of
	String foo12 = "0 the d1gits of the2 string3 4 are8 reversed 9";
	String foo21 = "9 the d8gits of the4 string3 2 are1 reversed 0";

	assertEquals(HW2.reverseDigits(foo12), foo21); //many, f/m/l
	assertEquals(HW2.reverseDigits("8"), "8"); //one
	assertEquals(HW2.reverseDigits(""), ""); //none
	assertEquals(HW2.reverseDigits("foo"), "foo"); //none
	assertEquals(HW2.reverseDigits("zer0"), "zer0"); //none
    }

    /**
     * Test method editOut(String)
     */
    @Test
    public void testEditOut(){
	String old1 = "(this is an unusual (example)) of (editing out and (retain"
	              + "ing)) text"; 
	String new1 = "example of retaining text";

	String old2 = "this is (another) (example) showing (((((removal))))) -( "
	              + "and )- ((((retention))))";
	String new2 = "this is   showing  -- retention";

	assertEquals(HW2.editOut(old1), new1); //many, f/m
	assertEquals(HW2.editOut(old2), new2); //many, m/l
	assertEquals(HW2.editOut("the(invisible man)"), "the"); //one, m/l
	assertEquals(HW2.editOut("foo"), "foo"); //none, n/a
    }

    /**
     * Test method replaceText(String, String)
     */
    @Test
    public void testReplaceText(){
        String old1a="a (simple) programming (example)";
	String old1b="(cool) (problem)";
	String new1 ="a cool programming problem";
	String old2a="a ((nested) example) w/ (three) replacements (to (handle))";
	String old2b="the replacements are (answer) and (really (two) not three)";
	String new2 ="a answer w/ really (two) not three replacements ";

	assertEquals(HW2.replaceText(old1a, old1b), new1);//many, f/m/l
	assertEquals(HW2.replaceText(old2a, old2b), new2);//many, f/m/l
	assertEquals(HW2.replaceText("foo", "vacant"), "foo");//none, n/a
	assertEquals(HW2.replaceText("foo", "(bar)"), "foo");//none, f/m/l
	assertEquals(HW2.replaceText("(foo)", "bar"), "");//none, f/m/l
	assertEquals(HW2.replaceText("I'm lying(!)", "(?)"), "I'm lying?");//m,m/l
    }
}
