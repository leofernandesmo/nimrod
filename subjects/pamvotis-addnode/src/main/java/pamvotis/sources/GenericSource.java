package pamvotis.sources;

import pamvotis.core.VirtualPacket;
import pamvotis.exceptions.UnknownDistributionException;

/**
 * Represent a source generating continuous generic (abstract) traffic, CBR or
 * VBR
 * 
 * @author Dimitris El. Vassis
 */
public class GenericSource extends Source {

	char _intArrDstr; // The packet interarrival time distribution

	int _avgIntArr = 0; // The packet interarrival time (sec)

	char _pktLngthDstr; // The packet length distribution

	float _avgPktLength; // The packet length (bits)

	int packetToBeTransmittedID;

	/**
	 * Instantiates a generic source with specified parameters.
	 * 
	 * @param sourceId
	 *            The ID of the source
	 * @param intArrDstr
	 *            The packet interarrival time (sec)
	 * @param rate
	 *            The packet generation rate (packets/sec)
	 * @param pktLngthDstr
	 *            The packet length distribution c for constant, u for uniform e
	 *            for exponential, p for pareto, l for lognormal
	 * @param pktLength
	 *            The packet length (bits)
	 * @throws UnknownDistributionException
	 */
	public GenericSource(int sourceId, char intArrDstr, float rate,
			char pktLngthDstr, float pktLength)
			throws UnknownDistributionException {
		String msg = null;
		if ((pktLngthDstr != RandomGenerator.CONSTANT_DISTR) && (pktLngthDstr != RandomGenerator.EXPONENTIAL_DISTR)
				&& (pktLngthDstr != RandomGenerator.UNIFORM_DISTR)) {
			msg = "\'"
					+ pktLngthDstr
					+ "\' is not a valid distribution for packet length. Only \'c\',\'u\' and \'e\' are allowed.";
			throw new UnknownDistributionException(msg);
		} else if ((intArrDstr != RandomGenerator.CONSTANT_DISTR) && (intArrDstr != RandomGenerator.EXPONENTIAL_DISTR)
				&& (intArrDstr != RandomGenerator.UNIFORM_DISTR)) {
			msg = "\'"
					+ intArrDstr
					+ "\' is not a valid distribution for packet interarrival time. Only \'c\',\'u\' and \'e\' are allowed.";
			throw new UnknownDistributionException(msg);
		} else {
			_sourceId = sourceId;
			_intArrDstr = intArrDstr;
			_avgIntArr = (int) ((float) 1 / (float) (slot * rate));
			_pktLngthDstr = pktLngthDstr;
			_avgPktLength = pktLength;
			getNextPacket();
		}
	}

	/**
	 * If a session is empty, meaning that no packet exists, create a new
	 * packet. Then the first packet of the session is picked up in order to be
	 * born when its interarrival counter expires. For the case of the generic
	 * source, only one packet exists so each session is constituted of one
	 * packet only.
	 */
	public void getNextPacket() {
		if (session.isEmpty()) {
			RandomGenerator random = RandomGenerator.getInstance();
			session.addElement(new VirtualPacket((int) random.genRv(
					_pktLngthDstr, _avgPktLength, Float.MAX_VALUE, 1f),
					(int) random.genRv(_intArrDstr, _avgIntArr, Float.MAX_VALUE, 1f),
					1));
		}
		interArTime = session.firstElement().interarrival;
		pktLength = session.firstElement().length;
		packetToBeTransmittedID = session.firstElement().id;
		session.removeElementAt(0);
	}

	/**
	 * This function is not needed for the case of the generic source.
	 */
	public void synchronize() {
	}
}
