/**
 * James Starkman (jas497)<br/>
 * Cash class<br/><br/>
 * 
 * The Date class represents the date.  Java's built-in is too heavy, this is
 * lighter.  Saves memory, processing.
 */

public class Date{
    //what day it is, 1-31
    private int day;
    //the month, 1-12
    private int month;
    //the year.  No limits except int.
    private int year;
    //dpm = days per month, no leap years.  The zero at beginning saves math later.
    private int[] dpm = {0,31,28,31,30,31,30,31,31,30,31,30,31};

    //constructor
    public Date(int day, int month, int year){
	//super(); //unneeded
	this.day = day;
	this.month = month;
	this.year = year;
    }

    //Go forward one day.
    public void incrementDay(){
	int MONTHS_PER_YEAR = 12;  //because magic numbers are bad...
	//no leap years
	this.day++;
	if(this.day > dpm[month]){ //new month.  dpm is full of magic numbers.
	    this.day = 1;
	    this.month++;
	    if(this.month > MONTHS_PER_YEAR){
		this.month = 1;
		this.year++;
	    }
	}
    }

    //Returns what day (of the month) it is (1 to dpm[month])
    public int getDay(){
	return this.day;
    }

    //Returns what month it is (1-12)
    public int getMonth(){
	return this.month;
    }

    //Returns the year (any valid int)
    public int getYear(){
	return this.year;
    }
}
