package pamvotis.exceptions;

/**
 * Handles exceptions related to addition of nodes or sources that already exist.
 * For example, the user previously added a source with ID 2 and later on, it adds another source with the same ID.
 * @author Dimitris El. Vassis
 */
public class ElementExistsException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Prints a message configured by the entity thrown the exception.
	 * @param msg The message to be printed.
	 */
	public ElementExistsException(String msg){
		super(msg);
	}
}
