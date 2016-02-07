/**
 * James Starkman (jas497)<br/>
 * Stock class<br/>
 * 
 * Represents a stock.  Can be used to access said stock, for buying, selling, etc.
 * Specifically, this is one investor's record of a stock.  
 * It holds the quantity of shares owned, all piled into one int.  Separate pur-
 * chases should be separate, but that would need arrays.<br/>
 * <br/><br/>
 * To be clear, each instance of this class is a separate corporation.
 */

public class Stock { //extends Object - un-necessary
    //full company name
    private String nameFull = "Default";
    //ticker symbol will be supplied by constructors
    private String nameTicker;
    //price in US Dollar (not that other currencies are used), will be constructed
    private double priceUSD;
    //qty = quantity.  Is amount owned.
    private int    qtyHeld = 0;
    //cost to the user
    private double costBasis = 0.0;
    //profit, changes with sale at gain or loss
    private double capitalGains = 0.0;

    //constructors second
    public Stock(String tickerSymbol, double currentPrice){
	this.nameTicker = tickerSymbol;
	this.priceUSD = currentPrice;
    }

    public Stock(String companyName, String tickerSymbol, double currentPrice){
	this(tickerSymbol, currentPrice);
	this.nameFull = companyName;
    }
    
    //methods (functions) last

    //Returns the name of the corporation
    public String getCompanyName(){
	return nameFull;
    }

    //Sets the full name of the corp. to input
    public void setCompanyName(String name){
	nameFull = name;
    }
    
    //Returns the NYSE ticker symbol (e.g., ORCL for Oracle Corp.)
    public String getTickerSymbol(){
	return nameTicker;
    }

    //Sets the ticker symbol
    public void setTickerSymbol(String tickerSymbol){
	nameTicker = tickerSymbol;
    }

    //Returns current price of stock (in $US), as a double
    public double getCurrentPrice(){
	return priceUSD;
    }
    
    //Sets the price of the stock.  Should be called from an external updater.
    public void setCurrentPrice(double price){
	priceUSD = price;
    }
    
    //Returns quantity of shares held in this corp.
    public int getNumberShares(){
	return qtyHeld;
    }

    //Returns the cost basis, the cost to the purchaser
    public double getCostBasis(){
	return costBasis;
    }

    //Returns current capital gains, the "profit".  The IRS can tax this.
    public double getCapitalGains(){
	return capitalGains;
    }

    //Allows user to buy stock, and returns the money paid.
    public double buy(int numShares, double commission){
	//need the conditional to deal with accidents and cheaters
	if(numShares > 0){
	    qtyHeld += numShares;
	    this.costBasis += ((numShares * priceUSD) + commission);
	    return (numShares * priceUSD) + commission;
	}
	else{ //if numShares <= 0
	    return 0.0;
	}
    }

    //Allows user to sell stock, and returns money received, less commission.
    public double sell(int numShares, double commission){
	if(numShares > this.qtyHeld || qtyHeld == 0){
	    return 0.0;
	}
	else{
	    //modular design!
	    return this.sellDouble((double)numShares, commission, false);
	}
    }

    //Allows a stock to split, which divides the price and mult. the qty of shares.
    public double split(int ratioNumerator, int ratioDenominator){
	int rn = ratioNumerator, rd = ratioDenominator;
	if (rn==0 || rd==0){
	    return 0.0;
	}
	else{
	    //change the qtyHeld by rn/rd; 2/1 means 2x, 1/2 means half
	    //if not a perfect split, sell the partial share back to company

	    //outline of implementation:
	    //everyone sells all holdings back, stock volume and price are scaled,
	    //everyone re-invests proceeds of previous sale.  No commission.

	    //first, sell all
	    double sellBack = this.sell(qtyHeld, 0.0);//stores proceeds
	    
	    //then, scale stock price.  Halving the price will double the volume.
	    priceUSD *= rd;
	    priceUSD /= rn;
	    
	    //re-invest the proceeds of the sale
	    double maxBuyable = sellBack / priceUSD;  //the most one can buy
	    this.buy((int)maxBuyable, 0.0); //typecast will round for us
	    
	    //and sell the fraction
	    double moneyPartial = this.sellDouble((maxBuyable % 1.0),0.0, true);
	    this.capitalGains += moneyPartial;
	    return moneyPartial;
	}
    }

    //The selling backend, which can (if allowed) sell fractional shares.
    private double sellDouble(double numShares, double commission, boolean split){
        double profit = numShares * this.priceUSD - commission;//money made

	//updating data
	if(!split){ //split is true if it is a split, false otherwise
	    //then proceed as normal.  
	    if(numShares == (double) Math.floor(numShares)){ //if is an integer
		//this is what used to be in the sell fxn/method
		//This variable holds the $ to be manipulated.
		double sale = this.costBasis * (numShares / this.qtyHeld);
		this.costBasis -= sale;
		//this.costBasis += commission;
		this.capitalGains += (profit - sale);
	    }
	    else{ //user erred or lied - numShares SHOULD be integer if !split.
		numShares = 0.0;
		profit = 0.0;
	    }
	    this.qtyHeld -= numShares;
	}

	return profit;
    }
}
