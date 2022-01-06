// -----------------------------------------------------
// Assignment 3
// Written by: Reuven Ostrofsky - 40188881
// -----------------------------------------------------
package Exceptions;
import java.io.File;
/**
 * 
 * @author Reuven Ostrofsky
 *	Exception class thrown in the case there is missing attributes in the csv file
 */
public class CSVFileInvalidException extends InvException {
    
	private static final long serialVersionUID = 1L;
	/**
	 * Parametrized constructor
	 * @param f - file being processed
	 */
	public CSVFileInvalidException(File f) {
        System.out.println(getMessage(f));
        

    }
	/**
	 * 
	 * @param f File being processed
	 * @return Error message in String
	 */
    public String getMessage(File f) {
        return "File " + f.getName() + " is invlaid: attribute is missing.";
    }
}
