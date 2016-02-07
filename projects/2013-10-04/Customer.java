/**
 * James Starkman (jas497)<br/>
 * Cash class<br/><br/>
 * 
 * The Customer class, which represents a customer's account.  It binds together
 * all of the components in the other classes.  
 */

public class Customer{
    //do not want to ruin this table, like the others
    private String nameGiven;     //given/first name
    private String nameFamily;    //sur/last name
    private String addressStreet; //as opposed to e-mail address
    private Stock  accountStock;  //a Stock obj to hold stock
    private Cash   accountCash;   //a Cash  obj to hold cash
    private double commission;    //a flat rate
    private Date   date;          //a Date  obj to hold the current date
    private double capGains;      //capital gains, taken from Stock obj.

    //constructors in this order for a reason
    public Customer(double commission, Stock stockAccount, Cash cashAccount,
		    Date date){
	this.commission   = commission;
	this.accountStock = stockAccount;
	this.accountCash  = cashAccount;
	this.date         = date;
    }
    
    public Customer(String firstName, String lastName, String Address,
		    double commission, Stock stockAccount, Cash cashAccount,
		    Date date){
	//order matters! First one is special.
	this(commission, stockAccount, cashAccount, date);
	this.nameGiven     = firstName;
	this.nameFamily    = lastName;
	this.addressStreet = Address;
    }

    //Rationale for given/family instead of first/last:
    //not all cultures use the same ordering.

    //Returns the given ("first") name.
    public String getFirstName(){
	return nameGiven;
    }

    //Sets the given name
    public void setFirstName(String firstName){
	this.nameGiven = firstName;
    }

    //Returns the family name (or surname, or last name)
    public String getLastName(){
	return nameFamily;
    }

    //Sets the family name
    public void setLastName(String lastName){
	this.nameFamily = lastName;
    }

    //Returns the street address
    public String getAddress(){
	return addressStreet;
    }

    //Sets the (street) address
    public void setAddress(String address){
	this.addressStreet = address;
    }

    //Returns the Stock obj of this account, as only one is allowed.  Commies.
    public Stock getStockAccount(){
	return accountStock;
    }

    //Sets the Stock obj used by this account
    public void setStockAccount(Stock stockAccount){
	this.accountStock = stockAccount;
    }

    //Returns the Cash obj
    public Cash getCashAccount(){
	return accountCash;
    }

    //Sets the Cash obj used by this account
    public void setCashAccount(Cash cashAccount){
	this.accountCash = cashAccount;
    }

    //Returns the commission amt
    public double getCommissionAmount(){
	return commission;
    }

    //Sets the commission amt
    public void setCommissionAmount(double commission){
	this.commission = commission;
    }

    //Returns the Date obj
    public Date getDate(){
	return date;
    }

    //Returns cash bal + total value of stock
    public double currentValue(){
	return accountCash.getBalance() + (accountStock.getNumberShares() * 
					   accountStock.getCurrentPrice());
    }

    //Returns the cap gains so far this year
    public double getYearlyCapitalGains(){
	return capGains;
    }

    //Makes a deposit
    public void deposit(double amount){
	accountCash.deposit(amount);
    }

    //Tries to make a withdrawal.
    public boolean withdraw(double amount){
	return accountCash.withdraw(amount); //true if worked, false if no
    }

    //Sells shares and deposits the proceeds
    public void sellShares(int numShares){
	accountCash.deposit(accountStock.sell(numShares, this.commission));
    }

    //Tries to buy shares.  Returns false if too expensive.
    public boolean buyShares(int numShares){
	//cur val cannot go neg
	double willCost = this.accountStock.getCurrentPrice() * numShares + 
	    this.commission;

	if(this.currentValue() - willCost < 0.0){ //if cost more than you worth
	    return false;
	}
	else{
	    accountCash.withdraw(accountStock.buy(numShares, this.commission));
	    return true;
	}
    }

    //Goes forward in time by one day
    public void incrementDate(){
	//to handle year changes, old year is captured before time is updated
	//could add a field to Customer to hold current year, but that would
	//not fit with OOP.  It should only do ONE thing, not several unrelated.

	int oldYear = date.getYear();          //present year
	this.date.incrementDay();
	this.accountCash.processDay();
	if(this.date.getDay() == 1){           //month just changed
	    this.accountCash.processMonth();
	    if(this.date.getYear() != oldYear){//year only change if month just did
		                               //this avoids 353 conditionals/yr
		//year changed
		this.capGains -= this.accountStock.getCapitalGains();
		this.capGains *= -1;           //thus, cG = Stock.cG - cG.
	    }
	}
    }
}
