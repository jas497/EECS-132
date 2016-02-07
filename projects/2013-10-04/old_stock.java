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

    /**
     * Effects a purchase of the stock.  Commission must be determined externally,
     * and the resulting double must be passed in here.
     *
     * @param  numShares  the number of shares to buy
     * @param  commission the commission, in USD. Use a positive value.
     * @return            the total price of the purchase
     */
    public double buy(int numShares, double commission){
	//need the conditional to deal with accidents and cheaters
	if(numShares > 0){
	    qtyHeld += numShares;
	    System.out.printf("You bought %d shares.  You now have %d shares.",
			      numShares, this.getNumberShares());
	    this.costBasis += ((numShares * priceUSD) + commission);
	}
	else if(numShares == 0){
	    System.out.println("Please buy something.");
	}
	else{ //if numShares < 0, is negative
	    System.out.println("Please use the sell feature for that.");
	}
	
	// you spent ___ USD.
	return (numShares * priceUSD) + commission;
    }

    /**
     * Sell shares from the ones currently owned.  Commission must be determined
     * externally and passed in.  Short selling is not available.
     *
     * @param  numShares  the number of shares to sell
     * @param  commission the commission, in USD.  Use a positive value.
     * @return            money received from the sale, in USD
     */
    public double sell(int numShares, double commission){
	if(numShares > this.qtyHeld || qtyHeld == 0){
	    System.out.println("No short selling.  Yet.");
	    return 0.0;
	}
	else{
	    return this.sellDouble((double)numShares, commission, false);
	    /*old - moved to subroutine above
	    // numShares/qtyHeld is the fraction sold. One minus same is fraction
	    // left. Scale costBasis by this.
	    // the typecast is to prevent int/int rounding.  
	    // costBasis *= (1.0 - ((double)numShares / qtyHeld));

	    //trying again...
   	    double sale = costBasis * ((double)numShares / qtyHeld);
	    costBasis -= sale;
	    costBasis += commission;

	    double profit = numShares * priceUSD - commission;
	    capitalGains += (profit - sale);
	    qtyHeld -= numShares;
	    return profit;
	    */
	}
    }

    /**
     * Effects a split, which splits a few shares into so many new ones.  Or
     * consolidates several old into fewer new.  If the split is not perfect, 
     * the fraction of a share left over will be sold.  <br/><br/>
     * 
     * Format of fraction is (qty of new shares) / (qty of old shares).  Doing
     * split(2,1) would double volume and halve price.
     *
     * @param  ratioNumerator   numerator of the fraction
     * @param  ratioDenominator denominator of the fraction
     * @return                  money received from sale of fractional share
     */
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
	    double sellBack = this.sell(qtyHeld, 0.0);
	    
	    //then, scale stock price.  Halving the price will double the volume.
	    priceUSD *= ((double)rd/rn);
	    
	    //re-invest the proceeds of the sale
	    double maxBuyable = sellBack / priceUSD;
	    this.buy((int)maxBuyable, 0.0); //typecast will round for us
	    
	    //and sell the fraction
	    return this.sellDouble(maxBuyable - (double)Math.floor(maxBuyable),
				   0.0, true);
	}
    }

    /**
     * The real selling function.  Parameter "split" allows for sale of the
     * remaining partial share.  
     *
     * @param  numShares  quantity of shares to be sold
     * @param  commission commission to deduct.  Use 0.0 for splits.
     * @param  split      true if is a split
     * @return            money received from sale.  Is 0.0 if bad practice.
     */
    private double sellDouble(double numShares, double commission, boolean split){
        double profit = numShares * this.priceUSD - commission;

	//updating data
	if(!split){
	    if(numShares == (double) Math.floor(numShares)){ //if is an integer
		double sale = this.costBasis * (numShares / this.qtyHeld);
		this.costBasis -= sale;
		this.costBasis += commission;
		this.capitalGains += (profit - sale);
	    }
	    else{ //user erred or lied
		System.out.println("Partial shares can only be sold in a" + 
				   "split.  No shares were sold.  ");
		numShares = 0.0;
		profit = 0.0;
	    }
	}
	this.qtyHeld -= numShares;
	return profit;
    }
}
