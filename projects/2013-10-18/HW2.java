/**
 * Name:   James Starkman
 * Due:    2013-10-18
 * Desc:   This class exists to be a homework assignment.  Besides that, it can 
 *     manipulate arrays and strings, mostly dealing with parsing on parentheticals
 *     and using StringBuilder to form output.  
 * Limits: NO ARRAYS!!!
 *     Use only these API methods:
 *         String.charAt(), String.length();
 *         StringBuilder: append(), charAt(), setCharAt(), length(), toString();
 *         Character.isDigit();
 *     No breaks nor continues.  Do not mess with i instead; mess w/ condition
 * Notice: All methods can be done in one pass of the input.
 */

public class HW2{
    //Returns false if parens do not match, true otherwise.
    public static boolean matchingParentheses(String in){
	boolean out   = false;
	int     count = 0;
	for(int i=0; i<in.length() && count>=0; i++){
	    if(in.charAt(i) == '('){
		count++;
	    }
	    else if(in.charAt(i) == ')'){
		count--;
	    }
	}
	if(count == 0){
	    out = true;
	}
	return out;
    }

    //Returns the skip-th chars, less the except-th chars.  Count starts at start.
    public static String everyNthExcept(String string, int start, int skip, 
					int except){
	StringBuilder out = new StringBuilder();

	//to avoid failing on negs, zeroes.
	//if(((start|skip|except)&(1<<31)) ==0 && !(start==0||skip==0||except==0)){
	if(start > 0 && skip > 0 && except > 0 && !string.equals("")){ //sanitise
	    out.append(string.charAt(start-1));
	    for(int i = start-1; i < string.length(); i+=skip){ //i is the index
		//if could be on the skip and is not excepted:
		if((i-start+1) % except != 0){
		    //do include it
		    out.append(string.charAt(i));
		}
	    }
	    return out.toString();
	}
	else{
	    return string; // >= 1 bad input, so no processing
	}
    }

    //Returns what it received, but with 0-9 reversed
    //Example: 1337 5p34k --> 4357 3p31k.
    public static String reverseDigits(String in){
	StringBuilder out = new StringBuilder();
	int           j   = in.length()-1;

	//loop: i walks on in.  When i finds a digit, it tells j to race to next
	//      one in j's path, and writes that digit instead of what it found
	for(int i=0; i < in.length(); i++){
	    if(Character.isDigit(in.charAt(i))){ //if found a digit
		//make j race to a digit
		while(!Character.isDigit(in.charAt(j))) j--;
		//now, in.charAt(j) is the digit
		out.append(in.charAt(j--));
	    }
	    else out.append(in.charAt(i));
	}
	/*
      	char[]        locNumbers    = new char[in.length()];//locations in in
	int           locNumbersPos = 0;                    //current loc. in prev.
	StringBuilder out           = new StringBuilder(in);//output

	for(char i=(char)(in.length()-1); i >=0; i--){  //working backwards
	    //use isDigit
	    
	}
	//filling time
	for(int i=0; i < locNumbersPos; i++){
	    out.setCharAt(i, locNumbers[i]);
	}
	*/
	return out.toString();
    }

    //Removes anything in an odd # of parens, and returns the result
    public static String editOut(String text){
	if(matchingParentheses(text)){ //an extra pass, but sanitised now.
	    //parsing
	    boolean       modeRemoval = false;
	    StringBuilder out         = new StringBuilder();

	    for(int i=0; i < text.length(); i++){
		//if paren, flip mR.  If not, add to out.
		if(text.charAt(i) == '(' || text.charAt(i) == ')'){
		    //flip the bit
		    modeRemoval = modeRemoval ? false : true;
		}
		else{ //if not a paren
		    if(!modeRemoval){ //if not in kill-mode
			out.append(text.charAt(i));
		    }
		}
	    }
	    return out.toString();
	}
	else{
	    return text; //exact behavior does not matter
	}
    }

