package it.polimi.dei.provafinale.carcassonne.model.entity;

/**
 * Class to represent a counter: it stores an integer that can be incremented.
 * */
public class Counter {
	
	private int count;	

	/**
	 * Initializes the counter at 0.
	 * */
	public Counter(){
		count = 0;
	}

	/**
	 * Increments the counter by 1.
	 * */
	public void increment(){
		count++;
	}
	
	/**
	 * Gets the counter current value.
	 * @return the current counter value
	 * */
	public int getValue(){
		return count;
	}
}
