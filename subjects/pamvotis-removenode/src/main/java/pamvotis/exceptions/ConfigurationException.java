package pamvotis.exceptions;

/**
 * Exception related to configuration errors in the network configuration file.
 * @author Dimitris El. Vassis
 */
public class ConfigurationException extends Exception{

	private static final long serialVersionUID = 1L;

	/**
	 * Prints a message configured by the entity thrown the exception.
	 * @param msg The message to be printed.
	 */
	public ConfigurationException(String msg){
		super(msg);
	}
}
