package pamvotis.exceptions;

/**
 * Handles Exceptions related to invalid definition of node or source ID.
 * For example, the function getNode(3) is called, but the used did not configure any node with ID 3.
 * @author Dimitris El. Vassis
 */
public class ElementDoesNotExistException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Prints a message configured by the entity thrown the exception.
	 * @param msg The message to be printed.
	 */
	public ElementDoesNotExistException(String msg){
		super(msg);
	}
}