    //Replaces (text) in 1st entry with (text) in second entry.  Nests are ignored,
    //and excess (terms) in one are removed (1st) or ignored (2nd).
    public static String replaceText(String one, String two){
	//revised version; old is below
	//basic structure of new version:
	//go along 1st term.  When find paren block, write from two, not one.
	//Only one level of nesting.  
	StringBuilder out      = new StringBuilder();
	int           count    = 0;     //one paren counter
	boolean       oneFE    = false; //falling edge
	boolean       writeOnce= false; //write from two loop
	int           j        = 0;     //counter for two loop
	boolean       twoWrite = false; //to pause two loop btwn replacements
	int           nests    = 0;     //like count, but for two loop
	if(matchingParentheses(one) && matchingParentheses(two)){//avoid errors
	    //walk along one
	    for(int i = 0; i < one.length(); i++){
		if(one.charAt(i) == '('){
		    count++;
		    twoWrite = true;
		}
		else if(one.charAt(i) == ')'){
		    count--;
		    oneFE = true;
		}

		//now, check the counter
		if(count == 0){ //outside ()
		    if(!oneFE){ //if are not on the falling edge; systems normal
			out.append(one.charAt(i));//I get skipped if on FE
		    }
		    oneFE = false;
		    writeOnce = false;
		}
		else if(count > 0 && !writeOnce){ //reimplement one loop, sort of
		    //write from two, remember to pause
		    while(j < two.length() && twoWrite){
			char foo = two.charAt(j);
			if(foo == '('){
			    nests++;
			}
			else if(foo == ')'){
			    nests--;
			    if(nests == 0){
				twoWrite = false;//pause, to resume later
			    }
			}
			//below: NOR(end results of either above cond. for 1st lvl
			//this is similar to an else, but lets the lower () through
			if(!((nests==1 && foo=='(') || (nests==0 && foo==')'))){
			    //now, check the accumulator
			    if(nests > 0){
				out.append(two.charAt(j));//write
			    }
			}
			j++;
		    }
		    writeOnce = true;
		}
	    }
	    return out.toString();
	}
	else{//bad parens
	    return one; //no processing
	}
    }

    /*
        int           noNests     = 0;
	StringBuilder out         = new StringBuilder();
	int[]         newTerms    = new int[two.length()];//_should_ be linked list
	int           newTermsPos = 0;
	int           foo         = 0; //used for reading from newTerms

	if(matchingParentheses(one) && matchingParentheses(two)){ //sanitary!
	    //mark where along two the new words are, store in newTerms
	    for(int i=0; i<two.length(); i++){
		if(two.charAt(i) == '('){
		    noNests++;
		    if(noNests == 1){ //if only one paren
			newTerms[newTermsPos] = i+1;
			newTermsPos++;
		    }
		}
		else if(two.charAt(i) == ')'){
		    noNests--;
		    if(noNests == 0){ //if just stopped being in parens
			newTerms[newTermsPos] = i-1;
			newTermsPos++;
		    }
		}
	    }
	    noNests = 0;              //just to be certain

	    //now, newTerms has list of all 1st level ().  Easy to find them again.

	    //find locations in one and replace
	    for(int i=0; i < one.length(); i++){ //main pass of one
		if(one.charAt(i) == '('){
		    noNests++;
		}
		else if(one.charAt(i) == ')'){
		    noNests--;
		    if(noNests == 0){
			//just left parens - insert letterwise from newTerms
			//note: newTerms = {start, end, start, end, etc...}
			for(int j = newTerms[foo], target = newTerms[++foo]
				; j <= target; j++){
			    out.append(two.charAt(j));
			    foo++;
			    //to clarify the somewhat-messy code here:
			    //foo is normally the start of the slice.  ++foo makes
			    //it odd.  foo++ makes it odd again, and on next start.
			}
		    }
		}
		else{ //if not a paren
		    if(noNests == 0){ //not in parens
			out.append(one.charAt(i));
		    }
		}
	    }
	    return out.toString();
	}
	else{
	    return one; //exact behavior unimportant, so no extra processing
	}
	}
*/
    /*
    public static String reverseAll(String in){
	//foo;
	return "no";
    }
    */
}
