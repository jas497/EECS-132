/**
 * James Starkman (jas497)<br/>
 * Cash class<br/><br/>
 * 
 * Represents cash on hand ("an account holding money reserves").  Can be used to 
 * store money and manipulate it.  One account per customer.
 */

public class Cash{
    //money in account
    private double  balance = 0.0;
    //rate applied to pos balance
    private double  rateInterest;
    //rate applied to neg balance, use .05 for 5%
    private double  rateLoan;
    //withdraw no more than this.  Should be positive.
    private double  limitLoan;
    //penalty charged if too much is withdrawn
    private double  penaltyOverdraft;
    //interest is held separate until processing
    private double  currentInterest = 0.0;
    //flag that is false if no OD, true if there was
    private boolean flagOverdraft = false;

    //for construction.  All should be positive, and use 0.05 for 5%.
    public Cash(double savingsRate, double loanRate, double loanLimit,
		double overdraftPenalty){
	this.rateInterest = savingsRate;
	this.rateLoan = loanRate;
	this.limitLoan = loanLimit;
	this.penaltyOverdraft = overdraftPenalty;
    }

    //Returns the amt of money in the account.
    public double getBalance(){
	return balance;
    }

    //Returns the rate of interest that money earns while here.
    public double getSavingsRate(){
	return rateInterest;
    }
    
    //Sets the rate of interest on money held.
    public void setSavingsRate(double savingsRate){
	rateInterest = savingsRate;
    }

    //Returns the rate of interest that is charged on negative money.
    public double getLoanRate(){
	return rateLoan;
    }

    //Sets the rate of loan on money owed.
    public void setLoanRate(double loanRate){
	rateLoan = loanRate;
    }

    //Returns the loan limit.
    public double getLoanLimit(){
	return limitLoan;
    }

    //Sets the upper bound on lent money, do not go over it.
    public void setLoanLimit(double limit){
	limitLoan = limit;
    }

    //Returns the penalty/fee applied in case of an overdraft
    public double getOverdraftPenalty(){
	return penaltyOverdraft;
    }

    //Sets the fee for removing more money than allowed.
    public void setOverdraftPenalty(double penalty){
	penaltyOverdraft = penalty;
    }

    //Put money in the account.
    public void deposit(double amount){
	if(amount >= 0.0){
	    balance += amount;
	}
    }

    //Take money from the account, but no loans
    public boolean withdraw(double amount){ //returns true if worked
	if(amount <= balance){
	    balance -= amount;
	    return true;
	}
	else{
	    //you can't withdraw what you don't have
	    return false;
	}
    }
    
    //Take money from the account, loans will be made as needed.
    public void transfer(double amount){
	//can withdraw more than have - just take a loan
        balance -= amount;
    }
    
    //Applies rates to the account, and checks for overdrafting
    public void processDay(){
	double delta = balance/365.0; //holds the change in currentInterest

	if(balance >= 0.0){
	    delta *= rateInterest; //thus, bal*(rateInt. /365) is the result
	}
	else{ //neg. balance
	    delta *= rateLoan;
	}
	currentInterest += delta; //sign is det. by that of balance

	//now, for overdrafting (OD).  Could use one if, but this is for clarity
	//processMonth sets this to false
	if(flagOverdraft == false){//tells it to check
	    double temp = balance + currentInterest;    //saves extra adding
	    if(temp < (0.0 - limitLoan)){               //as per requirements
		flagOverdraft = true;
		balance -= penaltyOverdraft;
	    }
	}
    }
    
    //Deals with compounding interest and setting the overdraft flag.
    public void processMonth(){
	balance += currentInterest;
	currentInterest = 0.0;
	flagOverdraft = false; //clear the flag
	//overdraft limit and fee has been moved to processDay
	//the fee will now be applied "at any time that processDay was called
	//since the last time processMonth was called".  
	//This allows for an overdraft fee every day that the user is overdrawn.
    }
}
