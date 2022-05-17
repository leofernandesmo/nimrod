package pamvotis.exceptions;

/**
 * Handles exceptions related to incorrect difinition of stochastic process distributions.
 * For example, the user configures a distribution for the packet length other than 'c', 'u' or 'e' that
 * are currently supported by the system.
 * @author Dimitris El. Vassis
 */
public class UnknownDistributionException extends Exception{

	private static final long serialVersionUID = 1L;

	/**
	 * Prints a message configured by the entity thrown the exception.
	 * @param msg The message to be printed.
	 */
	public UnknownDistributionException(String msg){
		super(msg);
	}
}
