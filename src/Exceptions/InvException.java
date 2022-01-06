// -----------------------------------------------------
// Assignment 3
// Written by: Reuven Ostrofsky - 40188881
// -----------------------------------------------------
package Exceptions;
/**
 * 
 * @author Reuven Ostrofsky
 * Base exception class n the case where there is missing data in csv files
 */
public class InvException extends Exception {
    
	private static final long serialVersionUID = 1L;
	/**
	 * Default constructor displays error message
	 */
	public InvException() {
        System.out.println("Error: input row cannot be parsed due to missing information");
    }   
}
